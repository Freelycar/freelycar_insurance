package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.QueryUtils;
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
	
    //指定cloumn
    public QuoteRecord getQuoteRecordBySpecify(String specify,String value){
    	String hql = "from QuoteRecord where "+specify+" = :"+specify;
    	QuoteRecord result = (QuoteRecord) getSession().createQuery(hql).setString(specify, value).uniqueResult();
    	return result;
    }
	
	//查询所有的QuoteRecord	
	@SuppressWarnings("unchecked")
	public List<QuoteRecord> listQuoteRecord(QuoteRecord quoteRecord,int from, int num){
		QueryUtils utils = new QueryUtils(getSession(), "from QuoteRecord");
		if(quoteRecord != null){
			utils = utils.addStringLike("openId", quoteRecord.getOpenId());
		}
		return utils.setFirstResult(from)
			 .setMaxResults(num)
			 .getQuery().list();
	}
	
	
	/**
	 * 查询QuoteRecord的总数
	 * @return
	 */
	public long getQuoteRecordCount(QuoteRecord quoteRecord){
		QueryUtils utils = new QueryUtils(getSession(), "select count(*) from QuoteRecord");
		if(quoteRecord != null){
			utils = utils.addString("openId", quoteRecord.getOpenId());
		}
		return (long) utils.getQuery().uniqueResult();
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
	
	public void update(QuoteRecord quoterecord){
		getSession().update(quoterecord);
	}
}