package com.jhh.dc.baika.common.util;

/**
 * 返回码
 *
 * @author zhangweixing
 * @time 2016年10月25日 上午10:30:16
 */
public class CodeReturn {
    //成功
    public static final int success = 200;
    //失败
    public static final int fail = 201;
    //处理中
    public static final int PROCESSING = 204;
    //手机号已存在
    public static final int PHONE_EXIST = 1001;
    //系统错误
    public static final int systemerror = 9999;


    public static final String TOKEN_WRONG = "301";

    public static final int baiqishi = 2008;
    //返回参数及消息设置
    public static final String SUCCESS_CODE = "200";

    public static final String FAIL_CODE = "201";
    public static final String NOW_BORROW_CODE = "202";

    //风控返回code

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
    public static final String STATUS_EARLY_PAYBACK = "BS014";// 提前结清

    //产品表借款类型
    /**放卡*/
    public static final String PRODUCT_TYPE_CODE_CARD = "pay_card";
    /**放钱*/
    public static final String PRODUCT_TYPE_CODE_MONEY = "pay_money";

    /**合同表同步状态成功位移标识 BS005*/
    public static final Integer BORRSTATUS_OVERDUE_SYNC = 1 << 5;
    /**合同表同步状态成功位移标识 BS006 BS010 BS014 结清*/
    public static final Integer BORRSTATUS_FINAL_SYNC = 1 << 6;
    /**合同表同步状态成功位移标识 BS007 取消*/
    public static final Integer BORRSTATUS_CANCEL_SYNC = 1 << 7;
    /**合同表同步状态成功位移标识 BS008 BS009 审核未通过*/
    public static final Integer BORRSTATUS_CHECK_SYNC = 1 << 8;

    public static final String SMS_REPAY_TYPE = "repay";

}
