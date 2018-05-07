package com.freelycar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

	// 池化管理
	private static PoolingHttpClientConnectionManager poolConnManager = null;

	private static CloseableHttpClient httpClient;
	// 请求器的配置
	private static RequestConfig requestConfig;

	static {

		try {
			//System.out.println("初始化HttpClientTest~~~开始");
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			poolConnManager.setMaxTotal(200);
			// 设置最大路由
			poolConnManager.setDefaultMaxPerRoute(2);
			// 根据默认超时限制初始化requestConfig
			int socketTimeout = 10000;
			int connectTimeout = 10000;
			int connectionRequestTimeout = 10000;
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

	public static CloseableHttpClient getConnection() {
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

	public static void httpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");   
		httpGet.setHeader("Accept-Language", "zh-cn");   
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");   
		httpGet.setHeader("Accept-Charset", "UTF-8");   
		httpGet.setHeader("Keep-Alive", "300");   
		httpGet.setHeader("Connection", "Keep-Alive");   
		httpGet.setHeader("Cache-Control", "no-cache");   
		
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			//System.out.println(result);
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
		HttpClientTest.httpGet("http://odds.500.com/static/info/odds/json/live/summary.txt");
		//HttpClientTest.httpGet("https://kmg343.gitbooks.io/httpcl-ient4-4-no2/content/233_lian_jie_chi_guan_li_qi.html");
		/*URL url;
		BufferedReader br = null;
		try {
			// get URL content
			
			for(int i=0;i<100;i++){
				url = new URL("http://odds.500.com/static/info/odds/json/live/summary.txt");
				URLConnection conn = url.openConnection();
				// open the stream and put it into BufferedReader
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				String inputLine;
				while ((inputLine = br.readLine()) != null) {
					System.out.println(inputLine);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				br.close();
			}

			
		} catch (IOException e) {
			// TODO: handle exception
		}*/
	}

}
