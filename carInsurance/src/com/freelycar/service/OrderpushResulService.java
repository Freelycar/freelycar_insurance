package com.freelycar.service; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.OrderDao;
import com.freelycar.dao.OrderpushResulDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.OrderpushResul;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.Constant;
import com.freelycar.util.INSURANCE;
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
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private QuoteRecordDao quoteRecordDao;
    
    @Autowired
    private InsuranceService insuranceService;
    

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
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
				final String orderId = resultobj.getString("orderId");
				//根据oferId查提交核保的那条记录
				final InsuranceOrder order = orderDao.getOrderByOrderId(orderId);
				
				JSONObject underwritingJson = new JSONObject(resultobj.getString("underwritingJson"));
				if(underwritingJson.has("errorMsg")){
					//核保异常
					//更新订单状态
					order.setState(INSURANCE.QUOTESTATE_NO_HEBAOSHIBAI.getCode());
					order.setStateString(INSURANCE.QUOTESTATE_NO_HEBAOSHIBAI.getName());
					order.setHebaoMessage(underwritingJson.getString("errorMsg"));
					//SocketHelper.sendMessage(order.getOpenId(), RESCODE.PUSHBACK_HEBAO_EXCEPTION.getJSONObject(underwritingJson.getString("errorMsg")).toString());
					return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(underwritingJson.getString("errorMsg"));
				}
				
				int state = resultobj.getInt("state");
				String licenseNumber = resultobj.getString("licenseNumber");
				int underwritingPriceCent = resultobj.getInt("underwritingPriceCent");
				
				if(underwritingJson.has("biNo")){//商业险
					String bino = underwritingJson.getJSONObject("biNo").getString("value");//保单号
					order.setBiPolicyNo(bino);
				}
				
				if(underwritingJson.has("ciNo")){//较强险
					String ciNo = underwritingJson.getJSONObject("ciNo").getString("value");//保单号
					order.setCiPolicyNo(ciNo);
				}

				//把单号和过期时间存在map中
				JSONObject jsonObject = underwritingJson.getJSONObject("checkCode");
				final Map<String, Long> proposalMap = Constant.getProposalMap();
				
				try {
					//2018-3-19 17:33:27
					proposalMap.put(orderId, format.parse(jsonObject.getString("value")).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				//在这里我们在过期时间之内定时调用 7、确认是否承保接口
				Constant.getTimeExecutor().scheduleAtFixedRate(new Runnable() {
					public void run() {
						
						if(proposalMap.get(orderId) > System.currentTimeMillis()){
							Map<String, Object> confirmChengbao = insuranceService.confirmChengbao(orderId);
							if(confirmChengbao.get("code").equals("0")){//用户交钱了
								//更新支付时间
								order.setPayTime(System.currentTimeMillis());
								
								//等待骆驼推送
								Constant.getTimeExecutor().shutdown();
							}
							
						}else{
							Constant.getTimeExecutor().shutdown();
						}
					}
				},0, 1, TimeUnit.SECONDS);
				
				
				QuoteRecord qr = quoteRecordDao.getQuoteRecordBySpecify("offerId", orderId);
				order.setOfferDetail(qr.getOfferDetail());
				order.setTotalPrice(underwritingPriceCent);
				order.setLicenseNumber(licenseNumber);
				order.setOrderId(orderId);
				//更新订单状态
				order.setState(INSURANCE.QUOTESTATE_NO_HEBAOCHENGGONG.getCode());
				order.setStateString(INSURANCE.QUOTESTATE_NO_HEBAOCHENGGONG.getName());
				
				//更新支付二维码和失效时间
				if(underwritingJson.has("payJson")){//较强险
					JSONObject payQrcode = underwritingJson.getJSONObject("payJson").getJSONObject("payQrcode");//支付信息
					order.setPaycodeurl(payQrcode.getString("content"));
					order.setEffectiveTime(jsonObject.getString("value"));
				}
				
				orderService.updateOrder(order);
				
				
				//推给客户端
				//SocketHelper.sendMessage(order.getOpenId(), RESCODE.PUSHBACK_HEBAO.getJSONObject(orderId).toString());
				
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
				
				
				InsuranceOrder orderByOrderId = orderDao.getOrderByOrderId(orderId);
				orderByOrderId.setState(state);
				orderDao.updateOrder(orderByOrderId);
				
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