package com.jhh.dc.baika.entity.app;

import com.jhh.id.annotation.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BorrowStatusLog {
    @IdGenerator(value = "dc_baika_borrow_status_log")
    private Integer id;

    private Integer borrowid;

    private String status;

    private Date recordTime;

    private Integer overdueDays;

    private BigDecimal borrAmount;

    private BigDecimal payAmount;

    private Integer noServiceRefund;

    private BigDecimal serviceAmount;

    private BigDecimal capitalSum;

    private BigDecimal capitalRepay;

    private BigDecimal capitalSurplus;

    private BigDecimal interestRate;

    private BigDecimal interestSum;

    private BigDecimal interestRepay;

    private BigDecimal interestSurplus;

    private BigDecimal penaltyAmount;

    private BigDecimal penaltySum;

    private BigDecimal penaltyRepay;

    private BigDecimal penaltySurplus;

    private BigDecimal forfeitRate;

    private BigDecimal forfeitSum;

    private BigDecimal forfeitRepay;

    private BigDecimal forfeitSurplus;

    private BigDecimal amountSum;

    private BigDecimal amountRepay;

    private BigDecimal amountSurplus;

    private BigDecimal planRepay;

    private BigDecimal actReduceAmount;

    private Integer termNum;

    private Integer totalTermNum;

    private BigDecimal balance;

 /*   public void getBorrowStatusLog(BorrowList borrowList){
        this.borrowid = borrowList.getId();
        this.actReduceAmount = borrowList.getActReduceAmount();
        this.amountRepay = borrowList.getAmountRepay();
        this.amountSum = borrowList.getAmountSum();
        this.amountSurplus = borrowList.getAmountSurplus();
        this.borrAmount = borrowList.getBorrAmount();
        this.capitalRepay = borrowList.getCapitalRepay();
        this.capitalSum =borrowList.getCapitalSum();
        this.capitalSurplus = borrowList.getCapitalSurplus();
        this.forfeitRate = borrowList.getForfeitRate();
        this.forfeitRepay = borrowList.getForfeitRepay();
        this.forfeitSum = borrowList.getForfeitSum();
        this.forfeitSurplus = borrowList.getForfeitSurplus();
        this.interestRate = borrowList.getInterestRate();
        this.interestRepay = borrowList.get
    }*/
}