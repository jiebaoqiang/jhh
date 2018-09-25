package com.jhh.dc.baika.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.entity.Body;
import com.jhh.dc.baika.common.entity.Head;
import com.jhh.dc.baika.common.entity.PersonRiskEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

public class CollectNetUtil {
    public static String send(String url, Object obj) {
        String content = JSONObject.toJSONString(obj);
        Body body = new Body();
        body.setContent(content);
        Head head = new Head();
        head.setCommand("1009");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        long currentTime = System.currentTimeMillis();
        String time = sdf.format(currentTime);
        head.setReqTime(time);
        String Guid = BorrNum_util.createBorrNum();
        head.setGuid(Guid);
        head.setUser("测试");
        head.setPassWord("");
        head.setRespCode("");
        head.setRespMessage("");
        head.setMd5(MD5Util.encodeToMd5(content));
        head.setLang("zh-CN");

        PersonRiskEntity entity = new PersonRiskEntity();
        entity.setHead(head);
        entity.setBody(body);
        String params = JSON.toJSONString(entity);
        // System.out.println(params);
        String response = HttpTools.post(url, params);
        try {
            response = URLDecoder.decode(response, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}
