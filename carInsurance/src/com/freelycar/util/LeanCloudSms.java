package com.freelycar.util;

import java.util.Map;

import org.junit.Test;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;

public class LeanCloudSms {

	static {
		AVOSCloud.initialize("YPVPvcghD0yT1CtQKUOpOUGI-gzGzoHsz", "AnrwmLo01qL7RuKNbV0NwWR4",
				"TPP91RhAzOntQMb2elHO7nKu");
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
	
	@Test
	public void testverify(){
		verifySMSCode("18252085210", "016700");
	}
}
