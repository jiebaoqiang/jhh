package com.jhh.dc.baika.constant;


import com.jhh.dc.baika.common.util.PropertiesReaderUtil;

/**
 * 海尔支付相关常量
 */
public class HaierConstants {

        /**平台商户号*/
        public final static String PARTNER_ID = PropertiesReaderUtil.read("haier", "partner_id");
        /**平台入账号*/
        public final static String PAYEE_IDENTITY = PropertiesReaderUtil.read("haier", "payee_identity");

        public final static String QUERY_TRADE ="trade_query";//代扣查询地址
        public final static String QUERY_FUND_OUT ="query_fund_out"; //代付查询地址
        public final static String SERVICE_WITHHOLD ="trade_bank_witholding";//代扣请求地址
        public final static String SERVICE_PAYMENT ="transfer_to_card"; //代付请求地址
        public final static String SUCCESS_VALUE ="交易成功";
        //通用常量
        public final static double VERSION =1.0;
        public final static String CHARSET ="UTF-8";
        public final static String IDENTITY_TYPE ="1";
        public final static String COMPANY_OR_PERSONAL ="C";
        public final static String SIGN_TYPE ="RSA";
        public final static String FUNDOUT_GRADE ="1"; //快速到帐
        /**证件类型 01 身份证*/
        public final static String CERTIFICATES_TYPE ="01";
        /**20204-银行卡代扣*/
        public final static String BIZ_PRODUCT_CODE ="20204";
        /**61-银行卡代扣-借记卡*/
        public final static String PAY_PRODUCT_CODE ="61";

        /**20221-银行卡代付*/
        public final static String BIZ_PRODUCT_CODE_PAY ="10221";
        /**14-银行卡代付-对私*/
        public final static String PAY_PRODUCT_CODE_PAY ="14";

        /**代扣回调地址*/
        public final static String DEDUCT_NOTIFY_URL ="";

        /**代付回调地址*/
        public final static String PAY_NOTIFY_URL ="";

        public static final String PARAM_ERROR = "参数缺失";

         /*is_success 返回状态  只代表请求是否成功*/
        /**成功状态码*/
        public final static String SUCCESS_CODE ="S10000";
        /**成功*/
        public final static String SUCCESS_STATUS ="S";
        /**处理中*/
        public final static String DEAL_STATUS ="P";
        /**失败*/
        public final static String FAIL_STATUS ="F";

        /********交易成功**********/
        public static final String SUCCESS = "200";
        /********交易失败**********/
        public static final String FILE = "201";
        /********交易处理中**********/
        public static final String WAIT = "202";
        /********交易关闭**********/
        public static final String CLOSE = "204";

        /********传给页面成功**********/
        public static final String SUCCESS_OO = "00";
        /********传给页面处理中**********/
        public static final String SUCCESS_1O = "10";
        /********传给页面失败**********/
        public static final String SUCCESS_20 = "20";

        public static final String SUCESS_VALUE = "交易状态：交易成功";
        public static final String FILE_VALUE = "交易状态：交易失败";
        public static final String WAIT_VALUE = "交易状态：交易创建，等待买家付款";
        public static final String OVER_VALUE = "交易状态：交易结束";
        public static final String CLOSE_VALUE = "交易状态：交易关闭";
        public static final String SIGN_FAIL = "签名失败";
        public static final String SEARCH_ERROR = "查询失败";
        public static final String PAY_SUBMITTED_VALUE = "交易状态：提交银行成功";
        /**
         * 回调状态代扣
         **/
        public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";//交易创建
        public static final String TRADE_SUCCESS = "TRADE_SUCCESS";//交易成功
        public static final String TRADE_FINISHED = "TRADE_FINISHED";//交易结束
        public static final String TRADE_CLOSED = "TRADE_CLOSED";//交易关闭
        public static final String PAY_FINISHED = "success"; //代付已成功
        public static final String PAY_FAILED = "failed"; //代付已失败
        public static final String PAY_SUBMITTED = "submitted"; //代付提交至银行成功
        public static final String TRANSFER_FAIL = "TRANSFER_FAIL"; //交易失败
        /**
         * 回调状态代付
         **/
        public static final String WITHDRAWAL_SUCCESS = "WITHDRAWAL_SUCCESS"; //转账成功
        public static final String WITHDRAWAL_FAIL = "WITHDRAWAL_FAIL"; //转账失败
        public static final String WITHDRAWAL_SUCCESS_VALUE = "出款成功"; //出款成功
        public static final String WITHDRAWAL_FAIL_VALUE = "出款失败"; //出款失败
        /**
         * 交易状态代付
         **/
        public static final String SUBMITTED = "submitted"; //提交银行成功
        public static final String FAILED = "failed"; //出款失败
        public static final String PAYSUCCESS = "success"; //出款成功

        public static final String SYSTEM_ERROR = "系统内部异常";
        public static final String ORDER_ERROR = "订单号不存在";
        public static final String PROCESS = "处理中";
        /**代表信用卡 目前不支持**/
        public static final String CREDIT_CODE = "0";
        public static final String CREDIT_CODE_ERROR = "暂不支持绑定信用卡";
        public static final String CARDINFO_DIFFER = "银行卡与银行不相符";



}
