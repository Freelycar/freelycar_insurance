package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.InvitionDao;
import com.freelycar.entity.Invition;
import com.freelycar.util.RESCODE;  
/**  
 *  
 */  
@Transactional
@Service
public class InvitionService  
{  
    /********** 注入InvitionDao ***********/  
    @Autowired
	private InvitionDao invitionDao;
    
    
    
    
    
    //增加一个Invition
    public Map<String,Object> saveInvition(Invition invition){
		
		invitionDao.saveInvition(invition);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listInvition(Invition invition, int page,int number){
	    int from = (page-1)*number;
	    List<Invition> listPage = invitionDao.listInvition(invition,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = invitionDao.getInvitionCount(invition);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除Invition
	public Map<String,Object> removeInvitionById(String id,String... ids){
		boolean res =  invitionDao.removeInvitionById(id,ids);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新Invition
	public Map<String,Object> updateInvition(Invition invition){
	    invitionDao.updateInvition(invition);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}