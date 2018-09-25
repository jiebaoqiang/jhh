package com.jhh.dc.baika.common.enums;

/**
 * 站内信模板整理
 * zhushuaifei
 */
public enum MsgTemplateEnum {
    //悠兔白卡/悠兔白卡


    //悠回收
    SIGN_SUCCESS_REMIND(1,"签约成功"),
    LOAN_SUCCESS_REMIND(2,"放款成功"),
    TRIAL_FAIL_REMIND(3,"电审失败"),
    RENT_REMIND(4,"租金提醒"),
    DEDUCT_SUCCESS_REMIND(5,"扣款成功"),
    OVERDUE_REMIND(6,"逾期"),
    PAY_FAIL_REMIND(9,"付款失败"),
    PAY_SUCCESS_REMIND(10,"付款成功"),
    RECHARGE_SUCCESS_REMIND(12,"充值成功"),
    SETTLE_SUCCESS_REMIND(13,"全部结清"),
    COMM_APPROVE_FAIL(14,"佣金审核未通过"),
    COMM_APPROVE_SUCCESS(15,"佣金审核通过且发放成功提醒"),
    REFUND_SUCCESS(16,"财务退款成功"),
    REFUND_FAIL(17,"财务退款失败")
    ;
    private Integer code;
    private String desc;

    MsgTemplateEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescByCode(String code){
        for (MsgTemplateEnum payChannelEnum: MsgTemplateEnum.values()){
            if(payChannelEnum.code.equals(code)){
                return payChannelEnum.desc;
            }
        }
        return null;
    }
}
