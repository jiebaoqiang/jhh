package com.jhh.dc.baika.api.constant;

public class Constants {

    public final static String SWITCH_ON = "1";//打开
    public final static String SWITCH_OFF = "0";//关闭

    /**
     * 返回参数定义
     */
    public final static String CODE_200 = "200";//成功
    public final static String CODE_201 = "201";//失败

    public final static String YM_ADMIN_SYSTEN_KEY = "YM_b_";//redis前缀
    public final static String PERSON_KEY = "per_info";
    public final static String PPHONE_KEY = "phoneInfo";
    public final static String COLLECTORS_KEY = "crs_info";
    public final static String COLLECTORS_USERID = "crs_uid_info";
    public final static String BANK_KEY = "bank_info";
    public final static String CARD_KEY = "card_info";
    public final static String PRODUCT_KEY = "pct_info";
    public final static String COUPON_KEY = "con_info";
    public final static String CARD_RD_NUM = "cct_info";
    public final static String PERSON_RD_PHONE = "per_rd_info";
    public final static String BANK_RD_NUM = "bank_rd_id";
    public final static String REPAYMENT_SUM_TIME = "repaymentInfo";
    public final static String SETTLEMENT_SWITCH = "kk:st:power:switch";
    public final static String WHITELIST_PERID = "whiteList_perId";
    public final static String WHITELIST_PHONE = "whiteList_phone";

    public final static String HASHTAG_AUTO_LOAN = "{autoLoan}";

    public final static String AUTO_LOAN_TOTAL_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_status";

    public final static String AUTO_LOAN_AMOUNT_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_amount_switch";
    public final static String AUTO_LOAN_CONCURRENCY_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_concurrency_switch";
    public final static String AUTO_LOAN_LAST_ORDER_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_lastOrder_switch";
    public final static String AUTO_LOAN_TIME_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_time_switch";

    public final static String AUTO_LOAN_NORMALORDER_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_normalOrder_switch";
    public final static String AUTO_LOAN_OVERDUEORDER_SWITCH  = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_overdueOrder_switch";
    public final static String AUTO_LOAN_NONELOAN_SWITCH = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_noneLoan_switch";

    public final static String AUTO_LOAN_STARTTIME = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_startTime";
    public final static String AUTO_LOAN_ENDTIME = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_endTime";

    public final static String AUTO_LOAN_AMOUNT_LIMIT = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_amount_limit";
    public final static String AUTO_LOAN_CONCURRENCY_LIMIT = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_concurrency_limit";

    public final static String AUTO_LOAN_AMOUNT = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_amount";
    public final static String AUTO_LOAN_CONCRURENCY = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_concurrency";
    public final static String AUTO_LOAN_OVERDUEORDER_DAY = HASHTAG_AUTO_LOAN + YM_ADMIN_SYSTEN_KEY + "autoLoan_overdueOrder_day";

    public final static String PAY_FEE_TAG = "pay_fee";

    public final static String LOGIN_USER_KEY = "login";
    public final static String LOGIN_AUTH_KEY = "user_auth";
    public final static String SUCESS = "sucess";
    public final static String FAIL = "fail";

    public final static String DOWNLOAD_COUNT = "total_download_count";
    public final static int DOWNLOAD_MAX_ITEMS = 50000;

    /**
     * 切换海尔支付
     */
    public final static String HAIER_PLATFORM = "haier";
    /**
     * 切换银生宝支付
     */
    public final static String YSB_PLATFORM = "ysb";

    /**
     * 验证码KEY
     */
    public final static String VERIFY_CODE = "verify_code";
    /**
     * 催收类型
     */
    public final static Integer COLLECTORS_OUT = 2;//外包催收
    public final static Integer COLLECTORS_INNER = 1;//公司内部催收

    /**
     * 贷后管理redis key
     */
    public final static String TYPE_LOAN = "loan";
    public final static String MANAGE_LOAN_COUNT = "m_loan_count";

    /**
     * 还款流水redis key
     */
    public final static String TYPE_REPAY = "repay";
    public final static String MANAGE_REPAY_COUNT = "m_repay_count";

    /**
     * 还款计划
     */
    public final static String TYPE_REPAY_PLAN = "repayplan";
    public final static String MANAGE_REPAY_PLAN_COUNT = "m_rp_count";
    /**
     * 催收信息
     */
    public final static String TYPE_COLL_INFO = "coll";
    public final static String TYPE_COLL_INFO_COUNT = "m_coll_count";

    public final static String TYPE_AUTH_INFO = "auth";
    public final static String TYPE_AUTH_INFO_COUNT = "m_auth_count";


    /*
        代扣及主动还款及批量代扣公用redis-key   can payback or collect
     */
    public final static String TYPE_PAYBACK_LOCK = "can_p_o_c";

    /**
     * 申请减免锁
     */
    public final static String TYPE_REDUCE_LOCK = "can_reduce_lock";

    /**
     * 管理级别6， 总监级别9
     */
    public static final int MANAGER_LEVEL = 6;

    /**
     * 订单类型
     */
    public abstract class OrderType {
        /********批量扣款**********/
        public static final String BATCH_COLLECTION = "8";
        /********减免**********/
        public static final String MITIGATE_PUNISHMENT = "6";
        /********线下还款**********/
        public static final String OFFLINE_REPAYMENT = "7";
    }

    /**
     * 订单状态
     */
    public abstract class OrderStatus {
        /********成功**********/
        public static final String SUCESS = "s";
        /********处理中**********/
        public static final String WAIT = "p";
        /********失败**********/
        public static final String FILE = "f";
    }

    /**
     * 审核类型
     */
    public abstract class ReviewType {
        /********人工审核**********/
        public static final String MANUALLY_REVIEW = "1";
        /********洗白**********/
        public static final String WHITE = "2";
        /********拉黑**********/
        public static final String BLACK = "3";
    }

    /**
     * 人工审核操作类型
     */
    public abstract static class OperationType {
        /********拉黑*********/
        public static final Integer BLACK = 3;
        /********通过**********/
        public static final Integer PASS = 4;
        /********拒绝**********/
        public static final Integer REJECT = 5;
    }

    /**
     * 机器人订单状态
     */
    public abstract static class OrderRobotState {
        /********发送中**********/
        public static final Integer SEND_ING = 1;
        /********通过**********/
        public static final Integer PASS = 2;
        /********未通过**********/
        public static final Integer UN_PASS = 3;
        /********未接通**********/
        public static final Integer BUSY = 4;
        /********非本人**********/
        public static final Integer NOT_ONESELF = 5;
        /********未知**********/
        public static final Integer UNKNOWN = 9999;
    }

    /**
     * 性别
     */
    public abstract static class Gender {
        /********男**********/
        public static final Integer MAN = 1;
        /********女**********/
        public static final Integer WOMAN = 2;
    }

    /**
     * 性别
     */
    public abstract static class ContractStatus {
        /********1:创建异常,**********/
        public static final Integer EXCEPTION = 1;
        /********2:创建中,**********/
        public static final Integer CREATEING = 2;
        /********3:成功,**********/
        public static final Integer SUCESS = 3;
        /********4:失败**********/
        public static final Integer FAIL = 4;
    }

    public  static class PayStyleConstants{
        /**
         * 金互行支付中心银生宝支付CODE码，对应table:code_value的code_code字段
         */
        public static final Integer PAY_JHH_YSB_CODE_VALUE = 0;
        /**
         * 银生宝支付CODE码，对应table:code_value的code_code字段
         */
        public static final Integer PAY_YSB_CODE_VALUE = 1;
        /**
         * 悠兔白卡CODE码，对应table:code_value的code_code字段
         */
        public static final Integer PAY_BK_CODE_VALUE = 2;

        /**悠兔白卡支付渠道ID代付*/
        public static final Integer DC_PAY_APPID = 100901;
        /**悠兔白卡支付渠道ID代扣*/
        public static final Integer DC_DEDUCT_APPID = 100903;
        /**悠兔白卡支付渠道ID代扣主动还款*/
        public static final Integer DC_REPAY_SWITCH= 100902;
        /**悠兔白卡支付渠道ID批量代扣*/
        public static final Integer DC_BATCH_APPID= 100404;
        /**悠兔白卡绑卡appid*/
        public static final Integer DC_BIND_CARD_APPID= 100905;
        /**悠兔白卡绑卡appid*/
        public static final Integer DC_SEND_MSG_APPID= 100905;

        public static final String JHH_PAY_STATE_PROGRESSING = "PROGRESSING";
        public static final String JHH_PAY_STATE_ERROR = "ERROR";
        public static final String JHH_PAY_STATE_SUCCESS = "SUCCESS";
        public static final String JHH_PAY_STATE_FAIL = "FAIL";

    }

    public  static class payOrderType{
        /**银生宝代付*/
        public static final String YSB_PAY_TYPE = "1";
        /**手续费*/
        public static final String DEPOSITORY_FEE = "3";
        /**海尔代付*/
        public static final String HAIER_PAY_TYPE = "11";
        /**支付中心代付*/
        public static final String PAYCENTER_PAY_TYPE = "15";
        /**支付中心代收*/
        public static final String PAYCENTER_DEDUCT_TYPE = "16";
        /**支付中心主动*/
        public static final String PAYCENTER_INITIATIVE_TYPE = "17";
        /**支付中心批量*/
        public static final String PAYCENTER_DEDUCTBATCH_TYPE = "18";
        /**存管充值*/
        public static final String DEPOSITORY_RECHARGE_TYPE = "19";
        /**存管还款*/
        public static final String DEPOSITORY_REPAY_TYPE = "20";
        /**存管提现*/
        public static final String DEPOSITORY_WITHDRAWAL_TYPE = "21";
        /**悠兔白卡代付*/
        public static final String BAIKA_PAY_TYPE = "22";
        /**佣金提现*/
        public static final String COMMISSION_WITHDRAWAL_TYPE = "99";
        /**海尔退款*/
        public static final String HAIER_REFUND_PAY_TYPE = "100";
        /**银生宝退款*/
        public static final String YSB_REFUND_PAY_TYPE = "101";
        /**支付中心退款*/
        public static final String PAYCENTER_REFUND_PAY_TYPE = "102";
        /**拉卡拉退款*/
        public static final String LKLREFUND_PAY_TYPE = "103";
        /**发放京东卡*/
        public static final String JD_PAY_CARD = "104";
        /**佣金提现本地ysb接口 channel*/
        public static final String COMMISSION_YSBCHANNEL_TYPE = "ysb";
        /**佣金提现 海尔本地接口 channel*/
        public static final String COMMISSION_HAIERCHANNEL_TYPE = "haier";
        /**支付宝渠道*/
        public static final String PAYCENTER_CHANNEL_ZFB = "pay-zfb";
    }

    public static class CommonPayResponseConstants{
        /**
         * 第三方成功状态码
         */
        public static final Integer SUCCESS_CODE = 200;

        /**
         * 第三方返回失败CODE
         */
        public static final Integer FAIL_CODE = 201;

        /**
         * 内部系统繁忙，超时等装填码
         */
        public static final Integer SYSTEM_ERROR_CODE = 202;

        /**
         * 内部业务级别的失败
         */
        public static final Integer BUSINESS_ERROR_CODE = 203;

        /**
         * 验证码相关错误
         */
        public static final Integer VALIDATE_ERROR_CODE = 204;

    }

    public static class DeductQueryResponseConstants{
        /**
         * 交易成功，
         */
        public static final Integer SUCCESS_CODE = 200;

        /**
         * 交易失败
         */
        public static final Integer SUCCESS_ORDER_SETTLE_FAIL = 201;
        /**
         * 订单失败，清结算失败
         */
        public static final Integer ORDER_FAIL_SETTLE_FAIL = 202;
        /**
         * 订单失败，清结算成功
         */
        public static final Integer ORDER_FAIL_SETTLE_SUCCESS = 203;

        /**
         * 处理中
         */
        public static final Integer PROGRESSING  = 204;

        /**
         * 发生系统错误
         */
        public static final Integer SYSTEM_ERROR_CODE = 205;

    }

    /**
     * 邀请用户适用人群
     */
    public static class  ApplyPeople{
        /****1 全部用户***/
        public static final Integer ALL = 1;
        /****2 安卓****/
        public static final Integer ANDROID = 2;
        /****3 苹果****/
        public static final Integer IOS = 3;
    }

    /**
     * 邀请用户级数
     */
    public static class  InviterLevel{
        /*****一级用户*****/
        public static final Integer LEVEL1 = 1;
        /*****二级用户*****/
        public static final Integer LEVEL2 = 2;
    }

    /**
     * 被邀请人状态 1.已注册、2.已放款、3.已还第一期、4.已还第二期、5.已还第三期、6.已还清
     */
    public static class  TrackingStatus{
        /*****已注册*****/
        public static final Integer REGISTER = 1;
        /*****已放款*****/
        public static final Integer LOAN = 2;
        /*****已还第一期*****/
        public static final Integer REPAYMENT1 = 3;
        /*****已还第二期*****/
        public static final Integer REPAYMENT2 = 4;
        /*****已还第三期*****/
        public static final Integer REPAYMENT3 = 5;
        /*****已还清*****/
        public static final Integer PAYOFF = 6;
    }
}
