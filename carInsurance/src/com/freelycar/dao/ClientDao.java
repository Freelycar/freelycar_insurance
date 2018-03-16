package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Client;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class ClientDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Client
    public void saveClient(Client client){
		getSession().save(client);
	}
	
	//根据id查询Client
    public Client getClientById(int id){
        String hql = "from Client where id = :id";
        Client result = (Client) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的Client	
	@SuppressWarnings("unchecked")
	public List<Client> listClient(Client client,int from, int num){
		QueryUtils utils = new QueryUtils(getSession(), "from Client");
		
		if(client != null){
			utils = utils.addStringLike("idCard", client.getIdCard())
			 .addStringLike("source", client.getSource())
			 .addString("licenseNumber", client.getLicenseNumber())
			 .addString("nickName", client.getNickName())
			 .addString("quoteState", client.getQuoteState())
			 .addBoolean("transfer", client.getTransfer())
			 .addBoolean("toubao", client.isToubao())
			 .addBoolean("cashback", client.isCashback())
			 .addString("phone", client.getPhone());
		}
		
		return utils.setFirstResult(from)
			 .setMaxResults(num)
			 .getQuery().list();
	}
	
	
	/**
	 * 查询Client的总数
	 * @return
	 */
	public long getClientCount(Client client){
		QueryUtils utils = new QueryUtils(getSession(), "select count(*) from Client");
		
		if(client != null){
			utils = utils.addStringLike("idCard", client.getIdCard())
			 .addString("licenseNumber", client.getLicenseNumber())
			 .addString("nickName", client.getNickName())
			 .addBoolean("transfer", client.getTransfer())
			 .addBoolean("toubao", client.isToubao())
			 .addString("phone", client.getPhone());
		}
		return (long) utils.getQuery().uniqueResult();
	}
	
	
	//根据id删除Client
	public boolean removeClientById(String id){
		String hql = "delete from Client where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Client
	public void updateClient(Client client){
	    String hql = SqlHelper.genUpdateSql(client, Client.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(client, Client.class, query);
		
		query.executeUpdate();
	    
	}
    
}