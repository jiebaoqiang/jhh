package com.jhh.dc.baika.api.entity.capital;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AgentRefundRequest implements Serializable {
    private static final long serialVersionUID = 9130765589035101444L;

    @NonNull
    private Integer perId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Integer triggerStyle;
    @NonNull
    private String payChannel;
    @NonNull
    private String bankNum;

    public AgentRefundRequest(){

    }

    public AgentRefundRequest(Integer perId, BigDecimal amount, Integer triggerStyle, String payChannel, String bankNum){
        this.perId = perId;
        this.amount = amount;
        this.triggerStyle = triggerStyle;
        this.payChannel = payChannel;
        this.bankNum = bankNum;
    }
}
