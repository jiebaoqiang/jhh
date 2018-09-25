package com.jhh.dc.baika.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户付款页面数据
 */
@Data
public class PaymentInfoDo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer perId;

    private Integer bankId;

    private Integer borrId;

    private String borrNum;

    private Integer prodId;

    private String bankNum;

    private String name;

    private String cardNum;

    /**
     *  银行预留手机号 可能和peron表中不同
     */
    private String phone;

    /**
     *  用户注册手机号
     */
    private String registerPhone;
    /**
     * 付款金额
     */
    private String amountSurplus;
    /**
     * 加手续费的付款金额
     */
    private String actualAmount;

    /**
     * 客服电话
     */
    private String servicePhone;

}
