package com.jhh.dc.baika.service.capital;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.mapper.manager.RepaymentPlanMapper;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.contract.ElectronicContractService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.common.enums.AgentpayResultEnum;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.JdCardInfo;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.loan.PerAccountLog;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.entity.utils.BorrPerInfo;
import com.jhh.dc.baika.entity.utils.RepaymentDetails;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.loan.PerAccountLogMapper;
import com.jhh.dc.baika.mapper.manager.OrderMapper;
import com.jhh.dc.baika.util.PostAsync;
import com.jinhuhang.settlement.dto.RepaymentFromYoutu;
import com.jinhuhang.settlement.dto.SettlementResult;
import com.jinhuhang.settlement.service.SettlementAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 2018/3/28.
 */
@Slf4j
@Service
public class BaseServiceImpl {


    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BorrowListMapper borrMapper;

    @Autowired
    private PerAccountLogMapper palMapper;

    @Autowired
    private RepaymentPlanMapper rpMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SettlementAPI settlementAPI;

    @Autowired
    SmsService smsService;

    @Autowired
    ElectronicContractService electronicContractService;

    @Value("${A.synchrodata.borrowUrl}")
    private String borrowUrl;

    /**
     * 悠兔理财放款成功回调
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void youtuCallbackLoanSucc(String orderId, RepaymentFromYoutu repaymentFromYoutu){
        // 幂等操作，统一订单只处理一遍
        if (StringUtils.isEmpty(jedisCluster.get(RedisConst.ORDER_KEY + orderId))) {
            String setnx = jedisCluster.set(RedisConst.ORDER_KEY + orderId, orderId, "NX", "EX", 60 * 5);
            if (!"OK".equals(setnx)) {
                log.error("直接返回，重复数据orderId" + orderId);
                return;
            }

        } else {
            log.error("直接返回，重复数据orderId" + orderId);
            return;
        }
        // orderId = "0116120210154754988";
        // 修改订单状态为s，订单更新时间
        try {
            Order order = new Order();
            Order order1 = new Order();
            order = orderMapper.selectBySerial(orderId);
            order1 = orderMapper.selectByPid(order.getId());

            // 幂等操作，还款计划只生成一条
            if (StringUtils.isEmpty(jedisCluster.get(RedisConst.REPAYMENT_KEY + order.getContractId()))) {

                String setnx = jedisCluster.set(RedisConst.REPAYMENT_KEY + order.getContractId(), order.getContractId().toString(), "NX", "EX", 60 * 5);
                if (!"OK".equals(setnx)) {
                    log.error("setnx失败，直接返回，重复数据还款计划，borrId" + order.getContractId());
                    return;
                }

            } else {
                log.error("直接返回，重复数据还款计划，borrId" + order.getContractId());
                return;
            }
            order.setRlState("s");
            order.setRlDate(new Date());
            order.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            if (order1 != null) {
                order1.setRlState("s");
                order1.setRlDate(new Date());
                order1.setUpdateDate(new Date());
                orderMapper.updateByPrimaryKeySelective(order1);
            }
            log.info("修改订单状态为s成功》插入一条资金记录开始...");
            PerAccountLog pal = new PerAccountLog();
            pal.setPerId(order.getPerId());
            pal.setOrderId(order.getId());
            pal.setOperationType(order.getType());
            pal.setAmount(order.getActAmount());
            pal.setRemark("收款");
            pal.setAddtime(new Date());
            palMapper.insertSelective(pal);
            log.info("增加一条ym_per_account_log新增一条资金明细成功》修改合同状态开始...");


            // 获得合同信息
            Calendar calendar = new GregorianCalendar();
            BorrowList borr = borrMapper.selectByPrimaryKey(order.getContractId());
            // 合同和人的信息
            BorrPerInfo bpi = borrMapper.selectByBorrId(borr.getId());

//            RepaymentDetails rd = new RepaymentDetails();
//            rd = borrMapper.getRepaymentDetails(String.valueOf(borr.getId()));
//            if (null == rd.getCouAmount() || "".equals(rd.getCouAmount())) {
//                rd.setCouAmount("0.00");
//            }
            log.info("successCaozuo------borr:" + JSON.toJSON(borr));


            // 插入一条还款计划
             SettlementResult settlementResult = settlementAPI.createRepayment(repaymentFromYoutu);
            if("0".equals(settlementResult.getCode())){
                log.info("\n插入还款计划失败！borrowId{}",borr.getId());
                throw new RuntimeException("插入还款计划失败");
            }else {
                log.error("\n插入还款计划成功！borrowId{}",borr.getId());
            }

            log.info("successCaozuo------RepaymentPlan:" + JSON.toJSON(borr));


            log.info("插入还款计划成功》发送消息给用户开始...");
            // 发送消息给用户 TODO:目前没有站内信
            DecimalFormat df = new DecimalFormat("######0.00");
            if (CodeReturn.PRODUCT_TYPE_CODE_MONEY.equals(bpi.getProdType())){
                smsService.sendSms(SmsTemplateEnum.DC_LOAN_SUCCESS.getCode(),bpi.getPhone(),bpi.getName(),df.format(bpi.getMaximum_amount()),bpi.getTotalTermNum(),bpi.getPlanrepay_date());
            }else {
                smsService.sendSms(SmsTemplateEnum.LOAN_CARD_SUCCESS.getCode(),bpi.getPhone(),bpi.getName(),df.format(bpi.getMaximum_amount()),bpi.getPlanrepay_date());
            }
        } catch (java.lang.Exception e) {
            log.error("\n-------代付查询成功后出现异常",e);
            throw new RuntimeException("代付查询成功后出现异常");
        }


    }

    /**
     * 第三方操作成功进行的数据库操作
     *
     * @param orderId
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void paySuccessAfter(String orderId)  {
        // 幂等操作，统一订单只处理一遍
        if (StringUtils.isEmpty(jedisCluster.get(RedisConst.ORDER_KEY + orderId))) {
            String setnx = jedisCluster.set(RedisConst.ORDER_KEY + orderId, orderId, "NX", "EX", 60 * 5);
            if (!"OK".equals(setnx)) {
                log.error("直接返回，重复数据orderId" + orderId);
                return;
            }

        } else {
            log.error("直接返回，重复数据orderId" + orderId);
            return;
        }


        // orderId = "0116120210154754988";
        // 修改订单状态为s，订单更新时间
        try {
            Order order = new Order();
            Order order1 = new Order();
            order = orderMapper.selectBySerial(orderId);
            order1 = orderMapper.selectByPid(order.getId());

            // 幂等操作，还款计划只生成一条
            if (StringUtils.isEmpty(jedisCluster.get(RedisConst.REPAYMENT_KEY + order.getContractId()))) {

                String setnx = jedisCluster.set(RedisConst.REPAYMENT_KEY + order.getContractId(), order.getContractId().toString(), "NX", "EX", 60 * 5);
                if (!"OK".equals(setnx)) {
                    log.error("setnx失败，直接返回，重复数据还款计划，borrId" + order.getContractId());
                    return;
                }

            } else {
                log.error("直接返回，重复数据还款计划，borrId" + order.getContractId());
                return;
            }


            order.setRlState("s");
            order.setRlDate(new Date());
            order.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            if (order1 != null) {
                order1.setRlState("s");
                order1.setRlDate(new Date());
                order1.setUpdateDate(new Date());
                orderMapper.updateByPrimaryKeySelective(order1);
            }
            log.info("修改订单状态为s成功》插入一条资金记录开始...");
            PerAccountLog pal = new PerAccountLog();
            pal.setPerId(order.getPerId());
            pal.setOrderId(order.getId());
            pal.setOperationType(order.getType());
            pal.setAmount(order.getActAmount());
            pal.setRemark("收款");
            pal.setAddtime(new Date());
            palMapper.insertSelective(pal);
            log.info("增加一条ym_per_account_log新增一条资金明细成功》修改合同状态开始...");


            // 获得合同信息
            Calendar calendar = new GregorianCalendar();
            BorrowList borr = borrMapper.selectByPrimaryKey(order.getContractId());
            // 合同和人的信息
            BorrPerInfo bpi = borrMapper.selectByBorrId(borr.getId());

            RepaymentDetails rd = new RepaymentDetails();
            rd = borrMapper.getRepaymentDetails(String.valueOf(borr.getId()));
            if (null == rd.getCouAmount() || "".equals(rd.getCouAmount())) {
                rd.setCouAmount("0.00");
            }
            log.info("successCaozuo------borr:" + JSON.toJSON(borr));


            // 插入一条还款计划
           // SettlementResult settlementResult = settlementAPI.createRepayment(borr.getId(),new Date());
            SettlementResult settlementResult =null;
            if("0".equals(settlementResult.getCode())){
                log.info("\n插入还款计划失败！borrowId{}",borr.getId());
                throw new RuntimeException("插入还款计划失败");
            }else {
                log.error("\n插入还款计划成功！borrowId{}",borr.getId());
            }

            log.info("successCaozuo------RepaymentPlan:" + JSON.toJSON(borr));


            log.info("插入还款计划成功》发送消息给用户开始...");
            // 发送消息给用户 TODO:目前没有站内信
            DecimalFormat df = new DecimalFormat("######0.00");
            if (CodeReturn.PRODUCT_TYPE_CODE_MONEY.equals(bpi.getProdType())){
                smsService.sendSms(SmsTemplateEnum.DC_LOAN_SUCCESS.getCode(),bpi.getPhone(),bpi.getName(),df.format(bpi.getMaximum_amount()),bpi.getTotalTermNum(),bpi.getPlanrepay_date());
            }else {
               smsService.sendSms(SmsTemplateEnum.LOAN_CARD_SUCCESS.getCode(),bpi.getPhone(),bpi.getName(),df.format(bpi.getMaximum_amount()),bpi.getPlanrepay_date());
            }
            //成功需要生成电子合同
            electronicContractService.createElectronicContract(order.getContractId());
            log.info("\n生成电子合同成功perId:" + order.getContractId());

        } catch (java.lang.Exception e) {
            log.error("\n-------代付查询成功后出现异常",e);
            throw new RuntimeException("代付查询成功后出现异常");
        }
    }


    /**
     * 第三方操作失败进行的数据库操作
     *
     * @param orderId
     * @param result_msg
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
    public void payFailAfter(String orderId, String result_msg) {
        // 修改订单状态为f，订单更新时间
        Order order = new Order();
        Order order1 = new Order();
        try {
            order = orderMapper.selectBySerial(orderId);
            order1 = orderMapper.selectByPid(order.getId());
            order.setRlState("f");
            order.setRlDate(new Date());
            order.setReason(result_msg);
            order.setUpdateDate(new Date());
            order1.setRlState("f");
            order1.setRlDate(new Date());
            order1.setReason(result_msg);
            order1.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            orderMapper.updateByPrimaryKeySelective(order1);
            // 合同状态改为放款失败(只有在借款状态为放款中时才能改为失败)
            BorrowList record = borrMapper.selectByPrimaryKey(order.getContractId());
            if (CodeReturn.STATUS_COM_PAYING.equals(record.getBorrStatus())){
                record.setBorrStatus(CodeReturn.STATUS_COM_PAY_FAIL);
                record.setUpdateDate(new Date());
                int i = borrMapper.updateByPrimaryKeySelective(record);
                if (i > 0) {
                    //向A同步
                    AsyncExecutor.execute(new PostAsync<>(record, borrowUrl,borrMapper));
                    log.error("\n[代付查询]更改借款状态为放款失败" + record.getId());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 退款操作成功数据库
     * @param serialNo
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void refundSuccessAfter(String serialNo){
        // 幂等操作，统一订单只处理一遍
        if (StringUtils.isEmpty(jedisCluster.get(RedisConst.ORDER_REFUND_KEY + serialNo))) {
            String setnx = jedisCluster.set(RedisConst.ORDER_REFUND_KEY + serialNo, serialNo, "NX", "EX", 60 * 5);
            if (!"OK".equals(setnx)) {
                log.error("直接返回，重复数据流水号" + serialNo);
                return;
            }
        } else {
            log.error("直接返回，重复数据流水号" + serialNo);
            return;
        }

        try {
            Order order =orderMapper.selectBySerial(serialNo);
            Order order1 =orderMapper.selectByPid(order.getId());

            order.setRlState("s");
            order.setRlDate(new Date());
            order.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            order1.setRlState("s");
            order1.setRlDate(new Date());
            order1.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order1);

        } catch (java.lang.Exception e) {
            log.error("\n-------退款查询成功后出现异常",e);
            throw new RuntimeException("退款查询成功后出现异常");
        }
    }

    /**
     * 第三方操作失败数据库操作
     * @param serialNo
     * @param msg
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void refundFailAfter(String serialNo,String msg){
        // 修改订单状态为f，订单更新时间
        Order order =  orderMapper.selectBySerial(serialNo);
        Order order1 = orderMapper.selectByPid(order.getId());
        try {
            order.setRlState("f");
            order.setRlDate(new Date());
            order.setReason(msg);
            order.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            order1.setRlState("f");
            order1.setRlDate(new Date());
            order1.setReason(msg);
            order1.setUpdateDate(new Date());
            orderMapper.updateByPrimaryKeySelective(order1);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 扣款前验证是否可以扣款
     *
     * @param borrId
     * @return
     */
    public NoteResult canPayCollect(String borrId, double thisAmount) {

        NoteResult result = new NoteResult(CodeReturn.FAIL_CODE, "失败");
        try {
            // 查出应还金额减去p状态还款订单总额=剩余可还金额
            double canPay = rpMapper.selectCanPay(borrId);
            log.error("本次提交borrId:" + borrId + ",本次提交金额：" + thisAmount + ",剩余应还金额：" + canPay);

            // 如果本次订单金额大于剩余可还金额 不允许还款及代扣
            if (canPay == 0 || thisAmount > canPay) {
                result.setInfo("还款金额超限，您当前可还最大金额为" + canPay + "元");
                result.setData(canPay);
                return result;
            } else {
                result.setCode(CodeReturn.SUCCESS_CODE);
                result.setData(canPay);
                return result;
            }
        } catch (Exception e) {
            log.error("出错：",e);
            return new NoteResult(CodeReturn.FAIL_CODE, "失败");
        }

    }


}
