package com.jhh.dc.baika.service.refund;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jhh.dc.baika.mapper.manager.OrderMapper;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.refund.RefundService;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = RefundService.class)
@Slf4j
public class RefundServiceImpl implements RefundService {

    @Autowired
    private AgentChannelService agentChannelService;

    @Autowired
    private OrderMapper orderMapper;

    @Value("${refund_callback_url}")
    private String refundCallbackUrl;

    @Override
    public ResponseDo<?> refundState(String serialNo) {

        log.info("退款服务查询订单，订单号 serialNo = {}", serialNo);

        if (StringUtils.isEmpty(serialNo)) {
            return ResponseDo.newFailedDo("未知订单号");
        }
        ResponseDo responseDo;
        try {
            responseDo = agentChannelService.state(serialNo);

            log.info("查询退款服务响应结果 responseDo = {}", responseDo);

            if (!ObjectUtils.isEmpty(responseDo)) {
                Order order = orderMapper.selectBySerial(serialNo);

                Map<String, String> result = Maps.newHashMap();
                if (Constants.DeductQueryResponseConstants.SUCCESS_CODE.equals(responseDo.getCode())) {
                    //回调后台通知提现成功
                    result.put("status", "1");
                    result.put("serialNo",serialNo);
                    result.put("perId",order.getPerId() + "");
                } else {
                    result.put("perId",order.getPerId() + "");
                    result.put("status", "0");
                    result.put("serialNo",serialNo);
                }

                log.info("请求退款服务回调参数 params = {}", result);

                AsyncExecutor.execute(new PostRefundCallback(result));
                return responseDo;
            } else {
                return ResponseDo.newFailedDo("未知订单，请稍等");
            }

        } catch (Exception e) {
            log.error("退款失败，异常信息 message = {}", e);
            return ResponseDo.newFailedDo("退款失败");
        }
    }

    class PostRefundCallback extends AbstractSimpleRunner {

        private Map<String, String> params;

        public PostRefundCallback(Map<String, String> params) {
            this.params = params;
        }

        @Override
        public boolean doExecute() {
            String result = HttpUrlPost.sendPost(refundCallbackUrl, params);
            log.info("退款回调参数响应-----------------\n" + result);
            JSONObject obj = JSONObject.parseObject(result);
            if (!"200".equals(obj.getString("status"))) {
                AsyncExecutor.delayExecute(new PostRefundCallback(params), 5, TimeUnit.MINUTES);
            }
            return true;
        }
    }
}
