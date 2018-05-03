package com.freelycar.dao; 

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.Invition;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.Tools;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class OrderDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Order
    public void saveOrder(InsuranceOrder order){
		getSession().save(order);
	}
    
    //增加一个Order
    public void saveUpdateOrder(InsuranceOrder order){
    	getSession().saveOrUpdate(order);
    }
    
    public String saveUpdateOrder(InsuranceOrder order,boolean saveOrupdate){
    	if(saveOrupdate){
    		return getSession().save(order).toString();
    	}
    	getSession().update(order);
    	return null;
    }
	
	//根据id查询Order
    public InsuranceOrder getOrderById(int id){
        String hql = "from InsuranceOrder where id = :id";
        InsuranceOrder result = (InsuranceOrder) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
    
    //根据id查询Order
    public List<InsuranceOrder> getOrderByLicenseNumber(String licenseNumber,int page,int number){
    	String hql = "from InsuranceOrder where licenseNumber = :licenseNumber";
    	@SuppressWarnings("unchecked")
		List<InsuranceOrder> result = getSession().createQuery(hql).setString("licenseNumber", licenseNumber)
		.setFirstResult(page)
		.setMaxResults(number)
		.list();
    	return result;
    }
    
    
    //查询最新的查询Order
    public QuoteRecord getLatestQuoteByNameLice(String ownerName,String licenseNumber){
    	String hql = "from QuoteRecord where ownerName = :ownerName and licenseNumber = :licenseNumber ORDER BY createTime,id desc";
    	QuoteRecord result = (QuoteRecord) getSession().createQuery(hql)
    			.setString("ownerName", ownerName)
    			.setString("licenseNumber", licenseNumber)
    			.setMaxResults(1)
    			.uniqueResult();
    	return result;
    }
    
    //查询最新的查询Order
    public InsuranceOrder getLatestOrderByNameLice(String ownerName,String licenseNumber){
    	String hql = "from InsuranceOrder where insureName = :ownerName and licenseNumber = :licenseNumber ORDER BY createTime,id desc LIMIT 1";
    	InsuranceOrder result = (InsuranceOrder) getSession().createQuery(hql)
    			.setString("ownerName", ownerName)
    			.setString("licenseNumber", licenseNumber)
    			.uniqueResult();
    	return result;
    }
    
    
    //根据id查询Order
    public InsuranceOrder getOrderByOrderId(String orderId){
    	String hql = "from InsuranceOrder where orderId = :orderId";
    	InsuranceOrder result = (InsuranceOrder) getSession().createQuery(hql).setString("orderId", orderId).uniqueResult();
    	return result;
    }
    
   
    
    //根据id查询Order dealtime ：成交时间
    public List<Object[]> getCountBySourId(Date startTime, Date endTime){
    	String sql = "select sourceId, source, sum(totalPrice) from InsuranceOrder where dealTime > :startTime and dealTime<= :endTime group by sourceId";
    	@SuppressWarnings("unchecked")
		List<Object[]> list = getSession().createQuery(sql)
    							.setLong("startTime", startTime.getTime())
    							.setLong("endTime", endTime.getTime())
    							.list();
    	return list;
    }
	
    //根据id查询Order
    @SuppressWarnings("unchecked")
	public List<Object[]> listCount(Invition invition, int page,int number,Date startTime, Date endTime){
    	String sql = "select sourceId, source, sum(totalPrice) from InsuranceOrder where dealTime > :startTime and dealTime<= :endTime";
    	if(Tools.notEmpty(invition.getName())){
    		sql += " and source like  :invName";
    	}
    	sql += " group by sourceId";
    	
    	Query query = getSession().createQuery(sql)
    	.setLong("startTime", startTime.getTime())
    	.setLong("endTime", endTime.getTime());
    	if(Tools.notEmpty(invition.getName())){
    		query.setString("invName", "%"+invition.getName()+"%");
    	}
    	
    	return query.list();
    }
	
	//查询所有的Order	
	@SuppressWarnings("unchecked")
	public List<InsuranceOrder> listOrder(InsuranceOrder order,int from, int num){
		QueryUtils utils = new QueryUtils(getSession(), "from InsuranceOrder");
		
		if(order != null){
			utils = utils.addInteger("state", order.getState())
					.addString("openId", order.getOpenId());
		}
		
		return utils.setFirstResult(from)
			 .setMaxResults(num)
			 .getQuery().list();
	}
	
	@SuppressWarnings("unchecked")
	public List<InsuranceOrder> listValidOrder(InsuranceOrder order){
		
		String hql = "from InsuranceOrder where state = :state and effetiveTimeLong > :effetiveTimeLong";
		return getSession().createQuery(hql)
				.setInteger("state", order.getState())
				.setLong("effetiveTimeLong", order.getEffetiveTimeLong())
		.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<InsuranceOrder> listOrder(InsuranceOrder order){
		QueryUtils utils = new QueryUtils(getSession(), "from InsuranceOrder");
		
		if(order != null){
			utils = utils.addInteger("state", order.getState())
					.addString("openId", order.getOpenId());
		}
		
		return utils.getQuery().list();
	}
	
	/**
	 * 查询Order的总数
	 * @return
	 */
	public long getOrderCount(InsuranceOrder order){
		QueryUtils utils = new QueryUtils(getSession(), "select count(*) from InsuranceOrder");
		if(order != null){
			utils = utils.addInteger("state", order.getState());
		}
		return (long) utils.getQuery().uniqueResult();
	}
	
	
	//根据id删除Order
	public boolean removeOrderById(String id){
		String hql = "delete from Order where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Order
	public void updateOrder(InsuranceOrder order){
	    getSession().update(order);
	    
	}
    
}