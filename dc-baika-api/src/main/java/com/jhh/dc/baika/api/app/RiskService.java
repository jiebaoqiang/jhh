package com.jhh.dc.baika.api.app;

/**
 * 认证相关服务，包括芝麻、白骑士、个人节点等
 * @author xuepengfei
 */
public interface RiskService {


    /**
     * 统一调风控是否黑名单方法   phone idCard 可有一个为空
     * @param phone
     * @param idCard
     * @return
     */
    boolean isBlack(String phone, String idCard);
}
