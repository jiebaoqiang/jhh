package com.jhh.dc.baika.entity.manager;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人信息表 
 * @author xuepengfei
 *2016年9月28日上午11:47:02
 */

@Getter
@Setter
public class PrivateOld implements Serializable {
    private Integer id;
    private Integer perId;
    private String qqNum;
    private String email;
    private String usuallyaddress;
    private String education;
    private String marry;
    private String getchild;
    private String profession;
    private String monthlypay;
    private String business;
    private String busiProvince;
    private String busiCity;
    private String busiAddress;
    private String busiPhone;
    private String relatives;
    private String relativesName;
    private String relaPhone;
    private String society;
    private String sociPhone;
    private String societyName;
    private String isCar;
    private String houseCondition;
    private Date updateDate;
}
