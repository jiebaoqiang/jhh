package com.jhh.dc.baika.manage.configuration;

import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 风控相关bean注入
 *
 * @author wenfucheng
 */
@Configuration
public class RiskBean {

    @Bean
    public BlacklistAPIClient blacklistAPIClient(){
        return new BlacklistAPIClient();
    }
}
