package com.freelycar.controller; 

import com.freelycar.entity.CashbackRecord;
import com.freelycar.service.CashbackRecordService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/cashbackrecord")
public class CashbackRecordController  
{  
    /********** 注入CashbackRecordService ***********/  
    @Autowired
	private CashbackRecordService cashbackRecordService;


    
    //增加一个CashbackRecord
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveCashbackRecord(CashbackRecord cashbackrecord){
		return cashbackRecordService.saveCashbackRecord(cashbackrecord);
	}
	
	//查询所有的CashbackRecord	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listCashbackRecord(CashbackRecord cashbackRecord, int _pageNumber,int _pageSize){
		return cashbackRecordService.listCashbackRecord(cashbackRecord, _pageNumber, _pageSize);
	}
	
	//根据id删除CashbackRecord
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeCashbackRecordById(String id){
		return cashbackRecordService.removeCashbackRecordById(id);
	}
	
	//更新CashbackRecord
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateCashbackRecord(CashbackRecord cashbackrecord){
	    return cashbackRecordService.updateCashbackRecord(cashbackrecord);
	}
    
}