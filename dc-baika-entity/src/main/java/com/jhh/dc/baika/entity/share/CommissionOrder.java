package com.jhh.dc.baika.entity.share;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "commission_order")
public class CommissionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer perId;

    private String device;

    private String phone;

    private Integer inviterLevel1;

    private String inviterPhoneLevel1;

    private Integer inviterLevel2;

    private String inviterPhoneLevel2;

    private Integer channel;

    private String channelPhone;

    private Integer ruleId;

    private Integer triggerGroup;

    private Integer inviterLevel;

    private Integer trackingStatus;

    private Integer reviewId;

    private Integer withdrawStatus;

    private BigDecimal commissionAmount;

    private Date creationDate;

    private Date updateDate;

    private Date maxCreateDate;

    private String inviterId;
}