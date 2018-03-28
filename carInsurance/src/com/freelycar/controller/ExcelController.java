package com.freelycar.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.freelycar.entity.Client;
import com.freelycar.entity.Insurance;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.service.ClientService;
import com.freelycar.service.InsuranceService;
import com.freelycar.service.OrderService;
import com.freelycar.service.QuoteRecordService;
import com.freelycar.util.ExcelHandler;
import com.freelycar.util.Tools;

@Component
@RequestMapping(value = "excel")
public class ExcelController {

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private QuoteRecordService quoteRecordService;
	
	@Autowired
	private OrderService orderService;
	
	
	private List<String> titles = Arrays.asList("车牌号码","车主姓名","手机号码","保险到期","报价时间","报价状态","报价编号","来源渠道","订单编号","订单时间","订单状态","付款时间","是否返现","返现金额","返现时间","配送时间","运单编号","签收时间");
	
	@RequestMapping(value = "/exportClient",method = RequestMethod.POST)
    public Map<String,Object> isExistByOpenId(@RequestBody Client client){
    	
		List<Client> listAllClient = clientService.listAllClient();
		
		for(Client c : listAllClient){
			List<String> row = new ArrayList<>();
			row.add(c.getLicenseNumber());
			row.add(c.getOwnerName());
			row.add(c.getPhone());
			
			//查询续保记录
			Insurance insuranceByNameLiceAndState = insuranceService.getInsuranceByNameLiceAndState(c.getOwnerName(), c.getLicenseNumber(), false);
			row.add(insuranceByNameLiceAndState.getInsuranceEndTime());//保险到期
			
			//最近报价时间
			QuoteRecord findLatestQuoteRecord = quoteRecordService.findLatestQuoteRecord(c.getOwnerName(), c.getLicenseNumber());
			row.add(Tools.date2Str(findLatestQuoteRecord.getCreateTime()));
			
			//报价状态
			row.add(findLatestQuoteRecord.getState()+"");
			row.add(findLatestQuoteRecord.getOfferId());
			
			//来源渠道
			row.add("小易");
			
			/*orderService.getOrderByOrderId(orderId)
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);
			row.add(c.get);*/
		}
		
		//ExcelHandler.generateExcel(titles, list, response);
		//return insuranceService.queryLastYear(client);
		return null;
		
    }
}
