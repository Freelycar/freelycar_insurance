package com.freelycar.controller; 

import com.freelycar.entity.InvoiceInfo;
import com.freelycar.service.InvoiceInfoService;
import com.freelycar.util.RESCODE;

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
@RequestMapping(value = "/invoiceinfo")
public class InvoiceInfoController  
{  
    /********** 注入InvoiceInfoService ***********/  
    @Autowired
	private InvoiceInfoService invoiceInfoService;
    
    //增加一个InvoiceInfo
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveInvoiceInfo(@RequestBody InvoiceInfo invoiceinfo){
		return invoiceInfoService.saveInvoiceInfo(invoiceinfo);
	}
	
    @RequestMapping(value = "/getInvoiceInfoByOpenId",method = RequestMethod.GET)
    public Map<String, Object> getInvoiceInfoByOpenId(String openId){
    	return invoiceInfoService.getInvoiceInfoByOpenId(openId);
    }
    
    
	//查询所有的InvoiceInfo	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listInvoiceInfo(InvoiceInfo invoiceInfo, int _pageNumber,int _pageSize){
		return invoiceInfoService.listInvoiceInfo(invoiceInfo, _pageNumber, _pageSize);
	}
	
	//根据id删除InvoiceInfo
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeInvoiceInfoById(String id){
		return invoiceInfoService.removeInvoiceInfoById(id);
	}
	
	//更新InvoiceInfo
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateInvoiceInfo(@RequestBody InvoiceInfo invoiceinfo){
		System.out.println(invoiceinfo);
	    return invoiceInfoService.updateInvoiceInfo(invoiceinfo);
	}
    
}