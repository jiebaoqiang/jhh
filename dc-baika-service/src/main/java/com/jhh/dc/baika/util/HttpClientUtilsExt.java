package com.jhh.dc.baika.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtilsExt {

	private static Logger log = Logger.getLogger(HttpClientUtilsExt.class);
	private HttpClientUtilsExt(){
		
	}

	
	/**
	 * 使用post方式提交http请求，需要调用者自己关闭CloseableHttpResponse
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求参数编码
	 * @return  CloseableHttpResponse 
	 * @throws IOException 
	 */
	public static String post(String url,Map<String, String> params,String charset)
			throws IOException	{
		String log_str = url + "==" + params;
		//结果
		CloseableHttpResponse response = null;
		String content="";
		CloseableHttpClient httpclient = createSSLClientDefault();
		try {
		HttpPost httpPost = buildHttpPost(url, params, charset);
		response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				content = EntityUtils.toString(response.getEntity(),"utf-8");
				// System.out.println(content);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			log.error(log_str + "报错=>" + e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(log_str + "报错=>" + e);
		} finally{
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.error(log_str + "返回=>" + content);
		return content;
	}

	/**
	 * 构建post请求数据
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static  HttpPost buildHttpPost(String url,Map<String, String> params,String charset)
			throws UnsupportedEncodingException{
		HttpPost httpPost = new HttpPost(url);
		if (params==null || params.isEmpty()) {
			return httpPost;
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String value = "";
		for (String key : params.keySet()) {
			value = params.get(key)==null?"":params.get(key);
			
			nvps.add(new BasicNameValuePair(key, URLEncoder.encode(value, charset)));
		}

		if (charset != null) {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,charset));
		}else{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		}
		return httpPost;
	}
	
	/**
	 * 创建信任https协议的请求，若失败则返回默认http协议
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault() {

		try {
			TrustStrategy trustStrategy = new TrustStrategy() {

				// 信任所有
				public boolean isTrusted(X509Certificate[] chain,
				String authType) throws CertificateException {
					return true;
				}
			};
			
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null,trustStrategy ).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);

			return HttpClients.custom().setSSLSocketFactory(sslsf).build();

		} catch (KeyManagementException e) {

			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (KeyStoreException e) {

			e.printStackTrace();

		}

		return HttpClients.createDefault();

	}
}
