package com.jhh.dc.baika.api.constant;

/**
 * 2017/12/29.
 */
public class StateCode {
    /**状态码*/
    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "操作成功";
    public static final int BLACKLIST_CODE = 2001;
    public static final String BLACKLIST_CODE_MSG = "黑名单";
    /**-------------------------------错误码 -------------------------------------*/
    public static final int SYSTEM_CODE = 9999;
    public static final String SYSTEM_MSG = "系统繁忙";
    /**-----------------业务相关错误码-------------------------------------------*/
    public static final int PARAM_EMPTY_CODE = 1000;
    public static final String PARAM_EMPTY_MSG = "有必要参数为空%s";
    public static final int CHECK_MAC_ERROR_CODE = 5000;
    public static final String CHECK_MAC_ERROR_MSG = "mac验证失败";
    /**手机号相关错误信息*/
    public static final int PHONE_NOTREGISTER_CODE = 1001;
    public static final String PHONE_NOTREGISTER_MSG = "该手机号未注册或被禁用";
    public static final int PHONE_ERROR = 1001;
    public static final String PHONE_ERROR_MSG = "手机号格式错误，请重新输入";
    public static final int UN_ALLOW_REGISTER = 1001;
    public static final String UN_ALLOW_REGISTER_MSG = "前旗银行不允许注册";
    /**图形码相关错误信息*/
    public static final int GRAPHICODE_ERROR_CODE = 1002;
    public static final String GRAPHICODE_ERROR__MSG = "图形码输入错误，请重新输入";
    /**短信验证码相关错误信息*/
    public static final int SMS_SEND_FAIL_CODE = 1003;
    public static final String SMS_SEND_FAIL_MSG = "短信发送失败，请稍候再试";
    public static final int SMS_VALID_FAIL_CODE = 1003;
    public static final String SMS_VALID_FAIL_MSG = "短信验证码输入错误，请重新输入";

    public static final int SMS_EMPTY_CODE = 1003;
    public static final String SMS_EMPTY_MSG = "验证码不存在";
    public static final int MSG_FAILURE_CODE = 1003;
    public static final String MSG_FAILURE_MSG = "模板已失效";
    public static final int MSG_EMPTY_CODE = 1003;
    public static final String MSG_EMPTY_MSG = "模板不存在";


    /**----------------------借款相关错误码----------------------------------*/
    public static final String LOANSTATUS_ERROR_MSG = "当前合同状态不正确";
    public static final int ORDER_REPEAT_CODE = 2002;
    public static final String ORDER_REPEAT_MSG = "1分钟内无法重复借款，请稍后";
    public static final int ORDER_UNFINISHED_CODE = 2003;
    public static final String ORDER_UNFINISHED_MSG = "有正在处理中的借款，不允许重复借款！";


    /**----------------------风空相关------------------------------------*/
    public static final int BQS_VALIT_ERROR_CODE = 2008;
    public static final String  BQS_VALIT_ERROR_MSG = "风控拒绝";

    /**----------------------产品相关------------------------------------*/
    public static final int PRODUCT_EMPTY_CODE = 4001;
    public static final String  PRODUCT_EMPTY_MSG = "在不支持该产品，请选择其他产品";
}
