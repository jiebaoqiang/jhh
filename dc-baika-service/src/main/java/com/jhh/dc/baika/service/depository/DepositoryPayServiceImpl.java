package com.jhh.dc.baika.service.depository;

import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.depository.DepositoryPayService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.trade.DepositoryTrade;
import com.jhh.dc.baika.common.enums.AgentDeductResponseEnum;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.SerialNumUtil;
import com.jhh.dc.baika.constant.Constant;
import com.jhh.dc.baika.entity.PersonInfoDto;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.*;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDOExample;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jhh.dc.baika.service.capital.SettlementUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/9/7.
 */
@Slf4j
@Service
public class DepositoryPayServiceImpl extends BasePayServiceImpl implements DepositoryPayService {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private QianQiDepositWebService qianQiDepositWebService;

    @Autowired
    private QianQiDepositBackService qianQiDepositBackService;

    @Autowired
    private SettlementUtil settlementUtil;

    @Value("${depository.host}")
    private String host;

    @Value("${depository.recharge}")
    private String responsePath;

    @Value("${depository.withdrawal}")
    private String responseWithdrawalPath;

    @Override
    public ResponseDo<BankDepositCommon> depositoryRecharge(DepositoryTrade recharge) {
        log.info("[充值开始] -->走银行存管{} ", recharge);
        //请结算锁
        ResponseDo<String> result = settleLock(String.valueOf(recharge.getTriggerStyle()));
        if (AgentDeductResponseEnum.SUCCESS_CODE.getCode() != result.getCode()) {
            return ResponseDo.newFailedDo(result.getInfo());
        }
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.DEPOSITORY_RECHARGE_KEY + recharge.getBorrId(), "off", "NX", "EX", 3 * 60))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有充值在处理中，请稍后");
        }

        try {
            BorrowList borrow = borrowListMapper.getBorrowListByBorrId(recharge.getBorrId());
            //金额判断
            NoteResult canPay = canPayCollect(borrow, Double.parseDouble(recharge.getAmount()));
            if (!CodeReturn.SUCCESS_CODE.equals(canPay.getCode())) {
                return ResponseDo.newFailedDo(canPay.getInfo());
            }
            //判断当前合同是否有充值成功记录
            if (checkRechargeOrder(borrow.getId())) {
                return ResponseDo.newFailedDo("还款正在处理中，请稍候查看");
            }
            LoanOrderDO loanOrderDO = saveLoanOrder(recharge, Constants.payOrderType.DEPOSITORY_RECHARGE_TYPE);
            //调用存管充值
            RechargeRequest request = setRechargeRequest(recharge, loanOrderDO.getSerialNo());
            ResponseDo<BankDepositCommon> responseDo = qianQiDepositWebService.reCharge(request);
            log.info("存管充值请求返回参数 BankDepositCommon = " + responseDo);
            if (responseDo.getData() != null){
                //保存存管流水号
                loanOrderDO.setSid(responseDo.getData().getMerOrderNo());
                loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
            }
             if (CodeReturn.success != responseDo.getCode()){
                settlementUtil.deductFailAfter(loanOrderDO, responseDo.getInfo());
            }
            return responseDo;
        } catch (Exception e) {
            log.error("出错：" + e.getMessage(), e);
            return ResponseDo.newFailedDo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
        } finally {
            jedisCluster.del(RedisConst.DEPOSITORY_RECHARGE_KEY + recharge.getBorrId());

        }
    }

    @Override
    public ResponseDo<BankDepositCommon> depositoryWithdrawal(DepositoryTrade withdrawal, String fee) {
        log.info("[提现开始] -->走银行存管{} ", withdrawal);
        ResponseDo<String> result = new ResponseDo<>();
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.DEPOSITORY_WITHDRAWAL_KEY + withdrawal.getBorrId(), "off", "NX", "EX", 6))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有提现操作在处理中，请稍后");
        }

        try {
            BorrowList borrow = borrowListMapper.getBorrowListByBorrId(withdrawal.getBorrId());
            //判断当前合同是否有充值成功记录
            /*if (checkRechargeOrder(borrow.getId())){
                return ResponseDo.newFailedDo("还款正在处理中，请稍候查看");
            }*/
            // 生成流水号
            LoanOrderDO loanOrderDO = saveWithdrawalLoanOrder(withdrawal, Constants.payOrderType.DEPOSITORY_WITHDRAWAL_TYPE, fee);
            //生成手续费订单
            //手续费
            //String fee = qianQiDepositWebService.queryWithdrawCharge();
            saveFeeOrder(loanOrderDO, fee);
            WithdrawRequest request = setWithdrawRequest(withdrawal, loanOrderDO.getSerialNo());
            request.setIncomeAmt(fee);
            //调用存管充值 TODO
            ResponseDo<BankDepositCommon> responseDo = qianQiDepositWebService.withDraw(request);
            log.info("存管提现请求返回参数 BankDepositCommon = " + responseDo);
            //发起代付
            return responseDo;
        } catch (Exception e) {
            log.error("出错：" + e.getMessage(), e);
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
            result.setInfo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
            return ResponseDo.newFailedDo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
        } finally {
            jedisCluster.del(RedisConst.PAY_ORDER_KEY + withdrawal.getBorrId());

        }
    }


    @Override
    public ResponseDo<?> depositoryRepay(DepositoryTrade repay) {
        log.info("[存管还款开始] -->{} ", repay);
        ResponseDo<String> result;
        //请结算锁
        result = settleLock(String.valueOf(repay.getTriggerStyle()));
        if (AgentDeductResponseEnum.SUCCESS_CODE.getCode() != result.getCode()) {
            return result;
        }
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.DEPOSITORY_RECHARGE_KEY + repay.getBorrId(), "off", "NX", "EX", 3))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有还款在处理中，请稍后");
        }
        try {
            BorrowList borrow = borrowListMapper.getBorrowListByBorrId(repay.getBorrId());
            //更新代扣时间
            borrow.setCurrentRepayTime(new Date());
            borrowListMapper.updateByPrimaryKeySelective(borrow);
            //提前结清，正常结算这俩中类型做金额判断
            NoteResult canPay = canPayCollect(borrow, Double.parseDouble(repay.getAmount()));
            if (!CodeReturn.SUCCESS_CODE.equals(canPay.getCode())) {
                return ResponseDo.newFailedDo(canPay.getInfo());
            }
            LoanOrderDO loanOrderDO = saveLoanOrder(repay, Constants.payOrderType.DEPOSITORY_REPAY_TYPE);
            //调用还款接口 TODO
            RepaymentRequest request = setRepaymentRequest(repay, loanOrderDO.getSerialNo());
            ResponseDo<YoutuQuery> repayment = qianQiDepositBackService.repayment(request);
            log.info("还款返回结果 repayment" + repayment);
            //保存存管订单号
            if (repayment != null) {
                if (repayment.getData() != null) {
                    loanOrderDO.setSid(repayment.getData().getU2jinfuOrderNo());
                    loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
                }
                if (CodeReturn.success == repayment.getCode()) {
                    //后台触发 修改合同
                    if (loanOrderDO.getTriggerStyle().equals(PayTriggerStyleEnum.BACK_GROUND.getCode())) {
                        borrow.setRepayRetryFlag(Constant.REPAY_RETRY_FLAG_SUCCESS);
                        borrowListMapper.updateByPrimaryKeySelective(borrow);
                    }
                    settlementUtil.deductSuccessAfter(loanOrderDO);
                }
            }
            return repayment;
        } catch (Exception e) {
            log.error("出错：" + e.getMessage(), e);
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
            result.setInfo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
            return result;
        } finally {
            jedisCluster.del(RedisConst.DEPOSITORY_RECHARGE_KEY + repay.getBorrId());
        }
    }


    private LoanOrderDO saveLoanOrder(DepositoryTrade trade, String type) {
        LoanOrderDO loanOrder = new LoanOrderDO();
        loanOrder.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrder.setpId(0);
        loanOrder.setCompanyId(1);
        loanOrder.setBorrNum(trade.getBorrNum());
        loanOrder.setPerId(trade.getPerId());
        loanOrder.setOptAmount(new BigDecimal(trade.getAmount()));
        loanOrder.setActAmount(new BigDecimal(trade.getAmount()));
        loanOrder.setRlState("p");
        loanOrder.setSettleType(Short.parseShort("2"));
        loanOrder.setTriggerStyle(3);
        loanOrder.setType(type);
        loanOrder.setContractId(trade.getBorrId());
        loanOrder.setStatus("y");
        loanOrder.setCreationDate(new Date());
        loanOrder.setUpdateDate(new Date());
        loanOrder.setTriggerStyle(trade.getTriggerStyle());
        loanOrderDOMapper.insertSelective(loanOrder);
        return loanOrder;
    }

    private LoanOrderDO saveWithdrawalLoanOrder(DepositoryTrade trade, String type, String fee) {
        LoanOrderDO loanOrder = new LoanOrderDO();
        loanOrder.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrder.setpId(0);
        loanOrder.setCompanyId(1);
        loanOrder.setBorrNum(trade.getBorrNum());
        loanOrder.setPerId(trade.getPerId());
        loanOrder.setOptAmount(new BigDecimal(trade.getAmount()).subtract(new BigDecimal(fee)));
        loanOrder.setActAmount(new BigDecimal(trade.getAmount()).subtract(new BigDecimal(fee)));
        loanOrder.setRlState("p");
        loanOrder.setSettleType(Short.parseShort("2"));
        loanOrder.setTriggerStyle(3);
        loanOrder.setType(type);
        loanOrder.setContractId(trade.getBorrId());
        loanOrder.setStatus("y");
        loanOrder.setCreationDate(new Date());
        loanOrder.setUpdateDate(new Date());
        loanOrder.setTriggerStyle(trade.getTriggerStyle());
        loanOrderDOMapper.insertSelective(loanOrder);
        return loanOrder;
    }

    /**
     * 组装存管充值参数
     *
     * @return
     */
    private RechargeRequest setRechargeRequest(DepositoryTrade trade, String serialNo) {
        Person p = personMapper.getPersonByPhone(trade.getPhone());
        RechargeRequest request = new RechargeRequest();
        request.setAcctNo(p.getAcctNo());
        request.setAmount(trade.getAmount());
        request.setBaikaOrderNo(serialNo);
        request.setRegisterPhone(p.getPhone());
        request.setResponsePath(host + responsePath + "/" + p.getPhone());
        return request;
    }

    /**
     * 组装还款参数
     *
     * @return
     */
    private RepaymentRequest setRepaymentRequest(DepositoryTrade trade, String serialNo) {
        Person p = personMapper.getPersonByPhone(trade.getPhone());
        BorrowList nowBorr = borrowListMapper.selectByPrimaryKey(trade.getBorrId());
        RepaymentRequest request = new RepaymentRequest();
        request.setAmount(trade.getAmount());
        request.setBaikaOrderNo(serialNo);
        request.setBorrowId(nowBorr.getInvestBorrowId());
        request.setCapital(String.valueOf(nowBorr.getBorrAmount()));
        //商户管理费
        BigDecimal incomeAmt = nowBorr.getAccountManageSurplus().add(nowBorr.getInformationServiceSurplus());
        request.setIncomeAmt(String.valueOf(incomeAmt));
        request.setPayerAcctNo(p.getAcctNo());
        request.setPeriod("1");
        return request;
    }

    /**
     * 组装存管提现参数
     *
     * @return
     */
    private WithdrawRequest setWithdrawRequest(DepositoryTrade trade, String serialNo) {
        Person p = personMapper.getPersonByPhone(trade.getPhone());
        WithdrawRequest request = new WithdrawRequest();
        request.setAcctNo(p.getAcctNo());
        request.setAmount(trade.getAmount());
        request.setBaikaOrderNo(serialNo);
        request.setRegisterPhone(p.getPhone());
        request.setResponsePath(host + responseWithdrawalPath);
        return request;
    }

    /**
     * 判断是否有充值成功订单
     *
     * @param borrId
     * @return
     */
    private Boolean checkRechargeOrder(Integer borrId) {
        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        cia.andContractIdEqualTo(borrId);
        cia.andRlStateEqualTo("s");
        cia.andTypeEqualTo(Constants.payOrderType.DEPOSITORY_RECHARGE_TYPE);
        List<LoanOrderDO> list = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        return list != null && list.size() > 0;
    }

}
