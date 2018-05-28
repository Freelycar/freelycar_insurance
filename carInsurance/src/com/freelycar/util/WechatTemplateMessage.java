package com.freelycar.util;

import com.freelycar.entity.InsuranceOrder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.freelycar.util.Constant.XIAOYIAICHE_URL;

public class WechatTemplateMessage {

    //模版请求地址
    public final static String WECHAT_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private static final String PAY_SUCCESS_TEMPLATE_ID = "F5CyYJ5u9_1wRcapK1ECOYRrjxcLcL3rjB0xUg_VQn0";
    private static final String PAY_FAIL_ID = "o0TOjg7KkxoL4CtQ91j--TVVmxYDSNk-dLWqoUVd8mw";

    //	private static final Logger log = Logger.getLogger(WechatTemplateMessage.class);
    private static final Logger log = LogManager.getLogger(WechatTemplateMessage.class);

    private static final String PAY_ERROR_DATABASE_FAIL = "服务异常";

    private static void invokeTemplateMessage(JSONObject params) {
        StringEntity entity = new StringEntity(params.toString(), "utf-8"); //解决中文乱码问题
        try {
            HttpClientUtil.httpPost(WECHAT_TEMPLATE_MESSAGE_URL +
                    WechatConfig.getAccessTokenForInteface().getString("access_token"), new JSONObject());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //{{first.DATA}}
//类型：{{keyword1.DATA}}
//金额：{{keyword2.DATA}}
//状态：{{keyword3.DATA}}
//时间：{{keyword4.DATA}}
//备注：{{keyword5.DATA}}
//{{remark.DATA}}
    public static void paySuccess() {
        log.debug("准备支付成功模版消息。。。");
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", "xxxxxxxxxx");
        params.put("template_id", PAY_SUCCESS_TEMPLATE_ID);
//		params.put("url", "http://www.geariot.com/fitness/class.html");
        data.put("first", keywordFactory("支付成功", "#173177"));
        data.put("keyword3", keywordFactory("成功", "#173177"));
        data.put("keyword4", keywordFactory("", "#173177"));
        data.put("keyword5", keywordFactory(""));
        data.put("remark", keywordFactory(""));
        params.put("data", data);
        invokeTemplateMessage(params);
//		return result;
    }


//	{{first.DATA}}
//	支付金额：{{keyword1.DATA}}
//	商品信息：{{keyword2.DATA}}
//	失败原因：{{keyword3.DATA}}
//	{{remark.DATA}}
	/*public static void errorCancel(){
		log.debug("支付成功，数据库更新失败！");
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		params.put("touser", wxPayOrder.getId());
		params.put("template_id", PAY_FAIL_ID);
		
		data.put("first", keywordFactory("支付失败", "#173177"));
		data.put("keyword1", keywordFactory((float)(Math.round(wxPayOrder.getTotalPrice()*100))/100 + "元", "#173177"));
		data.put("keyword2", keywordFactory(wxPayOrder.getProductName(), "#173177"));
		data.put("keyword3", keywordFactory("服务异常", "#173177"));
		data.put("remark", keywordFactory("请妥善保存单号，联系客服人员"));
		params.put("data", data);
		String result = invokeTemplateMessage(params);
		log.debug("微信支付失败结果：" + result);
	}*/
	
	/*private static String getConsumOrderProductName(ConsumOrder consumOrder){
		String productName = "";
		for(ProjectInfo projectInfo : consumOrder.getProjects())
			productName+=projectInfo.getName();
		return productName;
	}*/

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


    /**
     * {{first.DATA}}
     * 订单编号: {{OrderSn.DATA}}
     * 订单状态: {{OrderStatus.DATA}}
     * {{remark.DATA}}
     */
    public static void pushOrderUnpaidInfo(InsuranceOrder insuranceOrder) {
        if (null == insuranceOrder) {
            log.error("推送待支付信息失败：参数insuranceOrder为NULL");
        }
        String openId = insuranceOrder.getOpenId();
        Integer id = insuranceOrder.getId();
        String orderId = insuranceOrder.getOrderId();
        String orderStatus = insuranceOrder.getStateString();


        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("url", "https://www.howmuchweb.com/carInsurance/pay.html?id=" + id);
        data.put("first", keywordFactory("车险报价订单状态提醒", "#173177"));
        data.put("OrderSn", keywordFactory(orderId, "#173177"));
        data.put("OrderStatus", keywordFactory(orderStatus, "#173177"));
        data.put("remark", keywordFactory("点击“详情”跳转到订单详情及支付页面"));
        params.put("data", data);

        System.out.println(XIAOYIAICHE_URL + "wechatTemplateMessage/unpaidMessage");
        System.out.println(params);

        Map<String,Object> map = new HashMap<>(1);
        map.put("params", params.toString());

        HttpClientUtil.httpPost(XIAOYIAICHE_URL + "wechatTemplateMessage/unpaidMessage", map);

    }

}
