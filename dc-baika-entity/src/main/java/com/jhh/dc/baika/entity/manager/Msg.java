package com.jhh.dc.baika.entity.manager;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity @Table(name = "b_msg") @Getter @Setter
public class Msg implements Serializable{
    @Id
    @IdGenerator
    private Integer id;

    private String title;

    private Integer perId;

    private Integer type;

    private String status;

    private Date createTime;

    private String sync;

    private String content;
    
    private String create_time;
}