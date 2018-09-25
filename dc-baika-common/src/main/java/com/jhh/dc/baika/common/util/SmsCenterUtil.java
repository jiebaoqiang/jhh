package com.jhh.dc.baika.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * 短信中心api
 */
public class SmsCenterUtil {

    private static final Logger LOG = Logger.getLogger(SmsCenterUtil.class);
    //提醒类短信__悠兔白卡
    private static  String remindChannelAppIdGo="10003";
    //催收类短信_悠兔白卡
    private static  String collectionChannelAppIdGo="10003";


    //提醒类短信__悠兔白卡
    private static  String remindChannelAppIdLoan="10004";
    //提醒类短信__悠兔白卡
    private static  String collectionChannelAppIdLoan="10004";

    private static  String remindChannelCode="SMS_CHANNEL_FUTURE";
    private static  String collectionChannelCode="SMS_CHANNEL_WUXUN";


//    private static  String urlTest="http://192.168.1.90:8085/sms/send";
//    private static  String urlProduct="http://172.16.11.75:8080/sms/send";
    private static  String getCode="code";

    /**
     * 提醒类短信
     * @param message
     * @param phone
     * @param type
     * @return
     */
    public static boolean sendRemindSms(String message, String phone,String reqUrl,int type) {
        boolean flag=false;
        String result="";
        try {
            JSONObject param=new JSONObject();
            if(type== SmsTemplateEnum.BUY_WITH_HEART.getType()){
                param.put("appId", remindChannelAppIdGo);
            }else{
                param.put("appId", remindChannelAppIdLoan);
            }
            param.put("channelCode", remindChannelCode);
            param.put("bizNo", UUID.randomUUID().toString().replace("-", ""));
            param.put("phoneNumber", phone);
            param.put("content", message);
            LOG.info("手机号:【" + phone + "】，请求短信中心参数:【" + param + "】");
            System.out.println(param);
                result=httpClientPost(reqUrl,param,"utf-8");
            System.out.println(result);
            LOG.info("手机号:【" + phone + "】，短信中心响应数据:【" + result + "】");
            if (StringUtils.isNotEmpty(result)) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null && object.get(getCode).equals(CodeReturn.success)) {
                    flag = true;
                }
            }
        }catch(Exception e){
            LOG.info("手机号:【" + phone + "】,请求短信中心异常:",e);
        }
        return flag;
    }

    /**
     * 催收类短信
     * @param message
     * @param phone
     * @return
     */
    public static boolean sendCollectionSms(String message, String phone,String reqUrl,int type) {
        boolean flag=false;
        String result="";
        try {
            JSONObject param=new JSONObject();
            if(type==SmsTemplateEnum.BUY_WITH_HEART.getType()){
                param.put("appId", collectionChannelAppIdGo);
            }else{
                param.put("appId", collectionChannelAppIdLoan);
            }
            param.put("channelCode", collectionChannelCode);
            param.put("bizNo", UUID.randomUUID().toString().replace("-", ""));
            param.put("phoneNumber", phone);
            param.put("content", message);
            LOG.info("手机号:【" + phone + "】，请求短信中心参数:【" + param + "】");
           result=httpClientPost(reqUrl,param,"utf-8");
            System.out.println(result);
            LOG.info("手机号:【" + phone + "】，短信中心响应数据:【" + result + "】");
            if (StringUtils.isNotEmpty(result)) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null && object.get(getCode).equals(CodeReturn.success)) {
                    flag = true;
                }
            }
        }catch(Exception e){
            LOG.info("手机号:【" + phone + "】,请求短信中心异常:",e);
        }
        return flag;
    }
    public static String httpClientPost(String urlParam, JSONObject json, String charset) {
        StringBuffer resultBuffer = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlParam);
        BufferedReader br = null;

        try {
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);
            HttpResponse response = client.execute(httpPost);
            // 读取服务器响应数据
            resultBuffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String temp;
            while ((temp = br.readLine()) != null) {
                resultBuffer.append(temp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return resultBuffer.toString();
    }
    public static void main(String[] args) {
        boolean flag=SmsCenterUtil.sendRemindSms("您正在登录，登陆验证码为561445,请在15分钟内按照页面提示填写验证码，切勿将验证码泄露给他人。","13122578489","http://192.168.1.90:8085/sms/send",2);
        System.out.println(flag);
//       boolean flag=SmsCenterUtil.sendCollectionSms("尊敬的张一鸣，您已超期10天，请尽快结清所欠金额。感谢您的配合!如有疑虑请详询：021-60550260","13122578489",true);
//        System.out.println(flag);
    }
}
