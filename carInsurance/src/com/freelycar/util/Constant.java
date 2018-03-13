package com.freelycar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
}
