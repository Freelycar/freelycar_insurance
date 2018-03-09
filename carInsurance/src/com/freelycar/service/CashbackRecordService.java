package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.util.RESCODE;  
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
    
}