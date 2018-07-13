package com.freelycar.service;

import com.freelycar.dao.*;
import com.freelycar.entity.*;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.RESCODE;
import com.freelycar.util.TasConfig;
import com.freelycar.util.Tools;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 唐炜（修改）
 */
@Transactional
@Service
public class OrderService {
    private static Logger log = Logger.getLogger(OrderService.class);

    /********** 注入OrderDao ***********/
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InvoiceInfoDao invoiceInfoDao;

    @Autowired
    private QuoteRecordDao quoteRecordDao;

    @Autowired
    private CashbackRecordDao cashbackDao;

    @Autowired
    private ReciverDao reciverDao;

    @Autowired
    private InvitionDao invitionDao;

    @Autowired
    private CashbackRecordDao cashbackRecordDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private TasUserInfoDao tasUserInfoDao;

    /********** 注入ClientService ***********/
    @Autowired
    private ClientService clientService;

    public Map<String, Object> getOrderById(int id) {
        InsuranceOrder orderById = orderDao.getOrderById(id);
        if (orderById != null) {
            Map<String, Object> jsonres = RESCODE.SUCCESS.getJSONRES(orderById);
            String orderId = orderById.getOrderId();
            String openId = orderById.getOpenId();
            String licenseNumber = orderById.getLicenseNumber();
            if (Tools.notEmpty(orderId)) {
                //根据orderId查询发票和收件人信息 和报价记录
                Map<String, Object> res = fingOrderIdRelative(orderId);
                res.put("data", orderById);

                //查询车辆信息
                Client clientByOpenId = null;
                if (Tools.notEmpty(openId) && Tools.notEmpty(licenseNumber)) {
                    clientByOpenId = clientDao.getClientByOpenIdAndLicenseNumber(openId, licenseNumber);
                }
                res.put("carInfo", clientByOpenId);


                return res;
            }
            return jsonres;
        } else {
            return RESCODE.NOT_FOUND.getJSONRES();
        }
    }

    public Map<String, Object> getOrderByOrderId(String orderId) {
        InsuranceOrder orderById = orderDao.getOrderByOrderId(orderId);
        if (orderById != null) {
            Map<String, Object> res = fingOrderIdRelative(orderId);
            res.put("data", orderById);
            return res;
        } else {
            return RESCODE.NOT_FOUND.getJSONRES();
        }
    }

    /**
     * 查询订单的运单信息
     *
     * @param orderId
     * @return
     */
    public InsuranceOrder getOrderSignForInfoByOrderId(String orderId) {
        InsuranceOrder orderById = orderDao.getOrderByOrderId(orderId);
        if (orderById != null) {
            return orderById;
        } else {
            return null;
        }
    }

    public Map<String, Object> getCountBySourId(Date startTime, Date endTime) {
//        if (startTime.getTime() == endTime.getTime()) {
        //表示同一天
        startTime = Tools.setTimeToBeginningOfDay(startTime);
        endTime = Tools.setTimeToEndofDay(endTime);
//        }

        List<Object[]> list = orderDao.getCountBySourId(startTime, endTime);
        if (list.isEmpty()) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }

        List<CountBySource> newlist = new ArrayList<>();

        for (Object[] obj : list) {
            //System.out.println(obj);
            CountBySource count = new CountBySource();
            int sourceId = (int) obj[0];
            count.setSourceId(sourceId);
            //如果表里面没查到source 保险一点再去查invition表
            String source = (String) obj[1];
            if (Tools.isEmpty(source)) {
                Invition invitionById = invitionDao.getInvitionById(sourceId);
                if (null != invitionById) {
                    source = invitionById.getName();
                } else {
                    source = "[已删除的渠道]";
                }
            }
            count.setSource(source);
            count.setPrice((long) obj[2]);
            count.setPrice_yuan((long) obj[2] / 100.0);
            newlist.add(count);
        }

        return RESCODE.SUCCESS.getJSONRES(newlist);
    }

    public Map<String, Object> listCount(Invition invition, int page, int number, Date startTime, Date endTime) {
//        if (startTime.getTime() == endTime.getTime()) {
        //表示同一天
        startTime = Tools.setTimeToBeginningOfDay(startTime);
        endTime = Tools.setTimeToEndofDay(endTime);
//        }

        List<Object[]> list = orderDao.listCount(invition, page, number, startTime, endTime);

        List<CountBySource> newlist = new ArrayList<>();
        if (list.isEmpty()) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }

        for (Object[] obj : list) {
            //System.out.println(obj);
            CountBySource count = new CountBySource();
            count.setSourceId((int) obj[0]);
            count.setSource((String) obj[1]);
            count.setPrice((long) obj[2]);
            count.setPrice_yuan((long) obj[2] / 100.0);
            newlist.add(count);
        }

        return RESCODE.SUCCESS.getJSONRES(newlist);
    }

    //根据orderId查询发票和收件人信息 和报价记录
    private Map<String, Object> fingOrderIdRelative(String orderId) {
        Map<String, Object> jsonres = RESCODE.SUCCESS.getJSONRES();
        InvoiceInfo invoiceInfoByOrderId = invoiceInfoDao.getInvoiceInfoByOrderId(orderId);
        if (invoiceInfoByOrderId != null) {
            jsonres.put("invoiceInfo", invoiceInfoByOrderId);
        }

        CashbackRecord cashbackRecordByOrderId = cashbackDao.getCashbackRecordByOrderId(orderId);
        if (cashbackRecordByOrderId != null) {
            jsonres.put("cashbackRecord", cashbackRecordByOrderId);
        }

        //查询报价记录
        QuoteRecord quoteRecordBySpecify = quoteRecordDao.getQuoteRecordBySpecify("offerId", orderId);
        if (quoteRecordBySpecify != null) {
            //处理offerDetail的字符串
            String offerDetail = quoteRecordBySpecify.getOfferDetail();
            quoteRecordBySpecify.setShangyeList(QuoteRecord.getShangYeJsonObj(offerDetail));
            quoteRecordBySpecify.setQiangzhiList(QuoteRecord.getQiangzhiJsonObj(offerDetail));
            //设置不计免赔险价格
            quoteRecordBySpecify.setAdditionalPrice(QuoteRecord.getAdditionalPriceObj(offerDetail));
            jsonres.put("quoteRecord", quoteRecordBySpecify);
            //设置返回的红包
            BigDecimal bg = new BigDecimal(CalcuateMoneyBack(offerDetail));
            double calBackMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            quoteRecordBySpecify.setBackmoney(String.valueOf(calBackMoney));
        }
        //收获信息
        Reciver reciverByOrderId = reciverDao.getReciverByOrderId(orderId);
        if (reciverByOrderId != null) {
            jsonres.put("reciver", reciverByOrderId);
        }
        InvoiceInfo invoiceByOrderId = invoiceInfoDao.getInvoiceInfoByOrderId(orderId);
        if (reciverByOrderId != null) {
            jsonres.put("invoiceInfo", invoiceByOrderId);
        }
        return jsonres;
    }

    //增加一个Order
    public Map<String, Object> saveOrder(InsuranceOrder order) {

        orderDao.saveOrder(order);
        return RESCODE.SUCCESS.getJSONRES();
    }

    /**
     * 分页查询
     *
     * @param page   从第几页开始查询
     * @param number 每页数量
     * @return
     */
    public Map<String, Object> listOrder(InsuranceOrder order, int page, int number) {
        int from = (page - 1) * number;
        List<InsuranceOrder> listPage = orderDao.listOrder(order, from, number);
        if (listPage != null && !listPage.isEmpty()) {
            long count = orderDao.getOrderCount(order);
            return RESCODE.SUCCESS.getJSONRES(listPage, (int) Math.ceil(count / (float) number), count);
        }
        return RESCODE.NOT_FOUND.getJSONRES();
    }

    /**
     * @return
     */
    public List<InsuranceOrder> listValidOrder(InsuranceOrder order) {
        List<InsuranceOrder> listPage = orderDao.listValidOrder(order);
        return listPage;
    }

    //根据id删除Order
    public Map<String, Object> removeOrderById(String id) {
        boolean res = orderDao.removeOrderById(id);
        if (res) {
            return RESCODE.SUCCESS.getJSONRES();
        } else {
            return RESCODE.DELETE_FAIL.getJSONRES();
        }
    }

    //更新Order
    public Map<String, Object> updateOrderByOfferId(InsuranceOrder order) {
        InsuranceOrder or = orderDao.getOrderByOrderId(order.getOrderId());
        String backMoney = order.getBackmoney();
        if (!StringUtils.isEmpty(backMoney)) {
            or.setBackmoney(backMoney);
        }
        String biPolicyNo = order.getBiPolicyNo();
        if (!StringUtils.isEmpty(biPolicyNo)) {
            or.setBiPolicyNo(biPolicyNo);
        }
        Long biPolicyPrice = order.getBiPolicyPrice();
        if (!StringUtils.isEmpty(biPolicyPrice)) {
            or.setBiPolicyPrice(biPolicyPrice);
        }
        Boolean cashBack = order.getCashback();
        if (!StringUtils.isEmpty(cashBack)) {
            or.setCashback(cashBack);
        }
        String ciPolicyNo = order.getCiPolicyNo();
        if (!StringUtils.isEmpty(ciPolicyNo)) {
            or.setCiPolicyNo(ciPolicyNo);
        }
        Long ciPolicyPrice = order.getCiPolicyPrice();
        if (!StringUtils.isEmpty(ciPolicyPrice)) {
            or.setCiPolicyPrice(ciPolicyPrice);
        }
        String expressCompany = order.getExpressCompany();
        if (!StringUtils.isEmpty(expressCompany)) {
            or.setExpressCompany(expressCompany);
        }
        String expressNumber = order.getExpressNumber();
        if (!StringUtils.isEmpty(expressNumber)) {
            or.setExpressNumber(expressNumber);
        }
        String licenseNumber = order.getLicenseNumber();
        if (!StringUtils.isEmpty(licenseNumber)) {
            or.setLicenseNumber(licenseNumber);
        }
        String offerDetail = order.getOfferDetail();
        if (!StringUtils.isEmpty(offerDetail)) {
            or.setOfferDetail(offerDetail);
        }
        String remark = order.getRemark();
        if (!StringUtils.isEmpty(remark)) {
            or.setRemark(remark);
        }

        orderDao.updateOrder(or);
        return RESCODE.SUCCESS.getJSONRES();
    }

    //更新Order
    public Map<String, Object> updateOrder(InsuranceOrder order) {
        InsuranceOrder orderById = orderDao.getOrderById(order.getId());
        orderById.setState(order.getState());
        orderDao.updateOrder(orderById);

        //同步更新Client中的状态
        clientService.updateClientQuoteState(orderById.getOpenId(), orderById.getLicenseNumber(), orderById.getState());

        return RESCODE.SUCCESS.getJSONRES();
    }

    //报价记录
    public Map<String, Object> getClientQuoteRecordByLicenseNumber(String licenseNumber) {
        List<QuoteRecord> quoteRecordBylicenseNumber = quoteRecordDao.getQuoteRecordBylicenseNumber(licenseNumber);
        return RESCODE.SUCCESS.getJSONRES(quoteRecordBylicenseNumber);
    }

    //车险订单
	/*public Map<String,Object> getOrderByLicenseNumber(String licenseNumber){
		List<InsuranceOrder> orderByLicenseNumber = orderDao.getOrderByLicenseNumber(licenseNumber);
		return RESCODE.SUCCESS.getJSONRES(orderByLicenseNumber);
	}*/

    //报价记录
    public Map<String, Object> getClientOrderByLicenseNumber(String licenseNumber, String openId, int page, int number) {
        int from = (page - 1) * number;
        if (Tools.isEmpty(licenseNumber)) {
            return RESCODE.USER_LICENSENUMBER_EMPTY.getJSONRES();
        }
        if (null == openId) {
            return RESCODE.PARAM_EMPTY.getJSONRES();
        }

        List<InsuranceOrder> orderByLicenseNumber = orderDao.getOrderByLicenseNumber(licenseNumber, openId, from, number);
        //循环出来这单的报价记录
        for (InsuranceOrder order : orderByLicenseNumber) {
            QuoteRecord quoteRecordBylicenseNumberAndOfferId = quoteRecordDao.getQuoteRecordBylicenseNumberAndOfferId(licenseNumber, order.getOrderId());

            long totalPrice = order.getTotalPrice();

            String offerDetail = quoteRecordBylicenseNumberAndOfferId.getOfferDetail();
            quoteRecordBylicenseNumberAndOfferId.setQiangzhiList(QuoteRecord.getQiangzhiJsonObj(offerDetail));
            quoteRecordBylicenseNumberAndOfferId.setShangyeList(QuoteRecord.getShangYeJsonObj(offerDetail));
            quoteRecordBylicenseNumberAndOfferId.setTotalPrice(totalPrice + "");

            quoteRecordBylicenseNumberAndOfferId.setAdditionalPrice(QuoteRecord.getAdditionalPriceObj(quoteRecordBylicenseNumberAndOfferId.getOfferDetail()));

            order.setQuoteRecord(quoteRecordBylicenseNumberAndOfferId);

            Reciver reciverByOrderId = reciverDao.getReciverByOrderId(order.getOrderId());
            if (reciverByOrderId != null) {
                reciverByOrderId.setExpressCompany(order.getExpressCompany());
                reciverByOrderId.setExpressNumber(order.getExpressNumber());
                reciverByOrderId.setRemark(order.getRemark());
            }
            order.setReciver(reciverByOrderId);


            String backMoney = order.getBackmoney();
            //如果返现金额为null，则设置为0显示
            if (Tools.isEmpty(backMoney)) {
                backMoney = "0";
                order.setBackmoney(backMoney);
            }

            //计算实付金额
            Float backMoneyFloat = Float.parseFloat(backMoney);
            float theAmountActuallyPaid = (totalPrice - backMoneyFloat * 100) / 100;
            order.setTheAmountActuallyPaid(String.valueOf(theAmountActuallyPaid));

        }

        long count = orderDao.getOrderCountByLicenseNumber(licenseNumber, openId);
        return RESCODE.SUCCESS.getJSONRES(orderByLicenseNumber, (int) Math.ceil(count / (float) number), count);
    }

    private Double CalcuateMoneyBack(String offerDetail) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(offerDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Double curPrice = obj.getDouble("currentPrice");
        Double ciBasePrice = obj.getDouble("ciBasePrice");
        float rate = cashbackDao.listCashbackRate().get(0).getRate();
        Double calReturn = curPrice * rate;
        return calReturn;
    }

    public Map<String, Object> affirmDistributionInfo(InsuranceOrder insuranceOrder) {
        InsuranceOrder order = orderDao.getOrderByOrderId(insuranceOrder.getOrderId());
        order.setExpressCompany(insuranceOrder.getExpressCompany());
        order.setExpressNumber(insuranceOrder.getExpressNumber());
        order.setRemark(insuranceOrder.getRemark());
        order.setState(INSURANCE.QUOTESTATE_NOTREVICER.getCode());
        order.setStateString(INSURANCE.QUOTESTATE_NOTREVICER.getName());
        order.setDeliveredTime(System.currentTimeMillis());
        orderDao.updateOrder(order);

        QuoteRecord quoteRecord = quoteRecordDao.getQuoteRecordBylicenseNumberAndOfferId(order.getLicenseNumber(), order.getOrderId());
        if (null == quoteRecord) {
            return RESCODE.NOT_FOUND.getJSONRES("对应的报价记录查询失败！");
        }
        quoteRecord.setState(INSURANCE.QUOTESTATE_NOTREVICER.getCode());
        quoteRecordDao.update(quoteRecord);

        Client client = clientDao.getClientByOpenIdAndLicenseNumber(order.getOpenId(), order.getLicenseNumber());
        if (null == client) {
            return RESCODE.NOT_FOUND.getJSONRES("对应的client信息查询失败！");
        }
        client.setQuoteStateCode(INSURANCE.QUOTESTATE_NOTREVICER.getCode());
        client.setQuoteState(INSURANCE.QUOTESTATE_NOTREVICER.getName());
        clientDao.updateClient(client);

        //推送模板消息到公众号
        String unionId = client.getUnionId();
        TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);

        if (null != tasUserInfo) {
            String tasMessageSendResult = TasConfig.pushOrderChangeInfo(order, tasUserInfo.getOpenId());
            log.debug(tasMessageSendResult);
        }

        return RESCODE.SUCCESS.getJSONRES();
    }

    /**
     * 确认签收
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> affirmSignForInfo(String orderId) {
        InsuranceOrder order = orderDao.getOrderByOrderId(orderId);
        order.setState(INSURANCE.QUOTESTATE_HASTOUBAO.getCode());
        order.setStateString(INSURANCE.QUOTESTATE_HASTOUBAO.getName());
        order.setDeliveredTime(System.currentTimeMillis());
        orderDao.updateOrder(order);

        QuoteRecord quoteRecord = quoteRecordDao.getQuoteRecordBylicenseNumberAndOfferId(order.getLicenseNumber(), order.getOrderId());
        if (null == quoteRecord) {
            return RESCODE.NOT_FOUND.getJSONRES("对应的报价记录查询失败！");
        }
        quoteRecord.setState(INSURANCE.QUOTESTATE_HASTOUBAO.getCode());
        quoteRecordDao.update(quoteRecord);

        Client client = clientDao.getClientByOpenIdAndLicenseNumber(order.getOpenId(), order.getLicenseNumber());
        if (null == client) {
            return RESCODE.NOT_FOUND.getJSONRES("对应的client信息查询失败！");
        }
        client.setQuoteStateCode(INSURANCE.QUOTESTATE_HASTOUBAO.getCode());
        client.setQuoteState(INSURANCE.QUOTESTATE_HASTOUBAO.getName());
        clientDao.updateClient(client);

        //推送模板消息到公众号
        String unionId = client.getUnionId();
        TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);

        if (null != tasUserInfo) {
            String tasMessageSendResult = TasConfig.pushOrderChangeInfo(order, tasUserInfo.getOpenId());
            log.debug(tasMessageSendResult);
        }

        return RESCODE.SUCCESS.getJSONRES();
    }

    /**
     * 确认返现
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> affirmCashBackRecordInfo(String orderId) {
        InsuranceOrder order = orderDao.getOrderByOrderId(orderId);
        order.setCashback(true);
        order.setCashbackTime(System.currentTimeMillis());
        orderDao.updateOrder(order);

        Client client = clientDao.getClientByOpenIdAndLicenseNumber(order.getOpenId(), order.getLicenseNumber());
        if (null == client) {
            return RESCODE.NOT_FOUND.getJSONRES("对应的client信息查询失败！");
        }
        client.setCashback(true);
        clientDao.updateClient(client);

        return RESCODE.SUCCESS.getJSONRES();
    }

    static class CountBySource {
        private String source;
        private int sourceId;
        private long price;
        private double price_yuan;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getSourceId() {
            return sourceId;
        }

        public void setSourceId(int sourceId) {
            this.sourceId = sourceId;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public double getPrice_yuan() {
            return price_yuan;
        }

        public void setPrice_yuan(double price_yuan) {
            this.price_yuan = price_yuan;
        }

    }

}