package com.jhh.dc.baika.entity.manager;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter @Setter @Entity @Table(name = "coupon")
public class Coupon implements Serializable{
    private Integer id;

    private String couponName;

    private Integer productId;

    private Integer duation;

    private String status;

    private Long amount;

    private Date updateDate;

    private String updateUser;

    private Date creationDate;

    private String creationUser;

}