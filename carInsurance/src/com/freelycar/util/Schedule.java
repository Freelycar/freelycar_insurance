package com.freelycar.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.freelycar.entity.InsuranceOrder;
import com.freelycar.service.OrderService;

@Component
@PropertySource("classpath:config.properties")
public class Schedule {

	@Autowired
	private OrderService service;
	
	private static final String BASEURL = Constant.INTERFACE_BASE_URL;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final boolean onoff = Constant.CONFIRM_CHENGBAO_SWITCH;
    @Scheduled(fixedRateString="${luotuo_confirmchengbao_rate}")
    public void reportCurrentTime() {
    	if(onoff){
    	   System.out.println("The time is now " + dateFormat.format(new Date()));
	       //查寻未支付的订单，调用承保变成已支付
	       InsuranceOrder order = new InsuranceOrder();
	       order.setState(INSURANCE.QUOTESTATE_NOTPAY.getCode());
	       //检查失效时间
	       order.setEffetiveTimeLong(System.currentTimeMillis());
	       List<InsuranceOrder> listOrder = service.listValidOrder(order);
	       
	       //20分钟去调用承保
	       for(InsuranceOrder inorder : listOrder){
			   Map<String,Object> obj = new HashMap<>();
			   obj.put("orderId", inorder.getOrderId());
			   HttpClientUtil.httpPost(BASEURL+"insurance/confirmChengbao", obj);
	       }
    		
    	}
    }
}
