package com.jhh.dc.baika.service.capital;

import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.MD5Util;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectUtils {

    private static final Logger logger = LoggerFactory.getLogger(CollectUtils.class);

    /**
     * 请求第三方绑定银行卡操作
     *
     * @param bankNum
     * @param name
     * @param cardNum
     * @param phone
     * @return
     * @throws Exception
     */
    public static String requestBind(String bankNum, String name, String cardNum, String phone) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //协议当天有效 需要协议开始时间为当前时间的前一天
        Date start = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        //协议结束时间

        String startDate = sdf.format(start);

        //读取配置文件
        String key = PropertiesReaderUtil.read("third", "key");
        String accountId = PropertiesReaderUtil.read("third", "accountId");
        String contractId = PropertiesReaderUtil.read("third", "contractId");
        String url = PropertiesReaderUtil.read("third", "bindingUrl");
        String endDate = PropertiesReaderUtil.read("third", "endDate");

        String mac = "accountId=" + accountId + "&contractId=" + contractId + "&name=" + name +
                "&phoneNo=" + phone + "&cardNo=" + bankNum + "&idCardNo=" + cardNum +
                "&startDate=" + startDate + "&endDate=" + endDate + "&key=" + key;
        String macMD5 = MD5Util.encodeToMd5(mac);
        Map<String, String> param = new HashMap<String, String>();
        param.put("cardNo", bankNum);
        param.put("name", name);
        param.put("idCardNo", cardNum);
        param.put("phoneNo", phone);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("contractId", contractId);
        param.put("accountId", accountId);
        param.put("mac", macMD5.toUpperCase());

        //发送查询请求
        logger.info("请求第三方绑卡参数=====:{}",CollectUtils.toJson(param));
        String response = HttpUrlPost.sendPostJson(url, param);
        logger.info("请求第三方绑卡响应=====bankNum:{},response:{}",bankNum,response);

        return response;
    }

    /**
     * 请求第三方查询银行卡是否绑定操作
     *
     * @param bankNum
     * @param name
     * @param cardNum
     * @return
     * @throws Exception
     */
    public static String requestQuery(String bankNum, String name, String cardNum) throws Exception {

        String key = PropertiesReaderUtil.read("third", "key");
        String accountId = PropertiesReaderUtil.read("third", "accountId");
        String url = PropertiesReaderUtil.read("third", "queryUrl");

        String mac = "accountId=" + accountId + "&name=" + name + "&cardNo=" + bankNum +
                "&idCardNo=" + cardNum + "&key=" + key;
        String macMD5 = MD5Util.encodeToMd5(mac);

        //请求参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("cardNo", bankNum);
        param.put("name", name);
        param.put("idCardNo", cardNum);
        param.put("accountId", accountId);
        param.put("mac", macMD5.toUpperCase());

        //发送查询请求
        String response = HttpUrlPost.sendPostJson(url, param);

        return response;
    }

    public static String requestCollect(String amount, String phone, String subContractId, String orderId, String responseUrl) throws Exception {
        // uat环境金额限制
        String isTest = PropertiesReaderUtil.read("third", "isTest");
        if ("on".equals(isTest)) {
            amount = "0.01";
        }

        //读取配置文件
        String key = PropertiesReaderUtil.read("third", "key");
        String accountId = PropertiesReaderUtil.read("third", "accountId");
        String url = PropertiesReaderUtil.read("third", "collectUrl");
        String purpose = "还贷";
        String mac = "accountId=" + accountId + "&subContractId=" + subContractId + "&orderId=" + orderId +
                "&purpose=" + purpose + "&amount=" + amount + "&phoneNo=" + phone +
                "&responseUrl=" + responseUrl + "&key=" + key;
        String macMD5 = MD5Util.encodeToMd5(mac);

        //请求参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("accountId", accountId);
        param.put("subContractId", subContractId);
        param.put("orderId", orderId);
        param.put("purpose", purpose);
        param.put("amount", amount);
        param.put("phoneNo", phone);
        param.put("responseUrl", responseUrl);
        param.put("mac", macMD5.toUpperCase());

        logger.info("请求第三方代扣参数:{}",CollectUtils.toJson(param));
        String response = HttpUrlPost.sendPostJson(url, param);
        logger.info("第三方代扣响应=====orderId:{},response:{}",orderId,response);

        return response;
    }

    public static String queryOrder(String serialNo) {
        String accountId = PropertiesReaderUtil.read("third", "accountId");
        String key = PropertiesReaderUtil.read("third", "key");
        String url = PropertiesReaderUtil.read("third", "orderUrl");
        String str = "accountId=" + accountId + "&orderId=" + serialNo + "&key=" + key;
        String MD5 = MD5Util.encodeToMd5(str);
        //请求参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("accountId", accountId);
        param.put("orderId", serialNo);
        param.put("mac", MD5.toUpperCase());

        logger.info("查询代扣第三方参数===orderId:{}, requestUrl:{}",serialNo,url);
        String response = HttpUrlPost.sendPostJson(url, param);
        logger.info("查询代扣第三方响应=====orderId:{},response:{}",serialNo,response);
        return response;

    }

    private static String toJson(Object o){
        if (o == null) {
            return "";
        }
        return JSONObject.toJSONString(o);
    }

   /* public static void main(String[] args) {
        CollectUtils utils = new CollectUtils();
        Map hash =new  HashMap();
        hash.put("name", "name");
        hash.put("age", "18");
        System.out.println(utils.toJson(hash));
    }*/
}
