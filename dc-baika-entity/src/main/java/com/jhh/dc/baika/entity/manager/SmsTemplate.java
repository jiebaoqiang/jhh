package com.jhh.dc.baika.entity.manager;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity @Table(name = "c_sms_template") @Getter @Setter
public class SmsTemplate implements Serializable{
    /**
     * 模板类型
     * */
    private String templateType;
    /**
     * 模板id
     * */
    private Integer templateSeq;
    /**
     * 模板内容
     * */
    private String content;
    /**
     * 模板状态
     * */
    private String status;
    /**
     * 创建时间
     * */
    private Date createDate;
    /**
     * 创建用户
     * */
    private String createUser;
    /**
     * 更新时间
     * */
    private Date updateDate;
    /**
     * 更新用户
     * */
    private String updateUser;

}