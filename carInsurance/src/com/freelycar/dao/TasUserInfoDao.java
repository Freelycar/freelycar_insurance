package com.freelycar.dao;

import com.freelycar.entity.TasUserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公众号用户信息表DAO层
 *
 * @author 唐炜
 */
@Repository
public class TasUserInfoDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void saveOrUpdate(TasUserInfo tasUserInfo) {
        this.getSession().saveOrUpdate(tasUserInfo);
    }

    /**
     * 通过unionId查询TasUserInfo对象信息
     *
     * @param unionId 微信开放平台统一编号
     * @return list
     */
    public TasUserInfo getTasUserInfoByUnionId(String unionId) {
        String hql = "from TasUserInfo where unionId = :unionId";
        return (TasUserInfo) this.getSession().createQuery(hql).setString("unionId", unionId).uniqueResult();
    }

    /**
     * 查询所有数据
     *
     * @return list
     */
    public List<TasUserInfo> queryAll() {
        String hql = "from TasUserInfo order by id asc";
        return this.getSession().createQuery(hql).setEntity("TasUserInfo", TasUserInfo.class).list();
    }

    /**
     * 查询所有openId不为空的数据的openId集合
     *
     * @return list
     */
    public List<String> queryAllOpenIds() {
        String hql = "select openId from TasUserInfo where openId is not null";
        return this.getSession().createQuery(hql).list();
    }

    /**
     * 查询没有openId记录的unionId集合
     * @return  list
     */
    public List<String> queryUnionIdWithoutOpenId() {
        String hql = "select unionId from TasUserInfo where openId is null";
        return this.getSession().createQuery(hql).list();
    }


}
