package com.freelycar.controller; 

import com.freelycar.entity.Client;
import com.freelycar.entity.Invition;
import com.freelycar.service.ClientService;
import com.freelycar.service.InsuranceService;
import com.freelycar.service.InvitionService;
import com.freelycar.util.Constant;
import com.freelycar.util.HttpClientUtil;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.LeanCloudSms;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/client")
public class ClientController  
{  
    /********** 注入ClientService ***********/  
    @Autowired
	private ClientService clientService;

    @Autowired
    private InvitionService invitionService;
    
    @Autowired
    private InsuranceService insuranceService;

    private final static Logger logger = Logger.getLogger(ClientController.class);
    //获取短信验证码
    @RequestMapping(value = "/sms/getCode",method = RequestMethod.POST)
    public Map<String,Object> smsgetCode(@RequestBody String phone){
		return LeanCloudSms.sendSmsCode(phone);
	}
    
    
    //验证短信验证码
    @RequestMapping(value = "/sms/verification",method = RequestMethod.POST)
    public Map<String,Object> smsgetCode(@RequestBody SmsEntity entity){
    	String phone = entity.getPhone();
    	String smscode = entity.getSmscode();
    	String invCode = entity.getInvCode();
    	String openId = entity.getOpenId();
    	Map<String, Object> res = LeanCloudSms.verifySMSCode(phone, smscode);
    	if((int)res.get("code")== 0){
    		Client client = new Client();
    		client.setPhone(phone);
    		client.setOpenId(openId);
    		Invition invitionByInvcode2 = invitionService.getInvitionByInvcode2(invCode);
    		if(invitionByInvcode2 != null){
    			client.setSource(invitionByInvcode2.getName());
    		}
    		client.setLicenseNumber(entity.getLicenseNumber());
    		client.setCashback(false);
    		client.setQuoteStateCode(8);
			client.setQuoteState(INSURANCE.getQuotestateName(8));
    		client.setTransfer(false);
			client.setToubao(false);
    		logger.debug("短信验码字的client"+client);
    		return clientService.saveClient(client);
    	}
    	return res;
    }
    
    @RequestMapping(value = "/getSmallWeixinInfo")
    public String getSmallWeixinInfo(String code){
    	JSONObject httpGet = HttpClientUtil.httpGet("https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.APPID+"&secret="+Constant.APPSECRET+"&js_code="+code+"&grant_type=authorization_code");
    	return httpGet.toString();
    }
    
    
    @RequestMapping(value = "/isExistByOpenId",method = RequestMethod.POST)
    public Map<String,Object> isExistByOpenId(@RequestBody Client client){
    	
    	return insuranceService.queryLastYear(client);
    }

    
    //增加一个Client
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public Map<String,Object> getClientDetail(int clientId){
		return clientService.getClientDetail(clientId);
	}
    
	
    //增加一个Client
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveClient(Client client){
    	return clientService.saveClient(client);
    }
    
	//查询所有的Client	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listClient(Client client, int page,int number){
		return clientService.listClient(client, page, number);
	}
	
	//根据id删除Client
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeClientById(String id){
		return clientService.removeClientById(id);
	}
	
	//更新Client
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateClient(Client client){
	    return clientService.updateClient(client);
	}
    
	
	
	static class SmsEntity{
		private String phone;
		private String smscode;
		private String invCode;
		private String openId;
		private String licenseNumber;
		
		public String getLicenseNumber() {
			return licenseNumber;
		}
		
		public void setLicenseNumber(String licenseNumber) {
			this.licenseNumber = licenseNumber;
		}
		
		public String getOpenId() {
			return openId;
		}
		
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getSmscode() {
			return smscode;
		}
		public void setSmscode(String smscode) {
			this.smscode = smscode;
		}
		public String getInvCode() {
			return invCode;
		}
		public void setInvCode(String invCode) {
			this.invCode = invCode;
		}
	}
}