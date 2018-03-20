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
    
    
    //线城池
    private static class CachedThreadPoolHolder{
    	private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    }
    
    public static ExecutorService getCachedThreadPool(){
    	return CachedThreadPoolHolder.cachedThreadPool;
    }
    
    //定时器
    private static class TimeExecutorHolder{
    	private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1000);
    }
    
    public static ScheduledExecutorService getTimeExecutor(){
    	return TimeExecutorHolder.scheduledExecutor;
    }
    
}
