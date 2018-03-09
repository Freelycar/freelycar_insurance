package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.InsuranceList;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class InsuranceListDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个InsuranceList
    public void saveInsuranceList(InsuranceList insurancelist){
		getSession().save(insurancelist);
	}
	
	//根据id查询InsuranceList
    public InsuranceList getInsuranceListById(int id){
        String hql = "from InsuranceList where id = :id";
        InsuranceList result = (InsuranceList) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的InsuranceList	
	@SuppressWarnings("unchecked")
	public List<InsuranceList> listInsuranceList(InsuranceList insuranceList,int from, int num){
		String hql = SqlHelper.generatorSql(insuranceList, InsuranceList.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(insuranceList, InsuranceList.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询InsuranceList的总数
	 * @return
	 */
	public long getInsuranceListCount(InsuranceList insuranceList){
	    String hql = SqlHelper.generatorSql(insuranceList, InsuranceList.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除InsuranceList
	public boolean removeInsuranceListById(String id){
		String hql = "delete from InsuranceList where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新InsuranceList
	public void updateInsuranceList(InsuranceList insurancelist){
	    String hql = SqlHelper.genUpdateSql(insurancelist, InsuranceList.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(insurancelist, InsuranceList.class, query);
		
		query.executeUpdate();
	    
	}
    
}