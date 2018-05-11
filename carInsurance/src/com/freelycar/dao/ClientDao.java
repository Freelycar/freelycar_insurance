package com.freelycar.dao;

import com.freelycar.entity.Client;
import com.freelycar.util.QueryUtils;
import com.freelycar.util.SqlHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
     * 查询Client的总数
     *
     * @return
     */
    public long getClientCount(Client client) {
        QueryUtils utils = new QueryUtils(getSession(), "select count(*) from Client");

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
        return (long) utils.getQuery().uniqueResult();
    }


    //根据id删除Client
    public boolean removeClientById(String id) {
        String hql = "delete from Client where id = :id";
        int executeUpdate = getSession().createQuery(hql).setString("id", id).executeUpdate();
        return executeUpdate > 0;
    }

    //更新Client
    public void updateClient(Client client) {
        String hql = SqlHelper.genUpdateSql(client, Client.class);

        Query query = getSession().createQuery(hql);
        query = SqlHelper.getQuery(client, Client.class, query);

        query.executeUpdate();

    }

}