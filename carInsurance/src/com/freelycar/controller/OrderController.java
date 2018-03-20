package com.freelycar.controller; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.InsuranceOrder;
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
    public Map<String, Object> getOrderById(int  orderId){
    	return orderService.getOrderById(orderId);
    }

    
    //增加一个Order
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveOrder(InsuranceOrder order){
		return orderService.saveOrder(order);
	}
	
	//查询所有的Order	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	public Map<String,Object> listOrder(InsuranceOrder order, int page,int number){
		System.out.println(order);
		System.out.println(page);
		System.out.println(number);
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
    
}