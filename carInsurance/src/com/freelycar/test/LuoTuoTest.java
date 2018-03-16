package com.freelycar.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.junit.Test;

import com.freelycar.util.HttpClientUtil;

public class LuoTuoTest {

	
	@Test
	public void testXunBao() throws ClientProtocolException, IOException{
		
		JSONObject obj = new JSONObject();
		obj.put("licenseNumber", "苏A4PZ99");
		obj.put("ownerName", "刘扬");
		HttpClientUtil.httpPost("http://www.xhnky.cn/carInsurance/api/insurance/queryLastYear", obj);
	}
	
	
	@Test//询价
	public void testcreateEnquiry(){
		
		Map<String,Object> obj = new HashMap<>();
		/*obj.put("phone", "18362981113");
		obj.put("licenseNumber", "苏A129B0");
		obj.put("cityCode", "320100");
		obj.put("cityName", "南京");
		obj.put("ownerName", "朱祥珍");
		obj.put("insuranceCompanyId", "2");*/
		
		/*obj.put("phone", "13913980918");
		obj.put("licenseNumber", "苏A2F6S5");
		obj.put("cityCode", "320100");
		obj.put("cityName", "南京");
		obj.put("ownerName", "王惺嫱");
		obj.put("insuranceCompanyId", "2");*/
		obj.put("phone", "18252085210");
		obj.put("licenseNumber", "苏A4PZ99");
		obj.put("cityCode", "320100");
		obj.put("cityName", "南京");
		obj.put("ownerName", "刘扬");
		obj.put("insuranceCompanyId", "2");
		obj.put("forceInsuranceStartTime", "1524067199");
		
		
		//obj.put("insurances", "[{\"englishName\":\"车损\",\"compensation\":\"true\",\"isSupported\":\"false\",\"price\":\"1\",\"isToubao\":\"1\",\"amountStr\":\"投保\",\"quotesPrice\":\"0\",\"state\":\"1\",\"isHot\":\"false\",\"insuranceId\":\"1\",\"type\":\"0\",\"insuranceName\":\"车辆损失险\"},{\"englishName\":\"三者\",\"compensation\":\"true\",\"isSupported\":\"false\",\"price\":\"500000\",\"isToubao\":\"1\",\"amountStr\":\"50万\",\"quotesPrice\":\"0\",\"state\":\"1\",\"isHot\":\"false\",\"insuranceId\":\"2\",\"type\":\"0\",\"insuranceName\":\"第三者责任险\"}]");
		obj.put("insurances", "[{\"compensation\":\"true\",\"price\":\"1\",\"isToubao\":\"1\",\"insuranceId\":\"1\"},{\"compensation\":\"true\",\"price\":\"500000\",\"isToubao\":\"1\",\"insuranceId\":\"2\"}]");
		//obj.put("insurancesList", "[{"englishName":"车损","compensation":true,"isSupported":false,"price":1,"isToubao":"1","amountStr":"投保","quotesPrice":0,"state":1,"isHot":false,"insuranceId":1,"type":0,"insuranceName":"车辆损失险"},{"englishName":"三者","compensation":true,"isSupported":false,"price":500000,"isToubao":"1","amountStr":"50万","quotesPrice":0,"state":1,"isHot":false,"insuranceId":2,"type":0,"insuranceName":"第三者责任险"}]");
		//obj.put("insuranceBeginTime", "0");
		
		
		HttpClientUtil.httpPost("http://www.xhnky.cn/carInsurance/api/insurance/queryPrice", obj);
		//HttpClientUtil.httpPost("http://localhost:8080/carInsurance/api/insurance/queryPrice", obj);
	}
	
	@Test//核保
	public void testsubmitProposal(){
		
		Map<String,Object> obj = new HashMap<>();
		/*obj.put("offerId", "200-20180315094928-0b4f04");
		obj.put("ownerName", "朱祥珍");
		obj.put("idCard", "320113196303202013");
		obj.put("phone", "13705159506");
		obj.put("contactAddress", "江苏南京");*/
		obj.put("offerId", "109-20180315172137-27694");
		obj.put("ownerName", "刘扬");
		obj.put("idCard", "320112198807021630");
		obj.put("phone", "13705159506");
		obj.put("contactAddress", "江苏南京");
		
		HttpClientUtil.httpPost("http://www.xhnky.cn/carInsurance/api/insurance/submitProposal", obj);
		//HttpClientUtil.httpPost("http://localhost:8080/carInsurance/api/insurance/queryPrice", obj);
	}
	@Test
	public void testDate(){
		System.out.println((int)Math.pow(10, 7));
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parse = formate.parse("2018-04-18 23:59:59");
			System.out.println(parse.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
