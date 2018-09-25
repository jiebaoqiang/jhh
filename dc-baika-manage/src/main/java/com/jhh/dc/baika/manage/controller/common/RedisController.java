package com.jhh.dc.baika.manage.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

/**
 * @author xingmin
 */
@Controller
@RequestMapping("/redis")
@Slf4j
public class RedisController {

    @Autowired
    private JedisCluster jedisCluster;

    @ResponseBody
    @RequestMapping("/get")
    public Object get(String key){
        return jedisCluster.get(key);
    }

    @ResponseBody
    @RequestMapping("/add")
    public Object set(String key, String val) {
        return jedisCluster.set(key, val);
    }

    @ResponseBody
    @RequestMapping("/del")
    public Object del(String key) {
        return jedisCluster.del(key);
    }

}
