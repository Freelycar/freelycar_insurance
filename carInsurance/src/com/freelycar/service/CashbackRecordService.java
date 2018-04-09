package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.entity.CashBackRate;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;  
/**  
 *  
 */  
@Transactional
@Service
public class CashbackRecordService  
{  
    /********** 注入CashbackRecordDao ***********/  
    @Autowired
	private CashbackRecordDao cashbackRecordDao;
    
    
    
    
    
    //增加一个CashbackRecord
    public Map<String,Object> saveCashbackRecord(CashbackRecord cashbackRecord){
		if(Tools.isEmpty(cashbackRecord.getPayee())){
			return RESCODE.CASH_PAYEE_EMPTY.getJSONRES();
		}
		if(Tools.isEmpty(cashbackRecord.getAccount())){
			return RESCODE.CASH_ACCOUNT_EMPTY.getJSONRES();
		}
		if(Tools.isEmpty(cashbackRecord.getBankname())){
			return RESCODE.CASH_BANKNAME_EMPTY.getJSONRES();
		}
    	
		cashbackRecordDao.saveCashbackRecord(cashbackRecord);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listCashbackRecord(CashbackRecord cashbackRecord, int page,int number){
	    int from = (page-1)*number;
	    List<CashbackRecord> listPage = cashbackRecordDao.listCashbackRecord(cashbackRecord,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = cashbackRecordDao.getCashbackRecordCount(cashbackRecord);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除CashbackRecord
	public Map<String,Object> removeCashbackRecordById(String id){
		boolean res =  cashbackRecordDao.removeCashbackRecordById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新CashbackRecord
	public Map<String,Object> updateCashbackRecord(CashbackRecord cashbackRecord){
	    cashbackRecordDao.updateCashbackRecord(cashbackRecord);
	    return RESCODE.SUCCESS.getJSONRES();
	}
	
	  //增加一个CashBackRate
    public Map<String, Object> saveCashbackRecord(CashBackRate cashBackRate){
    	//只能有一条记录
    	List<CashBackRate> listCashbackRate = cashbackRecordDao.listCashbackRate();
    	if(listCashbackRate.isEmpty()){
    		cashbackRecordDao.saveCashbackRate(cashBackRate);
    		return RESCODE.SUCCESS.getJSONRES();
    	}
    	return RESCODE.DATA_EXIST.getJSONRES();
    }
    
	//查询所有的CashBackRate	
	public Map<String, Object> listCashbackRate(){
		List<CashBackRate> listCashbackRate = cashbackRecordDao.listCashbackRate();
		return RESCODE.SUCCESS.getJSONRES(listCashbackRate);
	}
	
	//查询当前返现率
	public Map<String, Object> getCurentCashBackRate(){
		List<CashBackRate> listCashbackRate = cashbackRecordDao.listCashbackRate();
		if(listCashbackRate.isEmpty()){
			return RESCODE.NOT_FOUND.getJSONRES();
		}
		return RESCODE.SUCCESS.getJSONRES(listCashbackRate.get(0));
	}
	
	//删除CashBackRate	
	public Map<String, Object> deleteCashBackRate(int id){
		cashbackRecordDao.deleteCashBackRate(id);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	//更新CashBackRate	
	public Map<String, Object> updateCashBackRate(CashBackRate rate){
		cashbackRecordDao.updateCashBackRate(rate);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	//更新CashBackRate	
	public Map<String, Object> saveupdateCashBackRate(CashBackRate rate){
		List<CashBackRate> listCashbackRate = cashbackRecordDao.listCashbackRate();
		if(listCashbackRate.isEmpty()){
			cashbackRecordDao.saveupdateCashBackRate(rate);
		}else{
			CashBackRate cashBackRate = listCashbackRate.get(0);
			cashBackRate.setRate(rate.getRate());
			cashbackRecordDao.saveupdateCashBackRate(cashBackRate);
		}
		return RESCODE.SUCCESS.getJSONRES();
	}
    
}