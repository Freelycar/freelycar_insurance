package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.OrderpushResul;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class OrderpushResulDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个OrderpushResul
    public void saveOrderpushResul(OrderpushResul orderpushresul){
		getSession().save(orderpushresul);
	}
	
	//根据id查询OrderpushResul
    public OrderpushResul getOrderpushResulById(int id){
        String hql = "from OrderpushResul where id = :id";
        OrderpushResul result = (OrderpushResul) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
    public OrderpushResul getOrderpushResulByOrderId(String orderId){
    	String hql = "from OrderpushResul where orderId = :orderId";
    	OrderpushResul result = (OrderpushResul) getSession().createQuery(hql).setString("orderId", orderId).uniqueResult();
    	return result;
    }
    
	//查询所有的OrderpushResul	
	@SuppressWarnings("unchecked")
	public List<OrderpushResul> listOrderpushResul(OrderpushResul orderpushResul,int from, int num){
		String hql = SqlHelper.generatorSql(orderpushResul, OrderpushResul.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(orderpushResul, OrderpushResul.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询OrderpushResul的总数
	 * @return
	 */
	public long getOrderpushResulCount(OrderpushResul orderpushResul){
	    String hql = SqlHelper.generatorSql(orderpushResul, OrderpushResul.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除OrderpushResul
	public boolean removeOrderpushResulById(String id){
		String hql = "delete from OrderpushResul where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新OrderpushResul
	public void updateOrderpushResul(OrderpushResul orderpushresul){
	    String hql = SqlHelper.genUpdateSql(orderpushresul, OrderpushResul.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(orderpushresul, OrderpushResul.class, query);
		
		query.executeUpdate();
	    
	}
	
	//更新OrderpushResul
	public void updateOrderpushResulBySpecifyId(OrderpushResul orderpushresul,String specifyId){
		String hql = SqlHelper.genUpdateSqlBySpecifyId(orderpushresul,specifyId,OrderpushResul.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(orderpushresul, OrderpushResul.class, query);
		
		query.executeUpdate();
		
	}
    
}