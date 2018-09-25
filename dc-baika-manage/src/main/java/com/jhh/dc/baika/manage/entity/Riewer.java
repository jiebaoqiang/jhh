package com.jhh.dc.baika.manage.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter @Entity @Table(name = "riewer")
public class Riewer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String employNum;

    private String employeeName;

    private String status;

    private Date creationDate;

    private Date updateDate;
}