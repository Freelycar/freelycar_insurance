package com.freelycar.util;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.entity.InsuranceOrder;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小易爱车公众号的接口类
 * 注：主要用于车险小程序向小易爱车公众号推送模板消息等
 *
 * @author 唐炜
 */
public class TasConfig {

    public final static String LANG_CN = "zh_CN";
    public final static String LANG_TW = "zh_TW";
    public final static String LANG_EN = "en";
    public final static String WECHAT_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private final static Logger log = Logger.getLogger(TasConfig.class);
    private final static String TAS_APP_ID = "wxfd188f8284ee297b";
    private final static String TAS_SECRET = "70b5522f3ea3ab0071441efe33f37e6f";
    private final static String ORDER_STATUS_UPDATE_TEMPLATE_ID = "PeRe0M0iEbm7TpN6NOThhBUjwzy_aHsi6r2E7Pa8J1A";
    private final static String GET_TASACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + TAS_APP_ID + "&secret=" + TAS_SECRET;

    /**
     * 获取公众号的accessToken
     *
     * @return string
     */
    public static String getTasAccessToken() {
        JSONObject accessTokenJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(GET_TASACCESS_TOKEN_URL).toString());
        return accessTokenJSONObject.getString("access_token");
    }

    /**
     * 获取关注公众号的所有用户的openId列表
     *
     * @param accessToken 授权码
     * @param nextOpenId  开始的openId（不传就是从头开始）
     * @return list    openId集合
     */
    public static List<String> getTasUsers(String accessToken, String nextOpenId) {
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = getTasAccessToken();
        }
        if (null == nextOpenId) {
            nextOpenId = "";
        }
        String getTasUsersUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=" + nextOpenId;
        JSONObject tasUsersJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(getTasUsersUrl).toString());
        return tasUsersJSONObject.getJSONObject("data").getObject("openid", List.class);
    }

    /**
     * 获取关注公众号的所有用户的openId列表
     * 不需要传accessToken
     *
     * @param nextOpenId 开始的openId（不传就是从头开始）
     * @return list    openId集合
     */
    public static List<String> getTasUsers(String nextOpenId) {
        return getTasUsers(null, nextOpenId);
    }

    /**
     * 查询用户信息
     *
     * @param accessToken 授权码
     * @param openId      用户的openId
     * @param lang        返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语(非必填）
     * @return json    用户信息
     */
    public static JSONObject getUserInfo(String accessToken, String openId, String lang) {
        if (StringUtils.isEmpty(openId)) {
            log.error("调用方法getUserInfo失败：参数openId为空值！");
            return null;
        }
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = getTasAccessToken();
        }
        if (StringUtils.isEmpty(lang)) {
            lang = "";
        }
        String getTasUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=" + lang;
        JSONObject tasUsersJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(getTasUserInfoUrl).toString());
        return tasUsersJSONObject;
    }

    /**
     * 查询用户信息
     *
     * @param openId 用户的openId
     * @return json    用户信息
     */
    public static JSONObject getUserInfo(String openId) {
        return getUserInfo(null, openId, null);
    }


    /**
     * 获取用户信息集合
     *
     * @param openIds openId的集合
     * @return list
     */
    public static List getUserInfoByOpenIds(List<String> openIds) {
        List result = new ArrayList();
        if (null != openIds && !openIds.isEmpty()) {

        }
        return result;
    }

    /**
     * 发送微信模板消息
     *
     * @param params 模板消息参数
     * @return string  返回发送结果
     */
    private static String invokeTemplateMessage(JSONObject params) {
        //解决中文乱码问题
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        String result = HttpRequest.postCall(WECHAT_TEMPLATE_MESSAGE_URL + getTasAccessToken(), entity, null);
        log.debug("微信模版消息结果：" + result);
        return result;
    }

    /**
     * 推送待支付信息到公众号
     *
     * @param insuranceOrder 订单信息
     * @param tasOpenId      用户在公众号中的openId
     * @return string  发送接口调用结果
     */
    public static String pushOrderUnpaidInfo(InsuranceOrder insuranceOrder, String tasOpenId) {
        if (null == insuranceOrder) {
            log.error("推送待支付信息失败：参数insuranceOrder为NULL");
            return "推送待支付信息失败：参数insuranceOrder为NULL";
        }
        if (StringUtils.isEmpty(tasOpenId)) {
            log.error("推送待支付信息失败：参数tasOpenId为NULL");
            return "推送待支付信息失败：参数insuranceOrder为NULL";
        }
        Integer id = insuranceOrder.getId();
        String orderId = insuranceOrder.getOrderId();
        String orderStatus = insuranceOrder.getStateString();

        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", tasOpenId);
        params.put("template_id", ORDER_STATUS_UPDATE_TEMPLATE_ID);
        params.put("url", "https://www.freelycar.com/carInsurance/pay.html?id=" + id);
        data.put("first", keywordFactory("车险报价订单状态提醒", "#173177"));
        data.put("OrderSn", keywordFactory(orderId, "#173177"));
        data.put("OrderStatus", keywordFactory(orderStatus, "#173177"));
        data.put("remark", keywordFactory("点击“详情”跳转到订单详情及支付页面"));
        params.put("data", data);

        Map<String, Object> map = new HashMap<>(1);
        map.put("params", params.toString());

        return TasConfig.invokeTemplateMessage(params);
    }

    /**
     * 推送订单状态变更信息到公众号
     * 待配送、待签收、已投保
     * @param insuranceOrder 订单信息
     * @param tasOpenId      用户在公众号中的openId
     * @return string  发送接口调用结果
     */
    public static String pushOrderChangeInfo(InsuranceOrder insuranceOrder, String tasOpenId) {
        if (null == insuranceOrder) {
            log.error("推送订单状态变更信息失败：参数insuranceOrder为NULL");
            return "推送订单状态变更信息失败：参数insuranceOrder为NULL";
        }
        if (StringUtils.isEmpty(tasOpenId)) {
            log.error("推送订单状态变更信息失败：参数tasOpenId为NULL");
            return "推送订单状态变更信息失败：参数insuranceOrder为NULL";
        }
        Integer id = insuranceOrder.getId();
        String orderId = insuranceOrder.getOrderId();
        String orderStatus = insuranceOrder.getStateString();

        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", tasOpenId);
        params.put("template_id", ORDER_STATUS_UPDATE_TEMPLATE_ID);
        data.put("first", keywordFactory("车险报价订单状态提醒", "#173177"));
        data.put("OrderSn", keywordFactory(orderId, "#173177"));
        data.put("OrderStatus", keywordFactory(orderStatus, "#173177"));
        data.put("remark", keywordFactory("点击“详情”跳转到小程序订单详情页面"));
        params.put("data", data);

        JSONObject miniProgramJSON = new JSONObject();
        miniProgramJSON.put("appid", WechatConfig.APP_ID);
        //跳转到小程序的对应页面（需要小程序先发布）
//        miniProgramJSON.put("pagepath", "pages/orderDetail?id=" + id);
        miniProgramJSON.put("path", "pages/orderDetail?id=" + id);
        params.put("miniprogram", miniProgramJSON);

        Map<String, Object> map = new HashMap<>(1);
        map.put("params", params.toString());

        return TasConfig.invokeTemplateMessage(params);
    }

    private static JSONObject keywordFactory(String value) {
        JSONObject keyword = new JSONObject();
        keyword.put("value", value);
        return keyword;
    }

    private static JSONObject keywordFactory(String value, String color) {
        JSONObject keyword = keywordFactory(value);
        keyword.put("color", color);
        return keyword;
    }

}
