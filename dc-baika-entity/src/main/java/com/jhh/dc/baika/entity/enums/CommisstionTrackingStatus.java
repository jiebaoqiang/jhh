package com.jhh.dc.baika.entity.enums;

/**
 * Created by chenchao on 2018/3/14.
 */
public enum CommisstionTrackingStatus {
    register(1, "已注册"), loan(2, "已放款"), payFirst(3, "已还第一期"), paySecond(4, "已还第二期"), payThird(5, "已还第三期"), settled(6, "已结清");

    private int status;
    private String description;

    CommisstionTrackingStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }


    public String getDescription() {
        return description;
    }
}
