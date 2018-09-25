package com.jhh.dc.baika.util;

import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.entity.manager.CodeValue;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * @author xingmin
 */
public class CodeValueUtil {

    /**
     * 从缓存中获取对应的codeValue，若不存在，则从数据库中取出并放入缓存
     *
     * @param redisKey
     * @param seconds
     * @param codeType
     * @return
     */
    public static String getCodeValueFromRedis(String redisKey, int seconds, String codeType) {
        if (!StringUtils.isEmpty(getJedisCluster().get(redisKey))) {
            return getJedisCluster().get(redisKey);
        }

        List<CodeValue> list = getCodeValueMapper().getEnabledCodeValues(codeType);
        if (list.size() < 1) {
            return null;
        }

        String value = list.get(0).getCodeCode();
        getJedisCluster().setex(redisKey, seconds, value);

        return value;
    }

    private static JedisCluster getJedisCluster() {
        return SpringContextHolder.getBean("jedisCluster");
    }

    private static CodeValueMapper getCodeValueMapper() {
        return SpringContextHolder.getBean("codeValueMapper");
    }
}
