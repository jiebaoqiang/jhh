package com.jhh.dc.baika.entity.enums;

/**
 * @author xingmin
 */
public enum CommissionOrderStatusEnum {
    /**
     * 未领取
     */
    UNRECEIVED(0, "未领取"),
    /**
     * 已申请
     */
    APPLIED(1, "已申请"),
    /**
     * 待放款
     */
    PENDING(2, "待放款"),
    /**
     * 已放款
     */
    LOANED(3, "已放款"),
    /**
     * 放款中
     */
    LOANING(4, "放款中")
    ;

    private int status;
    private String description;

    CommissionOrderStatusEnum(int status, String description) {this.status = status; this.description = description;}

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getStatusString() {return Integer.toString(status);}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommissionOrderStatusEnum{");
        sb.append("status=").append(status);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
