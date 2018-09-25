package com.jhh.dc.baika.common.constant;

public interface Constants {

    /**
     * 节点认证
     */
    enum BpmNode {
        ID_CARD_FRONT(1, "身份证正面认证"),
        ID_CARD_BEHIND(2, "身份证反面认证"),
        ZHIMA(3, "芝麻信用"),
        PHONE_LIST(4, "通讯录认证"),
        PERSONAL(5, "个人认证"),
        FACE_RECOGNITION(6, "人脸识别"),
        PHONE(7, "手机认证"),
        BANK(8, "银行卡认证"),;
        public int id;

        public String name;

        BpmNode(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /**
     * 认证节点状态
     */
    enum BpmNodeStatus {

        STATUS_BPM_N("NS001", "未认证"),
        STATUS_BPM_Y("NS002", "已认证"),
        STATUS_BPM_FAIL("NS003", "认证失败"),
        STATUS_BPM_UP("NS004", "已提交"),//,仅手机认证节点有次状态
        STATUS_BPM_FAIL_B("NS005", "认证失败且进黑名单"),
        STATUS_BPM_UNLOCK("NS006", "未解锁"),;
        public String status;

        public String statusName;

        BpmNodeStatus(String status, String statusName) {
            this.status = status;
            this.statusName = statusName;
        }
    }

    /**
     * 风控认证节点
     */
    enum RiskBpmNode{
        ID_CARD_FRONT(1, "身份证正面认证"),
        ID_CARD_BEHIND(2, "身份证反面认证"),
        PHONE_LIST(3, "通讯录认证"),
        PERSONAL(4, "个人认证"),
        FACE_RECOGNITION(5, "人脸识别"),
        PHONE(6, "手机认证"), // 聚信立
        ZHIMA(7, "芝麻征信"),
        BANK(8, "公积金"),
        PROVIDENT_FUND(9,"信用卡账单"),
        LEARNING_LETTER_NETWORK(10,"学信网"),
        JINGDONG_CERTIFICATION(11,"京东认证"),
        TAOBAO_CERTIFICATION(12,"淘宝认证")
        ;
        public int id;

        public String name;

        RiskBpmNode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static String getNameById(int id){
            for (RiskBpmNode riskBpmNode: RiskBpmNode.values()){
                if(riskBpmNode.id==id){
                    return riskBpmNode.name;
                }
            }
            return null;
        }
    }

    /**
     * 风控认证节点匹配枚举
     */
    enum RiskBpmNodeStatus {
        STATUS_BPM_N("NS001", 1, "未认证"),
        STATUS_BPM_Y("NS002", 2, "已认证"),
        STATUS_BPM_FAIL("NS003", 3, "认证失败"),
        STATUS_BPM_UP("NS004", 4, "已提交"),//,仅手机认证节点有次状态
        STATUS_BPM_FAIL_B("NS005", 5, "认证失败且进黑名单"),
        STATUS_BPM_UNLOCK("NS006", 6, "未解锁"),;
        public String status;

        public int riskCode;

        public String statusName;

        RiskBpmNodeStatus(String status, int riskCode, String statusName) {
            this.status = status;
            this.riskCode = riskCode;
            this.statusName = statusName;
        }

        public static String getStatusNameByCode(String status){
            for (RiskBpmNodeStatus riskBpmNodeStatus: RiskBpmNodeStatus.values()){
                if(riskBpmNodeStatus.status.equals(status)){
                    return riskBpmNodeStatus.statusName;
                }
            }
            return null;
        }
    }

    /**
     * 合同状态
     */
    enum BorrowStatus {

        APPLY_PROCESS("BS001", "申请中"),
        WAIT_SIGN("BS002", "待签约"),
        SIGNED("BS003", "已签约"),
        WAIT_REPAY("BS004", "待付款/待还款"),
        EXPIRE_REPAY("BS005", "逾期未还"),
        NORMAL_REPAY("BS006", "正常结清"),
        CANCEL("BS007", "已取消"),
        REJECT_AUDIT("BS008", "审核未通过"),
        REJECT_AUTO_AUDIT("BS009", "电审未通过"),
        EXPIRE_SETTLE("BS010", "逾期结清"),
        LOAN_PROCESS("BS011", "发放中/放款中"),
        LOAN_FAIL("BS012", "发放失败/放款失败"),
        EARLY_SETTLE("BS014", "提前结清"),
        WAIT_PAY("BS015", "待缴费"),
        PAY_ING("BS016", "缴费中"),
        PAY_FAIL("BS017", "缴费失败"),
        PAY_SUCESS("BS018", "已缴费");

        public String status;

        public String statusName;

        BorrowStatus(String status, String statusName) {
            this.status = status;
            this.statusName = statusName;
        }
    }
}
