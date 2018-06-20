package com.freelycar.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;

public class LeanCloudSms {

	static {
		AVOSCloud.initialize("HIktLTOXYtCUOliWdq2XLlbR-gzGzoHsz", "3rhewMSocLrXdN1PrWifb5T3",
				"VPp1zn6x40touQFHB1EmQcfD");
	}

	// https://leancloud.cn/docs/sms-guide.html#hash1173669716
	public static Map<String, Object> sendSmsCode(String phone) {
		try {
			AVOSCloud.requestSMSCode(phone, "小易车险", null, 10);
			return RESCODE.SUCCESS.getJSONRES();
		} catch (AVException ex) {
			ex.printStackTrace();
			return RESCODE.SMS_SEND_FAIL.getJSONRES(ex.getMessage());
		}
	}

	public static Map<String,Object> verifySMSCode(String phone, String code) {
		try {
			AVOSCloud.verifySMSCode(code, phone);
			return RESCODE.SUCCESS.getJSONRES();
		} catch (AVException ex) {
			/* 验证失败 */
			return RESCODE.SMS_ERROR.getJSONRES(ex.getMessage());
		}
	}

	/**
	 * 发送通知类消息
	 */
/*	public static Map<String, Object> sendNotice() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		//使用实际的值来替换模板中的变量
//		parameters.put("order_id", "7623432424540");
		try {
			AVOSCloud.requestSMSCode("177xxxxxxxx", "车险办理成功通知", parameters);
			return RESCODE.SUCCESS.getJSONRES();
		} catch (AVException e) {
			e.printStackTrace();
			return RESCODE.SMS_ERROR.getJSONRES(e.getMessage());
		}
	}

	public static void main(String[] args) {
		sendNotice();
	}*/
	
	@Test
	public void testverify(){
		verifySMSCode("18252085210", "016700");
	}
}
