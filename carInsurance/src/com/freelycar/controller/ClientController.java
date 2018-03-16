package com.freelycar.controller; 

import com.freelycar.entity.Client;
import com.freelycar.service.ClientService;
import com.freelycar.util.LeanCloudSms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    //获取短信验证码
    @RequestMapping(value = "/sms/getCode",method = RequestMethod.POST)
    public Map<String,Object> smsgetCode(String phone){
		return LeanCloudSms.sendSmsCode(phone);
	}

    //获取短信验证码
    @RequestMapping(value = "/sms/verification",method = RequestMethod.POST)
    public Map<String,Object> smsgetCode(String phone, String code){
    	return LeanCloudSms.verifySMSCode(phone, code);
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
	@RequestMapping(value = "/list")
	public Map<String,Object> listClient(Client client, int pages,int number){
		return clientService.listClient(client, pages, number);
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
    
}