package com.freelycar.controller; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.OrderpushResul;
import com.freelycar.service.OrderpushResulService;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/orderpushresult")
public class OrderpushResulController  
{  
    /********** 注入OrderpushResulService ***********/  
    @Autowired
	private OrderpushResulService orderpushResulService;

    
    /**
     * 核保推送
     * @param result
     * @return
     */
    @RequestMapping(value="/resultPush")
    public Map<String,Object> orderpushResult(String data){
    	return orderpushResulService.orderpushResult(data);
    }

   /**
    * 承保推送
    * @param result
    * @return
    */
    @RequestMapping(value="/chengbaoPush")
    public Map<String,Object> chengbaoResult(String result){
    	return orderpushResulService.chenbaopPushResult(result);
    }
    
    @RequestMapping(value="/payPush")
    public Map<String,Object> payPushResult(String result){
    	return orderpushResulService.payPushResult(result);
    }
    

    //增加一个OrderpushResul
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public Map<String,Object> getOrderpushResulByOrderId(String orderId){
    	return orderpushResulService.getOrderpushResulByOrderId(orderId);
    }

    
    //增加一个OrderpushResul
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveOrderpushResul(OrderpushResul orderpushresul){
		return orderpushResulService.saveOrderpushResul(orderpushresul);
	}
	
	//查询所有的OrderpushResul	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listOrderpushResul(OrderpushResul orderpushResul, int _pageNumber,int _pageSize){
		return orderpushResulService.listOrderpushResul(orderpushResul, _pageNumber, _pageSize);
	}
	
	//根据id删除OrderpushResul
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeOrderpushResulById(String id){
		return orderpushResulService.removeOrderpushResulById(id);
	}
	
	//更新OrderpushResul
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateOrderpushResul(OrderpushResul orderpushresul){
	    return orderpushResulService.updateOrderpushResul(orderpushresul);
	}
    
}