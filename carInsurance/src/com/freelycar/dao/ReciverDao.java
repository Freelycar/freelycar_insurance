package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Reciver;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class ReciverDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Reciver
    public void saveReciver(Reciver reciver){
		getSession().save(reciver);
	}
	
	//根据id查询Reciver
    public Reciver getReciverById(int id){
        String hql = "from Reciver where id = :id";
        Reciver result = (Reciver) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的Reciver	
	@SuppressWarnings("unchecked")
	public List<Reciver> listReciver(Reciver reciver,int from, int num){
		String hql = SqlHelper.generatorSql(reciver, Reciver.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(reciver, Reciver.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询Reciver的总数
	 * @return
	 */
	public long getReciverCount(Reciver reciver){
	    String hql = SqlHelper.generatorSql(reciver, Reciver.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除Reciver
	public boolean removeReciverById(String id){
		String hql = "delete from Reciver where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Reciver
	public void updateReciver(Reciver reciver){
	    String hql = SqlHelper.genUpdateSql(reciver, Reciver.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(reciver, Reciver.class, query);
		
		query.executeUpdate();
	    
	}
    
}