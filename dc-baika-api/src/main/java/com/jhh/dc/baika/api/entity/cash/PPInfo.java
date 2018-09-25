package com.jhh.dc.baika.api.entity.cash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tangxd
 * @Description: TODO
 * @date 2018/1/5
 */
public class PPInfo implements Serializable {
    /** 融资总金额 */
    private BigDecimal financeAmount  = new BigDecimal("0");
    /** 融资产品利率 */
    private BigDecimal financeInterestRate  = new BigDecimal("0");
    /** 融资成功时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date financeSuccessDate;
    /** 融资计息开始时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date financeStartInterestDate;
    /** 还款方式  (1:等额本金;2:等额本息;3:到期还本付息;4:按月付息,到期还本;)*/
    private String financeRepayType;
    /** 计息方式(1:月计息;2:季计息;3:日计息) */
    private String interestRateType;
    /** 还款期数 */
    private int financePeriod = 0;
    /** 成交管理费 */
    private BigDecimal financeManageFee  = new BigDecimal("0");
    /** 成交管理费比例 */
    private BigDecimal financeManageFeeScale  = new BigDecimal("0");
    /** 账户管理费金额 */
    private BigDecimal accountManagerFee  = new BigDecimal("0");
    /** 账户管理费比例 */
    private BigDecimal accountManagerRate  = new BigDecimal("0");
    /** 实地上门费 */
    private BigDecimal visitingFee  = new BigDecimal("0");
    /** 起息方式 1:满标复审起息;2:即投即起息*/
    private String startInterestType;
    /** 起息时间类型 1:正常起息;2:下一工作日起息*/
    private String startInterestTime;
    /** 还款日类型 "1:正常还款;2:遇节假日提前;
     3:遇节假日顺延;4:即满即还;"
     */
    private String repayTimeType;

    /** 利息计算方式 CODE 按日计息 按月计息 1:按月计息；2:按日计息*/
    private String financeRateCalculationType;

    public BigDecimal getFinanceAmount() {
        return financeAmount;
    }

    public void setFinanceAmount(BigDecimal financeAmount) {
        this.financeAmount = financeAmount;
    }

    public BigDecimal getFinanceInterestRate() {
        return financeInterestRate;
    }

    public void setFinanceInterestRate(BigDecimal financeInterestRate) {
        this.financeInterestRate = financeInterestRate;
    }

    public Date getFinanceSuccessDate() {
        return financeSuccessDate;
    }

    public void setFinanceSuccessDate(Date financeSuccessDate) {
        this.financeSuccessDate = financeSuccessDate;
    }

    public Date getFinanceStartInterestDate() {
        return financeStartInterestDate;
    }

    public void setFinanceStartInterestDate(Date financeStartInterestDate) {
        this.financeStartInterestDate = financeStartInterestDate;
    }

    public String getFinanceRepayType() {
        return financeRepayType;
    }

    public void setFinanceRepayType(String financeRepayType) {
        this.financeRepayType = financeRepayType;
    }

    public String getInterestRateType() {
        return interestRateType;
    }

    public void setInterestRateType(String interestRateType) {
        this.interestRateType = interestRateType;
    }

    public int getFinancePeriod() {
        return financePeriod;
    }

    public void setFinancePeriod(int financePeriod) {
        this.financePeriod = financePeriod;
    }

    public BigDecimal getFinanceManageFee() {
        return financeManageFee;
    }

    public void setFinanceManageFee(BigDecimal financeManageFee) {
        this.financeManageFee = financeManageFee;
    }

    public BigDecimal getFinanceManageFeeScale() {
        return financeManageFeeScale;
    }

    public void setFinanceManageFeeScale(BigDecimal financeManageFeeScale) {
        this.financeManageFeeScale = financeManageFeeScale;
    }

    public BigDecimal getAccountManagerFee() {
        return accountManagerFee;
    }

    public void setAccountManagerFee(BigDecimal accountManagerFee) {
        this.accountManagerFee = accountManagerFee;
    }

    public BigDecimal getAccountManagerRate() {
        return accountManagerRate;
    }

    public void setAccountManagerRate(BigDecimal accountManagerRate) {
        this.accountManagerRate = accountManagerRate;
    }

    public BigDecimal getVisitingFee() {
        return visitingFee;
    }

    public void setVisitingFee(BigDecimal visitingFee) {
        this.visitingFee = visitingFee;
    }

    public String getStartInterestType() {
        return startInterestType;
    }

    public void setStartInterestType(String startInterestType) {
        this.startInterestType = startInterestType;
    }

    public String getStartInterestTime() {
        return startInterestTime;
    }

    public void setStartInterestTime(String startInterestTime) {
        this.startInterestTime = startInterestTime;
    }

    public String getRepayTimeType() {
        return repayTimeType;
    }

    public void setRepayTimeType(String repayTimeType) {
        this.repayTimeType = repayTimeType;
    }

    public String getFinanceRateCalculationType() {
        return financeRateCalculationType;
    }

    public void setFinanceRateCalculationType(String financeRateCalculationType) {
        this.financeRateCalculationType = financeRateCalculationType;
    }
}
