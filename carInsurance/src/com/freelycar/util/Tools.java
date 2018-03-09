package com.freelycar.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Tools {

	public static String uuid() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}


	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 按照yyyy年MM月dd日 HH:mm的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2StrCN(Date date) {
		return date2Str(date, "yyyy年MM月dd日 HH:mm");
	}
	
	/**
	 * 按照yyyy年MM月dd日 HH:mm的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String date2ShortStrCN(Date date) {
		return date2Str(date, "yyyy年MM月dd日");
	}
	/**
	 * 按照yyyy-MM-dd的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String date2StrShort(Date date) {
		return date2Str(date, "yyyy-MM-dd");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date) {
		if (notEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		} else {
			return null;
		}
	}
	
	/**
     * 按照yyyy-MM-dd的格式，字符串转日期
     * 
     * @param date
     * @return
     */
	public static Date str2DateShort(String date) {
	    if (notEmpty(date)) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            return sdf.parse(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return new Date();
	    } else {
	        return null;
	    }
	}

	
	/**
	 * 按照参数format的格式，日期转字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
	    if (date != null) {
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        return sdf.format(date);
	    } else {
	        return "";
	    }
	}
	
	
	/**
	 * 创建文件路径
	 * @param url
	 */
	public static void createDir(String url){
		Path path = Paths.get(url);
        if(Files.notExists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	
	/**
	 * 
	 * @param str
	 * @return 首字母大写
	 */
	public static String capFirst(String str){
	   
	    if (notEmpty(str)) {
	        StringBuilder sb = new StringBuilder(str);
	        char first = sb.charAt(0);
	        if(first>='a' && first<='z'){
	            sb.setCharAt(0, (char)(first-32));
	        }
	        return sb.toString();
	        
	    } else {
	        return null;
	    }
	}
	
	/**
	 * 
	 * @param str
	 * @return 首字母小写
	 */
	public static String uncapFirst(String str){
	    
	    if (notEmpty(str)) {
	        StringBuilder sb = new StringBuilder(str);
	        char first = sb.charAt(0);
	        if(first>='A' && first<='A'){
	            sb.setCharAt(0, (char)(first+32));
	        }
	        return sb.toString();
	        
	    } else {
	        return null;
	    }
	}
	
	/**
	 * 提取用，分割的含有数字的数组
	 * @param str
	 * @return
	 */
	public static int[] splitInt(String str){
	    String[] split = str.split(",");
	    int len = 0;
	    for(String s : split){
	        if(notEmpty(s)){
	            len++;
	        }
	    }
	    int arr[] = new int[len];
	    for(int i=0,length=split.length;i<length;i++){
            String s = split[i];
	        if(notEmpty(s)){
                arr[i] = Integer.parseInt(s);
            }
        }
	    return arr;
	}
	
	
	// MD5密码加密工具
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.reset();
		try {
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
	
    
}
