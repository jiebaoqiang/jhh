package com.jhh.dc.baika.entity.app;

import com.jhh.dc.baika.entity.manager.CodeValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 用户额度
 */
@Getter
@Setter
@ToString
public class PersonQuota implements Serializable{

    private static final long serialVersionUID = 5565004819273485000L;
    private Integer borrowId;
    /**
     * 最小额度
     */
    private Float minAmount;
    /**
     * 区间
     */
    private Float intervalAmount;
    /**
     * 最大额度
     */
    private Float maxAmount;
    /**
     * 信息管理费
     */
    private Float informationManageFee;
    /**
     * 账户管理费
     */
    private Float accountManageFee;
    /**
     * 年化利率
     */
    private Float interestRate;
    /**
     * 期数
     */
    private Integer term;

    private Person person;
    /**
     * 协议地址
     */
    private List<CodeValue> agreement;
}
