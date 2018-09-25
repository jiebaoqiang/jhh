package com.jhh.dc.baika.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 沃动短信开发api
 */
public class WoDongSmsUtil {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(WoDongSmsUtil.class);
    //请求url
    private static String sendUrl="http://client.movek.net:8888/sms.aspx";
    //特服号
    private  static String userid="2145";
    //序列号
    private  static String account="WEB-A2145-2145";
    //密码
    private static String password="741852";
    //是否返回json 1为返回json 其它返回xml
    private static  String isResJson="1";
    private static String codingType = "UTF-8";
    private static String backEncodType = "UTF-8";
    /**
     * 沃动短信接口发送
     * @return
     */
    public static boolean wokeSmsSend(String mobile,String content){
        boolean flag=false;
        StringBuffer send = new StringBuffer();
        try {
            send.append("action=send");
            send.append("&userid=").append(userid);
            send.append("&account=").append(URLEncoder.encode(account, codingType));
            send.append("&password=").append(URLEncoder.encode(password, codingType));
            send.append("&mobile=").append(mobile);
            send.append("&content=").append(URLEncoder.encode(content, codingType));
            send.append("&json=").append(isResJson);
            String res= doAccessHTTPPost(sendUrl,send.toString());
           logger.info("沃动短信响应:"+res+"");
            System.out.println(res);
           JSONObject jsonObject=JSONObject.parseObject(res);
           if(null!=jsonObject&&!"".equals(jsonObject)){
                 if("Success".equals(jsonObject.getString("code"))){
                     flag=true;
                     logger.info("沃动短信发送成功："+mobile+",SUCCESS");
                     System.out.println("沃动短信发送成功："+mobile+"");
                 }
           }

        }catch(Exception e){
            logger.error("沃动短信接口封装参数异常");
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * <p>
     * POST方法
     * </p>
     *
     * @param sendUrl
     *            ：访问URL
     * @param sendParam
     *            ：参数串
     * @return
     */
    private  static String doAccessHTTPPost(String sendUrl, String sendParam) {

        StringBuffer receive = new StringBuffer();
        BufferedWriter wr = null;
        try {
            URL url = new URL(sendUrl);
            HttpURLConnection URLConn = (HttpURLConnection) url
                    .openConnection();

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            URLConn.setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);

            URLConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            URLConn.setRequestProperty("Content-Length", String
                    .valueOf(sendParam.getBytes().length));

            DataOutputStream dos = new DataOutputStream(URLConn
                    .getOutputStream());
            dos.writeBytes(sendParam);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    URLConn.getInputStream(), backEncodType));
            String line;
            while ((line = rd.readLine()) != null) {
                receive.append(line).append("\r\n");
            }
            rd.close();
        } catch (java.io.IOException e) {
            receive.append("访问产生了异常-->").append(e.getMessage());
            logger.error("沃动短信请求异常");
            e.printStackTrace();
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                wr = null;
            }
        }

        return receive.toString();
    }
    public static void main(String[] args) {
        WoDongSmsUtil.wokeSmsSend("15000034315","悠兔白卡项目上线了，好友邀请即送1-3元现金红包，赶快行动吧,下载地址https://uhuishou.ronghezulin.com/download/UURecovery_1_0_0.apk【悠兔白卡】");
    }
}
