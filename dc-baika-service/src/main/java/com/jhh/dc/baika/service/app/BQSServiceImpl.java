package com.jhh.dc.baika.service.app;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.app.BQSService;
import com.jinhuhang.risk.dto.QueryResultDto;
import com.jinhuhang.risk.dto.bqs.jsonbean.BQSRequestBean;
import com.jinhuhang.risk.service.impl.bqs.BQSApiClient;
import org.springframework.beans.factory.annotation.Value;


/**
 * 白骑士公共服务
 *
 * @author xuepengfei
 */
@Service
public class BQSServiceImpl implements BQSService {

    private static final Logger logger = LoggerFactory.getLogger(BQSServiceImpl.class);

    @Value("${productId}")
    private String productId;

    @Override
    public boolean runBQS(String phone, String name, String idNumber, String event,String tokenKey,String device) {
        BQSApiClient bqsApiClient = new BQSApiClient();
        try {
            BQSRequestBean bqsRequestBean = new BQSRequestBean();
            bqsRequestBean.setCertNo(idNumber);
            bqsRequestBean.setEventType(event);
            bqsRequestBean.setMobile(phone);
            bqsRequestBean.setName(name);
            bqsRequestBean.setTokenKey(tokenKey);
            bqsRequestBean.setRequestId(String.valueOf(System.currentTimeMillis()));
            bqsRequestBean.setPlatform(device);
            logger.info("白骑士请求参数：bqsRequestBean："+ JSONObject.toJSONString(bqsRequestBean));

            QueryResultDto bqsRisk = bqsApiClient.getBQSRisk(productId,bqsRequestBean);

            logger.info("白骑士返回参数：bqsRisk："+ JSONObject.toJSONString(bqsRisk));
            return "1".equals(bqsRisk.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("调用白骑士出现异常，请求全部通过");
            return true;
        }
    }
}
