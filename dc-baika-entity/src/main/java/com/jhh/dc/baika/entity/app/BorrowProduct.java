package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * 2018/1/3.
 */
@Getter
@Setter
@ToString
public class BorrowProduct {

    @NonNull
    private String per_id;

    @NonNull
    private String product_id;

    @NonNull
    private String borr_type;

    @NonNull
    private String termId;

    @NonNull
    private float borrAmount;

}
