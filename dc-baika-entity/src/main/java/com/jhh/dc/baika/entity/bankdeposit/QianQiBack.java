package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhangqi on 2018/9/10.
 */

@Setter
@Getter
@ToString
public class QianQiBack implements Serializable{

    private static final long serialVersionUID = -1L;
    /**
     * 账户号码
     */
    private String acctNo;
    /**
     * 客户号码
     */
    private String custNo;
    /**
     * 交易代码
     */
    private String tradeCode;
    /**
     * 白卡订单号
     */
    private String baikaOrderNo;
    /**
     * 悠兔金服订单号
     */
    private String u2jinfuOrderNo;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 授权类型
     */
    private String authType;

    /**
     *  响应码
     */
    private String status;

    /**
     * 描述
     */
    private String message;

}
