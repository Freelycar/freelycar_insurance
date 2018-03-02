package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class QuoteRecordDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个QuoteRecord
    public void saveQuoteRecord(QuoteRecord quoterecord){
		getSession().save(quoterecord);
	}
	
	//根据id查询QuoteRecord
    public QuoteRecord getQuoteRecordById(int id){
        String hql = "from QuoteRecord where id = :id";
        QuoteRecord result = (QuoteRecord) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
	
	//查询所有的QuoteRecord	
	@SuppressWarnings("unchecked")
	public List<QuoteRecord> listQuoteRecord(QuoteRecord quoteRecord,int from, int num){
		String hql = SqlHelper.generatorSql(quoteRecord, QuoteRecord.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(quoteRecord, QuoteRecord.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询QuoteRecord的总数
	 * @return
	 */
	public long getQuoteRecordCount(QuoteRecord quoteRecord){
	    String hql = SqlHelper.generatorSql(quoteRecord, QuoteRecord.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除QuoteRecord
	public boolean removeQuoteRecordById(String id){
		String hql = "delete from QuoteRecord where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新QuoteRecord
	public void updateQuoteRecord(QuoteRecord quoterecord){
	    String hql = SqlHelper.genUpdateSql(quoterecord, QuoteRecord.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(quoterecord, QuoteRecord.class, query);
		
		query.executeUpdate();
	    
	}
    
	public void updateQuoteRecordBySpecify(QuoteRecord quoterecord,String specify){
		String hql = SqlHelper.genUpdateSqlBySpecifyId(quoterecord,specify, QuoteRecord.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(quoterecord, QuoteRecord.class, query);
		
		query.executeUpdate();
		
	}
}