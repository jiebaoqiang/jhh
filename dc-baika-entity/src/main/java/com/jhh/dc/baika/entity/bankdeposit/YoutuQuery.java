package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangqi on 2018/9/10.
 */
@Setter
@Getter
public class YoutuQuery {

    private String tradeCode;

    /**
     * 1处理
     * 2成功
     * 3失败
     */
    private String status;

    /**
     * 账户号码
     */
    private String acctNo;
    /**
     * 客户号码
     */
    private String custNo;

    /**
     * 订单号
     */
    private String baikaOrderNo;

    /**
     * 悠兔订单号
     */
    private String u2jinfuOrderNo;
}
