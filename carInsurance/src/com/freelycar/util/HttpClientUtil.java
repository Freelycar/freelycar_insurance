package com.freelycar.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sun.util.logging.resources.logging;

public class HttpClientUtil {
	// 池化管理
	private static PoolingHttpClientConnectionManager poolConnManager = null;

	private static CloseableHttpClient httpClient;
	// 请求器的配置
	private static RequestConfig requestConfig;

	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	static {

		try {
			//System.out.println("初始化HttpClientTest~~~开始");
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			poolConnManager.setMaxTotal(200);
			// 设置最大路由
			poolConnManager.setDefaultMaxPerRoute(2);
			// 根据默认超时限制初始化requestConfig
			int socketTimeout = 1000*60*5;
			int connectTimeout = 1000*60*5;
			int connectionRequestTimeout = 1000*60*5;
			requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
					.setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

			// 初始化httpClient
			httpClient = getConnection();

			//System.out.println("初始化HttpClientTest~~~结束");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	private static CloseableHttpClient getConnection() {
		CloseableHttpClient httpClient = HttpClients.custom()
				// 设置连接池管理
				.setConnectionManager(poolConnManager)
				// 设置请求配置
				.setDefaultRequestConfig(requestConfig)
				// 设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

//		if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
//			System.out.println("now client pool " + poolConnManager.getTotalStats().toString());
//		}

		return httpClient;
	}
	
	
	public static JSONObject httpGet(String url) {
		return httpGet(url, null, null);
	}
	
	public static JSONObject httpGet(String url,Map<String,Object> param) {
		return httpGet(url, param, null);
	}

	public static JSONObject httpGet(String url,Map<String,Object> param,Map<String,String> head) {
		
		if(param!=null){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> map : param.entrySet()){
				if(Tools.notEmpty(map.getKey()) && map.getValue()!=null){
					nvps.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
				}
				
			}
			String paramString = URLEncodedUtils.format(nvps, "utf-8");
			url = url+"?"+paramString;
		}
		//System.out.println("请求的url: "+url);
		HttpGet httpGet = new HttpGet(url);
		if(head != null){
			for(Map.Entry<String, String> h : head.entrySet()){
				httpGet.setHeader(h.getKey(), h.getValue());
			}
		}
		
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
		httpGet.setHeader("Accept-Language", "zh-cn");
		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
		httpGet.setHeader("Accept-Charset", "UTF-8");
		httpGet.setHeader("Keep-Alive", "300");
		httpGet.setHeader("Connection", "Keep-Alive");
		httpGet.setHeader("Cache-Control", "no-cache");

		CloseableHttpResponse response = null;
		String result ="";
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			//System.out.println("请求的结果: "+result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		JSONObject obj = null;
		try {
			obj = new JSONObject(Tools.isEmpty(result)?"{}":result);
		} catch (JSONException e) {
			log.error("http get请求"+url+"的结果 json解析报错 。可能结果不是json格式");
			e.printStackTrace();
		}
		return obj;
	}

	
	public static void httpPost(String url,JSONObject params) throws ClientProtocolException, IOException {

	    HttpPost request = new HttpPost(url);
	    StringEntity paramsEntity =new StringEntity(params.toString(), "UTF-8");
	    request.addHeader("content-type", "application/json;charset=utf-8");
	    request.setEntity(paramsEntity);
	    HttpResponse response = httpClient.execute(request);
	    HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "utf-8");
		
		//System.out.println("post : "+result);
		EntityUtils.consume(entity);
	    
	}
	
	
	public static void httpPost(String url,Map<String,Object> params) {
		HttpPost httpPost = new HttpPost(url);
		
		if(params != null){
			httpPost.setEntity(getEntity(params));
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			
			//System.out.println("post : "+result);
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public static HttpEntity getEntity(Map<String,Object> param){
		HttpEntity entity = null;
		if(param!=null && !param.isEmpty()){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> map : param.entrySet()){
				nvps.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
			}
			try {
				entity =  new UrlEncodedFormEntity(nvps, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return entity;
	}
	
	
	/**
	 * 处理输出结果，如果是图片单独处理
	 */
	public static void downLoadPicture(String nerUrl,String folder) {

		HttpGet httpGet = new HttpGet(nerUrl);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpEntity entity = response.getEntity();
		
		try {
			InputStream body = entity.getContent();
			String filename = nerUrl.substring(nerUrl.lastIndexOf("/")+1, nerUrl.lastIndexOf("?"));
			folder = folder.replaceAll("\\\\", "/");//把\\替换成/
			if(!folder.endsWith("/")){
				folder+="/";
			}
			
			Path target = Paths.get(folder+filename);
			if (Files.notExists(target)) {
				Files.createDirectories(target);
			}
			Files.copy(body, target, StandardCopyOption.REPLACE_EXISTING);
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		//HttpClientTest.httpGet("http://odds.500.com/static/info/odds/json/live/summary.txt");
		HttpClientUtil.httpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx1394f2d5a7311394&secret=7a22adabbbd8b41d101f9604a42a5292");
		//downLoadPicture("http://p4lhabum1.bkt.clouddn.com/19e94611f03f41c2b2a40e09827f9b28.mp4?e=1519838929&token=u3OsilIqOFcyyXlPGgBHoyq-fe8ZHLAcL34mwSAz:FNGGKU4xg5kEq_Ejwq9jU-djPF0=","C://bookimg\\sdf");
	}

}
