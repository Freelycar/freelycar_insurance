package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.ClientDao;
import com.freelycar.entity.Client;
import com.freelycar.util.RESCODE;  
/**  
 *  
 */  
@Transactional
@Service
public class ClientService  
{  
    /********** 注入ClientDao ***********/  
    @Autowired
	private ClientDao clientDao;
    
    
    
    
    
    //增加一个Client
    public Map<String,Object> saveClient(Client client){
		clientDao.saveClient(client);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
    public Map<String,Object> getClientDetail(int id){
    	Client clientById = clientDao.getClientById(id);
    	return RESCODE.SUCCESS.getJSONRES(clientById);
    }
    
    public Map<String,Object> isExistByOpenId(String openId){
    	Client client = clientDao.getClientByOpenId(openId);
    	if(client == null){
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}
    	return RESCODE.SUCCESS.getJSONRES(client);
    }
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listClient(Client client, int page,int number){
	    int from = (page-1)*number;
	    List<Client> listPage = clientDao.listClient(client,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = clientDao.getClientCount(client);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除Client
	public Map<String,Object> removeClientById(String id){
		boolean res =  clientDao.removeClientById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新Client
	public Map<String,Object> updateClient(Client client){
	    clientDao.updateClient(client);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}