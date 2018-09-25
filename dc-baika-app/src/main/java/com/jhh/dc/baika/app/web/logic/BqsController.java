package com.jhh.dc.baika.app.web.logic;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.common.util.RedisConst;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

/**
 * 2018/7/11.
 */
@RestController
@RequestMapping("/bqs")
@Slf4j
public class BqsController {

    @Autowired
    private JedisCluster jedisCluster;

    @ApiOperation(value = "白骑士tokenKey", notes = "app调用保存用户白骑士tokenKey信息",hidden = true)
    @RequestMapping("/getTokenKey")
    public ResponseDo<?> getTokenKey(String phone, String tokenKey) {
        log.info("获取用户白骑士tokenKey并保存 phone={},tokenKey={}", phone, tokenKey);
        jedisCluster.set(RedisConst.APP_USER_BQSTONKEY+phone,tokenKey);
        return ResponseDo.newSuccessDo();
    }
}
