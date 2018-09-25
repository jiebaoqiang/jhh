package com.jhh.dc.baika.api.entity.capital;

import lombok.*;

import java.io.Serializable;

/**
 * 代付接收参数
 */
@Getter
@Setter
@ToString
public class AgentPayRequest implements Serializable {

    private static final long serialVersionUID = -4621880806772523520L;
    @NonNull
    private Integer userId;
    @NonNull
    private String borrId;

    private String prodType;

    public AgentPayRequest() {
    }

    public AgentPayRequest(Integer userId, String borrId, Integer triggerStyle, String payChannel) {
        this.userId = userId;
        this.borrId = borrId;
        this.triggerStyle = triggerStyle;
        this.payChannel = payChannel;
    }

    @NonNull

    private Integer triggerStyle;
    @NonNull
    private String payChannel;


}
