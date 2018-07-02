package com.freelycar.dao;

import com.freelycar.entity.Client;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.SqlHelper;
import com.freelycar.util.Tools;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 操作数据库的dao层
 */
@Repository
public class ClientDao {
    private final int delayDays = 90;
    /********** sessionFactory ***********/
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    //增加一个Client
    public void saveClient(Client client) {
        getSession().saveOrUpdate(client);
    }

    //根据id查询Client
    public Client getClientById(int id) {
        String hql = "from Client where id = :id";
        Client result = (Client) getSession().createQuery(hql).setInteger("id", id).uniqueResult();
        return result;
    }

    public Client getClient(String openId, String licenseNumber, String ownerName) {
        String hql = "from Client where openId = :openId and licenseNumber = :licenseNumber and ownerName = :ownerName";
        Client result = (Client) getSession().createQuery(hql)
                .setString("openId", openId)
                .setString("licenseNumber", licenseNumber)
                .setString("ownerName", ownerName)
                .uniqueResult();
        return result;
    }

    //根据phone查询Client
    public Client getClientByPhone(String phone) {
        String hql = "from Client where phone = :phone";
        Client result = (Client) getSession().createQuery(hql).setString("phone", phone).uniqueResult();
        return result;
    }

    //根据openId查询Client
    public Client getClientByOpenId(String openId) {
        String hql = "from Client where openId = :openId";
        Client result = (Client) getSession().createQuery(hql).setString("openId", openId).uniqueResult();
        return result;
    }

    //根据判断openId 存不存在
    @SuppressWarnings("unchecked")
    public List<Client> getClientByOpenIdList(String openId) {
        String hql = "from Client where openId = :openId";
        return getSession().createQuery(hql).setString("openId", openId).list();
    }

    //根据openId查询Client
    public Client getClientByOpenIdAndLicenseNumber(String openId, String licenseNumber) {
        String hql = "from Client where openId = :openId and licenseNumber = :licenseNumber";
        Client result = (Client) getSession().createQuery(hql)
                .setString("openId", openId)
                .setString("licenseNumber", licenseNumber)
                .uniqueResult();
        return result;
    }

    //查询所有的Client
    @SuppressWarnings("unchecked")
    public List<Client> listClient(Client client, int from, int num) {
        QueryUtils utils = new QueryUtils(getSession(), "from Client");

        if (client != null) {
            utils = utils.addStringLike("idCard", client.getIdCard())
                    .addStringLike("source", client.getSource())
                    .addStringLike("licenseNumber", client.getLicenseNumber())
                    .addString("nickName", client.getNickName())
                    .addInteger("quoteStateCode", client.getQuoteStateCode())
                    .addBoolean("transfer", client.getTransfer())
                    .addBoolean("toubao", client.isToubao())
                    .addBoolean("cashback", client.isCashback())
                    .addString("phone", client.getPhone());
        }
        return utils.setFirstResult(from)
                .setMaxResults(num)
                .getQuery().list();
    }

    /**
     * 查询未投保的车险客户
     *
     * @param client 查询条件
     * @param from   数据起始行
     * @param num    查询个数
     * @return Client集合
     */
    public List<Client> listClientOrderByQuoteTimeDescByPage(Client client, int from, int num) {
        //其他条件
        String licenseNumber = null;
        String phone = null;
        String source = null;
        Integer quoteStateCode = null;

        //赋值
        if (null != client) {
            licenseNumber = client.getLicenseNumber();
            phone = client.getPhone();
            source = client.getSource();
            quoteStateCode = client.getQuoteStateCode();
        }


        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id,c.headImg,c.idCard,c.insuranceDeadline,c.licenseNumber,c.nickName,c.openId,c.unionId,c.ownerName,c.phone,c.toubao,c.transfer,c.transferTime,c.cashback,c.source,c.quoteStateCode,c.leastQueryTime,qr.createTime as leastQuoteTime,c.quoteState as quoteState ")
                .append(" from Client c left join (select createTime,state,licenseNumber,ownerName from QuoteRecord qr where createTime in (select max(createTime) from QuoteRecord group by licenseNumber,ownerName)) qr on qr.licenseNumber=c.licenseNumber and qr.ownerName=c.ownerName where c.toubao=0 ");

        if (Tools.notEmpty(licenseNumber)) {
            sql.append(" and c.licenseNumber like '%" + licenseNumber + "%' ");
        }
        if (Tools.notEmpty(phone)) {
            sql.append(" and c.phone like '%" + phone + "%' ");
        }
        if (Tools.notEmpty(source)) {
            sql.append(" and c.source like '%" + source + "%' ");
        }
        if (null != quoteStateCode) {
            sql.append(" and c.quoteStateCode = " + quoteStateCode);
        }

        sql.append(" order by qr.createTime desc ");

        return getSession().createSQLQuery(sql.toString())
                .addEntity(Client.class)
                .setFirstResult(from)
                .setMaxResults(num)
                .list();
    }

    /**
     * 查询未投保的车险客户的数量
     * @param client 查询条件
     * @return long
     */
    public BigInteger getClientOrderByQuoteTimeCount(Client client){
        //其他条件
        String licenseNumber = null;
        String phone = null;
        String source = null;
        Integer quoteStateCode = null;

        //赋值
        if (null != client) {
            licenseNumber = client.getLicenseNumber();
            phone = client.getPhone();
            source = client.getSource();
            quoteStateCode = client.getQuoteStateCode();
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) ")
                .append(" from Client c left join (select createTime,state,licenseNumber,ownerName from QuoteRecord qr where createTime in (select max(createTime) from QuoteRecord group by licenseNumber,ownerName)) qr on qr.licenseNumber=c.licenseNumber and qr.ownerName=c.ownerName where c.toubao=0 ");

        if (Tools.notEmpty(licenseNumber)) {
            sql.append(" and c.licenseNumber like '%" + licenseNumber + "%' ");
        }
        if (Tools.notEmpty(phone)) {
            sql.append(" and c.phone like '%" + phone + "%' ");
        }
        if (Tools.notEmpty(source)) {
            sql.append(" and c.source like '%" + source + "%' ");
        }
        if (null != quoteStateCode) {
            sql.append(" and c.quoteStateCode = " + quoteStateCode);
        }
        return (BigInteger) getSession().createSQLQuery(sql.toString()).uniqueResult();
    }

    /**
     * 查询已投保的车险客户
     *
     * @param client 查询条件
     * @param from   数据起始行
     * @param num    查询个数
     * @return Client集合
     */
    public List<Client> listClientOrderByOrderTimeDescByPage(Client client, int from, int num) {
        //其他条件
        String licenseNumber = null;
        Integer quoteStateCode = null;
        Integer cashback = null;

        //赋值
        if (null != client) {
            licenseNumber = client.getLicenseNumber();
            quoteStateCode = client.getQuoteStateCode();
            if (null != client.isCashback()) {
                cashback = client.isCashback() ? 1 : 0;
            }
        }


        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id,c.headImg,c.idCard,c.insuranceDeadline,c.licenseNumber,c.nickName,c.openId,c.unionId,c.ownerName,c.phone,c.toubao,c.transfer,c.transferTime,c.cashback,c.source,c.quoteStateCode,c.leastQueryTime,qr.createTime as leastOrderTime,c.quoteState as quoteState ")
                .append(" from Client c left join (select createTime,state,licenseNumber,insureName from InsuranceOrder qr where createTime in (select max(createTime) from InsuranceOrder group by licenseNumber,insureName)) qr on qr.licenseNumber=c.licenseNumber and qr.insureName=c.ownerName where c.toubao=1 ");

        if (Tools.notEmpty(licenseNumber)) {
            sql.append(" and c.licenseNumber like '%" + licenseNumber + "%' ");
        }
        if (null != quoteStateCode) {
            sql.append(" and c.quoteStateCode = " + quoteStateCode);
        }
        if (null != cashback) {
            sql.append(" and c.cashback = " + cashback);
        }

        sql.append(" order by qr.createTime desc ");

        return getSession().createSQLQuery(sql.toString())
                .addEntity(Client.class)
                .setFirstResult(from)
                .setMaxResults(num)
                .list();
    }

    /**
     * 查询已投保的车险客户数量
     * @param client 查询条件
     * @return long
     */
    public BigInteger getClientOrderByOrderTimeCount(Client client){
        //其他条件
        String licenseNumber = null;
        Integer quoteStateCode = null;
        Integer cashback = null;

        //赋值
        if (null != client) {
            licenseNumber = client.getLicenseNumber();
            quoteStateCode = client.getQuoteStateCode();
            if (null != client.isCashback()) {
                cashback = client.isCashback() ? 1 : 0;
            }
        }


        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) ")
                .append(" from Client c left join (select createTime,state,licenseNumber,insureName from InsuranceOrder qr where createTime in (select max(createTime) from InsuranceOrder group by licenseNumber,insureName)) qr on qr.licenseNumber=c.licenseNumber and qr.insureName=c.ownerName where c.toubao=1 ");

        if (Tools.notEmpty(licenseNumber)) {
            sql.append(" and c.licenseNumber like '%" + licenseNumber + "%' ");
        }
        if (null != quoteStateCode) {
            sql.append(" and c.quoteStateCode = " + quoteStateCode);
        }
        if (null != cashback) {
            sql.append(" and c.cashback = " + cashback);
        }
        return (BigInteger) getSession().createSQLQuery(sql.toString()).uniqueResult();
    }


    //查询所有导出的client
    @SuppressWarnings("unchecked")
    public List<Client> listExportClient(boolean toubao) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -delayDays);
        java.sql.Date date = new java.sql.Date(cal.getTime().getTime());
        if (!toubao) {
            String hql = "from Client where toubao = :toubao and leastQueryTime > :date";
            List<Client> list = getSession().createQuery(hql).setBoolean("toubao", toubao)
                    .setTimestamp("date", date).list();
            return list;
        }
        return null;
    }

    //查询所有的Client
    @SuppressWarnings("unchecked")
    public List<Client> listAllClient() {
        QueryUtils utils = new QueryUtils(getSession(), "from Client");
        return utils.getQuery().list();
    }


    /**
     * 查询列表：获取相应条件下的数据总数
     * @param client    查询条件
     * @return  统计的总数
     */
    public BigInteger getClientCount(Client client) {

        Integer toubao = null;
        //其他条件
        String licenseNumber = null;
        String phone = null;
        String source = null;
        Integer quoteStateCode = null;
        Integer cashback = null;

        //赋值
        if (null != client) {
            if (null != client.isToubao()) {
                toubao = client.isToubao() ? 1 : 0;
            }
            licenseNumber = client.getLicenseNumber();
            phone = client.getPhone();
            source = client.getSource();
            quoteStateCode = client.getQuoteStateCode();
            if (null != client.isCashback()) {
                cashback = client.isCashback() ? 1 : 0;
            }
        }


        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from Client c where 1=1");

        if (null != cashback) {
            sql.append(" and c.toubao = " + toubao);
        }
        if (Tools.notEmpty(licenseNumber)) {
            sql.append(" and c.licenseNumber like '%" + licenseNumber + "%' ");
        }
        if (Tools.notEmpty(phone)) {
            sql.append(" and c.phone like '%" + phone + "%' ");
        }
        if (Tools.notEmpty(source)) {
            sql.append(" and c.source like '%" + source + "%' ");
        }
        if (null != quoteStateCode) {
            sql.append(" and c.quoteStateCode = " + quoteStateCode);
        }
        if (null != cashback) {
            sql.append(" and c.cashback = " + cashback);
        }

        return (BigInteger) getSession().createSQLQuery(sql.toString()).uniqueResult();
    }


    //根据id删除Client
    public boolean removeClientById(String id) {
        String hql = "delete from Client where id = :id";
        int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
        return executeUpdate > 0;
    }

    //更新Client
    public Client updateClient(Client client) {
        getSession().update(client);
        return client;
    }


}