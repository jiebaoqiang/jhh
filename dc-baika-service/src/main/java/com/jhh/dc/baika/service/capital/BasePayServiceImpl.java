package com.jhh.dc.baika.service.capital;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jhh.dc.baika.entity.PersonInfoDto;
import com.jhh.dc.baika.mapper.app.BankMapper;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDOExample;
import com.jhh.dc.baika.mapper.manager.RepaymentPlanMapper;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.BindCardVo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.entity.capital.AgentRefundRequest;
import com.jhh.dc.baika.api.loan.BankService;
import com.jhh.dc.baika.common.enums.AgentDeductResponseEnum;
import com.jhh.dc.baika.common.enums.AgentpayResultEnum;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.enums.TradeStatusEnum;
import com.jhh.dc.baika.common.util.BorrNum_util;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.SerialNumUtil;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.utils.RepaymentDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代付通用类
 */
@Slf4j
@Service
public class BasePayServiceImpl extends BaseServiceImpl {

    @Value("${isTest}")
    private String isTest;


    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private BankService bankService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private RepaymentPlanMapper rpMapper;

    @Autowired
    private SettlementUtil settlementUtil;

    /**
     * 清结算锁
     *
     * @param triggerStyle
     * @return
     */
    protected ResponseDo<String> settleLock(String triggerStyle) {
        //清结算锁
        if (!PayTriggerStyleEnum.AUTO.getCode().toString().equals(triggerStyle)) {
            String settleLock = jedisCluster.get(RedisConst.SETTLE_LOCK_KEY);
            if (!com.alibaba.dubbo.common.utils.StringUtils.isEmpty(settleLock) && "off".equals(settleLock)) {
                log.info("凌晨0点-5点开始为系统维护时间，期间不能进行还款，给您带来的不便敬请谅解！");
                return ResponseDo.newFailedDo("凌晨0点-5点开始为系统维护时间，期间不能进行还款，给您带来的不便敬请谅解！");
            }
        }
        return ResponseDo.newSuccessDo();
    }

    /**
     * 表单重复提交
     *
     * @param borrId
     * @return
     */
    protected ResponseDo<?> formLock(String borrId) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(jedisCluster.get(RedisConst.PAYCONT_KEY + borrId))) {

            String setnx = jedisCluster.set(RedisConst.PAYCONT_KEY + borrId, borrId, "NX", "EX", 60 * 5);
            if (!"OK".equals(setnx)) {

                log.warn("\n[代付] redis锁没有获取,有其他线程正在对此合同borrId {}进行放款操作", borrId);
                return ResponseDo.newFailedDo(AgentpayResultEnum.DONT_GET_LOCK.getDesc());
            }
        } else {
            log.warn("直接调银生宝重复数据, 不可重复放款 orderId:{}", borrId);
            return ResponseDo.newFailedDo(AgentpayResultEnum.DONT_GET_LOCK.getDesc());
        }
        return ResponseDo.newSuccessDo();
    }

    protected PersonInfoDto getPersonInfo(String borrId) {
        BorrowList bo = borrowListMapper.selectByPrimaryKey(Integer.parseInt(borrId));
        // 获取用户信息
        Person p = personMapper.selectByPrimaryKey(bo.getPerId());
        // 获取还款详情
        RepaymentDetails rd = borrowListMapper.getRepaymentDetails(borrId);
        return new PersonInfoDto(bo, p, rd);
    }

    /**
     * 更新 request信息
     *
     * @param request
     */
    protected ResponseDo<?> updateAgentDeductRequest(AgentDeductRequest request, BorrowList borrow, Person person) {
        request.setBorrNum(borrow.getBorrNum());
        request.setPhone(person.getPhone());
        request.setName(person.getName());
        request.setIdCardNo(person.getCardNum());
        //验证是否为支付宝渠道 支付宝渠道可能无银行卡
        if (!verifyZFBChannel(request.getPayChannel())) {
            if (StringUtils.isEmpty(request.getBankNum())) {
                //如果用户没传卡，去数据查询主卡进行扣款
                Bank bank = bankMapper.selectPrimayCardByPerId(String.valueOf(person.getId()));
                if (bank == null) {
                    return ResponseDo.newFailedDo("没有查到主卡，请传银行卡号跟银行ID");
                } else {
                    request.setBankNum(bank.getBankNum());
                    request.setBanklistId(bank.getBankId());
                    request.setBankId(String.valueOf(bank.getId()));
                }
            } else {
                //查询该应行卡是否绑定
                Bank bank = bankMapper.selectByBankNumAndStatus(request.getBankNum());
                if (bank == null) {
                    BindCardVo vo = new BindCardVo();
                    vo.setPer_id(String.valueOf(person.getId()));
                    vo.setBankName(request.getBankName());
                    vo.setBank_num(request.getBankNum());
                    vo.setBankCode(request.getBankCode());
                    vo.setPhone(request.getPhone());
                    vo.setStatus("2");
                    vo.setTokenKey("");
                    vo.setDevice("android");
                    ResponseDo<?> noteResult = bankService.payCenterBindCard(vo);
                    if (noteResult.getCode() != 200) {
                        return ResponseDo.newFailedDo(noteResult.getInfo());
                    }
                }
                bank = bankMapper.selectByBankNumAndStatus(request.getBankNum());
                request.setBankId(String.valueOf(bank.getId()));
                request.setBanklistId(bank.getBankId());


            }
        }
        return ResponseDo.newSuccessDo();
    }

    /**
     * 验证渠道是否为支付宝并随便复个银行卡号
     *
     * @param
     */
    private boolean verifyZFBChannel(String payChannel) {
        return Constants.payOrderType.PAYCENTER_CHANNEL_ZFB.equals(payChannel);
    }


    /**
     * 保存流水订单
     *
     * @param request
     */
    protected LoanOrderDO saveDeductLoanOrder(AgentDeductRequest request, Integer perId, Integer borrowId, String type, int payChannel) {
        LoanOrderDO loanOrder = new LoanOrderDO();
        if (request.getTriggerStyle() != null) {
            loanOrder.setTriggerStyle(Integer.parseInt(request.getTriggerStyle()));
        }
        if (request.getDeductionsType() != null) {
            loanOrder.setDeductionsType(Integer.parseInt(request.getDeductionsType()));
        }
        loanOrder.setpId(0);
        loanOrder.setBorrNum(request.getBorrNum());
        loanOrder.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrder.setType(String.valueOf(type));
        loanOrder.setPerId(perId);
        loanOrder.setCompanyId(1);
        //如果为支付宝渠道 则没有银行卡信息
        if (!Constants.payOrderType.PAYCENTER_CHANNEL_ZFB.equals(request.getPayChannel())) {
            loanOrder.setBankId(Integer.parseInt(request.getBankId()));
        }
        loanOrder.setContractId(borrowId);
        loanOrder.setOptAmount(new BigDecimal(request.getOptAmount()));
        loanOrder.setActAmount(new BigDecimal(request.getOptAmount()));
        if (StringUtils.isNotEmpty(request.getDescription())) {
            loanOrder.setRlRemark(request.getDescription());
        }
        loanOrder.setRlState("p");
        loanOrder.setSettleType(Short.parseShort(request.getType()));
        //2017-07-28新加两个参数
        loanOrder.setCreateUser(request.getCreateUser());
        loanOrder.setCollectionUser(request.getCollectionUser());
        loanOrder.setPayChannel(payChannel);
        //新增逾期天数
        BorrowList borrowList = borrowListMapper.selectByPrimaryKey(borrowId);
        loanOrder.setOverdueDays(borrowList.getOverdueDays());
        loanOrder.setProdId(borrowList.getProdId());
        loanOrderDOMapper.insertSelective(loanOrder);
        return loanOrder;
    }

    /**
     * 判断用户代扣合法性
     *
     * @param borrowList
     * @param thisAmount
     * @return
     */
    protected NoteResult canPayCollect(BorrowList borrowList, double thisAmount) {
        NoteResult result = NoteResult.SUCCESS_RESPONSE();
        //计算提前结清金额:计算公式：后几期租金+合同的当前日期需要的金额+手机赎回金额-押金
        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        cia.andContractIdEqualTo(borrowList.getId());
        cia.andRlStateEqualTo("p");
        //提现不影响
        cia.andTypeNotEqualTo(Constants.payOrderType.DEPOSITORY_WITHDRAWAL_TYPE);
        cia.andTypeNotEqualTo(Constants.payOrderType.DEPOSITORY_FEE);
        List<LoanOrderDO> list = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        if (list != null && list.size() > 0) {
            result.setCode("201");
            result.setInfo("当前有订单正在处理中，请稍后查看订单状态");
            log.error("可能是非法调用,borrowListId {}", borrowList.getId());
            return result;
        }
        result = canPayCollect(borrowList.getId().toString(), thisAmount);
        return result;
    }


    /**
     * 判断代付合法性
     *
     * @param borrowId
     * @param userId
     * @return
     */
    protected boolean checkAgentPayLog(String borrowId, Integer userId) {
        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        cia.andContractIdEqualTo(Integer.valueOf(borrowId));
        cia.andPerIdEqualTo(userId);
        //放款
        List<String> list = new ArrayList<>();
        list.add(Constants.payOrderType.YSB_PAY_TYPE);
        list.add(Constants.payOrderType.HAIER_PAY_TYPE);
        list.add(Constants.payOrderType.PAYCENTER_PAY_TYPE);
        list.add(Constants.payOrderType.JD_PAY_CARD);
        list.add(Constants.payOrderType.BAIKA_PAY_TYPE);
        cia.andTypeIn(list);
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        if (loanOrderDOList.size() > 0) {

            for (LoanOrderDO loanOrderDO : loanOrderDOList) {
                if ((!TradeStatusEnum.F.getCode().equals(loanOrderDO.getRlState()))) {
                    return false;
                }
            }

        }

        return true;
    }

    protected LoanOrderDO savePayLoanOrder(PersonInfoDto dto, String type, int triggerStyle, int payChannel) {
        BorrowList borrowList = dto.getBorrowList();
        LoanOrderDO loanOrderDO1 = new LoanOrderDO();
        loanOrderDO1.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrderDO1.setpId(0);
        loanOrderDO1.setBorrNum(borrowList.getBorrNum());
        loanOrderDO1.setCompanyId(1);
        loanOrderDO1.setPerId(borrowList.getPerId());
        loanOrderDO1.setProdId(borrowList.getProdId());
        loanOrderDO1.setBankId(Integer.parseInt(dto.getRepaymentDetails().getBankId()));
        loanOrderDO1.setContractId(borrowList.getId());
        loanOrderDO1.setOptAmount(new BigDecimal(borrowList.getPayAmount().toString()));
        loanOrderDO1.setActAmount(new BigDecimal(borrowList.getPayAmount().toString()));
        loanOrderDO1.setRlState("p");
        loanOrderDO1.setType(type);
        loanOrderDO1.setStatus("y");
        loanOrderDO1.setCreationDate(new Date());
        loanOrderDO1.setUpdateDate(new Date());
        loanOrderDO1.setPayChannel(payChannel);
        loanOrderDO1.setTriggerStyle(triggerStyle);
        loanOrderDOMapper.insertSelective(loanOrderDO1);
        return loanOrderDO1;
    }

    /**
     * 保存手续费订单
     *
     * @param loanOrder
     * @param fee
     */
    protected LoanOrderDO saveFeeOrder(LoanOrderDO loanOrder, String fee) {
        LoanOrderDO feeOrder = new LoanOrderDO();
        feeOrder.setGuid(BorrNum_util.createBorrNum());
        //手续费订单的pid为主订单id
        feeOrder.setTriggerStyle(loanOrder.getTriggerStyle());
        feeOrder.setpId(loanOrder.getId());
        feeOrder.setBorrNum(loanOrder.getBorrNum());
        feeOrder.setSerialNo(SerialNumUtil.createByType("dc03"));
        feeOrder.setPerId(loanOrder.getPerId());
        feeOrder.setProdId(loanOrder.getProdId());
        feeOrder.setCompanyId(loanOrder.getCompanyId());
        feeOrder.setContractId(loanOrder.getContractId());
        feeOrder.setPayChannel(loanOrder.getPayChannel());
        feeOrder.setBankId(loanOrder.getBankId());
        BigDecimal feeBG = BigDecimal.valueOf(Double.parseDouble(fee));
        feeOrder.setOptAmount(feeBG);
        feeOrder.setActAmount(feeBG);
        feeOrder.setRlState("p");
        feeOrder.setType("3");//手续费
        feeOrder.setSettleType(loanOrder.getSettleType());
        feeOrder.setCreationDate(new Date());
        feeOrder.setUpdateDate(new Date());
        loanOrderDOMapper.insertSelective(feeOrder);
        return feeOrder;
    }

    protected void handleSuccess(LoanOrderDO order) {
        order = loanOrderDOMapper.selectByPrimaryKey(order.getId());
        if (Constants.payOrderType.YSB_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.HAIER_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.PAYCENTER_PAY_TYPE.equals(order.getType())) {
            paySuccessAfter(order.getSerialNo());
        } else if (Constants.payOrderType.YSB_REFUND_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.HAIER_REFUND_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.PAYCENTER_REFUND_PAY_TYPE.equals(order.getType())) { // 退款
            refundSuccessAfter(order.getSerialNo());
        } else {
            settlementUtil.deductSuccessAfter(order);
        }
    }

    protected void handleFail(LoanOrderDO order, String msg) {
        if (Constants.payOrderType.YSB_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.HAIER_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.PAYCENTER_PAY_TYPE.equals(order.getType())) {
            payFailAfter(order.getSerialNo(), msg);
        } else if (Constants.payOrderType.YSB_REFUND_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.HAIER_REFUND_PAY_TYPE.equals(order.getType())
                || Constants.payOrderType.PAYCENTER_REFUND_PAY_TYPE.equals(order.getType())) { // 退款
            refundFailAfter(order.getSerialNo(), msg);
        } else {
            settlementUtil.deductFailAfter(order, msg);
        }
    }

    protected void afterStateHandle(ResponseDo<?> state, String serNO) throws Exception {
        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(serNO);
        if (Constants.DeductQueryResponseConstants.SUCCESS_CODE.equals(state.getCode())) {
            handleSuccess(loanOrderDO);
        } else if (Constants.DeductQueryResponseConstants.SUCCESS_ORDER_SETTLE_FAIL.equals(state.getCode())) {
            handleFail(loanOrderDO, state.getInfo());
        }
    }

    /**
     * 校验流水的处理状态
     *
     * @param perId 用户Id
     * @param type  类型
     */
    protected boolean verifyLoanOrderStatus(Integer perId, String type) {
        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        cia.andPerIdEqualTo(perId);
        cia.andRlStateEqualTo("p");
        cia.andTypeEqualTo(type);
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        return loanOrderDOList != null && loanOrderDOList.size() > 0;
    }

    protected LoanOrderDO savePayLoanOrder(AgentRefundRequest refund, int bankId, String type, int payChannel) {
        LoanOrderDO loanOrder = new LoanOrderDO();
        loanOrder.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrder.setpId(0);
        loanOrder.setCompanyId(1);
        loanOrder.setPerId(refund.getPerId());
        loanOrder.setBankId(bankId);
        loanOrder.setOptAmount(refund.getAmount());
        loanOrder.setActAmount(refund.getAmount());
        loanOrder.setRlState("p");
        loanOrder.setType(type);
//        loanOrder.setContractId(vo.getContractId());
        loanOrder.setStatus("y");
        loanOrder.setCreationDate(new Date());
        loanOrder.setUpdateDate(new Date());
        loanOrder.setPayChannel(payChannel);
        loanOrder.setTriggerStyle(refund.getTriggerStyle());
        loanOrderDOMapper.insertSelective(loanOrder);
        return loanOrder;
    }

    /**
     * 更改失败订单
     *
     * @param loanOrderDO 订单号
     * @param msg         失败原因
     */
    protected void updateOrderForFail(LoanOrderDO loanOrderDO, String msg) {
        LoanOrderDO order1 = loanOrderDOMapper.selectSubOrderByPid(loanOrderDO.getId());
        loanOrderDO.setRlState("f");
        loanOrderDO.setRlDate(new Date());
        loanOrderDO.setReason(msg);
        loanOrderDO.setUpdateDate(new Date());
        order1.setRlState("f");
        order1.setRlDate(new Date());
        order1.setReason(msg);
        order1.setUpdateDate(new Date());
        loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
        loanOrderDOMapper.updateByPrimaryKeySelective(order1);
    }
}
