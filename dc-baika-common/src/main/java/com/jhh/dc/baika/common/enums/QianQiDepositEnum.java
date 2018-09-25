package com.jhh.dc.baika.common.enums;

/**
 * 前旗存整理
 * zhushuaifei
 */
public enum QianQiDepositEnum {
    //开户类型
    CUST_TYPE_INVEST("00","个人投资用户"),
    CUST_TYPE_LOAN("03","个人融资用户"),


    //业务类型
    TRADE_CODE_OPEN_ACCOUNT("CG1044","业务类型_开户"),
    TRADE_CODE_CUST_AUTH("CG1050","业务类型_授权"),
    TRADE_CODE_EDIT_PWD("CG1048","业务类型_修改交易密码"),
    TRADE_CODE_RESET_PWD("CG1055","业务类型_重置交易密码"),
    TRADE_CODE_RE_CHARGE("CG1045","业务类型_充值"),
    TRADE_CODE_WITH_DRAW("CG1047","业务类型_提现"),
    TRADE_CODE_ACCOUNT_AMOUNT("CG2001","业务类型_查询账户余额"),

    //密码修改类型
    PWD_TYPE_PAY("01","密码修改类型_交易密码"),

    //授权类型
    AUTH_TYPE_LOAN("CG1021","商户放款"),
    AUTH_TYPE_REPAY("CG1010","还款转账"),
    AUTH_EXPIRY_TIME("20990101","认证有效期"),
    AUTH_AMOUNT("10000","授权交易金额"),

    //提现手续费查询常量配置
    WITH_DRAW_TYPE("WITHDRAW_FEE","提现手续费_费用类型"),
    WITH_DRAW_ID_TYPE("1","提现手续费_所属项目悠兔白卡(1)"),

    //悠兔存管返回数据格式
    YOUTU_RESPONSE_CODE("ret","悠兔前旗存管接口状态码"),
    YOUTU_RESPONSE_MSG("message","悠兔前旗存管接口描述"),
    YOUTU_RESPONSE_DATA("data","悠兔前旗存管接口响应JSON数据"),
    YOUTU_RESPONSE_USER_MONEY("useMoney","用户可用余额"),
    YOUTU_RESPONSE_FEE_MONOEY("feeValue","提现手续费"),
    ;
    private String code;
    private String desc;

    QianQiDepositEnum(String code, String desc){
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

    public String getDescByCode(String code){
        for (QianQiDepositEnum qianQiTemplateEnum: QianQiDepositEnum.values()){
            if(qianQiTemplateEnum.code.equals(code)){
                return qianQiTemplateEnum.desc;
            }
        }
        return null;
    }
}
