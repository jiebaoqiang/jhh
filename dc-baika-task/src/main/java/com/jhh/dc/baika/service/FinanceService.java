package com.jhh.dc.baika.service;

/**
 * Created by chenchao on 2018/3/29.
 */
public interface FinanceService {
    void sendRepayData();

    void sendPayData();

    //下午五点发送当天催收人催收数据
    void sendCollectorsDataToFinanceFiveOclock();

    //早上九点发送昨天天催收人催收数据
    void sendCollectorsDataToFinanceNineOclock();
}
