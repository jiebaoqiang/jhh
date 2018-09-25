package com.jhh.dc.baika.manage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RefundRecordVo {
    private String serialNo;
    private String channel;
    private String creationDate;
    private String updateDate;
    private String userName;
    private String idCard;
    private String phone;
    private String bankName;
    private String bankNum;
    private String amount ;
    private String remark;
}
