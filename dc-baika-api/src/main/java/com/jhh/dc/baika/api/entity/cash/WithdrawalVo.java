package com.jhh.dc.baika.api.entity.cash;

import lombok.*;

import java.io.Serializable;

/**
 *  提现请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WithdrawalVo implements Serializable {

    private static final long serialVersionUID = -488333214044397068L;
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

    @NonNull
    private Integer contractId;

}
