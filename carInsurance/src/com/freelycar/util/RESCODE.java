package com.freelycar.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public enum RESCODE {

	SUCCESS(0, "成功"), 
	FAIL(1, "失败"), 
	DELETE_FAIL(100, "删除失败"), 
	NOT_FOUND(101, "无记录"), 
	DATA_EXIST(102, "数据库已存在"), 
	
	
	USER_PHONE_REGISTERED(200,"手机号已注册"),
	USER_MAIL_USERD(201,"该邮箱已被注册"),
	USER_NAMEPAS_ERROR(203,"用户名或密码错误"),
	USER_EXIST(204,"用户已存在"),
	USER_NOT_EXIST(2041,"用户不存在在"),
	USER_NO_PHONE(205,"新用户没有手机号"),
	USER_NAME_LICENSENUMBER_NOT_FOUND(206,"没有查询到该车牌与姓名的续保信息"),
	USER_NAME_EMPTY(207,"用户名为空"),
	USER_PASS_EMPTY(208,"用户密码为空"),
	INV_NAME_EMPTY(209,"渠道名称为空"),
	INV_CODE_EMPTY(210,"邀请码为空"),
	INV_NAME_EXIST(209,"渠道名称已存在"),
	INV_CODE_EXIST(210,"邀请码已存在"),
	USER_LICENSENUMBER_EMPTY(211,"用户车牌为空"),
	USER_OPENID_EMPTY(212,"用户openid为空"),
	CASH_PAYEE_EMPTY(21,"收款人姓名为空"),
	CASH_ACCOUNT_EMPTY(21,"收款人账户为空"),
	CASH_BANKNAME_EMPTY(21,"收款人银行名称为空"),
	
	
	INSURANCE_BAOJIA_SUCCESS(300,"报价成功"),
	
	SMS_TIMEOUT(600,"短信验证码超时"),
	SMS_CODE_EMPTY(601,"短信验证码为空"),
	SMS_ERROR(602,"短信验证码错误"),
	SMS_SEND_FAIL(603,"短信验证码发送失败"),
	SMS_PHONE_EMPTY(604,"手机号码为空"),
	SMS_PHONE_ERROR(605,"手机号码错误"),
	
	
	PUSHBACK_BAOJIA(606,"报价推送"),
	PUSHBACK_HEBAO_EXCEPTION(607,"核保异常"),
	PUSHBACK_HEBAO(608,"核保推送"),
	PUSHBACK_BAOJIA_FAIL(609,"报价失败"),
	REQUEST_BAOJIA_EXCEPTION(610,"报价异常"),
	
	
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
		msg.put("message", this.nMsg.equals("success")?"操作成功":"操作失败");
		jsonres.put("errorMsg", msg);
		
		jsonres.put("data", data);
		jsonres.put("successful", this.nMsg.equals("success"));
		
		return jsonres;
	}
	
	
	
	public JSONObject getJSONObject(Object data){
		JSONObject map = new JSONObject();
		map.put(CODE, this.nCode);
		map.put(MSG, this.nMsg);
		map.put(DATA, data);
		return map;
	}
	
}
