package com.jhh.dc.baika.api.entity.details;

import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户中心 基本数据
 * 2018/8/23.
 */
@Data
@NoArgsConstructor
public class AccountDetails implements Serializable{

    private static final long serialVersionUID = -1L;

    private Integer perId;

    private String phone;

    private BigDecimal balance;

    /**
     * 本期应还
     */
    private BigDecimal currentMonthCapitalInterest;

    /**
     *  总剩余应还
     */
    private BigDecimal amountSurplus;

    private Integer borrId;

    /**
     * 客服电话
     */
    private String servicePhone;

    public AccountDetails(Person p, BorrowList borrowList, String balance){
        this.perId = p.getId();
        this.borrId = borrowList == null ? null: borrowList.getId();
        this.phone = p.getPhone();
        this.balance = balance == null ? null:new BigDecimal(balance);
        this.amountSurplus = borrowList == null ? new BigDecimal(0) : new BigDecimal(borrowList.getAmountSurplus());
    }
}
