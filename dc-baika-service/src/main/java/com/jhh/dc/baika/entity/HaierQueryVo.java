package com.jhh.dc.baika.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  海尔查询参数
 */
@Setter
@Getter
@ToString
public class HaierQueryVo {

    /**
     * 平台(商户)订单号
     */
    private String out_trade_no;
    /**
     * 快捷通交易订单号
     */
    private String trade_no;
}
