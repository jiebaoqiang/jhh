package com.jhh.dc.baika.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HaierRefundVo {

    /**
     *平台(商户)订单号
     */
    private String out_trade_no;
    /**
     *银行卡账户名
     */
    private String bank_account_name;
    /**
     * 银行卡号
     */
    private String bank_card_no;
    /**
     *  银行编码
     */
    private String bank_code;
    /**
     *  银行名称
     */
    private String bank_name;
    /**
     * 交易金额
     */
    private String amount;
    /**
     * 出款账号
     */
    private String payer_identity;
    /**
     * 业务产品码10221-付款到卡
     */
    private String biz_product_code;
    /**
     * 支付产品码14-付款到银行卡（对私） 15-付款到银行卡（对私）
     */
    private String pay_product_code;
    /**
     * 服务器异步通知地址
     */
    private String notify_url;
}
