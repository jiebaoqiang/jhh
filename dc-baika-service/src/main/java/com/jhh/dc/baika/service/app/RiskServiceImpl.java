package com.jhh.dc.baika.service.app;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.app.RiskService;
import com.jhh.dc.baika.mapper.app.*;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jinhuhang.risk.dto.*;
import com.jinhuhang.risk.dto.zhima.jsonbean.ZhiMaAuthorizeDto;
import com.jinhuhang.risk.dto.zhima.jsonbean.ZhiMaResult;
import com.jinhuhang.risk.dto.zhima.jsonbean.ZhiMaRiskDto;
import com.jinhuhang.risk.service.RiskAPI;
import com.jinhuhang.risk.service.impl.RiskAPIClient;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import com.jinhuhang.risk.service.impl.zhima.ZhiMaCreditApiClient;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.JedisCluster;


/**
 * 认证service
 *
 * @author xuepengfei
 */
@Service
public class RiskServiceImpl implements RiskService {

    private BlacklistAPIClient blacklistAPIClient = new BlacklistAPIClient();

    private static final Logger logger = LoggerFactory.getLogger(RiskServiceImpl.class);

    @Value("${productId}")
    private String productId;


    @Override
    public boolean isBlack(String phone, String idCard) {
        try {
            // 调用风控黑名单新接口，添加手机号码
            logger.info("调用风控黑名单接口参数，身份证号= 【" + idCard + "】, 手机号 = 【" + phone + "】");
            QueryResultDto queryResultDto = blacklistAPIClient.blacklistSingleQuery(idCard, phone);
            logger.info("调用风控黑名单接口返回，" + JSONObject.toJSONString(queryResultDto));
            return "1".equals(queryResultDto.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用风控黑名单接口失败，身份证号= 【" + idCard + "】, 手机号 = 【" + phone + "】");
            return true;
        }

    }


}
