package com.freelycar.service;

import com.freelycar.dao.*;
import com.freelycar.entity.*;
import com.freelycar.util.Constant;
import com.freelycar.util.INSURANCE;
import com.freelycar.util.RESCODE;
import com.freelycar.util.TasConfig;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Transactional
@Service
public class OrderpushResulService {
    private static Logger log = Logger.getLogger(OrderpushResulService.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /********** 注入OrderpushResulDao ***********/
    @Autowired
    private OrderpushResulDao orderpushResulDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private QuoteRecordDao quoteRecordDao;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private CashbackRecordDao cashbackDao;
    /********** 注入ClientService ***********/
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private TasUserInfoDao tasUserInfoDao;
    @Autowired
    private TasUserInfoService tasUserInfoService;

    //增加一个OrderpushResul
    public Map<String, Object> saveOrderpushResul(OrderpushResul orderpushResul) {

        orderpushResulDao.saveOrderpushResul(orderpushResul);
        return RESCODE.SUCCESS.getJSONRES();
    }

    //增加一个OrderpushResul
    public Map<String, Object> getOrderpushResulByOrderId(String orderId) {
        OrderpushResul orderpushResulByOrderId = orderpushResulDao.getOrderpushResulByOrderId(orderId);
        return RESCODE.SUCCESS.getJSONRES(orderpushResulByOrderId);
    }

    /**
     * 分页查询
     *
     * @param page   从第几页开始查询
     * @param number 每页数量
     * @return
     */
    public Map<String, Object> listOrderpushResul(OrderpushResul orderpushResul, int page, int number) {
        int from = (page - 1) * number;
        List<OrderpushResul> listPage = orderpushResulDao.listOrderpushResul(orderpushResul, from, number);
        if (listPage != null && !listPage.isEmpty()) {
            long count = orderpushResulDao.getOrderpushResulCount(orderpushResul);
            return RESCODE.SUCCESS.getJSONRES(listPage, (int) Math.ceil(count / (float) number), count);
        }
        return RESCODE.NOT_FOUND.getJSONRES();
    }


    //根据id删除OrderpushResul
    public Map<String, Object> removeOrderpushResulById(String id) {
        boolean res = orderpushResulDao.removeOrderpushResulById(id);
        if (res) {
            return RESCODE.SUCCESS.getJSONRES();
        } else {
            return RESCODE.DELETE_FAIL.getJSONRES();
        }
    }

    //更新OrderpushResul
    public Map<String, Object> updateOrderpushResul(OrderpushResul orderpushResul) {
        orderpushResulDao.updateOrderpushResul(orderpushResul);
        return RESCODE.SUCCESS.getJSONRES();
    }


    //核保结果推送
    public Map<String, Object> orderpushResult(String result) {
        try {
            log.info("核保推送结果： " + result);
            //System.out.println("核保推送结果： "+result);
            JSONObject resObj = new JSONObject(result);
            if (resObj.getJSONObject("errorMsg").getString("code").equals("success")) {
                JSONObject resultobj = resObj.getJSONObject("data");
                final String orderId = resultobj.getString("orderId");
                //根据oferId查提交核保的那条记录
                final InsuranceOrder order = orderDao.getOrderByOrderId(orderId);

                JSONObject underwritingJson = new JSONObject(resultobj.getString("underwritingJson"));
                if (underwritingJson.has("errorMsg")) {
                    //核保异常
                    //更新订单状态
                    order.setState(INSURANCE.QUOTESTATE_HEBAOFAIL.getCode());
                    order.setStateString(INSURANCE.QUOTESTATE_HEBAOFAIL.getName());
                    order.setHebaoMessage(underwritingJson.getString("errorMsg"));
                    //SocketHelper.sendMessage(order.getOpenId(), RESCODE.PUSHBACK_HEBAO_EXCEPTION.getJSONObject(underwritingJson.getString("errorMsg")).toString());
                    return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(underwritingJson.getString("errorMsg"));
                }

                int state = resultobj.getInt("state");
                String licenseNumber = resultobj.getString("licenseNumber");
                int underwritingPriceCent = resultobj.getInt("underwritingPriceCent");

                if (underwritingJson.has("biNo")) {//商业险
                    String bino = underwritingJson.getJSONObject("biNo").getString("value");//保单号
                    order.setBiPolicyNo(bino);
                }

                if (underwritingJson.has("ciNo")) {//较强险
                    String ciNo = underwritingJson.getJSONObject("ciNo").getString("value");//保单号
                    order.setCiPolicyNo(ciNo);
                }

                //把单号和过期时间存在map中
                JSONObject jsonObject = underwritingJson.getJSONObject("checkCode");
                final Map<String, Long> proposalMap = Constant.getProposalMap();

                try {
                    //2018-3-19 17:33:27
                    proposalMap.put(orderId, format.parse(jsonObject.getString("value")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //在另外一个计时器中 循环 map中的订单
                //理论上 他是不可能在推送过来就立即支付的 因此注释掉
				/*我:
					我在问个小问题 他没支付 我掉是否承保接口 在掉了承保接口之后 他支付了。我需要再次掉用才会返回是吧
					那一年:
					是的*/
				/*if(proposalMap.get(orderId) > System.currentTimeMillis()){
					
					Map<String, Object> confirmChengbao = insuranceService.confirmChengbao(orderId);
					if(!confirmChengbao.get("code").equals("0")){
						System.out.println("请求是否承保失败！。。");
					}
					
				}else{
					proposalMap.remove(orderId);
				}*/

                QuoteRecord qr = quoteRecordDao.getQuoteRecordBySpecify("offerId", orderId);
                //设置报价详细
                order.setOfferDetail(qr.getOfferDetail());
                order.setTotalPrice(underwritingPriceCent);
                order.setLicenseNumber(licenseNumber);
                order.setOrderId(orderId);
                order.setBackmoney(qr.getBackmoney());
                //更新订单状态
                order.setState(INSURANCE.QUOTESTATE_NOTPAY.getCode());
                order.setStateString(INSURANCE.QUOTESTATE_NOTPAY.getName());

                //更新支付二维码和失效时间
                if (underwritingJson.has("payJson")) {//较强险
                    JSONObject payQrcode = underwritingJson.getJSONObject("payJson").getJSONObject("payQrcode");//支付信息
                    order.setPaycodeurl(payQrcode.getString("content"));
                    order.setEffectiveTime(jsonObject.getString("value"));
                    order.setEffetiveTimeLong(proposalMap.get(orderId));
                }

                orderService.updateOrder(order);

                //同步更新Client中的状态
                clientService.updateClientQuoteState(order.getOpenId(), order.getLicenseNumber(), order.getState());

                /*
                 * 调用方法，将待支付信息推送给微信公众号
                 */
                Client client = clientDao.getClientByOpenIdAndLicenseNumber(order.getOpenId(), order.getLicenseNumber());
                if (null != client) {
                    String unionId = client.getUnionId();
                    TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);

                    if (null != tasUserInfo) {
                        String tasMessageSendResult = TasConfig.pushOrderUnpaidInfo(order, tasUserInfo.getOpenId());
                        log.debug(tasMessageSendResult);
                    } else {
                        //后面还得加一个逻辑，tasUserInfo找不到时，需要调用更新的方法去更新tasUserInfo表
                        int updateCount = 0;
                        if (!StringUtils.isEmpty(unionId)) {
                            updateCount = tasUserInfoService.updateUserOpenIds();
                        }
                        if (updateCount == 0) {
                            log.error("用户没有关注公众号，无法向其推送消息！");
                        } else {
                            tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);
                            if (null != tasUserInfo) {
                                String tasMessageSendResult = TasConfig.pushOrderUnpaidInfo(order, tasUserInfo.getOpenId());
                                log.debug(tasMessageSendResult);
                            }
                        }
                    }
                }


                Map<String, Object> msg = new HashMap<>();
                msg.put("orderId", orderId);
                return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
    }

    //承保接口推送
    //推送过来就说明支付用户支付
    public Map<String, Object> chenbaopPushResult(String result) {
        try {
            log.info("承保推送：" + result);
            //System.out.println("承保推送："+result);
            JSONObject resObj = new JSONObject(result);
            if (resObj.getJSONObject("errorMsg").getString("code").equals("success")) {
                JSONObject resultobj = resObj.getJSONObject("data");
                String orderId = resultobj.getString("orderId");
                InsuranceOrder orderByOrderId = orderDao.getOrderByOrderId(orderId);

                //yonghu支付
                //更新订单状态
                orderByOrderId.setPayTime(System.currentTimeMillis());
                orderByOrderId.setState(INSURANCE.QUOTESTATE_CHENGBAOING.getCode());
                orderByOrderId.setStateString(INSURANCE.QUOTESTATE_CHENGBAOING.getName());


                boolean fail = false;
                if (fail) {
                    orderByOrderId.setState(INSURANCE.QUOTESTATE_CHENGBAOFAIL.getCode());
                    orderByOrderId.setStateString(INSURANCE.QUOTESTATE_CHENGBAOFAIL.getName());
                } else {
                    orderByOrderId.setState(INSURANCE.QUOTESTATE_NOTDELIVER.getCode());
                    orderByOrderId.setStateString(INSURANCE.QUOTESTATE_NOTDELIVER.getName());
                }

                long totalPrice = orderByOrderId.getTotalPrice();
                BigDecimal bg = new BigDecimal(CalcuateMoneyBack(totalPrice));
                float calBackMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                orderByOrderId.setBackmoney(String.valueOf(calBackMoney));
                int state = resultobj.getInt("state");
                String ciPolicyNo = resultobj.getString("ciPolicyNo");
                String biPolicyNo = resultobj.getString("biPolicyNo");


                orderDao.updateOrder(orderByOrderId);
                //同步更新Client中的状态
                clientService.updateClientQuoteState(orderByOrderId.getOpenId(), orderByOrderId.getLicenseNumber(), orderByOrderId.getState());

                /*
                 * 调用方法，将待配送信息推送给微信公众号
                 */
                Client client = clientDao.getClientByOpenIdAndLicenseNumber(orderByOrderId.getOpenId(), orderByOrderId.getLicenseNumber());
                if (null != client) {
                    String unionId = client.getUnionId();
                    TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);

                    if (null != tasUserInfo) {
                        String tasMessageSendResult = TasConfig.pushOrderForTheShippingInfo(orderByOrderId, tasUserInfo.getOpenId());
                        log.debug(tasMessageSendResult);
                    }
                }

                Map<String, Object> msg = new HashMap<>();
                msg.put("orderId", orderId);
                return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
    }

    //支付接口推送
    public Map<String, Object> payPushResult(String result) {
        try {
            //System.out.println(result);
            JSONObject resObj = new JSONObject(result);
            if (resObj.getJSONObject("errorMsg").getString("code").equals("success")) {
                JSONObject resultobj = resObj.getJSONObject("data");


                int state = resultobj.getInt("state");
                String orderId = resultobj.getString("orderId");
                String message = resultobj.getString("message");

                OrderpushResul qr = new OrderpushResul();
                qr.setState(state);
                orderpushResulDao.updateOrderpushResulBySpecifyId(qr, "orderId");

                Map<String, Object> msg = new HashMap<>();
                msg.put("orderId", orderId);
                return RESCODE.LUOTUO_SUCCESS.getLuoTuoRes(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RESCODE.LUOTUO_FAIL.getLuoTuoRes(false);
    }

    private float CalcuateMoneyBack(long totalPrice) {
        float rate = cashbackDao.listCashbackRate().get(0).getRate();
        float calReturn = totalPrice * rate;
        return calReturn;
    }

}