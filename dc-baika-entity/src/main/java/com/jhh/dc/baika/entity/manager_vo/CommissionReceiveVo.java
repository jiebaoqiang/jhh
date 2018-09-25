package com.jhh.dc.baika.entity.manager_vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户领取佣金记录
 */
public class CommissionReceiveVo implements Serializable {

    private static final long serialVersionUID = -465462099816048099L;

    private Integer id;
    @Excel(name = "领取佣金",width = 20)
    private BigDecimal amount;

    @Excel(name = "领取时间",width = 20)
    private String receiveTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
