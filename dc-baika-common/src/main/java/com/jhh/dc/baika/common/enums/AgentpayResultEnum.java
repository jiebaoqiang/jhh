package com.jhh.dc.baika.common.enums;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.common.enums
 * @Description: 代付交易返回枚举
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/31 15:48
 * @version: V1.0
 */
public enum AgentpayResultEnum {

    DONT_GET_LOCK("300","未获取锁"),
    ACCOUNT_NOT_ENOUGH("301","商户保证金不足"),
    U_SYS_ERROR("500","系统异常"),
    BORROW_INFO_ERROR("600","订单信息有误"),
    PROCESSING("200","提示:,通过电审，已放款！"),
    RECOVER_ORDER("302","订单号重复"),
    BANKCARD_ERROR("303","银行卡号错误"),
    ACCOUNT_NOT_EXIST("304","商户号不存在"),ACCOUNT_NO_USER("305","商户已冻结"),BORROW_EXCEPTION("306","合同异常"),REQUEST_PARAMS_ERROR("307","请求参数错误"),
    AGENTPAY_CONFIG_ERROR("308","支付配置表出错"),NOT_SUPPORT_CHANNEL("309","不支持的支付通道"),HAD_PROCESSING("310","此订单正在处理中"),
    JHH_PAY_ERROR("311","金互行中心error"),
    CHANNEL_ERROR("400","支付中心抛出异常");

    private String code;
    private String desc;

    AgentpayResultEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescByCode(String code){
        for (AgentpayResultEnum agentpayResultEnum:AgentpayResultEnum.values()){
            if(agentpayResultEnum.code.equals(code)){
                return agentpayResultEnum.desc;
            }
        }
        return null;
    }
}
