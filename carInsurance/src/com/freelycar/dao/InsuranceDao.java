package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Insurance;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class InsuranceDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Insurance
    public void saveInsurance(Insurance insurance){
		getSession().save(insurance);
	}
	
    //增加一个Insurance
    public void saveUpdateInsurance(Insurance insurance){
    	getSession().saveOrUpdate(insurance);
    }
	//根据id查询Insurance
    public Insurance getInsuranceById(int id){
        String hql = "from Insurance where id = :id";
        Insurance result = (Insurance) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
    //根据姓名 车牌 和商业险还是交强险来定
    public Insurance getInsuranceByNameLiceAndState(String ownerName,String licenseNumber,boolean commercial){
    	String hql = "from Insurance where ownerName = :ownerName and licenseNumber = :licenseNumber and commercial= :commercial";
    	Insurance result = (Insurance) getSession().createQuery(hql)
    						.setString("ownerName", ownerName)
    						.setString("licenseNumber", licenseNumber)
    						.setBoolean("commercial", commercial)
    						.uniqueResult();
    	return result;
    }
    
    //查询最新的商业险查询续保
    public Insurance getLatestXuBaoByNameLice(String ownerName,String licenseNumber){
    	String hql = "from Insurance where ownerName = :ownerName and licenseNumber = :licenseNumber and commercial = false ORDER BY id desc";
    	Insurance result = (Insurance) getSession().createQuery(hql)
    			.setString("ownerName", ownerName)
    			.setString("licenseNumber", licenseNumber)
    			.setMaxResults(1)
    			.uniqueResult();
    	return result;
    }
    
	
	//查询所有的Insurance	
	@SuppressWarnings("unchecked")
	public List<Insurance> listInsurance(Insurance insurance,int from, int num){
		String hql = SqlHelper.generatorSql(insurance, Insurance.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(insurance, Insurance.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询Insurance的总数
	 * @return
	 */
	public long getInsuranceCount(Insurance insurance){
	    String hql = SqlHelper.generatorSql(insurance, Insurance.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除Insurance
	public boolean removeInsuranceById(String id){
		String hql = "delete from Insurance where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Insurance
	public void updateInsurance(Insurance insurance){
	    String hql = SqlHelper.genUpdateSql(insurance, Insurance.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(insurance, Insurance.class, query);
		
		query.executeUpdate();
	    
	}
    
}