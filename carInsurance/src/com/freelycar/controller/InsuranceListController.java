package com.freelycar.controller; 

import com.freelycar.entity.InsuranceList;
import com.freelycar.service.InsuranceListService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/insurancelist")
public class InsuranceListController  
{  
    /********** 注入InsuranceListService ***********/  
    @Autowired
	private InsuranceListService insuranceListService;

    
    //增加一个InsuranceList
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveInsuranceList(InsuranceList insurancelist){
		return insuranceListService.saveInsuranceList(insurancelist);
	}
	
	//查询所有的InsuranceList	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listInsuranceList(InsuranceList insuranceList, int _pageNumber,int _pageSize){
		return insuranceListService.listInsuranceList(insuranceList, _pageNumber, _pageSize);
	}
	
	//根据id删除InsuranceList
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeInsuranceListById(String id){
		return insuranceListService.removeInsuranceListById(id);
	}
	
	//更新InsuranceList
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateInsuranceList(InsuranceList insurancelist){
	    return insuranceListService.updateInsuranceList(insurancelist);
	}
    
}