package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @auther: wenfucheng
 * @date: 2018/7/19 15:46
 * @description:
 */
@Data
public class JdCardInfoVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品描述
     */
    private String productDesc;

    /**
     * 产品icon
     */
    private String productIcon;

    /**
     * 到期时间
     */
    private String endDate;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 合同id
     */
    private Integer borrId;

}
