package com.freelycar.util;

import java.util.HashMap;
import java.util.Map;

public enum RESCODE {

	SUCCESS(0, "成功"), 
	FAIL(1, "失败"), 
	DELETE_FAIL(100, "删除失败"), 
	NOT_FOUND(101, "无记录"), 
	
	
	USER_PHONE_REGISTERED(200,"手机号已注册"),
	USER_MAIL_USERD(201,"该邮箱已被注册"),
	USER_NAMEPAS_ERROR(202,"用户名或密码错误"),
	USER_EXIST(202,"用户已存在"),
	
	
	
	SMS_TIMEOUT(600,"短信验证码超时"),
	SMS_CODE_EMPTY(601,"短信验证码为空"),
	SMS_ERROR(602,"短信验证码错误"),
	SMS_SEND_FAIL(603,"短信验证码发送失败"),
	SMS_PHONE_EMPTY(604,"手机号码为空"),
	SMS_PHONE_ERROR(605,"手机号码为空"),
	
	
	LUOTUO_SUCCESS(700,"success"),
	LUOTUO_FAIL(701,"error");
	
	// 定义私有变量
	private int nCode;

	private String nMsg;

	// 构造函数，枚举类型只能为私有
	private RESCODE(int _nCode, String _nMsg) {
		this.nCode = _nCode;
		this.nMsg = _nMsg;
	}

	public String getMsg() {
		return nMsg;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
	
	
	private static String CODE = "code";
	private static String MSG = "msg";
	private static String DATA = "data";
	private static String PAGES = "pages";
	private static String COUNTS = "counts";
	
	
	/**
	 * controller 的json返回值
	 * @return
	 */
	public Map<String,Object> getJSONRES(){
		Map<String,Object> map = new HashMap<>();
		map.put(CODE, this.nCode);
		map.put(MSG, this.nMsg);
		return map;
	}
	
	
	public Map<String,Object> getJSONRES(Object entity){
		Map<String, Object> jsonres = getJSONRES();
		jsonres.put(DATA, entity);
		return jsonres;
	}
	
	public Map<String,Object> getJSONRES(Object entity,int pages,long count){
		Map<String, Object> jsonres = getJSONRES();
		jsonres.put(DATA, entity);
		jsonres.put(PAGES, pages);
		jsonres.put(COUNTS, count);
		return jsonres;
	}
	
	
	public Map<String,Object> getLuoTuoRes(Object data){
		Map<String,Object> jsonres = new HashMap<>();
		
		Map<String,Object> msg = new HashMap<>();
		msg.put("code", this.nMsg);
		msg.put("message", this.nMsg);
		jsonres.put("errorMsg", msg);
		
		jsonres.put("data", data);
		jsonres.put("successful", this.nMsg.equals("success"));
		
		return jsonres;
	}
	
	
}
