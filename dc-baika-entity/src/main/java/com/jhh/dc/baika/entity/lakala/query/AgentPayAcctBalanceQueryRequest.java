package com.jhh.dc.baika.entity.lakala.query;

import java.io.Serializable;

import com.jhh.dc.baika.entity.enums.LakalaCurrency;

/**
 * <p>
 * 拉卡拉跨境支付-商户代付账户余额查询请求参数
 * </p>
 *
 * @author jiangzhifei jiangzhifei@lakala.com
 */
public class AgentPayAcctBalanceQueryRequest implements Serializable {

    private static final long serialVersionUID = -6981545435426441159L;

    /**
     * 币种，目前只支持人民币 {@link LakalaCurrency}
     */
    private String currency = LakalaCurrency.CNY.getCode();

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
