package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class BpmNode implements Serializable {
    private Integer id;

    private Integer perId;

    private Integer bpmId;

    private Integer nodeId;

    private String nodeStatus;

    private Date nodeDate;

    private String description;

    private String sync;

    private Date updateDate;
}