package com.freelycar.controller; 

import com.freelycar.entity.Invition;
import com.freelycar.service.InvitionService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/invition")
public class InvitionController  
{  
    /********** 注入InvitionService ***********/  
    @Autowired
	private InvitionService invitionService;


    
    //增加一个Invition
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveInvition(Invition invition){
		return invitionService.saveInvition(invition);
	}
	
	//查询所有的Invition	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	public Map<String,Object> listInvition(Invition invition, int page,int number){
		return invitionService.listInvition(invition, page, number);
	}
	
	//根据id删除Invition
	@RequestMapping(value = "/remove",method = RequestMethod.POST)
	public Map<String,Object> removeInvitionById(String id,String ...ids){
		return invitionService.removeInvitionById(id,ids);
	}
	
	//更新Invition
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateInvition(Invition invition){
	    return invitionService.updateInvition(invition);
	}
    
}