package com.freelycar.test;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.util.Constant;
import com.freelycar.util.HttpClientUtil;
import com.freelycar.util.WechatTemplateMessage;
import org.junit.Test;

import java.util.List;

public class WeChatMessageTest {

    public static void main(String[] args) {
        //测试UnionId，遍历对应用户
        String demoUnionId = "oypWW1CZQebTpndrg4YJEpMIquP8";
        String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxfd188f8284ee297b&secret=70b5522f3ea3ab0071441efe33f37e6f";
        JSONObject accessTokenJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(getAccessTokenUrl).toString());
        System.out.println(accessTokenJSONObject);
        String accessToken = accessTokenJSONObject.getString("access_token");

        String getTasUsersUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid";
        JSONObject tasUsersJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(getTasUsersUrl).toString());
        List<String> tasOpenIds = tasUsersJSONObject.getJSONObject("data").getObject("openid", List.class);
        System.out.println(tasOpenIds);

        String getTasUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=";
        for (String tasOpenId : tasOpenIds) {
            JSONObject tasUsersInfoJSONObject = JSONObject.parseObject(HttpClientUtil.httpGet(getTasUserInfoUrl + tasOpenId).toString());
            if (demoUnionId.equals(tasUsersInfoJSONObject.getString("unionid"))) {
                System.out.println(tasUsersInfoJSONObject);
                break;
            }
        }

    }

    /**
     * 单号
     * {{keyword1.DATA}}
     * <p>
     * 金额
     * {{keyword2.DATA}}
     * <p>
     * 下单时间
     * {{keyword3.DATA}}
     * <p>
     * 物品名称
     * {{keyword4.DATA}}
     */
    @Test
    public void payMessageSend() {
        String sendModelMessageUrl = Constant.getSendModelMessageUrl();
        JSONObject param = new JSONObject();
        param.put("touser", "oziV35OGJFtKcX3E_3P48bLfbrAw");
        param.put("template_id", "drbANk9wFCJGH-aXl_iPqBd0HtM156z1wypVa-phtYQ");
        param.put("form_id", "");

        JSONObject moduleKeywords = new JSONObject();
        moduleKeywords.put("keyword1", JSONObject.parse("{\"value\":\"109-20180522153620-8390a\"}"));
        moduleKeywords.put("keyword2", JSONObject.parse("{\"value\":\"http://res.xiaomar.com/app-imgs/webimage/img-20180522154101-af105.png\"}"));
        moduleKeywords.put("keyword3", JSONObject.parse("{\"value\":\"2018-5-31 23:59:59\"}"));
        moduleKeywords.put("keyword4", JSONObject.parse("{\"value\":\"车险\"}"));

        param.put("data", moduleKeywords);

        System.out.println(sendModelMessageUrl);
        System.out.println(param);
//        HttpClientUtil.httpPost(sendModelMessageUrl,param);
    }

    @Test
    public void pushOrderUnpaidInfo() {
        InsuranceOrder insuranceOrder = new InsuranceOrder();
        insuranceOrder.setOpenId("oyBUGw2I67HN6upQbV52lZAPilnM");
        insuranceOrder.setId(111);
        insuranceOrder.setOrderId("109-20180524172339-d1199");
        insuranceOrder.setStateString("未支付");
        WechatTemplateMessage.pushOrderUnpaidInfo(insuranceOrder);
    }

}
