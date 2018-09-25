package com.jhh.dc.baika.entity.enums;

/**
 * @author xingmin
 */
public enum CommissionReviewStatusEnum {
    /**
     * 未审核
     */
    NO_REVIEW(0, "未放款"),
    /**
     * 审核同意(放款中)
     */
    AGREE_REVIEW(1, "放款中"),
    /**
     * 审核拒绝
     */
    REJECT_REVIEW(2, "审核拒绝"),
    /**
     * 放款失败
     */
    AGREE_BUT_PAY_FAIL(3, "放款失败"),
    /**
     * 放款成功
     */
    AGREE_AND_PAY_SUCCESS(4, "放款成功");

    private int status;
    private String description;

    CommissionReviewStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getStatusString() {return Integer.toString(status);}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommissionReviewStatusEnum{");
        sb.append("status=").append(status);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
