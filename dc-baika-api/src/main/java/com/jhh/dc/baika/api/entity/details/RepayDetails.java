package com.jhh.dc.baika.api.entity.details;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 2018/9/5.
 */
@Data
public class RepayDetails implements Serializable{

    private static final long serialVersionUID = -1L;

    private String perId;

    private String borrId;

    private String borrNum;

    /**
     *  剩余本金
     */
    private BigDecimal capitalSurplus;

    /**
     *  剩余利息
     */
    private BigDecimal interestSurplus;

    /**
     * 总剩余应还
     */
    private BigDecimal amountSurplus;
    /**
     * 应还金额
     */
    private BigDecimal planRepay;
    /**
     *  罚息
     */
    private BigDecimal forfeitSurplus;
    /**
     *  违约金
     */
    private BigDecimal penaltySurplus;
    /**
     *  已还金额
     */
    private BigDecimal amountRepay;

    /**
     *  账户管理费
     */
    private BigDecimal accountManageSurplus;

    /**
     *  信息服务费
     */
    private BigDecimal informationServiceSurplus;

    /**
     *  总计
     */
    private BigDecimal amountSum;

    private String borrStatus;
}
