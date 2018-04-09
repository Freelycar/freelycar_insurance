package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.InvitionDao;
import com.freelycar.entity.Invition;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;  
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
    

    //根据code查询Invition
    public Map<String, Object> getInvitionByInvcode(String invcode){
    	Invition iv = invitionDao.getInvitionByInvcode(invcode);
    	if(iv == null){
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}else{
    		return RESCODE.SUCCESS.getJSONRES();
    	}
    }
    
    //根据code查询Invition
    public Invition getInvitionByInvcode2(String invcode){
    	Invition iv = invitionDao.getInvitionByInvcode(invcode);
    	return iv;
    }
    
    //增加一个Invition
    public Map<String,Object> saveInvition(Invition invition){
    	if(Tools.isEmpty(invition.getName())){
    		return RESCODE.INV_NAME_EMPTY.getJSONRES();
    	}
    	if(Tools.isEmpty(invition.getInvcode())){
    		return RESCODE.INV_CODE_EMPTY.getJSONRES();
    	}
    	//渠道名称和马儿都不能重复
    	Invition invitionByInvcode = invitionDao.getInvitionByInvcode(invition.getInvcode());
    	if(invitionByInvcode != null){
    		return RESCODE.INV_CODE_EXIST.getJSONRES();
    	}
    	
    	Invition invitionByInvName = invitionDao.getInvitionByInvName(invition.getName());
    	if(invitionByInvName != null){
    		return RESCODE.INV_NAME_EXIST.getJSONRES();
    	}
    	
    	invition.setCreateTime(System.currentTimeMillis());
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
		return RESCODE.SUCCESS.getJSONRES(listPage);
    }
	
	
	//根据id删除Invition
	public Map<String,Object> removeInvitionById(List<Integer> ids){
		boolean res =  invitionDao.removeInvitionById(ids);
		return res ? RESCODE.SUCCESS.getJSONRES() : RESCODE.DELETE_FAIL.getJSONRES();
	}
	
	//更新Invition
	public Map<String,Object> updateInvition(Invition invition){
		if(Tools.isEmpty(invition.getInvcode())){
			return RESCODE.INV_CODE_EMPTY.getJSONRES();
		}
		
		if(Tools.isEmpty(invition.getName())){
			return RESCODE.INV_NAME_EMPTY.getJSONRES();
		}
		
		
		List<Invition> listInvitionByInvcode = invitionDao.getListInvitionByInvcode(invition.getInvcode());
		int size = listInvitionByInvcode.size();
		if(size == 1){
			if(invition.getId() != listInvitionByInvcode.get(0).getId()){
				return RESCODE.INV_CODE_EXIST.getJSONRES();
			}
		}else if(size>1){
			return RESCODE.INV_CODE_EXIST.getJSONRES();
		}
		
		List<Invition> listInvitionByName = invitionDao.getListInvitionByInvName(invition.getName());
		int namesize = listInvitionByName.size();
		if(namesize == 1){
			if(invition.getId() != listInvitionByName.get(0).getId()){
				return RESCODE.INV_NAME_EXIST.getJSONRES();
			}
		}else if(namesize>1){
			return RESCODE.INV_NAME_EXIST.getJSONRES();
		}
		
		
		
	    invitionDao.updateInvition(invition);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}