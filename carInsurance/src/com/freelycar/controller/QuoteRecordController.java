package com.freelycar.controller; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.QuoteRecord;
import com.freelycar.service.QuoteRecordService;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/quoterecord")
public class QuoteRecordController  
{  
    /********** 注入QuoteRecordService ***********/  
    @Autowired
	private QuoteRecordService quoteRecordService;

    /**
     * 询价推送
     * @param data
     * @return
     */
    
    @RequestMapping(value="/resultPush")
    public Map<String,Object> quoteRecordPush(String data){
    	System.out.println("一开始接受的data:>>>>"+data);
    	return quoteRecordService.quoteRecordPushDeal(data);
    }

    
    //增加一个QuoteRecord
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveQuoteRecord(QuoteRecord quoterecord){
		return quoteRecordService.saveQuoteRecord(quoterecord);
	}
	
	//查询所有的QuoteRecord	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listQuoteRecord(QuoteRecord quoteRecord, int page,int number){
		return quoteRecordService.listQuoteRecord(quoteRecord, page, number);
	}
	
	//根据id删除QuoteRecord
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeQuoteRecordById(String id){
		return quoteRecordService.removeQuoteRecordById(id);
	}
	
	//更新QuoteRecord
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateQuoteRecord(QuoteRecord quoterecord){
	    return quoteRecordService.updateQuoteRecord(quoterecord);
	}
    
}