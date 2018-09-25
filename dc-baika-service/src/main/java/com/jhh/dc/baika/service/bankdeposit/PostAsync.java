package com.jhh.dc.baika.service.bankdeposit;

import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.MapUtils;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/7/16.
 */
@Slf4j
public class PostAsync<T> extends AbstractSimpleRunner {

    private T obj;

    /**
     * 数据发送A公司请求地址
     */
    private String companyUrl;

    public PostAsync(T obj, String companyUrl) {
        this.obj = obj;
        this.companyUrl = companyUrl;
    }

    @Override
    public boolean doExecute() {
        log.info("同步A公司请求参数 T= "+obj+"发送地址 companyUrl"+companyUrl);
        Map<String,String> map = MapUtils.setConditionMap(obj);
        String result = HttpUrlPost.sendPost(companyUrl, map);
        log.info("同步A公司请求数响应-----------------\n" + result);
        JSONObject obj = JSONObject.parseObject(result);
        if ("200".equals(obj.getString("code"))) {
            return true;
        } else {
            AsyncExecutor.delayExecute(new PostAsync<>(obj,companyUrl), 5, TimeUnit.MINUTES);
        }
        return true;
    }
}
