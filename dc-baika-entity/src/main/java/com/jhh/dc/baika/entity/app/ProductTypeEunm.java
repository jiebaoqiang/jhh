package com.jhh.dc.baika.entity.app;

/**
 * Created by chenchao on 2018/1/5.
 */
public enum ProductTypeEunm {
    A("信贷"), B("车贷"), C("放贷贷");

    private String productType;

    ProductTypeEunm(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }
}
