package com.freelycar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constant {

    private static Properties prop = new Properties();
    static {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();  
        if (cl == null)
            cl = Constant.class.getClassLoader(); 
        InputStream in = cl.getResourceAsStream("jdbc.properties");
        try {
            prop.load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


      UPLOAD_URL = prop.getProperty("upload_url");
      UPLOAD_FOLDER = prop.getProperty("upload_folder");    
      }
    
    
    
    //上传文件 映射目录
    public static final String UPLOAD_URL;
    //上传文件目录
    public static final String UPLOAD_FOLDER;
}
