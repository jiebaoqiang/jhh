package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangqi on 2018/9/12.
 */
@Setter
@Getter
public class RepaymentRequest {

   private String borrowId; //U2标的Id
   private String payerAcctNo; //融资人账号号码
   private String amount; //还款总金额
   private String capital; //还款本金
   private String incomeAmt; //商户服务费
   private String baikaOrderNo; //白卡订单号
   private String period; // 还款期数1
}
