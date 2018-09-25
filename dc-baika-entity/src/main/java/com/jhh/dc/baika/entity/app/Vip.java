package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * vip表
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "vip")
public class Vip implements Serializable {
    private static final long serialVersionUID = 5233193391442885322L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer perId;

    public Vip(Integer perId,String status,VipTemplate vipTemplate) {
        this.perId = perId;
        this.vipTemplateId = vipTemplate.getId();
        this.vipLimit = vipTemplate.getVipLimit();
        this.status = status;
        this.createDate = new Date();
    }
    public Vip() {
    }

    private Integer vipTemplateId;

    private Integer vipLimit;

    private String status;

    private Date createDate;

    private String createUser;

    private Date updateDate;

    private String updateUser;

    private String sync;
}
