package com.freelycar.service; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.CashBackRate;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.RESCODE;
import com.freelycar.util.SocketHelper;  
/**  
 *  
 */  
@Transactional
@Service
public class QuoteRecordService  
{  
    /********** 注入QuoteRecordDao ***********/  
    @Autowired
	private QuoteRecordDao quoteRecordDao;
    
    @Autowired
    private CashbackRecordDao cashbackDao;
    
    
    //增加一个QuoteRecord
    public Map<String,Object> quoteRecordPushDeal(String result){
    	try {
    		System.out.println(result);
			JSONObject resObj = new JSONObject(result);
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				
				JSONObject data = resObj.getJSONObject("data");
				String requestHeader = data.getString("requestHeader");
				
				JSONObject resultobj = data.getJSONObject("result");
				
				String offerId = resultobj.getString("offerId");
				System.out.println("报价推送的offerId:"+offerId);
				String offerDetail = resultobj.getString("offerDetail");
				int state = resultobj.getInt("state");
				
				QuoteRecord qr = quoteRecordDao.getQuoteRecordBySpecify("requestHeader",requestHeader);
				qr.setRequestHeader(requestHeader);
				qr.setState(state);
				qr.setOfferId(offerId);
				qr.setOfferDetail(offerDetail);
				quoteRecordDao.update(qr);
				
				
				System.out.println("准备推送openId"+qr.getOpenId());
				//处理报价明细
				JSONObject obj = new JSONObject(offerDetail);
				obj.put("offerId", offerId);
				obj.put("insuranceStartTime", resultobj.getInt("insuranceStartTime"));
				obj.put("forceInsuranceStartTime", resultobj.getInt("forceInsuranceStartTime"));
				
				//根据当前价格  计算 返现 金额
				List<CashBackRate> listCashbackRate = cashbackDao.listCashbackRate();
				if(listCashbackRate.isEmpty()){
					obj.put("cashBackRate", 0);
					obj.put("cashBackMoney", 0);
				}else{
					float rate = listCashbackRate.get(0).getRate();
					BigDecimal   ratebig   =   new   BigDecimal(rate);
					double ratevalue =ratebig.setScale(2,RoundingMode.HALF_UP).doubleValue();
					
					obj.put("cashBackRate", ratevalue);
					double currentPrice = obj.getDouble("currentPrice");
					double cashback = currentPrice * ratevalue;
					BigDecimal   b   =   new   BigDecimal(cashback);
					cashback =b.setScale(2,RoundingMode.HALF_UP).doubleValue();
					obj.put("cashBackMoney", cashback);
				}
				SocketHelper.sendMessage(qr.getOpenId(),RESCODE.PUSHBACK_BAOJIA.getJSONObject(obj).toString());
				
				Map<String,Object> msg = new HashMap<>();
				msg.put("offerId", offerId);
				return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
		return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
	}
    
	
    //增加一个QuoteRecord
    public Map<String,Object> saveQuoteRecord(QuoteRecord quoteRecord){
    	
    	quoteRecordDao.saveQuoteRecord(quoteRecord);
    	return RESCODE.SUCCESS.getJSONRES();
    }
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listQuoteRecord(QuoteRecord quoteRecord, int page,int number){
	    int from = (page-1)*number;
	    List<QuoteRecord> listPage = quoteRecordDao.listQuoteRecord(quoteRecord,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = quoteRecordDao.getQuoteRecordCount(quoteRecord);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除QuoteRecord
	public Map<String,Object> removeQuoteRecordById(String id){
		boolean res =  quoteRecordDao.removeQuoteRecordById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新QuoteRecord
	public Map<String,Object> updateQuoteRecord(QuoteRecord quoteRecord){
	    quoteRecordDao.updateQuoteRecord(quoteRecord);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}