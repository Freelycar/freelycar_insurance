package com.freelycar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONObject;

import net.sf.ehcache.constructs.nonstop.store.ExecutorServiceStore;
import net.sf.ehcache.store.chm.ConcurrentHashMap;

public class Constant {

    private static Properties prop;
    static {
    	prop = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();  
        if (cl == null)
            cl = Constant.class.getClassLoader(); 
        InputStream in = cl.getResourceAsStream("config.properties");
        try {
            prop.load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


      }
    
    //骆驼加油的接口 key
    public static final String LUOTUOKEY = prop.getProperty("luotuo_key");
    
    
    //核保的 key :单号  value 过期时间戳
    private static class proposalHolder{
    	private static final Map<String,Long> proposalMap = new ConcurrentHashMap<String, Long>();
    }
    
    public static Map<String,Long> getProposalMap(){
    	return proposalHolder.proposalMap;
    }
    
    //存一般键值对
    private static class keyValueHolder{
    	private static final Map<String,String> keyvalueMap = new ConcurrentHashMap<String, String>();
    }
    
    public static Map<String,String> getKeyValueHolderMap(){
    	return keyValueHolder.keyvalueMap;
    }
    
    
    //线城池
    private static class CachedThreadPoolHolder{
    	private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    }
    
    public static ExecutorService getCachedThreadPool(){
    	return CachedThreadPoolHolder.cachedThreadPool;
    }
    
    //定时器
    private static class TimeExecutorHolder{
    	private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);
    }
    
    public static ScheduledExecutorService getTimeExecutor(){
    	return TimeExecutorHolder.scheduledExecutor;
    }
    
    //wetchat 接口相关
    public static final String APPID = prop.getProperty("appid");
    public static final String APPSECRET = prop.getProperty("appsecret");
    //正确返回{"access_token": "ACCESS_TOKEN", "expires_in": 7200}
    public static final String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
    
    public static String getSendModelMessageUrl(){
    	String access_token = null;
    	//map中是否有 是否过期
    	Map<String, String> keyValueHolderMap = Constant.getKeyValueHolderMap();
    	if(keyValueHolderMap.containsKey("expires_in")){
    		String expires_in = keyValueHolderMap.get("expires_in");
    		//如果没过期
    		if(System.currentTimeMillis()<Long.valueOf(expires_in)){
    			access_token = keyValueHolderMap.get("access_token");
    			return "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
    		}
    	}
    	
    	//过期或者没有
		JSONObject tokenobj = HttpClientUtil.httpGet(tokenUrl);
		if(tokenobj.has("access_token")){
			access_token = tokenobj.getString("access_token");
			int expires_in = tokenobj.getInt("expires_in");
			Constant.getKeyValueHolderMap().put("expires_in", (System.currentTimeMillis()+expires_in)+"");
			Constant.getKeyValueHolderMap().put("access_token", access_token);
			String messageurl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			return messageurl;
		}

		return null;
    }
    
}
