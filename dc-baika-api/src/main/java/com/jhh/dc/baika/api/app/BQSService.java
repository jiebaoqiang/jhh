package com.jhh.dc.baika.api.app;

/**
 * Created by xuepengfei on 2018/1/17.
 */
public interface BQSService {

    /**
     * 白骑士统一方法
     * @param phone
     * @param name
     * @param idNumber
     * @param event
     * @return
     */
    boolean runBQS(String phone, String name, String idNumber, String event, String tokenKey, String deivce);
}
