package com.jhh.dc.baika.service.channel;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.mapper.app.BankMapper;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDOExample;
import com.jhh.dc.baika.mapper.manager.OrderMapper;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jhh.dc.baika.api.channel.TradePayService;
import com.jhh.dc.baika.api.channel.WithdrawalService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.TradeVo;
import com.jhh.dc.baika.api.entity.cash.WithdrawalVo;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.common.enums.AgentpayResultEnum;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.SerialNumUtil;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/4/16.
 */
@Service(interfaceClass = com.jhh.dc.baika.api.channel.WithdrawalService.class)
@Slf4j
public class WithdrawalServiceImpl extends BasePayServiceImpl implements WithdrawalService {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Autowired
    private TradePayService tradePayService;

    @Value("${callback_commissionWithdrawal_url}")
    private String callbackUrl;

    @Override
    public ResponseDo<Integer> getCommissionWithdrawal(WithdrawalVo vo) {
        log.error("\n[提现]---------提现 请求参数 WithdrawalVo ={}", vo);
        //幂等操作 防止重复放款
        String setnx = jedisCluster.set(RedisConst.COMMISSION_WITHDRAWAL_LOCK + vo.getPerId(), String.valueOf(vo.getPerId()), "NX", "EX", 60 * 5);
        if (!"OK".equals(setnx)) {
            log.error("\n[提现] redis锁没有获取,有其他线程对该用户进行操作perId= {}进行放款操作", vo.getPerId());
            return ResponseDo.newFailedDo(AgentpayResultEnum.DONT_GET_LOCK.getDesc());
        }
        try {
            //判断用户当前是否有佣金未处理完毕
            if (verifyCommissionOrder(vo.getPerId())) {
                return ResponseDo.newFailedDo("存在未完成的佣金提现，请先处理");
            }
            //获取银行卡id
            Bank bank = bankMapper.selectByBankNumAndStatus(vo.getBankNum());
            if (bank == null || !bank.getPerId().equals(vo.getPerId())) {
                return ResponseDo.newFailedDo("该银行卡不存在，请验证");
            }
            //生成订单
            LoanOrderDO loanOrder = savePayLoanOrder(vo, bank.getId(), Constants.payOrderType.COMMISSION_WITHDRAWAL_TYPE);
            String fee = codeValueMapper.getMeaningByTypeCode("payment_fee", "2");
            saveFeeOrder(loanOrder, fee);
            //发起付款
            TradeVo tradeVo = new TradeVo(vo.getPerId(), loanOrder.getSerialNo(), vo.getPayChannel(),
                    vo.getTriggerStyle(), vo.getBankNum(), vo.getAmount());
            ResponseDo<String> responseDo = tradePayService.postPayment(tradeVo);
            ResponseDo<Integer> result = new ResponseDo<>();
            result.setCode(responseDo.getCode());
            result.setInfo(responseDo.getInfo());
            result.setData(loanOrder.getId());
            return result;
        } catch (Exception e) {
            log.error("佣金提现出现异常----", e);
            return ResponseDo.newFailedDo("支付发生异常，请稍候再试");
        } finally {
            jedisCluster.del(RedisConst.COMMISSION_WITHDRAWAL_LOCK + vo.getPerId());
        }
    }

    @Override
    public ResponseDo<?> commissionState(String serNo) {
        log.info("-------------佣金提现查询订单号 " + serNo);
        try {
            ResponseDo<?> state = tradePayService.state(serNo);
            log.info("-------------佣金提现查询响应 response = " + state);
            if (state != null) {
                Order order = orderMapper.selectBySerial(serNo);
                Order feeOrder = orderMapper.selectByPid(order.getId());
                Map<String, String> commissionResult = new HashMap<>();
                if (Constants.DeductQueryResponseConstants.SUCCESS_CODE.equals(state.getCode())) {
                    updateOrder(order, feeOrder, "s",null);
                    //回调后台通知提现成功
                    commissionResult.put("reviewId", String.valueOf(order.getContractId()));
                    commissionResult.put("status", "1");
                } else if (Constants.DeductQueryResponseConstants.SUCCESS_ORDER_SETTLE_FAIL.equals(state.getCode())) {
                    updateOrder(order, feeOrder, "f",state.getInfo());
                    commissionResult.put("reviewId", String.valueOf(order.getContractId()));
                    commissionResult.put("status", "0");
                }
                log.info("佣金提现回调请求参数----------------commissionResult----" + commissionResult);
                AsyncExecutor.execute(new PostCallBack(commissionResult));
                return state;
            } else {
                return ResponseDo.newFailedDo("订单查询失败，请稍候再试");
            }
        } catch (Exception e) {
            log.error("佣金提现查询出现异常", e);
            return ResponseDo.newFailedDo("订单查询失败，请稍候再试");
        }
    }

    private final class PostCallBack extends AbstractSimpleRunner {

        Map<String, String> commissionResult;

        PostCallBack(Map<String, String> commissionResult) {
            this.commissionResult = commissionResult;
        }

        @Override
        public boolean doExecute() {
            String result = HttpUrlPost.sendPost(callbackUrl, commissionResult);
            log.info("佣金回调参数响应-----------------\n" + result);
            JSONObject obj = JSONObject.parseObject(result);
            if ("200".equals(obj.getString("status"))) {
                return true;
            } else {
                AsyncExecutor.delayExecute(new PostCallBack(commissionResult), 5, TimeUnit.MINUTES);
            }
            return true;
        }
    }

    private boolean verifyCommissionOrder(int perId) {
        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        cia.andPerIdEqualTo(perId);
        cia.andRlStateEqualTo("p");
        cia.andTypeEqualTo(Constants.payOrderType.COMMISSION_WITHDRAWAL_TYPE);
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        return loanOrderDOList != null && loanOrderDOList.size() > 0;

    }

    private void updateOrder(Order order, Order feeOrder, String rlState,String msg) {
        order.setRlState(rlState);
        order.setRlRemark(msg);
        order.setRlDate(new Date());
        order.setUpdateDate(new Date());
        feeOrder.setRlState(rlState);
        feeOrder.setRlRemark(msg);
        feeOrder.setRlDate(new Date());
        feeOrder.setUpdateDate(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        orderMapper.updateByPrimaryKeySelective(feeOrder);
    }

    private LoanOrderDO savePayLoanOrder(WithdrawalVo vo, int bankId, String type) {
        LoanOrderDO loanOrder = new LoanOrderDO();
        loanOrder.setSerialNo(SerialNumUtil.createByType("dc" + String.format("%02d", Integer.parseInt(type))));
        loanOrder.setpId(0);
        loanOrder.setCompanyId(1);
        loanOrder.setPerId(vo.getPerId());
        loanOrder.setBankId(bankId);
        loanOrder.setOptAmount(new BigDecimal(vo.getAmount().toString()));
        loanOrder.setActAmount(new BigDecimal(vo.getAmount().toString()));
        loanOrder.setRlState("p");
        loanOrder.setType(type);
        loanOrder.setContractId(vo.getContractId());
        loanOrder.setStatus("y");
        loanOrder.setCreationDate(new Date());
        loanOrder.setUpdateDate(new Date());
        loanOrder.setTriggerStyle(vo.getTriggerStyle());
        loanOrderDOMapper.insertSelective(loanOrder);
        return loanOrder;
    }

}
