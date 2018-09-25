package com.jhh.dc.baika.common.enums;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @author  luolong
 * @date 2018-1-31
 */
public enum AgentDeductResponseEnum {

    SUCCESS_CODE(200,"处理成功"),
    BUSINESS_THIRD_SYSTEM_ERROR(1111,"系统异常"),
    BUSINESS_INNER_PARAM_ERROR(1112,"内部错误，请联系管理员"),
    BUSINESS_AMOUNT_ERROR(1003,"金额为空或金额格式不正确"),
    BUSINESS_ID_CARD_ERROR(1004,"身份证为空或身份证不正确"),
    BUSINESS_BANK_CARD_ERROR(1005,"银行卡号为空或银行卡号不正确"),
    BUSINESS_NAME_ERROR(1006,"开户人姓名为空或长度有限制"),
    BUSINESS_PHONE_ERROR(1007,"手机号为空或手机号格式不正确"),
    BUSINESS_ORDER_ERROR_ERROR(1010,"异常订单"),
    BUSINESS_COUNT_LIMIT_ERROR(1027,"交易笔数限制"),
    BUSINESS_AMOUNT_LIMIT_ERROR(1028,"金额超过单笔限额或小于单笔限额"),
    BUSINESS_FREQUENT_OPT_ERROR(1025,"操作频繁，稍后再试"),
    BUSINESS_CERTIFICATION_ERROR(2020,"未通过实名认证"),
    BUSINESS_SYSTEM_ERROR(203,"系统繁忙，请稍后再试");

    private Integer code;
    private String msg;


    AgentDeductResponseEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
