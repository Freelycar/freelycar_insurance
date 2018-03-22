package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.CashBackRate;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.util.SqlHelper;
/**  
 *  操作数据库的dao层
 */  
@Repository
public class CashbackRecordDao  
{  
    /********** sessionFactory ***********/  
    @Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
    
    //增加一个CashbackRecord
    public void saveCashbackRecord(CashbackRecord cashbackrecord){
		getSession().save(cashbackrecord);
	}
    
    //增加一个CashBackRate
    public void saveUpdateCashbackRecord(CashbackRecord cashbackrecord){
    	getSession().saveOrUpdate(cashbackrecord);
    }
    
	
    //增加一个CashBackRate
    public void saveCashbackRate(CashBackRate cashBackRate){
    	getSession().save(cashBackRate);
    }
    
    //增加一个CashBackRate
    public void saveUpdateCashbackRate(CashBackRate cashBackRate){
    	getSession().saveOrUpdate(cashBackRate);
    }
    
	//查询所有的CashBackRate	
	@SuppressWarnings("unchecked")
	public List<CashBackRate> listCashbackRate(){
		String hql = "from CashBackRate";
		return getSession().createQuery(hql).list();
	}
	
	//删除CashBackRate	
	public void deleteCashBackRate(int id){
		String hql = "delete from CashBackRate where id = :id";
		getSession().createQuery(hql).setInteger("id", id).executeUpdate();
	}
	
	//更新CashBackRate	
	public void updateCashBackRate(CashBackRate rate){
		getSession().update(rate);
	}
	
    //根据id查询CashbackRecord
    public CashbackRecord getCashbackRecordById(int id){
        String hql = "from CashbackRecord where id = :id";
        CashbackRecord result = (CashbackRecord) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }
	
    //根据id查询CashbackRecord
    public CashbackRecord getCashbackRecordByOpenId(String openId){
    	String hql = "from CashbackRecord where openId = :openId";
    	CashbackRecord result = (CashbackRecord) getSession().createQuery(hql).setString("openId", openId).uniqueResult();
    	return result;
    }
	
	//查询所有的CashbackRecord	
	@SuppressWarnings("unchecked")
	public List<CashbackRecord> listCashbackRecord(CashbackRecord cashbackRecord,int from, int num){
		String hql = SqlHelper.generatorSql(cashbackRecord, CashbackRecord.class);
		
		Query query = getSession().createQuery(hql);
		query = SqlHelper.getQuery(cashbackRecord, CashbackRecord.class, query);
		
		if(from>=0 && num>0){
			query = query.setFirstResult(from).setMaxResults(num);
		}
		
		return query.list();
	}
	
	
	/**
	 * 查询CashbackRecord的总数
	 * @return
	 */
	public long getCashbackRecordCount(CashbackRecord cashbackRecord){
	    String hql = SqlHelper.generatorSql(cashbackRecord, CashbackRecord.class);
	    hql = "select count(*)" + hql;
	    long count = (long) getSession().createQuery(hql).uniqueResult();
        return count;
	}
	
	
	//根据id删除CashbackRecord
	public boolean removeCashbackRecordById(String id){
		String hql = "delete from CashbackRecord where id = :id";
		int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新CashbackRecord
	public void updateCashbackRecord(CashbackRecord cashbackrecord){
		String hql = "update CashbackRecord set payee = :payee ,account = :account,bankname=:bankname where clientId = :clientId";
		int executeUpdate = getSession().createQuery(hql)
										.setString("payee", cashbackrecord.getPayee())
										.setString("account", cashbackrecord.getAccount())
										.setString("bankname", cashbackrecord.getBankname())
										.setInteger("clientId", cashbackrecord.getClientId())
										.executeUpdate();
	    
	}
    
}