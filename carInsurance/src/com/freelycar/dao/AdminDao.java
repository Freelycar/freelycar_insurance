package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Admin;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class AdminDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个Admin
    public void saveAdmin(Admin admin){
		getSession().save(admin);
	}
	
	//根据id查询Admin
    public Admin getAdminById(int id){
        String hql = "from Admin where id = :id";
        Admin result = (Admin) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的Admin	
	@SuppressWarnings("unchecked")
	public List<Admin> listAdmin(Admin admin,int from, int num){
		String hql = SqlHelper.generatorSql(admin, Admin.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(admin, Admin.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询Admin的总数
	 * @return
	 */
	public long getAdminCount(Admin admin){
	    String hql = SqlHelper.generatorSql(admin, Admin.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除Admin
	public boolean removeAdminById(String id){
		String hql = "delete from Admin where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Admin
	public void updateAdmin(Admin admin){
	    String hql = SqlHelper.genUpdateSql(admin, Admin.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(admin, Admin.class, query);
		
		query.executeUpdate();
	    
	}
    
}