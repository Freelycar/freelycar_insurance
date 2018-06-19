package com.freelycar.service;

import com.freelycar.dao.ClientDao;
import com.freelycar.dao.InsuranceDao;
import com.freelycar.dao.OrderDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.entity.*;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Transactional
@Service
public class ClientService {
    private final static Logger log = Logger.getLogger(ClientService.class);

    /********** 注入ClientDao ***********/
    @Autowired
    private ClientDao clientDao;

    @Autowired
    private QuoteRecordDao qrdao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InsuranceDao insuranceDao;


    //增加一个Client
    public Map<String, Object> saveClient(Client client) {
        clientDao.saveClient(client);
        return RESCODE.SUCCESS.getJSONRES();
    }

    public Map<String, Object> getClientDetail(int id) {
        Client clientById = clientDao.getClientById(id);
        if (clientById == null) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }

        QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
        if (quoteRecord != null) {
            clientById.setLeastQuoteTime(quoteRecord.getCreateTime());
        }

        Insurance insu = insuranceDao.getLatestXuBaoByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
        if (insu != null) {
            if (Tools.notEmpty(insu.getInsuranceEndTime())) {
                //查出来的是秒 所以加了三个零
                clientById.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime() + "000"));
            }
        }

        InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(clientById.getOwnerName(), clientById.getLicenseNumber());
        if (quoteRecord != null) {
            clientById.setLeastQuoteTime(quoteRecord.getCreateTime());
        }
        if (insuranceOrder != null) {
            clientById.setLeastOrderTime(insuranceOrder.getCreateTime());
        }

        return RESCODE.SUCCESS.getJSONRES(clientById);
    }

    public Client getClientDetail(String openId, String licenseNumber, String ownerName) {
        Client clientById = clientDao.getClient(openId, licenseNumber, ownerName);
        return clientById;
    }

    public Map<String, Object> isExistByOpenId(String openId) {
        Client client = clientDao.getClientByOpenId(openId);
        if (client == null) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }
        return RESCODE.SUCCESS.getJSONRES(client);
    }

    /**
     * 分页查询
     *
     * @param page   从第几页开始查询
     * @param number 每页数量
     * @return
     */
    public Map<String, Object> listClient(Client client, int page, int number) {
        int from = (page - 1) * number;
        if (null == client) {
            return RESCODE.PARAM_EMPTY.getJSONRES();
        }

        /*
            1.如果是“未投保”查询，按照“报价时间”倒序
	    	2.如果是“已投保”查询，按照“订单时间”倒序
         */
        List<Client> listPage;
        if (client.isToubao()) {
            listPage = clientDao.listClientOrderByOrderTimeDescByPage(client, from, number);
        } else {
            listPage = clientDao.listClientOrderByQuoteTimeDescByPage(client, from, number);
        }


        if (listPage != null && !listPage.isEmpty()) {
            //查询最新的订单时间
            for (Client c : listPage) {
                QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (quoteRecord != null) {
                    c.setLeastQuoteTime(quoteRecord.getCreateTime());
                    c.setQuoteStateCode(quoteRecord.getState());
                    c.setQuoteState(INSURANCE.getQuotestateName(quoteRecord.getState()));
                }
                Insurance insu = insuranceDao.getLatestXuBaoByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (insu != null) {
                    if (Tools.notEmpty(insu.getInsuranceEndTime())) {
                        //查出来的是秒 所以加了三个零
                        c.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime() + "000"));
                    }
                }

                InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(c.getOwnerName(), c.getLicenseNumber());

                if (insuranceOrder != null) {
                    c.setLeastOrderTime(insuranceOrder.getCreateTime());
                }
            }

            long count = clientDao.getClientCount(client).longValue();
            return RESCODE.SUCCESS.getJSONRES(listPage, (int) Math.ceil(count / (float) number), count);
        }
        return RESCODE.SUCCESS.getJSONRES(listPage);
    }

    public List<Client> getExportClientList(boolean toubao) {
        List<Client> listClient = clientDao.listExportClient(toubao);
        if (listClient != null && !listClient.isEmpty()) {
            //查询最新的订单时间
            for (Client c : listClient) {
                QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (quoteRecord != null) {
                    c.setLeastQuoteTime(quoteRecord.getCreateTime());
                    c.setQuoteStateCode(quoteRecord.getState());
                    c.setQuoteState(INSURANCE.getQuotestateName(quoteRecord.getState()));
                }
                Insurance insu = insuranceDao.getLatestXuBaoByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (insu != null) {
                    if (Tools.notEmpty(insu.getInsuranceEndTime())) {
                        //查出来的是秒 所以加了三个零
                        c.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime() + "000"));
                    }
                }
                InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (insuranceOrder != null) {
                    c.setLeastOrderTime(insuranceOrder.getCreateTime());
                }
            }
        }
        return listClient;
    }

    public List<ClientNOrder> getExportClientOrderList(boolean toubao) {
        List<ClientNOrder> retList = new ArrayList<ClientNOrder>();
        List<Client> listClient = clientDao.listExportClient(toubao);
        if (listClient != null && !listClient.isEmpty()) {
            //查询最新的订单时间
            for (Client c : listClient) {
                QuoteRecord quoteRecord = orderDao.getLatestQuoteByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (quoteRecord != null) {
                    c.setLeastQuoteTime(quoteRecord.getCreateTime());
                    c.setQuoteStateCode(quoteRecord.getState());
                    c.setQuoteState(INSURANCE.getQuotestateName(quoteRecord.getState()));
                }
                Insurance insu = insuranceDao.getLatestXuBaoByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (insu != null) {
                    if (Tools.notEmpty(insu.getInsuranceEndTime())) {
                        //查出来的是秒 所以加了三个零
                        c.setInsuranceDeadline(Long.parseLong(insu.getInsuranceEndTime() + "000"));
                    }
                }
                InsuranceOrder insuranceOrder = orderDao.getLatestOrderByNameLice(c.getOwnerName(), c.getLicenseNumber());
                if (insuranceOrder != null) {
                    c.setLeastOrderTime(insuranceOrder.getCreateTime());
                }
                ClientNOrder nn = new ClientNOrder();
                nn.setClient(c);
                nn.setOrder(insuranceOrder);
                retList.add(nn);
            }
        }
        return retList;
    }

    public List<Client> listAllClient() {
        List<Client> allClient = clientDao.listAllClient();
        return allClient;
    }

    //根据id删除Client
    public Map<String, Object> removeClientById(String id) {
        boolean res = clientDao.removeClientById(id);
        if (res) {
            return RESCODE.SUCCESS.getJSONRES();
        } else {
            return RESCODE.DELETE_FAIL.getJSONRES();
        }
    }

    //更新Client
    public Map<String, Object> updateClient(Client client) {
        clientDao.updateClient(client);
        return RESCODE.SUCCESS.getJSONRES();
    }


    /**
     * 根据openId更新Client的报价状态
     * @param openId    openId
     * @param quoteStateCode    报价状态
     * @return  boolean
     */
    public boolean updateClientQuoteState(String openId,String licenseNumber, int quoteStateCode) {
        if (StringUtils.isEmpty(openId)) {
            log.error("方法updateClientQuoteState执行失败:参数openId为空。");
            return false;
        }
        if (StringUtils.isEmpty(licenseNumber)) {
            log.error("方法updateClientQuoteState执行失败:参数licenseNumber为空。");
            return false;
        }
//        Client client = clientDao.getClientByOpenId(openId);
        Client client = clientDao.getClientByOpenIdAndLicenseNumber(openId, licenseNumber);
        if (null == client) {
            log.error("方法updateClientQuoteState执行失败:" + RESCODE.USER_NOT_EXIST.getMsg());
            return false;
        }
        client.setQuoteStateCode(quoteStateCode);
        client.setQuoteState(INSURANCE.getQuotestateName(quoteStateCode));
        try {
//            clientDao.updateClient(client);
            clientDao.saveClient(client);
        } catch (Exception e) {
            log.error("方法updateClientQuoteState执行失败:执行SQL出现异常。", e);
            e.printStackTrace();
        }
        return true;
    }

}