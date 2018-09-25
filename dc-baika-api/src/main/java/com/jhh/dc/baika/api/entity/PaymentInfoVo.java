package com.jhh.dc.baika.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * 2018/7/23.
 */
@Data
@NoArgsConstructor
public class PaymentInfoVo implements Serializable{

    private static final long serialVersionUID = -1L;

    @NonNull
    private Integer perId;
    @NonNull
    private Integer bankId;
    @NonNull
    private Integer borrId;
    @NonNull
    private String amountSurplus;
    @NonNull
    private String actualAmount;


}
