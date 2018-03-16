package com.freelycar.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.Invition;
import com.freelycar.util.QueryUtils;
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
		QueryUtils utils = new QueryUtils(getSession(), "from Invition");
		
		if(invition != null){
			utils = utils.addStringLike("name", invition.getName())
			 .addStringLike("invcode", invition.getInvcode());
		}
		
		return utils.setFirstResult(from)
			 .setMaxResults(num)
			 .getQuery().list();
	}
	
	
	/**
	 * 查询Invition的总数
	 * @return
	 */
	public long getInvitionCount(Invition invition){
        QueryUtils utils = new QueryUtils(getSession(), "select count(*) from Invition");
		if(invition != null){
			utils = utils.addStringLike("name", invition.getName())
			 .addStringLike("invcode", invition.getInvcode());
		}
		return (long) utils.getQuery().uniqueResult();
        
	}
	
	
	//根据id删除Invition
	public boolean removeInvitionById(List<Integer> ids){
		String hql = "delete from Invition where id in (:ids)";
		int executeUpdate = getSession().createQuery(hql).setParameterList("ids", ids).executeUpdate();
		return executeUpdate>0; 
	}
	
	//更新Invition
	public void updateInvition(Invition invition){
	    Invition invitionById = getInvitionById(invition.getId());
	    invitionById.setInvcode(invition.getInvcode());
	    invitionById.setName(invition.getName());
	    invitionById.setRemark(invition.getRemark());
	    getSession().update(invitionById);
	    
	}
    
}