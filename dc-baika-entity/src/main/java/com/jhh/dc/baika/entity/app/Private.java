package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "private")
public class Private implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "per_id")
    private Integer perId;
    @Column(name = "qq_num")
    private String qqNum;
    @Column(name = "email")
    private String email;
    @Column(name = "usuallyaddress")
    private String usuallyaddress;
    @Column(name = "education")
    private String education;
    @Column(name = "marry")
    private String marry;
    @Column(name = "getchild")
    private String getchild;

    private String profession;

    private String monthlypay;

    private String business;
    @Column(name = "busi_province")
    private String busiProvince;
    @Column(name = "busi_city")
    private String busiCity;
    @Column(name = "busi_address")
    private String busiAddress;
    @Column(name = "busi_phone")
    private String busiPhone;

    private String relatives;
    @Column(name = "relatives_name")
    private String relativesName;
    @Column(name = "rela_phone")
    private String relaPhone;

    private String society;
    @Column(name = "soci_phone")
    private String sociPhone;
    @Column(name = "society_name")
    private String societyName;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "house_condition")
    private String houseCondition;
    @Column(name = "is_car")
    private String isCar;

    private String sync;

}