package com.freelycar.controller; 

import com.freelycar.entity.Reciver;
import com.freelycar.service.ReciverService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/reciver")
public class ReciverController  
{  
    /********** 注入ReciverService ***********/  
    @Autowired
	private ReciverService reciverService;







    
    //增加一个Reciver
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveReciver(Reciver reciver){
		return reciverService.saveReciver(reciver);
	}
	
	//查询所有的Reciver	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listReciver(Reciver reciver, int _pageNumber,int _pageSize){
		return reciverService.listReciver(reciver, _pageNumber, _pageSize);
	}
	
	//根据id删除Reciver
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeReciverById(String id){
		return reciverService.removeReciverById(id);
	}
	
	//更新Reciver
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateReciver(Reciver reciver){
	    return reciverService.updateReciver(reciver);
	}
    
}