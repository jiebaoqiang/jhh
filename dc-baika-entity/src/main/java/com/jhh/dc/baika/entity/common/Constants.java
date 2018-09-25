package com.jhh.dc.baika.entity.common;

public class Constants {

    /**
     * 打开
     */
    public final static String SWITCH_ON = "1";
    /**
     * 关闭
     */
    public final static String SWITCH_OFF = "0";
    /**
     * 切换海尔支付
     */
    public final static String HAIER_PLATFORM = "haier";
    /**
     * 切换银生宝支付
     */
    public final static String YSB_PLATFORM = "ysb";
    /**
     * 返回参数定义-成功
     */
    public final static String CODE_200 = "200";
    /**
     * 返回参数定义-失败
     */
    public final static String CODE_201 = "201";

    /**
     * redis前缀
     */
    public final static String YM_ADMIN_SYSTEN_KEY = "dc_baika_";
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
     * 验证码KEY
     */
    public final static String VERIFY_CODE = "verify_code";
    /**
     * 催收类型:外包催收
     */
    public final static Integer COLLECTORS_OUT = 2;
    /**
     * 催收类型:公司内部催收
     */
    public final static Integer COLLECTORS_INNER = 1;

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


    /**
     * 代扣及主动还款及批量代扣公用redis-key   can payback or collect
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
    // app打开注册的开关 对应code_value 表的Code_type字段
    public static final String APP_SUBSCRIBE_FLAG = "app_subscribe_flag";

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
        /********人工审核历史**********/
        public static final String MANUALLY_REVIEW_HISTORY = "2";
    }

    /**
     * 人工审核操作类型
     */
    public abstract static class OperationType {
        /********拉黑*********/
        public static final Integer MANUALLY_BLACK = 1;
        /********洗白*********/
        public static final Integer WHITE = 2;
        /********审核管理通过**********/
        public static final Integer MANUALLY_PASS = 3;
        /********审核管理拒绝**********/
        public static final Integer MANUALLY_REJECT = 4;
        /********拉黑并拒绝**********/
        public static final Integer BLACK_REJECT = 5;
        /********审核管理取消**********/
        public static final Integer MANUALLY_CANCEL = 6;

    }

    /**
     * 风控拉黑类型
     */
    public abstract static class UserBlockWhite {
        /********拉黑**********/
        public static final Integer BLACK = 0;
        /********洗白**********/
        public static final Integer WHITE = 1;
    }

    /**
     * 机器人订单状态
     */
    public abstract static class OrderRobotState {

        /********发送中**********/
        public static final Integer SEND_ING = 1;
        /********未完成**********/
        public static final Integer UN_FINISHED = 2;
        /********通过**********/
        public static final Integer PASS = 3;
        /********拒绝**********/
        public static final Integer REFUSE = 4;
        /********未接通**********/
        public static final Integer UN_ACCESS = 5;
        /********非本人**********/
        public static final Integer NOT_ONESELF = 6;
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
    /**
     * 退款状态 状态 1:已确认、2:退款中、3:退款失败、4:退款成功、5:退款拒绝
     */
    public abstract static class refundStatus {
        /********1:已确认,**********/
        public static final Integer AFFIRM = 1;
        /********2:退款中,**********/
        public static final Integer ING = 2;
        /********3:退款失败,**********/
        public static final Integer FAIL = 3;
        /********4:退款成功**********/
        public static final Integer SUCESS = 4;
        /********5:退款拒绝**********/
        public static final Integer REUBJECT = 5;

    }

    /**
     * 操作余额类型 0加余额，1扣余额
     */
    public abstract static class ChangeBlanceType {
        /********0加余额,**********/
        public static final Integer ADDBLANCE = 0;
        /********1扣余额,**********/
        public static final Integer DEDUCTBLANCE = 1;

    }

}
