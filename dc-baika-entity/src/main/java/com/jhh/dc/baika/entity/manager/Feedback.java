package com.jhh.dc.baika.entity.manager;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity @Table(name = "b_feedback") @Getter @Setter
public class Feedback implements Serializable{
    @Id
    @IdGenerator(value = "dc_baika_feedback")
    private Integer id;

    private Integer perId;

    private String content;

    private Date createTime = new Date();

    private String sync;

    private String prodTypeCode;
}