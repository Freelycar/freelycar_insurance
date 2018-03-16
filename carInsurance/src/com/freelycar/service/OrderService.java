package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.OrderDao;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.util.RESCODE;
/**  
 *  
 */  
@Transactional
@Service
public class OrderService  
{  
    /********** 注入OrderDao ***********/  
    @Autowired
	private OrderDao orderDao;
    
    public Map<String, Object> getOrderById(int id){
    	InsuranceOrder orderById = orderDao.getOrderById(id);
    	if(orderById != null){
    		return RESCODE.SUCCESS.getJSONRES(orderById);
    	}else{
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}
    }
    
    
    
    //增加一个Order
    public Map<String,Object> saveOrder(InsuranceOrder order){
		
		orderDao.saveOrder(order);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listOrder(InsuranceOrder order, int page,int number){
	    int from = (page-1)*number;
	    List<InsuranceOrder> listPage = orderDao.listOrder(order,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = orderDao.getOrderCount(order);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除Order
	public Map<String,Object> removeOrderById(String id){
		boolean res =  orderDao.removeOrderById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新Order
	public Map<String,Object> updateOrder(InsuranceOrder order){
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}