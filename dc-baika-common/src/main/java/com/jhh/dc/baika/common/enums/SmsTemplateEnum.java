package com.jhh.dc.baika.common.enums;

/**
 * 短信模板整理
 * zhushuaifei
 */
public enum SmsTemplateEnum {
    //悠兔白卡
    LOAN_CHECK_CODE_LOGIN(1001,"悠兔白卡_验证码登陆",1),
    LOAN_SIGN_SUCCESS(1002,"悠兔白卡_签约成功",1),
    LOAN_CARD_SUCCESS(1003,"悠兔白卡_放卡成功",1),
    LOAN_REPAY_REMIND(1004,"悠兔白卡_还款提醒",1),
    LOAN_REPAY_SUCCESS(1005,"悠兔白卡_主动还款成功",1),
    LOAN_DETECT_SUCCESS(1006,"悠兔白卡_代扣成功",1),
    LOAN_ALL_REPAY_SUCCESS(1007,"悠兔白卡_全部结清",1),
    LOAN_BACK_CHECK_CODE(1008,"悠兔白卡_管理后台外网登录验证码",1),
    LOAN_CHECK_CODE_REMIND(1009,"悠兔白卡_绑卡验证码发送接口",1),
    LOAN_COMMON_CODE(1010,"悠兔白卡_验证码发送接口",1),

    //悠兔白卡
    DC_CHECK_CODE_LOGIN(2001,"悠兔白卡_验证码登陆",2),
    DC_SIGN_SUCCESS(2002,"悠兔白卡_签约成功",2),
    DC_LOAN_SUCCESS(2003,"悠兔白卡_放款成功",2),
    DC_BIND_CARD_CHECK_CODE(2004,"悠兔白卡_绑卡操作验证码",2),
    DC_REPAY_REMIND(2005,"悠兔白卡_还款提醒",2),
    DC_REPAY_SUCCESS_REMIND(2006,"悠兔白卡_主动付款成功",2),
    DC_PAY_SUCCESS_REMIND(2007,"悠兔白卡_代扣成功",2),
    DC_ALL_REPAY_REMIND(2008,"悠兔白卡_全部结清",2),
    DC_BACK_CHECK_CODE(2009,"悠兔白卡_管理后台外网登录验证码",2),
    DC_COMMON_CODE(2010,"悠兔白卡_验证码发送接口",2),



    //悠回收
    LOAN_SUCCESS_REMIND(500001,"放款成功",3),
    RENT_REMIND(500002,"租金提醒",3),
    OVERDUE_REMIND(500003,"逾期提醒",3),
    REPAY_SUCCESS_REMIND(500004,"主动付款成功",3),
    PAY_SUCCESS_REMIND(500005,"代扣成功",3),
    CHECK_CODE_REMIND(500006,"验证码发送接口",3),
    ALL_REPAY_REMIND(500007,"全部还清",3),
    PAY_FAIL_REMIND(500008,"付款失败",3),
    INIT_PASSWORD_REMIND(500009,"初始密码",3),
    COMM_APPROVE_FAIL(500010,"佣金审核未通过",3),
    REFUND_FAIL(500011,"财务退款失败",3),



    //产品类型
    BUY_WITH_HEART(1,"悠兔白卡",1),
    LOAN_WITH_HEART(2,"悠兔白卡",2),
    YOU_DUO_DUO(3,"悠多多",3),
    ;


    private Integer code;
    private String desc;
    private int type;  //1:悠兔白卡  2:悠兔白卡 3:悠多多

    SmsTemplateEnum(Integer code, String desc, int type){
        this.code = code;
        this.desc = desc;
        this.type=type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescByCode(Integer code){
        for (SmsTemplateEnum smsTemplateEnum: SmsTemplateEnum.values()){
            if(smsTemplateEnum.code.equals(code)){
                return smsTemplateEnum.desc;
            }
        }
        return null;
    }

    public static int getTypeByCode(Integer code){
        for (SmsTemplateEnum smsTemplateEnum: SmsTemplateEnum.values()){
            if(smsTemplateEnum.code.equals(code)){
                return smsTemplateEnum.type;
            }
        }
        return 0;
    }
}
