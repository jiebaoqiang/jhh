import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.pay.driver.pojo.ActiveQueryRequest;
import com.jhh.pay.driver.pojo.BankInfo;
import com.jhh.pay.driver.pojo.PayRequest;
import com.jhh.pay.driver.pojo.PayResponse;
import com.jhh.pay.driver.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext.xml"})
@Slf4j
public class HelipayTest {
    @Autowired
    private TradeService tradeService;

    @Test
    public void testPay() {
        Map<String, Object> ext = new HashMap<>();
        ext.put("purpose", "代付");
        ext.put("callbackUrl", "http://www.baidu.com");
        Map<String, Object> channelMap = new HashMap<>();
        channelMap.put("helipay-pay-ronghe", new JSONObject());
        BankInfo bankInfo = new BankInfo();
       // bankInfo.setBankNo("03080000");//银行代码
        bankInfo.setBankCard("6214835131175745");//银行卡号
        bankInfo.setPersonalName("张琦");
        PayRequest request = new PayRequest();
            request.setOrderNo("jhh2017120514365455");//金互行订单ID
        request.setOrderTime(System.currentTimeMillis() / 1000);
        request.setMoney(Float.valueOf(1));
        request.setAppId(2);//支付渠道
        request.setAsync(false);
        request.setActiveQuery(true);
        request.setChannels(channelMap);
        request.setExtension(ext);//扩展字段
        request.setNotifyUrl(" ");//假的回调，支付中心也不会来调
        request.setSign("123456");//写死
        request.setRespTimeout(30);
        request.setBankInfo(bankInfo);
        PayResponse response = tradeService.pay(request);
        log.info("请求参数:"+JSONObject.toJSONString(request));
        log.info("测试返回{}", response);
    }

    @Test
    public void testDeduct() {
        ResponseDo<Object> result = new ResponseDo();
        //封装请求参数
        PayRequest payRequest = new PayRequest();
        payRequest.setAppId(2);
        payRequest.setActiveQuery(true);
        payRequest.setAsync(false);
        payRequest.setOrderNo("jhh2018120124461156");
        payRequest.setMoney(Float.parseFloat("1"));
        payRequest.setOrderTime(System.currentTimeMillis() / 1000);

        Map<String, Object> channelMap = new HashMap<>();
        channelMap.put("helipay-pay-ronghe", new JSONObject());
        payRequest.setChannels(channelMap);
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankNo("03080000");//银行代码
        bankInfo.setBankCard("6214835131175745");//银行卡号
        bankInfo.setPersonalName("张琦");
        payRequest.setBankInfo(bankInfo);

        Map<String, Object> ext = new HashMap<>();
        ext.put("purpose", "代扣");//写
        ext.put("endDate", "20180124");
        ext.put("idCardNo", "320611199404071815");//身份证号
        ext.put("subcontract_no", "");
        payRequest.setExtension(ext);
        payRequest.setSign("12345");
        payRequest.setNotifyUrl("www.baidu.com");
        log.info(payRequest.toString());
        log.info("请求参数:"+JSONObject.toJSONString(payRequest));
        PayResponse payResponse = tradeService.deduct(payRequest);
        log.info("请求金互行统一支付中心返回:{}", payResponse);

    }

    @Test
    public void testState() {
        ActiveQueryRequest request = new ActiveQueryRequest();
        request.setAppId(4);
        request.setOrderNo("wzx201903201006");
        request.setSid("P20180320000020");
        request.setTs(new Date().getTime() / 1000);
        request.setSign("123");
        log.info("请求参数:"+JSONObject.toJSONString(request));
        PayResponse state = tradeService.state(request);
        log.info("测试返回：{}", state);
    }
}
