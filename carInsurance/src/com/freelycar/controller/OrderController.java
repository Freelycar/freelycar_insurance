package com.freelycar.controller; 

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.Invition;
import com.freelycar.service.OrderService;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/order")
public class OrderController  
{  
    /********** 注入OrderService ***********/  
    @Autowired
	private OrderService orderService;


    @RequestMapping(value = "/getOrderById",method = RequestMethod.GET)
    public Map<String, Object> getOrderById(int id){
    	System.out.println(id);
    	return orderService.getOrderById(id);
    }
    
    @RequestMapping(value = "/getOrderByOrderId",method = RequestMethod.GET)
    public Map<String, Object> getOrderByOrderId(String orderId){
    	return orderService.getOrderByOrderId(orderId);
    }

    
    
    //渠道统计 根据sourceId 统计订单 饼图
    @RequestMapping(value = "/getPieChart",method = RequestMethod.GET)
    public Map<String, Object> getPieChart(Date startTime,Date endTime){
    	return orderService.getCountBySourId(startTime, endTime);
    }
    
	
	//根据渠道统计
	//查询所有的Invition	
	@RequestMapping(value = "/listCount",method = RequestMethod.GET)
	public Map<String,Object> listCount(Invition invition, int page,int number,Date startTime,Date endTime){
		return orderService.listCount(invition, page, number,startTime, endTime);
	}
    
    
    //增加一个Order
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveOrder(InsuranceOrder order){
		return orderService.saveOrder(order);
	}
	
	//查询所有的Order	
	@RequestMapping(value = "/list")
	public Map<String,Object> listOrder(InsuranceOrder order, int page,int number){
		return orderService.listOrder(order, page, number);
	}
	
	//根据id删除Order
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeOrderById(String id){
		return orderService.removeOrderById(id);
	}
	
	//更新Order
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateOrder(InsuranceOrder order){
	    return orderService.updateOrder(order);
	}

	//根据clientId查询订单
	@RequestMapping(value = "/getClientQuoteRecordByLicenseNumber",method = RequestMethod.GET)
	public Map<String,Object> getClientQuoteRecordByLicenseNumber(String licenseNumber){
	    return orderService.getClientQuoteRecordByLicenseNumber(licenseNumber);
	}
	
	//根据clientId查询订单
	@RequestMapping(value = "/getClientOrderByLicenseNumber",method = RequestMethod.GET)
	public Map<String,Object> getClientOrderByLicenseNumber(String licenseNumber,int page,int number){
		return orderService.getClientOrderByLicenseNumber(licenseNumber,page,number);
	}
}