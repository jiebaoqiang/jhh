package com.jhh.dc.baika.service.bankdeposit;

import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.entity.app_vo.RabbitMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.util.StringUtil;

@Slf4j
public class YoutuPushDataListener implements ChannelAwareMessageListener {

    @Autowired
    private QianQiDepositWebService qianQiDepositWebService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String result=new String(message.getBody(),"UTF-8");
            log.info("悠兔理财数据同步队列-监听器收到消息:{}", result);
            if (StringUtil.isNotEmpty(result)) {
                RabbitMessage rabbitMessage= JSONObject.parseObject(result, RabbitMessage.class);
                try {
                    if(rabbitMessage==null||rabbitMessage.getData()==null){
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                    //满标审
                    if (rabbitMessage.getType() == 4) {
                        //调取清结算接口
                        boolean flag=qianQiDepositWebService.youtuLoanSucc(rabbitMessage);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                    //流标
                    if (rabbitMessage.getType() == 5) {
                        //调取取消订单接口
                        boolean flag=qianQiDepositWebService.youtuLoanCancel(rabbitMessage);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                } catch (Exception e) {
                    log.error("同步悠兔理财数据异常", e);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                   // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            }
        } catch (Exception e) {
            log.error("同步悠兔理财消息处理异常", e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
