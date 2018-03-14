package com.freelycar.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;

import com.freelycar.util.HttpClientUtil;

public class LuoTuoTest {

	@Test//询价
	public void testcreateEnquiry(){
		
		Map<String,Object> obj = new HashMap<>();
		obj.put("phone", "18362981113");
		obj.put("licenseNumber", "苏A129B0");
		obj.put("cityCode", "320100");
		obj.put("cityName", "南京");
		obj.put("ownerName", "朱祥珍");
		obj.put("insuranceCompanyId", "2");
		
		
		//obj.put("insurances", "[{\"englishName\":\"车损\",\"compensation\":\"true\",\"isSupported\":\"false\",\"price\":\"1\",\"isToubao\":\"1\",\"amountStr\":\"投保\",\"quotesPrice\":\"0\",\"state\":\"1\",\"isHot\":\"false\",\"insuranceId\":\"1\",\"type\":\"0\",\"insuranceName\":\"车辆损失险\"},{\"englishName\":\"三者\",\"compensation\":\"true\",\"isSupported\":\"false\",\"price\":\"500000\",\"isToubao\":\"1\",\"amountStr\":\"50万\",\"quotesPrice\":\"0\",\"state\":\"1\",\"isHot\":\"false\",\"insuranceId\":\"2\",\"type\":\"0\",\"insuranceName\":\"第三者责任险\"}]");
		obj.put("insurances", "[{\"compensation\":\"true\",\"price\":\"1\",\"isToubao\":\"1\",\"insuranceId\":\"1\"},{\"compensation\":\"true\",\"price\":\"500000\",\"isToubao\":\"1\",\"insuranceId\":\"2\"}]");
		//obj.put("insurancesList", "[{"englishName":"车损","compensation":true,"isSupported":false,"price":1,"isToubao":"1","amountStr":"投保","quotesPrice":0,"state":1,"isHot":false,"insuranceId":1,"type":0,"insuranceName":"车辆损失险"},{"englishName":"三者","compensation":true,"isSupported":false,"price":500000,"isToubao":"1","amountStr":"50万","quotesPrice":0,"state":1,"isHot":false,"insuranceId":2,"type":0,"insuranceName":"第三者责任险"}]");
		//obj.put("insuranceBeginTime", "0");
		obj.put("forceInsuranceStartTime", "1533830400");
		
		
		HttpClientUtil.httpPost("http://www.xhnky.cn/carInsurance/api/insurance/queryPrice", obj);
		//HttpClientUtil.httpPost("http://localhost:8080/carInsurance/api/insurance/queryPrice", obj);
	}
	
	@Test//核保
	public void testsubmitProposal(){
		
		Map<String,Object> obj = new HashMap<>();
		obj.put("offerId", "200-20180314160718-4c2fe2");
		obj.put("ownerName", "朱祥珍");
		obj.put("idCard", "320113196303202013");
		obj.put("phone", "13705159506");
		obj.put("contactAddress", "江苏南京");
		
		HttpClientUtil.httpPost("http://www.xhnky.cn/carInsurance/api/insurance/submitProposal", obj);
		//HttpClientUtil.httpPost("http://localhost:8080/carInsurance/api/insurance/queryPrice", obj);
	}
	@Test
	public void testDate(){
		System.out.println(System.currentTimeMillis());
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date parse = formate.parse("2018-08-10 00:00:00");
			System.out.println(parse.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

