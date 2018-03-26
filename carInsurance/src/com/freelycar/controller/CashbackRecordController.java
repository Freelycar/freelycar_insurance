package com.freelycar.controller; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.CashBackRate;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.service.CashbackRecordService;
import com.freelycar.util.RESCODE;
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



	  //增加一个CashBackRate
    @RequestMapping(value = "/saveCashbackRecord",method = RequestMethod.POST)
    public Map<String, Object> saveCashbackRecord(CashBackRate cashBackRate){
	   cashbackRecordService.saveCashbackRecord(cashBackRate);
	   return RESCODE.SUCCESS.getJSONRES();
    }
  
	//查询所有的CashBackRate	
    @RequestMapping(value = "/listCashbackRate",method = RequestMethod.GET)
	public Map<String, Object> listCashbackRate(){
		return cashbackRecordService.listCashbackRate();
	}
	
	//删除CashBackRate	
    @RequestMapping(value = "/deleteCashBackRate",method = RequestMethod.GET)
	public Map<String, Object> deleteCashBackRate(int id){
		cashbackRecordService.deleteCashBackRate(id);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	//更新CashBackRate
    @RequestMapping(value = "/updateCashBackRate",method = RequestMethod.POST)
	public Map<String, Object> updateCashBackRate(CashBackRate rate){
		cashbackRecordService.updateCashBackRate(rate);
		return RESCODE.SUCCESS.getJSONRES();
	}
    
    
    //目前使用
	//更新或增加CashBackRate
    @RequestMapping(value = "/saveupdateCashBackRate",method = RequestMethod.POST)
	public Map<String, Object> saveupdateCashBackRate(CashBackRate rate){
		cashbackRecordService.saveupdateCashBackRate(rate);
		return RESCODE.SUCCESS.getJSONRES();
	}
    
    //查看当前返现率
    @RequestMapping(value = "/getCurentCashBackRate",method = RequestMethod.GET)
    public Map<String, Object> getCurentCashBackRate(){
    	return cashbackRecordService.getCurentCashBackRate();
    }
    
    //增加一个CashbackRecord
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveCashbackRecord(@RequestBody CashbackRecord cashbackrecord){
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
	public Map<String,Object> updateCashbackRecord(@RequestBody CashbackRecord cashbackrecord){
	    return cashbackRecordService.updateCashbackRecord(cashbackrecord);
	}
    
}