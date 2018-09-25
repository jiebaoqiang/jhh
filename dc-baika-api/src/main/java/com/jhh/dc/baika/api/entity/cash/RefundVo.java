package com.jhh.dc.baika.api.entity.cash;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class RefundVo implements Serializable {
    private static final long serialVersionUID = 2975004247326742648L;

    @NonNull
    private Integer perId;

    @NonNull
    private Float amount;

    @NonNull
    private String bankNum;

    @NonNull
    private Integer triggerStyle;

    @NonNull
    private String payChannel;
}
