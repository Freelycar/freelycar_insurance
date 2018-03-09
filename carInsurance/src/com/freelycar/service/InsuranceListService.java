package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.InsuranceListDao;
import com.freelycar.entity.InsuranceList;
import com.freelycar.util.RESCODE;  
/**  
 *  
 */  
@Transactional
@Service
public class InsuranceListService  
{  
    /********** 注入InsuranceListDao ***********/  
    @Autowired
	private InsuranceListDao insuranceListDao;
    
    
    
    
    
    //增加一个InsuranceList
    public Map<String,Object> saveInsuranceList(InsuranceList insuranceList){
		
		insuranceListDao.saveInsuranceList(insuranceList);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listInsuranceList(InsuranceList insuranceList, int page,int number){
	    int from = (page-1)*number;
	    List<InsuranceList> listPage = insuranceListDao.listInsuranceList(insuranceList,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = insuranceListDao.getInsuranceListCount(insuranceList);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除InsuranceList
	public Map<String,Object> removeInsuranceListById(String id){
		boolean res =  insuranceListDao.removeInsuranceListById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新InsuranceList
	public Map<String,Object> updateInsuranceList(InsuranceList insuranceList){
	    insuranceListDao.updateInsuranceList(insuranceList);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}