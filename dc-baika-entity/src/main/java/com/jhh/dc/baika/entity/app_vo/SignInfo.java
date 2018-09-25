package com.jhh.dc.baika.entity.app_vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xuepengfei on 2018/1/19.
 */
@Getter
@Setter
public class SignInfo implements Serializable {

    private static final long serialVersionUID = 734164289531433453L;
    private int perId ;
    private int borrId;
    private String name;
    private String borrNum;
    private String phone;
    private BigDecimal borrAmount;
    private Integer termNum;
    private Integer termDay;
    private String bankNum;
    private BigDecimal planRepay;
    private BigDecimal interestRate;
    private BigDecimal serviceFee;
    private BigDecimal manageFee;

}
