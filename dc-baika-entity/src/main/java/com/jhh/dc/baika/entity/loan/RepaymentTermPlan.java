package com.jhh.dc.baika.entity.loan;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class RepaymentTermPlan implements Serializable {

    private Integer term;

    private Date repayDate;

    private String settleFlag;

    private String overdueFlag;

    private Integer bedueDays;

    private BigDecimal monthQuota;

    private BigDecimal monthMoney;

    private BigDecimal monthInterest;

    private BigDecimal penalty;

    private BigDecimal penaltyInterest;

    private String surplusQuota;

    private String surplusMoney;

    private String surplusInterest;

    private String surplusPenalty;

    private String surplusPenaltyInterest;

    private String status;
}