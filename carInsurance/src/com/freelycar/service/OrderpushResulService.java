package com.freelycar.service; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.OrderpushResulDao;
import com.freelycar.entity.OrderpushResul;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.RESCODE;  
/**  
 *  
 */  
@Transactional
@Service
public class OrderpushResulService  
{  
    /********** 注入OrderpushResulDao ***********/  
    @Autowired
	private OrderpushResulDao orderpushResulDao;
    
    
    
    
    
    //增加一个OrderpushResul
    public Map<String,Object> saveOrderpushResul(OrderpushResul orderpushResul){
		
		orderpushResulDao.saveOrderpushResul(orderpushResul);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
    //增加一个OrderpushResul
    public Map<String,Object> getOrderpushResulByOrderId(String orderId){
    	OrderpushResul orderpushResulByOrderId = orderpushResulDao.getOrderpushResulByOrderId(orderId);
    	return RESCODE.SUCCESS.getJSONRES(orderpushResulByOrderId);
    }
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listOrderpushResul(OrderpushResul orderpushResul, int page,int number){
	    int from = (page-1)*number;
	    List<OrderpushResul> listPage = orderpushResulDao.listOrderpushResul(orderpushResul,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = orderpushResulDao.getOrderpushResulCount(orderpushResul);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除OrderpushResul
	public Map<String,Object> removeOrderpushResulById(String id){
		boolean res =  orderpushResulDao.removeOrderpushResulById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新OrderpushResul
	public Map<String,Object> updateOrderpushResul(OrderpushResul orderpushResul){
	    orderpushResulDao.updateOrderpushResul(orderpushResul);
	    return RESCODE.SUCCESS.getJSONRES();
	}

	
	//核保结果推送
	public Map<String, Object> orderpushResult(String result) {
		try {
    		System.out.println("核保推送结果： "+result);
			JSONObject resObj = new JSONObject(result);
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				JSONObject resultobj = resObj.getJSONObject("data");
				JSONObject underwritingJson = resultobj.getJSONObject("underwritingJson");
				
				if(underwritingJson.has("errorMsg")){
					return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(underwritingJson.getString("errorMsg"));
				}
				
				
				int state = resultobj.getInt("state");
				String orderId = resultobj.getString("orderId");
				int underwritingPriceCent = resultobj.getInt("underwritingPriceCent");
				
				OrderpushResul qr = new OrderpushResul();
				qr.setState(state);
				qr.setUnderwritingJson(underwritingJson.toString());
				qr.setUnderwritingPriceCent(underwritingPriceCent);
				orderpushResulDao.updateOrderpushResulBySpecifyId(qr, "orderId");
				
				Map<String,Object> msg = new HashMap<>();
				msg.put("orderId", orderId);
				return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
		return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
	}
    
	//承保接口推送
	public Map<String, Object> chenbaopPushResult(String result) {
		try {
			System.out.println(result);
			JSONObject resObj = new JSONObject(result);
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				JSONObject resultobj = resObj.getJSONObject("data");
				
				
				int state = resultobj.getInt("state");
				String orderId = resultobj.getString("orderId");
				String ciPolicyNo = resultobj.getString("ciPolicyNo");
				String biPolicyNo = resultobj.getString("biPolicyNo");
				
				OrderpushResul qr = new OrderpushResul();
				qr.setState(state);
				orderpushResulDao.updateOrderpushResulBySpecifyId(qr, "orderId");
				
				Map<String,Object> msg = new HashMap<>();
				msg.put("orderId", orderId);
				return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
	}
	
	//支付接口推送
	public Map<String, Object> payPushResult(String result) {
		try {
			System.out.println(result);
			JSONObject resObj = new JSONObject(result);
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				JSONObject resultobj = resObj.getJSONObject("data");
				
				
				int state = resultobj.getInt("state");
				String orderId = resultobj.getString("orderId");
				String message = resultobj.getString("message");
				
				OrderpushResul qr = new OrderpushResul();
				qr.setState(state);
				orderpushResulDao.updateOrderpushResulBySpecifyId(qr, "orderId");
				
				Map<String,Object> msg = new HashMap<>();
				msg.put("orderId", orderId);
				return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
	}
}