package com.jhh.dc.baika.service.config;

import com.jhh.pay.driver.config.DriverConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.service.config
 * @Description:
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/22 17:37
 * @version: V1.0
 */

@Component
@Slf4j
public class JhhPayCenterInit implements ApplicationListener<ContextRefreshedEvent>{

    @Value("${jhh.pay.env}")
    private String env;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            log.info("\n#######\n金互行支付中心环境 {}",env);
            DriverConfig.setEnv(env);
        }
    }
}
