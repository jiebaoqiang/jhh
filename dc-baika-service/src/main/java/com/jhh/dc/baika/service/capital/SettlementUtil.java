package com.jhh.dc.baika.service.capital;


import com.alibaba.fastjson.JSON;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.manager.CollectorsListMapper;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.loan.PerAccountLog;
import com.jhh.dc.baika.entity.utils.BorrPerInfo;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.mapper.loan.PerAccountLogMapper;
import com.jhh.dc.baika.service.sms.SmsServiceImpl;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;
import com.jinhuhang.settlement.dto.SettleDto;
import com.jinhuhang.settlement.dto.SettlementResult;
import com.jinhuhang.settlement.service.SettlementAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author xuepengfei
 */
@Component
public class SettlementUtil {

    private static final Logger logger = LoggerFactory.getLogger(SettlementUtil.class);


    @Autowired
    private BorrowListMapper borrowListMapper;


    @Autowired
    private PerAccountLogMapper perAccountLogMapper;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private CollectorsListMapper collectorsListMapper;


    @Autowired
    private SettlementAPI settlementAPI;

    @Autowired
    private SmsServiceImpl smsService;

    @Autowired
    private UserService userService;

    private String isTest = PropertiesReaderUtil.read("third", "isTest");

    private static final String YSH_DK_SMS_DATE_PATTERN = "yyyy年MM月dd日 ahh:mm";

    /**
     * 代扣成功后的操作
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    @Transactional
    public String deductSuccessAfter(LoanOrderDO orderDO) {
        LoanOrderDO order;
        LoanOrderDO feeOrder;
        try {
            short settleType = orderDO.getSettleType();
            SettleDto settle = new SettleDto();
            settle.setAmount(orderDO.getOptAmount());
            settle.setBid(orderDO.getId());
            settle.setBorrowid(orderDO.getContractId());
            settle.setType(orderDO.getSettleType());
            //转换settletype，
            switch (settleType) {
                //正常还款
                case 2:
                    settle.setType((short) 0);
                    break;
                //提前结清
                case 1:
                    settle.setType((short) 1);
                    break;
                default:
                    throw new Exception("不支持的还款类型settleType：" + settleType);
            }
            logger.info("开始清结算处理:params:{}", settle.toString());
            SettlementResult result1 = settlementAPI.settle(settle);

            logger.info("清结算处理结果:result:code,{},msg,{}", result1.getCode(), result1.getMsg());
            order = loanOrderDOMapper.selectByPrimaryKey(orderDO.getId());
            feeOrder = loanOrderDOMapper.selectSubOrderByPid(order.getId());
            if (result1.getCode() == 1) {
                //更改订单状态为清结算成功
                loanOrderDOMapper.updateStatusById(order.getId(), "s", null);
                if (feeOrder !=null){
                    loanOrderDOMapper.updateStatusById(feeOrder.getId(), "s", null);
                }
                //资金流水
                PerAccountLog pal = new PerAccountLog();
                pal.setPerId(order.getPerId());
                pal.setOrderId(order.getId());
                pal.setOperationType(order.getType());
                pal.setAmount(order.getOptAmount().toString());
                // APP端主动还款后的流水中的标识为“代收”，应该为“缴款”
                pal.setRemark("缴款");
                pal.setAddtime(new Date());
                perAccountLogMapper.insertSelective(pal);
                logger.info("增加一条ym_per_account_log新增一条资金明细成功》...");

                Integer borrowId = order.getContractId();

                String borrStatus;
                BorrowList borrowList = borrowListMapper.selectByPrimaryKey(order.getContractId());
                if (result1.getModel() != null) {
                    borrStatus = JSON.parseObject(result1.getModel()).getString("borrStatus");
                } else {
                    logger.warn("合同ID=" + borrowId + "，无法通过清结算接口获取状态");
                    borrStatus = borrowList.getBorrStatus();
                }
                logger.info("合同ID=" + borrowId + "，合同状态=" + borrStatus);

                //todo 发送消息这里，重点
                if ("BS010".equals(borrStatus)
                        || "BS006".equals(borrStatus)
                        || "BS014".equals(borrStatus)) {
                    logger.info("逾期结清，更新催收人状态，合同单号：" + order.getContractId());
                    //逾期结清 算业绩
                    int yeji = collectorsListMapper.updateCollectorsList(order.getContractId());
                    if (yeji > 0) {
                        logger.info("结清算业绩成功");
                    } else {
                        logger.info("合同结清，但催收人状态未改变，合同单号：" + order.getContractId());
                    }
                }

                // 合同和人的信息
                BorrPerInfo bpi = borrowListMapper.selectByBorrId(borrowId);
                // 代扣成功改发代扣站内信,成功结清发送成功结清短信
                //发送短信，站内信

                sendSMSAndMsgSucc(order, borrowList, bpi);
                return "200";
            } else {
                if (!"s".equals(order.getRlState()) && !"f".equals(order.getRlState())) {
                    logger.error("清结算失败:订单：{},原因：{}", order.getId(), result1.getMsg());
                    //更改订单状态为清结算失败
                    loanOrderDOMapper.updateStatusById(order.getId(), "c", result1.getMsg());
                    if (feeOrder != null){
                        loanOrderDOMapper.updateStatusById(feeOrder.getId(), "c", result1.getMsg());
                    }
                } else {
                    logger.error("订单之前已结清，无需再次更新状态。本次清结算失败:订单：{},原因：{}", order.getId(), result1.getMsg());
                }
            }
        } catch (Exception e) {
            order = loanOrderDOMapper.selectByPrimaryKey(orderDO.getId());
            feeOrder = loanOrderDOMapper.selectSubOrderByPid(order.getId());
            loanOrderDOMapper.updateStatusById(order.getId(), "c", "清结算异常");
            if (feeOrder != null){
                loanOrderDOMapper.updateStatusById(feeOrder.getId(), "c", "清结算异常");
            }
            logger.error("出错：orderId:" + order.getId(), e);
            return "205";
        }
        return "201";
    }

    private void sendSMSAndMsgSucc(LoanOrderDO order, BorrowList borrowList, BorrPerInfo bpi) {

        try {
            String money = String.format("%.2f", order.getOptAmount().doubleValue());
            if (order.getTriggerStyle() == PayTriggerStyleEnum.AUTO.getCode() || order.getTriggerStyle() == PayTriggerStyleEnum.BACK_GROUND.getCode()) {
                if (CodeReturn.PRODUCT_TYPE_CODE_MONEY.equals(bpi.getProdType())){
                    smsService.sendSms(SmsTemplateEnum.DC_PAY_SUCCESS_REMIND.getCode(), bpi.getPhone(), bpi.getName(), DateUtil.getDateString(order.getUpdateDate(), YSH_DK_SMS_DATE_PATTERN), money);
                }else {
                    smsService.sendSms(SmsTemplateEnum.LOAN_DETECT_SUCCESS.getCode(), bpi.getPhone(), bpi.getName(), DateUtil.getDateString(order.getUpdateDate(), YSH_DK_SMS_DATE_PATTERN), money);
                }
            } else if (order.getTriggerStyle() == PayTriggerStyleEnum.USER_TRIGGER.getCode()) {
                if (CodeReturn.PRODUCT_TYPE_CODE_MONEY.equals(bpi.getProdType())){
                    smsService.sendSms(SmsTemplateEnum.DC_REPAY_SUCCESS_REMIND.getCode(), bpi.getPhone(), DateUtil.getDateString(order.getUpdateDate(), YSH_DK_SMS_DATE_PATTERN), money);
                }else {
                    smsService.sendSms(SmsTemplateEnum.LOAN_REPAY_SUCCESS.getCode(), bpi.getPhone(), DateUtil.getDateString(order.getUpdateDate(), YSH_DK_SMS_DATE_PATTERN), money);
                }
            }

            //提前结清发站内信短信，
         /*   String amount = borrowListMapper.getTotalLeft(borrowList.getId().toString());
            BigDecimal b = new BigDecimal(amount);
            if (b.doubleValue() == 0) {
                logger.info("合同编号：{}已经结清，发送短信,站内信", borrowList.getBorrNum());
                smsService.sendSms(SmsTemplateEnum.ALL_REPAY_REMIND.getCode(), bpi.getPhone(), bpi.getName(), DateUtil.getDateString(order.getUpdateDate(), YSH_DK_SMS_DATE_PATTERN), borrowList.getBorrNum());
            }*/
        } catch (Exception e) {
            logger.error("短信发送失败:orderId" + order.getId(), e);
        }
    }

    @SuppressWarnings("Duplicates")
    @Transactional
    public String deductFailAfter(LoanOrderDO order, String desc) {
        try {
            Integer orderId = order.getId();
            BorrowList borrowList = borrowListMapper.selectByPrimaryKey(order.getContractId());
            // 合同和人的信息
            BorrPerInfo bpi = borrowListMapper.selectByBorrId(borrowList.getId());
            //扣款失败发送短信
            LoanOrderDO feeOrder = loanOrderDOMapper.selectSubOrderByPid(orderId);
            if (feeOrder != null) {
                loanOrderDOMapper.updateStatusById(feeOrder.getId(), "f", desc);
            }
            loanOrderDOMapper.updateStatusById(orderId, "f", desc);
           // this.sendSMSAndMsgFail(order, borrowList, bpi);
        } catch (Exception e) {
            logger.error("订单值为失败异常",e);
            return "201";
        }
        return "200";
    }

  /*  public static void main(String[] args) {
        BigDecimal b = new BigDecimal("0.00");
        System.out.println(b.doubleValue()==0);
    }*/
}