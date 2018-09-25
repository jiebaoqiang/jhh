package com.jhh.dc.baika.entity.contract;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wanzezhong on 2017/11/23.
 */
@Setter
@Getter
public class IdEntity implements Serializable {

    private String borrNum;
    private String name;
    private String cardNum;
    private String phone;
    private String bankName;
    private String bankNum;
    private String bankPhone;
    private String payDate;
    private Date planrepayDate;
    private String interestSum;
    private Integer prodId;
    private String planRepay;
    private Integer perId;
    private String perCouponId;
    private String email;
    /**评估金额*/
    private String borrAmount;
    private Integer termId;
    private Integer termDay;
    private String informationManageFee;
    private String accountManageFee;
    /**押金*/
    private String depositAmount;
    /**实际打款金额*/
    private String payAmount;
    /**回购金额*/
    private String ransomAmount;
    /*服务费*/
    private String serviceAmount;
}

