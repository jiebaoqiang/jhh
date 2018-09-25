package com.jhh.dc.baika.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpUrlPost {
    public static final  Logger logger = LoggerFactory.getLogger(HttpUrlPost.class);
    /**
     * 向指定URL发送POST请求
     *
     * @param url
     * @param paramMap
     * @return 响应结果
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接  
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("charset", "UTF-8");
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流  
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性  
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key  
                    String value = paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
            }
//            String temp = new String(param.getBytes("utf-8"),"ISO-8859-1");
//            param = URLDecoder.decode(temp, "utf-8");
//            param = URLDecoder.decode(param,"utf-8");
            // 发送请求参数  
            out.print(param);
            // flush输出流的缓冲  
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流  
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST请求
     *
     * @param url
     * @param paramMap
     * @return 响应结果
     */
    @SuppressWarnings("Duplicates")
    public static String sendPostJson(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try{
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("charset", "UTF-8");
            //第三方接口只支持JSON
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //设置连接超时属性
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            Object json = JSONObject.toJSON(paramMap);
            System.out.println(json.toString());
            out.print(URLDecoder.decode(json.toString()));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！",e);
            return result;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            /*
             * Map<String, List<String>> map = connection.getHeaderFields(); //
             * 遍历所有的响应头字段 for (String key : map.keySet()) {
             * System.out.println(key + "--->" + map.get(key)); }
             */
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            // System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 测试主方法
     *
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
//    	String message = "";
//		message = URLDecoder.decode(message, "gb2312");
//
//		Map<String, String> paramMap = new HashMap<String, String>();
////		paramMap.put("message", message);
//        String res = sendPost("http://localhost:8088/loan-web/user/getAmount.action", paramMap);
//        System.out.println(res);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("per_id", "6286048");
        String result = HttpUrlPost.sendPost("http://localhost:8088/loan-web/loan/getPhoneList.action", paramMap);
        System.out.println(result);


    }
}
