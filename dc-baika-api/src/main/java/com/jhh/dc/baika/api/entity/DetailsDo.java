package com.jhh.dc.baika.api.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 2018/7/17.
 */
@Setter
@Getter
public class DetailsDo implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer perId;

    private Integer borrId;

    private Date askborrDate;

    private String borrNum;

    private float borrAmount;

    private Integer termNum;

    private Date payDate;

    private Date planrepayDate;

    private Date actRepayDate;

    private float planRepay;

    private String borrStatus;

    private String borrStatusName;

    private float amountSurplus;

    private Integer prodId;


}
