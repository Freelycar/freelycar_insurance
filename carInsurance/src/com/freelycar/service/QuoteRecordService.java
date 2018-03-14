package com.freelycar.service; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.RESCODE;  
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
    
    
    //增加一个QuoteRecord
    public Map<String,Object> quoteRecordPushDeal(String result){
    	try {
    		System.out.println(result);
			JSONObject resObj = new JSONObject(result);
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				
				JSONObject data = resObj.getJSONObject("data");
				JSONObject resultobj = data.getJSONObject("result");
				
				String offerId = resultobj.getString("offerId");
				String offerDetail = resultobj.getString("offerDetail");
				int state = resultobj.getInt("state");
				String requestHeader = resultobj.getString("requestHeader");
				
				QuoteRecord qr = quoteRecordDao.getQuoteRecordBySpecify("requestHeader");
				qr.setRequestHeader(requestHeader);
				qr.setState(state);
				qr.setOfferId(offerId);
				qr.setOfferDetail(offerDetail);
				quoteRecordDao.update(qr);
				
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