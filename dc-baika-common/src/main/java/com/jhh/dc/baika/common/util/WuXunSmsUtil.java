package com.jhh.dc.baika.common.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class WuXunSmsUtil {

    private static final Logger logger = Logger.getLogger(WuXunSmsUtil.class);

    private static final String BASE_URL = "http://sms.newsms.com.cn/WebAPI/SmsAPI.asmx/SendSms";

    private static final String USER_NAME = "jinhuhang";
    private static final String PASS_WORD = "888888";

    private static final String USER_NAME_YHS = "youhuishou";
    private static final String PASS_WORD_YHS = "888888";

    public static boolean send(String message, String phone, int type) {
        String result = "";
        Map<String, Object> map = new HashMap<String, Object>();
        if(type == 2){
            map.put("user", USER_NAME_YHS);
            map.put("pwd", PASS_WORD_YHS);
        }else{
            map.put("user", USER_NAME);
            map.put("pwd", PASS_WORD);
        }

        map.put("mobiles", phone);
        map.put("contents", message);
        try {
            result = HttpUtils.sendPost(BASE_URL, HttpUtils.toParam(map));
            //System.out.println(result);
            logger.error("短信发送-phone:" + phone + ",结果:" + result);
            String code = result.substring(result.indexOf("<Code>") + 6, result.indexOf("</Code>"));
            return Integer.valueOf(code) > -1;
        } catch (Exception e) {
            logger.error("短信发送异常 phone:" + phone + "------------:" + e.getMessage(), e);
            return false;
        }
    }

    public static String sendResult(String message, String phone, int type) {
        String result = "";
        Map<String, Object> map = new HashMap<String, Object>();
        if(type == 2){
            map.put("user", USER_NAME_YHS);
            map.put("pwd", PASS_WORD_YHS);
        }else{
            map.put("user", USER_NAME);
            map.put("pwd", PASS_WORD);
        }
        map.put("mobiles", phone);
        map.put("contents", message);
        try {
            result = HttpUtils.sendPost(BASE_URL, HttpUtils.toParam(map));
            logger.error("短信发送-phone:" + phone + ",结果:" + result);
        } catch (Exception e) {
            logger.error("短信发送异常 phone:" + phone + "------------:" + e.getMessage(), e);
        }
        return result;
    }

    public static void main(String[] args) {

		//String phone = "1363659813";
//		String[] phones = phone.split(",");
//		System.out.println();
		//String result = "【悠兔白卡】尊敬的用户，您本次账单人民币1002.0。到期还 款日2017-07-30 。"
		//		+ "http://uat.youmishanjie.com/static/register/youmiRepayment.html";
//		for(String p : phones){
//			System.out.println(WuXunSmsUtil.sendResult(result,  p, 1));
//		}
//        String filePath = "C:\\Users\\wanzezhong\\Desktop\\aa.txt";
//      "res/";
//        readTxtFile(filePath);
      //  sendResult(result,phone,2);
        String msg = "【悠兔白卡】尊敬的张三，您已超期7天，请于今日付清截至当期租金及相关费用，否则明天将强制要求您结清剩余全部费用。感谢您的配合!如有疑虑请详询：021-60550260";
     send(msg,"13122578489",2);
    }

    public static void readTxtFile(String filePath) {
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] aa = lineTxt.split(",");
                    System.out.println(WuXunSmsUtil.sendResult(aa[1], aa[0], 1));
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }


}
