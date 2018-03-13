package com.freelycar.service; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.InsuranceDao;
import com.freelycar.entity.Client;
import com.freelycar.entity.Insurance;
import com.freelycar.util.Constant;
import com.freelycar.util.HttpClientUtil;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;
/**  
 *  
 */  
@Transactional
@Service
public class InsuranceService  
{  
    /********** 注入InsuranceDao ***********/  
    @Autowired
	private InsuranceDao insuranceDao;
    
    private String LUOTUOKEY = Constant.LUOTUOKEY;
    
    public Map<String, Object> queryLastYear(Client client){
    	Map<String,Object> param = new HashMap<>();
    	param.put("licenseNumber", client.getLicenseNumber());
    	param.put("ownerName", client.getOwnerName());
    	JSONObject resultJson = HttpClientUtil.httpGet("http://wechat.bac365.com:8081/carRisk/car_risk/carApi/queryLatestPolicy?api_key="+LUOTUOKEY, param);
    	
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			JSONObject data = resultJson.getJSONObject("data");
    			JSONObject biInfo = data.getJSONObject("biInfo");
    			JSONObject ciInfo = data.getJSONObject("ciInfo");
    			JSONArray array = new JSONArray();
    			array.put(biInfo);
    			array.put(ciInfo);
    			
    			List<Insurance> result = new ArrayList<>();
    			for(int i=0;i<array.length();i++){
    				JSONObject info = array.getJSONObject(i);
    				
    				String policyNo = info.getString("policyNo");
    				String insuranceCompany = info.getString("companyName");
    				String insuranceBeginTime = info.getString("insuranceBeginTime");
    				String insuranceEndTime = info.getString("insuranceEndTime");
    				String insuranceJson = info.has("insuranceJson")?info.getString("insuranceJson"):null;
    				
    				Insurance insurance = new Insurance();
    				insurance.setPolicyNo(policyNo);
    				insurance.setCommercial(i==1);
    				insurance.setInsurances(insuranceJson);
    				insurance.setInsuranceBeginTime(insuranceBeginTime);
    				insurance.setInsuranceCompany(insuranceCompany);
    				insurance.setInsuranceEndTime(insuranceEndTime);
    				insurance.setTotalLicenseNumber(client.getLicenseNumber());
    				insurance.setTotalOpenId(client.getOpenId());
    				saveInsurance(insurance);
    				
    				result.add(insurance);
    			}
    			
    			return RESCODE.SUCCESS.getJSONRES(result);
    		}
    	}
    	
    	return RESCODE.NOT_FOUND.getJSONRES();
    	
    	
    }
    
    
    //查询价格
    public Map<String,Object> queryPrice(Client client, Insurance insurance,String cityCode,String cityName){
    	Map<String,Object> param = new HashMap<>();
    	param.put("licenseNumber", client.getLicenseNumber());
    	param.put("ownerName", client.getOwnerName());
    	param.put("cityCode", cityCode);
    	param.put("cityName", cityName);
    	param.put("insuranceCompanyName", insurance.getInsuranceCompany());//保险公司编号多加用逗号分隔
    	param.put("insurancesList", insurance.getInsurances());
    	param.put("insuranceStartTime", insurance.getInsuranceBeginTime());
    	param.put("forceInsuranceStartTime", insurance.getForceInsuranceStartTime());
    	param.put("transferDate", client.getTransfer()?client.getTransferTime():0);
    	param.put("requestHeader", Tools.uuid()+"HLDD");
    	
    	JSONObject resultJson = HttpClientUtil.httpGet("http://wechat.bac365.com:8081/carRisk/car_risk/carApi/ createEnquiry?api_key="+LUOTUOKEY, param);
    	
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			return RESCODE.SUCCESS.getJSONRES();
    		}
    	}
    	return RESCODE.FAIL.getJSONRES();
    	
    	
	}
	
    //增加一个Insurance
    public Map<String,Object> saveInsurance(Insurance insurance){
    	insuranceDao.saveInsurance(insurance);
    	return RESCODE.SUCCESS.getJSONRES();
    }
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listInsurance(Insurance insurance, int page,int number){
	    int from = (page-1)*number;
	    List<Insurance> listPage = insuranceDao.listInsurance(insurance,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = insuranceDao.getInsuranceCount(insurance);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除Insurance
	public Map<String,Object> removeInsuranceById(String id){
		boolean res =  insuranceDao.removeInsuranceById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新Insurance
	public Map<String,Object> updateInsurance(Insurance insurance){
	    insuranceDao.updateInsurance(insurance);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}