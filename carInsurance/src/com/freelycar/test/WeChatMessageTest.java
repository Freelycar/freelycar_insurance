package com.freelycar.test;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.util.Constant;
import com.freelycar.util.HttpClientUtil;
import org.junit.Test;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class WeChatMessageTest {

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
        moduleKeywords.put("keyword1",JSONObject.parse("{\"value\":\"109-20180522153620-8390a\"}"));
        moduleKeywords.put("keyword2",JSONObject.parse("{\"value\":\"http://res.xiaomar.com/app-imgs/webimage/img-20180522154101-af105.png\"}"));
        moduleKeywords.put("keyword3",JSONObject.parse("{\"value\":\"2018-5-31 23:59:59\"}"));
        moduleKeywords.put("keyword4",JSONObject.parse("{\"value\":\"车险\"}"));

        param.put("data", moduleKeywords);

        System.out.println(sendModelMessageUrl);
        System.out.println(param);
//        HttpClientUtil.httpPost(sendModelMessageUrl,param);
    }

}
