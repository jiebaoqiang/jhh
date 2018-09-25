package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @auther: wenfucheng
 * @date: 2018/7/24 17:42
 * @description:我的账单实体类
 */
@Data
public class BorrowListVO implements Serializable {
    /**
     * 合同id
     */
    private Integer borrowId;

    /**
     * 订单编号/合同编号
     */
    private String borrowNum;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 借款金额
     */
    private BigDecimal amount;

    /**
     * 到期日期
     */
    private String endDate;

    /**
     * 合同状态
     */
    private String status;
}
