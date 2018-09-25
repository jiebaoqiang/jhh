package com.jhh.dc.baika.api.entity;

import java.io.Serializable;

/**
 * 认证通用实体
 * @author xuepengfei
 */

public class RiskBase implements Serializable{
    private String productId;
    private String nodeCode;
    private String org;
    private String callbackUrl;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
