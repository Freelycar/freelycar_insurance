package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.InsuranceOrder;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.SqlHelper;
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
	
	//根据id查询Order
    public InsuranceOrder getOrderById(int id){
        String hql = "from Order where id = :id";
        InsuranceOrder result = (InsuranceOrder) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的Order	
	@SuppressWarnings("unchecked")
	public List<InsuranceOrder> listOrder(InsuranceOrder order,int from, int num){
		QueryUtils utils = new QueryUtils(getSession(), "from InsuranceOrder");
		
		if(order != null){
			utils = utils.addString("state", order.getState());
		}
		
		return utils.setFirstResult(from)
			 .setMaxResults(num)
			 .getQuery().list();
	}
	
	
	/**
	 * 查询Order的总数
	 * @return
	 */
	public long getOrderCount(InsuranceOrder order){
		QueryUtils utils = new QueryUtils(getSession(), "select count(*) from InsuranceOrder");
		if(order != null){
			utils = utils.addString("state", order.getState());
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
	    String hql = SqlHelper.genUpdateSql(order, InsuranceOrder.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(order, InsuranceOrder.class, query);
		
		query.executeUpdate();
	    
	}
    
}