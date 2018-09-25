package com.jhh.dc.baika.app.common.util;

import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.NoteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;

/**
 * 2018/9/4.
 */
@Component
public class IpUtil {



    @Value("${ip.limit}")
    private String limit;
    @Autowired
    private JedisCluster jedisCluster;

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip != null && ip.contains(",")){
            ip=ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     *  限制Ip
     * @return
     */
    public NoteResult checkIpLimit(String ip) {
        String key = RedisConst.DC_LOAN_IP_LIMIT_KEY+ ip;
        long count = jedisCluster.incrBy(key,1);
        if (count == 1) {
            jedisCluster.expire(key, 24 * 60 * 60);
        }
        if (count > Integer.parseInt(limit)) {
            return NoteResult.FAIL_RESPONSE("请求过于频繁,超出限制!");
        }
        return NoteResult.SUCCESS_RESPONSE();
    }

}
