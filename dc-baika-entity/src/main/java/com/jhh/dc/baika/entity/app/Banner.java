package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 2018/1/8.
 */
@Entity
@Table(name = "banner")
@Getter
@Setter
@ToString
public class Banner implements Serializable{
    private static final long serialVersionUID = 228739231613947189L;

    private Integer id;

    private String theme;

    private String path;

    private Integer status;

    private String url;

    private String directUrl;

    private Date creationTime;

}
