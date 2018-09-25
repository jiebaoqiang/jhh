package com.jhh.dc.baika.service.config;

import com.jhh.dc.baika.common.enums.BaikeluRemindEnum;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.pay.driver.config.DriverConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;


@Component
@Slf4j
public class BaikeluRemindInit implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private JedisCluster jedisCluster;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("百可录总开关>>>>>>>项目加载初始化操作开始");
        if(event.getApplicationContext().getParent() == null) {
            if(StringUtils.isEmpty(jedisCluster.get(RedisConst.BAIKELU_ALL_IS_OPEN_KEY))){
                jedisCluster.set(RedisConst.BAIKELU_ALL_IS_OPEN_KEY, BaikeluRemindEnum.IS_OPEN_BAIKELU.getCode());
            }

            if(StringUtils.isEmpty(jedisCluster.get(RedisConst.REGISTER_REMIND_NOTICE_LOCK))){
                jedisCluster.set(RedisConst.REGISTER_REMIND_NOTICE_LOCK, BaikeluRemindEnum.IS_CLOSE.getCode());
            }
            if(StringUtils.isEmpty(jedisCluster.get(RedisConst.LOGIN_REMIND_NOTICE_LOCK))){
                jedisCluster.set(RedisConst.LOGIN_REMIND_NOTICE_LOCK, BaikeluRemindEnum.IS_CLOSE.getCode());
            }
        }
        log.info("百可录总开关>>>>>>>项目加载初始化操作结束");
    }
}