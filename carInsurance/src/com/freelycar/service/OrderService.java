package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.dao.InvoiceInfoDao;
import com.freelycar.dao.OrderDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.InvoiceInfo;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;
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
    
    @Autowired
    private InvoiceInfoDao invoiceInfoDao;
    
    @Autowired
    private QuoteRecordDao quoteRecordDao;
    
    @Autowired
    private CashbackRecordDao cashbackDao;
    
    public Map<String, Object> getOrderById(int id){
    	InsuranceOrder orderById = orderDao.getOrderById(id);
    	if(orderById != null){
    		Map<String, Object> jsonres = RESCODE.SUCCESS.getJSONRES(orderById);
    		String orderId = orderById.getOrderId();
    		if(Tools.notEmpty(orderId)){
    			Map<String, Object> res = fingOrderIdRelative(orderId);
        		res.put("data", orderById);
        		return res;
    		}
    		return jsonres;
    	}else{
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}
    }
    
    public Map<String, Object> getOrderByOrderId(String orderId){
    	InsuranceOrder orderById = orderDao.getOrderByOrderId(orderId);
    	if(orderById != null){
    		Map<String, Object> res = fingOrderIdRelative(orderId);
    		res.put("data", orderById);
    		return res;
    	}else{
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}
    }
    
    
    
    
    //根据orderId查询发票和收件人信息 和报价记录
    private Map<String, Object> fingOrderIdRelative(String orderId){
    	Map<String, Object> jsonres = RESCODE.SUCCESS.getJSONRES();
    	InvoiceInfo invoiceInfoByOrderId = invoiceInfoDao.getInvoiceInfoByOrderId(orderId);
		if(invoiceInfoByOrderId != null){
			jsonres.put("invoiceInfo", invoiceInfoByOrderId);
		}
		CashbackRecord cashbackRecordByOrderId = cashbackDao.getCashbackRecordByOrderId(orderId);
		if(invoiceInfoByOrderId != null){
			jsonres.put("cashbackInfo", cashbackRecordByOrderId);
		}
		
		//查询报价记录
		QuoteRecord quoteRecordBySpecify = quoteRecordDao.getQuoteRecordBySpecify("offerId", orderId);
		if(quoteRecordBySpecify != null){
			jsonres.put("quoteRecord", quoteRecordBySpecify);
		}
    	return jsonres;
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
	    	for(InsuranceOrder io : listPage){
	    		io.setStateString(INSURANCE.getQuotestateName(io.getState()));
	    	}
	    	
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
	public Map<String,Object> updateOrderByOfferId(InsuranceOrder order){
		InsuranceOrder or = orderDao.getOrderByOrderId(order.getOrderId());
		or.setBackmoney(order.getBackmoney());
		or.setBiPolicyNo(order.getBiPolicyNo());
		or.setBiPolicyPrice(order.getBiPolicyPrice());
		or.setCashback(order.getCashback());
		or.setCiPolicyNo(order.getBiPolicyNo());
		or.setCiPolicyPrice(order.getCiPolicyPrice());
		or.setExpressCompany(order.getExpressCompany());
		or.setExpressNumber(order.getExpressNumber());
		or.setLicenseNumber(order.getLicenseNumber());
		or.setOfferDetail(order.getOfferDetail());
		orderDao.updateOrder(or);
	    return RESCODE.SUCCESS.getJSONRES();
	}
	//更新Order
	public Map<String,Object> updateOrder(InsuranceOrder order){
		orderDao.updateOrder(order);
		return RESCODE.SUCCESS.getJSONRES();
	}
    
}