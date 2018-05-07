package com.freelycar.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.Client;
import com.freelycar.entity.ClientNOrder;
import com.freelycar.entity.Insurance;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.service.ClientService;
import com.freelycar.service.InsuranceService;
import com.freelycar.service.OrderService;
import com.freelycar.service.QuoteRecordService;
import com.freelycar.util.ExcelHandler;
import com.freelycar.util.Tools;
import com.freelycar.util.RESCODE;


@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private QuoteRecordService quoteRecordService;
	
	@Autowired
	private OrderService orderService;
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<String> daitoubao = Arrays.asList("车牌号码","车主姓名","手机号码","保险到期","报价时间","报价状态","来源渠道");
	private List<String> yitoubao = Arrays.asList("车牌号码","车主姓名","手机号码","保险到期","来源渠道","订单编号","订单时间","订单状态","付款时间","是否返现","返现金额","返现时间","配送时间","运单编号");
	
	@RequestMapping(value = "/exportClient",method = RequestMethod.GET)
    public Map<String,Object> exportClient(boolean toubao, HttpServletResponse response){
    	String unknwon = "未知";
    	boolean result = false;
		if(!toubao)
		{
	    	List<Client> clientList = clientService.getExportClientList(toubao);
			List<List<String>>totalList = new ArrayList<List<String>>();
			for(Client c: clientList)
			{
				List<String>tmpList = new ArrayList<String>();
				tmpList.add(c.getLicenseNumber());
				tmpList.add(c.getOwnerName());
				tmpList.add(c.getPhone());
				if(c.getInsuranceDeadline() != null)
					tmpList.add(format.format(new Date(c.getInsuranceDeadline())));
				else
					tmpList.add(unknwon);
				
			
				if(c.getLeastQueryTime() != null)
					tmpList.add(format.format(new Date(c.getLeastQueryTime())));
				else
					tmpList.add(unknwon);
				//0 核保中， 1核保失败， 2未支付
				int state = c.getQuoteStateCode();
				if(state == 0)
					tmpList.add("核保中");
				else if(state == 1)
					tmpList.add("核保失败");
				else if(state == 2)
					tmpList.add("未支付");
				else
					tmpList.add(unknwon);
				tmpList.add(c.getSource());
				totalList.add(tmpList);
			}
			
			HSSFWorkbook wb = ExcelHandler.getInstanceOfWb();
			ExcelHandler.generateExcelSheet(wb, daitoubao, totalList);
			result = ExcelHandler.downExcel(wb,response);
		}
		else
		{
			List<ClientNOrder> clientList = clientService.getExportClientOrderList(toubao);
			List<List<String>>totalList = new ArrayList<List<String>>();
			for(ClientNOrder nn: clientList)
			{
				Client c = nn.getClient();
				List<String>tmpList = new ArrayList<String>();
				tmpList.add(c.getLicenseNumber());
				tmpList.add(c.getOwnerName());
				tmpList.add(c.getPhone());
				if(c.getInsuranceDeadline() != null)
					tmpList.add(format.format(new Date(c.getInsuranceDeadline())));
				else
					tmpList.add(unknwon);
				tmpList.add(c.getSource());
				
				InsuranceOrder order = nn.getOrder();
				//订单编号
				tmpList.add(order.getOrderId());
				//订单时间
				if(order.getCreateTime() != null)
					tmpList.add(format.format(new Date(order.getCreateTime())));
				else
					tmpList.add(unknwon);
				//订单状态
				int state = order.getState();
				if(state == 0)
					tmpList.add("核保中");
				else if(state == 1)
					tmpList.add("核保失败");
				else if(state == 2)
					tmpList.add("未支付");
				else if(state == 3)
					tmpList.add("承保中");
				else if(state == 4)
					tmpList.add("承保失败");
				else if(state == 5)
					tmpList.add("待配送");
				else if(state == 6)
					tmpList.add("待签收");
				else if(state == 7)
					tmpList.add("已投保");
				else
					tmpList.add(unknwon);
				//付款时间
				if(order.getPayTime() != null)
					tmpList.add(format.format(new Date(order.getPayTime())));
				else
					tmpList.add(unknwon);
				//是否返现
				tmpList.add(order.getCashback() ?"已返现":"尚未返现"); 
				//"返现金额","返现时间","配送时间","运单编号","签收时间"
				tmpList.add(order.getBackmoney());
				if(order.getCashbackTime() != null)
					tmpList.add(format.format(new Date(order.getCashbackTime())));
				else
					tmpList.add(unknwon);
				if(order.getDeliveredTime() != null)
					tmpList.add(format.format(new Date(order.getDeliveredTime())));
				else
					tmpList.add(unknwon);
				
				
				totalList.add(tmpList);
			}
			
			HSSFWorkbook wb = ExcelHandler.getInstanceOfWb();
			ExcelHandler.generateExcelSheet(wb, yitoubao, totalList);
			result = ExcelHandler.downExcel(wb,response);
		}
		return result?RESCODE.SUCCESS.getJSONRES():RESCODE.NOT_FOUND.getJSONRES();
    }
}
