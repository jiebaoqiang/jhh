package com.jhh.dc.baika.constant;

/**
 * 常量类
 */
public class Constant {

    /**3代表现金分期*/
    public static final int SMS_TYPE = 3;
    /**1代表悠兔白卡*/
    public static final  int SMS_YHS=4;
    /**借款用途 字典表中codeT*/
    public static final String PRODUCT_USE = "product_use";
    /*上传通讯录名称*/
    public static final String CONTACTSFILENAME = "dc_con";

    //借款状态常量
    public static final String STATUS_APLLY = "BS001";//申请中
    public static final String STATUS_CANCEL = "BS007";//已取消
    public static final String STATUS_WAIT_SIGN = "BS002";//待签约
    public static final String STATUS_SIGNED = "BS003";//已签约
    public static final String STATUS_TO_REPAY = "BS004";//待还款
    public static final String STATUS_LATE_REPAY = "BS005";//逾期未还
    public static final String STATUS_PAY_BACK = "BS006";//已还清
    public static final String STATUS_REVIEW_FAIL = "BS008";//审核未通过
    public static final String STATUS_PHONE_REVIEW_FAIL = "BS009";//电审未通过
    public static final String STATUS_DELAY_PAYBACK = "BS010";//逾期还清
    public static final String STATUS_COM_PAYING = "BS011";//放款中
    public static final String STATUS_COM_PAY_FAIL = "BS012";//放款失败
    public static final String STATUS_PAYING = "BS013";// 还款中
    public static final String STATUS_EARLY_PAYBACK = "BS014";// 提前结清
    /**风控成功码*/
    public static final String SUCCESS_CODE = "0000";
    public static final String WAIT_STATUS = "p";
    public static final String SUCCESS_STATUS = "s";
    public static final String FAIL_STATUS = "f";

    /**业务支付渠道*/
    public static final String YSB_CHANNEL = "ysb";
    public static final String HAIER_CHANNEL = "haier";
    public static final String PAYCENTER_CHANNEL = "payCenter";
    /**支付中心开启 type*/
    public static final String PAYCENTER_CHANNEL_TYPE = "0";
    /**本地支付开启 type*/
    public static final String LOCAL_CHANNEL_TYPE = "1";

    public static final String PAY_SWITCH = "pay_style_pay";

    public static final String DEDUCT_SWITCH = "pay_style_deduct";

    public static final String REPAY_SWITCH = "pay_style_repay";
    public static final String REFUND_SWITCH = "pay_style_refund";


    /**无业务调用第三方*/
    public static final String YSB_TRADE_CODE = "tradeysb";
    public static final String HAIER_TRADE_CODE = "tradehaier";
    public static final String PAYCENTER_TRADE_CODE = "tradePayCenter";


    public static final String CG1044 = "CG1044"; //开户绑卡CG1044
    public static final String CG1048 = "CG1048"; //     客户密码修改CG1048
    public static final String custNo = "custNo"; //  回调：custNo
    public static final String CG1055 = "CG1055";//支付密码重置CG1055
    public static final String CG1045 = "CG1045";//充值CG1045
    public static final String CG1047 = "CG1047";// T1提现CG1047
    public static final String CG1050 = "CG1050";//授权CG1050
    public static final String CG1010 = "CG1010";//还款CG1010

    public static final String AUTH_TYPE_REPAY = "3";//前旗银行还款转账授权状态
    public static final String AUTH_TYPE_LOAN = "5";//前旗银行商户放款授权状态

    public static final String ACCOUNT_P = "1";//处理中
    public static final String ACCOUNT_S = "2";//成功
    public static final String ACCOUNT_F = "3";//失败

    public static final int ACCOUNT = 1;
    public static final int AUTH_REPAY = 2;
    public static final int AUTH_LOAN = 3;

    /**还款失败 合同表表示*/
    public static final int REPAY_RETRY_FLAG_FAIL = 1;

    public static final int REPAY_RETRY_FLAG_SUCCESS = 3;
}
