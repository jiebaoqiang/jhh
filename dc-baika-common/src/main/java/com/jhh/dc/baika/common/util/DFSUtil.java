package com.jhh.dc.baika.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * 文件上传工具
 *
 * @author xuepengfei
 */
@Log4j
public class DFSUtil {
    public static String uploadFile(String dfsUrl, MultipartFile file) {
        return uploadFile(dfsUrl, file, "");
    }

    public static String uploadFile(String dfsUrl, MultipartFile file, String suffix) {
        try {
            return uploadContent(dfsUrl, file.getInputStream(), suffix);
        } catch (IOException e) {
            log.error("解析文件内容失败", e);
        }
        return null;
    }

    public static String uploadString(String dfsUrl, String content) {
        return uploadString(dfsUrl, content, "");
    }

    public static String uploadString(String dfsUrl, String content, String suffix) {
        try {
            return uploadByteArray(dfsUrl, content.getBytes("UTF-8"), suffix);
        } catch (UnsupportedEncodingException e) {
            log.error("解析文件内容失败", e);
        }
        return null;
    }

    public static String uploadByteArray(String dfsUrl, byte[] content) {
        return uploadByteArray(dfsUrl, content, "");
    }

    public static String uploadByteArray(String dfsUrl, byte[] content, String suffix) {
        return uploadContent(dfsUrl, new ByteArrayInputStream(content), suffix);
    }

    public static String uploadContent(String dfsUrl, InputStream content) {
        return uploadContent(dfsUrl, content, "");
    }

    public static String uploadContent(String dfsUrl, InputStream content, String suffix) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(dfsUrl + "/uploadFile.action");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", content, ContentType.MULTIPART_FORM_DATA, suffix);
            if (suffix != null && suffix.trim().length() > 0) {
                builder.addTextBody("fileExtName", suffix);
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                JSONObject object = JSONObject.parseObject(result);
                result = object.getString("data");
            }
        } catch (Exception e) {
            log.error("上传内容到DFS失败", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("关闭HttpClient失败", e);
            }
        }
        return result;
    }

    public static String deleteFile(String dfsUrl, String fileUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("fileName", fileUrl);
        return HttpUrlPost.sendPost(dfsUrl + "/deleteFile.action", params);
    }

    public static String downloadString(String fileUrl) {
        return HttpUrlPost.sendGet(fileUrl, "");
    }

    public static void main(String[] args) {
        try {

//            MultipartFile file1 = new MockMultipartFile("abc.txt", "abc".getBytes());
//            String url = uploadFile("http://localhost:8080/loan-dfs/fdfs", file1, "txt");
//            System.out.println(url);
//            System.out.println(downloadString(url));
//            System.out.println(deleteFile("http://localhost:8080/loan-dfs/fdfs", url));

            String url = uploadString("http://localhost:8080/loan-dfs/fdfs", "abcdefg", "png");
            System.out.println(url);
            System.out.println(downloadString(url));
            System.out.println(deleteFile("http://localhost:8080/loan-dfs/fdfs", url));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
