package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.InvoiceInfo;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class InvoiceInfoDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个InvoiceInfo
    public void saveInvoiceInfo(InvoiceInfo invoiceinfo){
		getSession().save(invoiceinfo);
	}
	
	//根据id查询InvoiceInfo
    public InvoiceInfo getInvoiceInfoById(int id){
        String hql = "from InvoiceInfo where id = :id";
        InvoiceInfo result = (InvoiceInfo) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的InvoiceInfo	
	@SuppressWarnings("unchecked")
	public List<InvoiceInfo> listInvoiceInfo(InvoiceInfo invoiceInfo,int from, int num){
		String hql = SqlHelper.generatorSql(invoiceInfo, InvoiceInfo.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(invoiceInfo, InvoiceInfo.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询InvoiceInfo的总数
	 * @return
	 */
	public long getInvoiceInfoCount(InvoiceInfo invoiceInfo){
	    String hql = SqlHelper.generatorSql(invoiceInfo, InvoiceInfo.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除InvoiceInfo
	public boolean removeInvoiceInfoById(String id){
		String hql = "delete from InvoiceInfo where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新InvoiceInfo
	public void updateInvoiceInfo(InvoiceInfo invoiceinfo){
	    String hql = SqlHelper.genUpdateSql(invoiceinfo, InvoiceInfo.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(invoiceinfo, InvoiceInfo.class, query);
		
		query.executeUpdate();
	    
	}
    
}