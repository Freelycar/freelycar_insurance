package com.freelycar.service; 

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.ClientDao;
import com.freelycar.dao.InsuranceDao;
import com.freelycar.dao.OrderDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.Client;
import com.freelycar.entity.Insurance;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;  
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
    
    @Autowired
    private QuoteRecordDao qrdao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private InsuranceDao insuranceDao;
    
    
    //增加一个Client
    public Map<String,Object> saveClient(Client client){
		clientDao.saveClient(client);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
    public Map<String,Object> getClientDetail(int id){
    	Client clientById = clientDao.getClientById(id);
    	if(clientById == null){
    		return RESCODE.NOT_FOUND.getJSONRES();
    	}
    	
    	QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
		if(quoteRecord != null){
			clientById.setLeastQuoteTime(quoteRecord.getCreateTime());
		}
		
		Insurance insu = insuranceDao.getLatestXuBaoByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
		if(insu != null){
			if(Tools.notEmpty(insu.getInsuranceEndTime())){
				//查出来的是秒 所以加了三个零
				clientById.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime()+"000"));
			}
		}
    	
		InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
		if(quoteRecord != null){
			clientById.setLeastQuoteTime(quoteRecord.getCreateTime());
		}
		if(insuranceOrder != null){
			clientById.setLeastOrderTime(insuranceOrder.getCreateTime());
		}
		
    	return RESCODE.SUCCESS.getJSONRES(clientById);
    }
    
    public Client getClientDetail(String openId,String licenseNumber, String ownerName){
    	Client clientById = clientDao.getClient(openId, licenseNumber, ownerName);
    	return clientById;
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
	    	//查询最新的订单时间
	    	for(Client c:listPage){
	    		QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(c.getOwnerName(), c.getLicenseNumber());
	    		if(quoteRecord != null){
	    			c.setLeastQuoteTime(quoteRecord.getCreateTime());
	    		}
	    		//System.out.println("quotetime:" + c.getLeastQuoteTime());
	    		
	    		Insurance insu = insuranceDao.getLatestXuBaoByNameLice(c.getOwnerName(), c.getLicenseNumber());
	    		if(insu != null){
	    			if(Tools.notEmpty(insu.getInsuranceEndTime())){
	    				//查出来的是秒 所以加了三个零
	    				c.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime()+"000"));
	    			}
	    		}
	    		
	    		InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(c.getOwnerName(), c.getLicenseNumber());
	    		
	    		if(insuranceOrder != null){
	    			c.setLeastOrderTime(insuranceOrder.getCreateTime());
	    		}
	    	}
	    	if(client.isToubao())
	    	{
	    		Collections.sort(listPage, new Comparator<Client>() {  
		            @Override  
		            public int compare(Client s1, Client s2) {  
		            	//System.out.println(s1.getLeastOrderTime());
		            	//System.out.println(s2.getLeastOrderTime());
		            	return (int) (s1.getLeastOrderTime() - s2.getLeastOrderTime());
		            }  
		        }); 
	    	}
	    	else
	    	{
		    	Collections.sort(listPage, new Comparator<Client>() {  
		            @Override  
		            public int compare(Client s1, Client s2) {  
		            	/*System.out.println(s1.getLeastQuoteTime());
		            	System.out.println(s2.getLeastQuoteTime());*/
		            	return (int) (s1.getLeastQueryTime() - s2.getLeastQueryTime());
		            }  
		        });  
	    	}
	  
	    	long count = clientDao.getClientCount(client);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.SUCCESS.getJSONRES(listPage);
    }
	
	public List<Client> listAllClient(){
		List<Client> allClient = clientDao.listAllClient();
		return allClient;
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