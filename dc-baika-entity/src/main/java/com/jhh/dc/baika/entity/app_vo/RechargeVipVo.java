package com.jhh.dc.baika.entity.app_vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 2018/1/10.
 */
@ToString
@Getter
@Setter
public class RechargeVipVo implements Serializable{

    private static final long serialVersionUID = 7645434155024165116L;
    @NonNull
    private String bankId;
    @NonNull
    private String bankNum;
    @NonNull
    private String bankName;
    @NonNull
    private String vipTemplatId;
    @NonNull
    private String phone;
    @NonNull
    private String userId;
}
