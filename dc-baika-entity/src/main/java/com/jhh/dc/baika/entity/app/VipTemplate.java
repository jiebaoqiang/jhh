package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * vip模板表
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "vip_template")
public class VipTemplate implements Serializable{
    private static final long serialVersionUID = 8703008870492535337L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Float amount;

    private Integer vipLimit;

    private Integer status;

    private Date createDate;

    private String createUser;

    private Date updateDate;

    private String updateUser;

    private String sync;
}
