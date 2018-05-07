package com.freelycar.controller; 

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freelycar.entity.Client;
import com.freelycar.entity.Insurance;
import com.freelycar.service.InsuranceService;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/insurance")
public class InsuranceController  
{  
    /********** 注入InsuranceService ***********/  
    @Autowired
	private InsuranceService insuranceService;

    /**
     * 查询续保信息
     * @param client
     * @return
     */
    @RequestMapping(value = "/queryLastYear",method = RequestMethod.POST)
    public Map<String, Object> queryLastYear(@RequestBody Client client){
    	return insuranceService.queryLastYear(client);
    }
    
    /**
     * 询价
     * @param client
     * @param insurance
     * @param cityCode
     * @param cityName
     * @return
     */
    @RequestMapping(value="/queryPrice",method = RequestMethod.POST)
    public Map<String, Object> queryPrice(@RequestBody Insurance.QueryPriceEntity entity){
    	return insuranceService.queryPrice(entity);
    }
    
    /**
     * 核保
     * @return
     */
    @RequestMapping(value="/submitProposal",method = RequestMethod.POST)
    public Map<String, Object> submitProposal(@RequestBody Insurance.ProposalEntity entity){
    	return insuranceService.submitProposal(entity);
    }
    
    
    /**
     * 7、确认是否承保接口
     * @param client
     * @param quoteRecord
     * @return
     */
    @RequestMapping(value="/confirmChengbao",method = RequestMethod.POST)
    public Map<String, Object> confirmChengbao(String orderId){
    	return insuranceService.confirmChengbao(orderId);
    }
    
    @RequestMapping(value = "/test1")
    public String queryPrice(){
    	return "hello";
    }
    
    
    //增加一个Insurance
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveInsurance(Insurance insurance){
		return insuranceService.saveInsurance(insurance);
	}
	
	//查询所有的Insurance	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listInsurance(Insurance insurance, int _pageNumber,int _pageSize){
		return insuranceService.listInsurance(insurance, _pageNumber, _pageSize);
	}
	
	//根据id删除Insurance
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeInsuranceById(String id){
		return insuranceService.removeInsuranceById(id);
	}
	
	//更新Insurance
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateInsurance(Insurance insurance){
	    return insuranceService.updateInsurance(insurance);
	}
    
}