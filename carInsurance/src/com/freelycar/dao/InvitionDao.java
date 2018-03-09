package com.freelycar.dao; 

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Invition;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class InvitionDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Invition
    public void saveInvition(Invition invition){
		getSession().save(invition);
	}
	
	//根据id查询Invition
    public Invition getInvitionById(int id){
        String hql = "from Invition where id = :id";
        Invition result = (Invition) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的Invition	
	@SuppressWarnings("unchecked")
	public List<Invition> listInvition(Invition invition,int from, int num){
		String hql = SqlHelper.generatorSql(invition, Invition.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(invition, Invition.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询Invition的总数
	 * @return
	 */
	public long getInvitionCount(Invition invition){
	    String hql = SqlHelper.generatorSql(invition, Invition.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除Invition
	public boolean removeInvitionById(String id,String... ids){
		String hql = "delete from Invition where id in :ids";
		
		List<String> idList  = new ArrayList<>();
		if(ids != null){
			for(String item : ids){
				idList.add(item);
			}
		}
		idList.add(id);
		
		int executeUpdate = getSession().createQuery(hql).setParameterList("ids", idList).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Invition
	public void updateInvition(Invition invition){
	    String hql = SqlHelper.genUpdateSql(invition, Invition.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(invition, Invition.class, query);
		
		query.executeUpdate();
	    
	}
    
}