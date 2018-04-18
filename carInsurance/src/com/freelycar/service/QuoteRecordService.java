package com.freelycar.service; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.CashBackRate;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.RESCODE;
import com.freelycar.util.SocketHelper;
import com.freelycar.util.Tools;  
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
    
    private static Logger log = Logger.getLogger(QuoteRecordService.class);
    
    //增加一个QuoteRecord
    public Map<String,Object> quoteRecordPushDeal(String result){
    	try {
    		if(Tools.isEmpty(result) || !result.startsWith("{")){
    			log.error("错误的报价推送的结果： "+result+"\n\n");
    			return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
    		}
    		
    		
			JSONObject resObj = new JSONObject(result);
			
			String openId = null;
			String message = null;
			System.out.println("最初报价推送的结果"+result);
			log.error("最初报价推送的结果： "+result+"\n\n");
			if(resObj.getJSONObject("errorMsg").getString("code").equals("success")){
				message = resObj.getJSONObject("errorMsg").getString("message");
				
				
				JSONObject data = resObj.getJSONObject("data");
				String requestHeader = data.getString("requestHeader");
				
				JSONObject resultobj = data.getJSONObject("result");
				String offerId = resultobj.getString("offerId");
				
				Map<String,Object> loutuomsg = new HashMap<>();
				loutuomsg.put("offerId", offerId);
				
				QuoteRecord qr = quoteRecordDao.getQuoteRecordBySpecify("requestHeader",requestHeader);
				
				if(qr==null){
					System.out.println("requestHeader:"+requestHeader);
					SocketHelper.sendMessage(openId,RESCODE.PUSHBACK_BAOJIA_FAIL.getJSONObject("requestHeader:"+requestHeader).toString());
					return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(loutuomsg);
				}
				openId = qr.getOpenId();
				
				//这里可能出现：商业险重复投保
				if(resultobj.has("errorMsg")){
					String string2 = resultobj.getString("errorMsg");
					if(string2.startsWith("{")){
						JSONObject errorMsg = new JSONObject(string2);
						if(!errorMsg.getString("code").equals("success")){
							System.out.println("报价推送重复投保："+resObj);
							String string = errorMsg.getString("message");
							SocketHelper.sendMessage(openId,RESCODE.PUSHBACK_BAOJIA_FAIL.getJSONObject(string).toString());
							return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(loutuomsg);
						}
					}
				}
				
				
				System.out.println("报价推送的offerId:"+offerId);
				String offerDetail = resultobj.getString("offerDetail");
				int state = resultobj.getInt("state");
				
				
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
				//加上商业险 强制险 价格
				obj.put("totalPrice", resultobj.getInt("ciBasePrice")+resultobj.getInt("currentPrice"));
				
				
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
				return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(loutuomsg);
				
			}else{
				System.out.println("报价失败推送"+openId+"---"+message);
				SocketHelper.sendMessage(openId,RESCODE.PUSHBACK_BAOJIA_FAIL.getJSONObject(message).toString());
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
		System.out.println(quoteRecord.getClientId());
		if(quoteRecord !=null){
			if(quoteRecord.getClientId()==null || quoteRecord.getClientId()==0){
				return RESCODE.USER_NOT_EXIST.getJSONRES("clientId 为空");
			}
		}
		
	    int from = (page-1)*number;
	    List<QuoteRecord> listPage = quoteRecordDao.listQuoteRecord(quoteRecord,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = quoteRecordDao.getQuoteRecordCount(quoteRecord);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public List<QuoteRecord> listExcelQuoteRecord(){
		List<QuoteRecord> listExcelQuoteRecord = quoteRecordDao.listExcelQuoteRecord();
		return listExcelQuoteRecord;
	}
	
	/**
	 * 查询最新一条数据
	 * @param quoteRecord
	 * @param page
	 * @param number
	 * @return
	 */
	public QuoteRecord findLatestQuoteRecord(String ownerName,String licenseNumber){
		QuoteRecord latestQuoteRecordByNameLice = quoteRecordDao.getLatestQuoteRecordByNameLice(ownerName,licenseNumber);
		return latestQuoteRecordByNameLice;
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