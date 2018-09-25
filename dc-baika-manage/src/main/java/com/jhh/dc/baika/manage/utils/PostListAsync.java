package com.jhh.dc.baika.manage.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.HttpUtil;
import com.jhh.dc.baika.common.util.HttpUtils;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2018/7/16.
 */
@Slf4j
public class PostListAsync<T> extends AbstractSimpleRunner {

    private List<T> objs;

    /**
     * 数据发送A公司请求地址
     */
    private String companyUrl;

    public PostListAsync(List<T> objs, String companyUrl) {
        this.objs = objs;
        this.companyUrl = companyUrl;
    }

    @Override
    public boolean doExecute() {
        log.info("同步A公司请求参数 T= "+objs+"发送地址 companyUrl"+companyUrl);
        String result = null;
        try {
            result = HttpUtils.sendPost(companyUrl, "borrowList="+ JSON.toJSONString(objs));
            log.info("同步A公司请求数响应-----------------\n" + result);
            JSONObject obj = JSONObject.parseObject(result);
            if ("200".equals(obj.getString("code"))) {
                return true;
            } else {
                AsyncExecutor.delayExecute(new PostListAsync<>(objs, companyUrl), 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
