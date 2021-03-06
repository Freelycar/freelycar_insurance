package com.freelycar.dao;

import java.util.List;

import com.freelycar.util.Tools;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freelycar.entity.QuoteRecord;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.SqlHelper;

/**
 * 操作数据库的dao层
 */
@Repository
public class QuoteRecordDao {
    /********** sessionFactory ***********/
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    //增加一个QuoteRecord
    public void saveQuoteRecord(QuoteRecord quoterecord) {
        getSession().save(quoterecord);
    }

    //根据id查询QuoteRecord
    public QuoteRecord getQuoteRecordById(int id) {
        String hql = "from QuoteRecord where id = :id";
        QuoteRecord result = (QuoteRecord) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }

    //查询最新的查询QuoteRecord
    public QuoteRecord getLatestQuoteRecordByNameLice(String ownerName, String licenseNumber) {
        String hql = "from QuoteRecord where ownerName = :ownerName and licenseNumber = :licenseNumber ORDER BY createTime,id desc LIMIT 1";
        QuoteRecord result = (QuoteRecord) getSession().createQuery(hql)
                .setString("ownerName", ownerName)
                .setString("licenseNumber", licenseNumber)
                .uniqueResult();
        return result;
    }

    //指定cloumn
    public QuoteRecord getQuoteRecordBySpecify(String specify, String value) {
        String hql = "from QuoteRecord where " + specify + " = :" + specify;
        QuoteRecord result = (QuoteRecord) getSession().createQuery(hql).setString(specify, value).uniqueResult();
        return result;
    }


    //指定cloumn
    public List<QuoteRecord> getQuoteRecordBylicenseNumber(String licenseNumber) {
        String hql = "from QuoteRecord where licenseNumber = :licenseNumber order by createTime desc";
        @SuppressWarnings("unchecked")
        List<QuoteRecord> list = getSession().createQuery(hql).setString("licenseNumber", licenseNumber).setMaxResults(3).list();
        return list;
    }

    public QuoteRecord getQuoteRecordBylicenseNumberAndOfferId(String licenseNumber, String offerId) {
        String hql = "from QuoteRecord where licenseNumber = :licenseNumber and offerId =:offerId";
        QuoteRecord uniqueResult = (QuoteRecord) getSession().createQuery(hql)
                .setString("licenseNumber", licenseNumber)
                .setString("offerId", offerId)
                .uniqueResult();
        return uniqueResult;
    }

    //查询所有的QuoteRecord
    @SuppressWarnings("unchecked")
    public List<QuoteRecord> listQuoteRecord(QuoteRecord quoteRecord, int from, int num) {
        QueryUtils utils = new QueryUtils(getSession(), "from QuoteRecord");
        if (quoteRecord != null) {
            String openId = quoteRecord.getOpenId();
            Integer clientId = quoteRecord.getClientId();
            if (Tools.notEmpty(openId)) {
                utils = utils.addStringLike("openId", openId);
            }
            if (null != clientId) {
                utils = utils.addInteger("clientId", clientId);
            }
        }
        utils.addOrderByDesc("createTime");
        return utils.setFirstResult(from)
                .setMaxResults(num)
                .getQuery().list();
    }

    //查看每辆车最新的报价记录
    @SuppressWarnings("unchecked")
    public List<QuoteRecord> listExcelQuoteRecord() {
        String sql = "SELECT * FROM QuoteRecord WHERE id IN ( SELECT MAX(id) FROM QuoteRecord GROUP BY licenseNumber)";
        List<QuoteRecord> list = getSession().createSQLQuery(sql).list();
        return list;
    }

    /**
     * 查询QuoteRecord的总数
     *
     * @return
     */
    public long getQuoteRecordCount(QuoteRecord quoteRecord) {
        QueryUtils utils = new QueryUtils(getSession(), "select count(*) from QuoteRecord");
        if (quoteRecord != null) {
            String openId = quoteRecord.getOpenId();
            Integer clientId = quoteRecord.getClientId();
            if (Tools.notEmpty(openId)) {
                utils = utils.addStringLike("openId", openId);
            }
            if (null != clientId) {
                utils = utils.addInteger("clientId", clientId);
            }
        }
        return (long) utils.getQuery().uniqueResult();
    }


    //根据id删除QuoteRecord
    public boolean removeQuoteRecordById(String id) {
        String hql = "delete from QuoteRecord where id = :id";
        int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
        return executeUpdate > 0;
    }

    //更新QuoteRecord
    public void updateQuoteRecord(QuoteRecord quoterecord) {
        String hql = SqlHelper.genUpdateSql(quoterecord, QuoteRecord.class);

        Query query = getSession().createQuery(hql);
        query = SqlHelper.getQuery(quoterecord, QuoteRecord.class, query);

        query.executeUpdate();

    }

    public void updateQuoteRecordBySpecify(QuoteRecord quoterecord, String specify) {
        String hql = SqlHelper.genUpdateSqlBySpecifyId(quoterecord, specify, QuoteRecord.class);

        Query query = getSession().createQuery(hql);
        query = SqlHelper.getQuery(quoterecord, QuoteRecord.class, query);

        query.executeUpdate();
    }

    public void update(QuoteRecord quoterecord) {
        getSession().update(quoterecord);
    }
}