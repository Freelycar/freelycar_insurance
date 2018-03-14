package com.freelycar.service; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
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
    	param.put("api_key", LUOTUOKEY);
    	param.put("ownerName", client.getOwnerName());
    	param.put("licenseNumber", client.getLicenseNumber());
    	JSONObject resultJson = HttpClientUtil.httpGet("http://wechat.bac365.com:8081/carRisk/car_risk/carApi/queryLatestPolicy", param);
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			
    			JSONObject data = null;
				try { data = new JSONObject(resultJson.getString("data"));
				} catch (JSONException e) { e.printStackTrace();
				}
				
    			JSONArray array = new JSONArray();
    			if(data.has("biInfo")){//商业险 
    				JSONObject biInfo = data.getJSONObject("biInfo");
    				array.put(biInfo);
    			}
    			if(data.has("ciInfo")){//商业险 
    				JSONObject ciInfo = data.getJSONObject("ciInfo");
    				array.put(ciInfo);
    			}
    			List<Insurance> result = new ArrayList<>();
    			for(int i=0;i<array.length();i++){
    				JSONObject info = array.getJSONObject(i);
    				
    				String policyNo = info.getString("policyNo");
    				String insuranceCompany = info.getString("insuranceCompany");
    				int insuranceCompanyId = info.getInt("insuranceCompanyId");
    				String insuranceBeginTime = info.getString("insuranceBeginTime");
    				String insuranceEndTime = info.getString("insuranceEndTime");
    				String insuranceJson = info.has("insurances")?info.getJSONArray("insurances").toString():null;
    				
    				Insurance insurance = new Insurance();
    				insurance.setPolicyNo(policyNo);
    				insurance.setCommercial(i==1);
    				insurance.setInsurances(insuranceJson);
    				insurance.setInsuranceBeginTime(insuranceBeginTime);
    				insurance.setInsuranceCompany(insuranceCompany);
    				insurance.setInsuranceCompanyId(insuranceCompanyId);
    				insurance.setInsuranceEndTime(insuranceEndTime);
    				insurance.setTotalLicenseNumber(client.getLicenseNumber());
    				insurance.setLicenseNumber(client.getLicenseNumber());
    				insurance.setOwnerName(client.getOwnerName());
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
    	param.put("api_key", LUOTUOKEY);
    	param.put("mobilePhone", client.getPhone());
    	
    	JSONObject createEnquiryParams = new JSONObject();
    	createEnquiryParams.put("licenseNumber", client.getLicenseNumber());//
    	createEnquiryParams.put("ownerName", client.getOwnerName());//
    	createEnquiryParams.put("cityCode", cityCode);//
    	//obj.put("cityName", cityName);//cityName可以不传
    	createEnquiryParams.put("insuranceCompanyName", insurance.getInsuranceCompanyId());//保险公司编号多加用逗号分隔
    	createEnquiryParams.put("insuranceStartTime", Tools.isEmpty(insurance.getInsuranceBeginTime())?0:insurance.getInsuranceBeginTime());//
    	createEnquiryParams.put("forceInsuranceStartTime", insurance.getForceInsuranceStartTime());//
    	createEnquiryParams.put("transferDate", !client.getTransfer()?"0":client.getTransferTime());//
    	createEnquiryParams.put("requestHeader", Tools.uuid()+"HLDD");//
    	
    	
    	JSONArray insurancesList = new JSONArray();
    	JSONObject fangan1 = new JSONObject();
    	fangan1.put("schemeName", "方案1");
    	JSONObject forcePremium = new JSONObject();
    	forcePremium.put("isToubao", "1");
    	fangan1.put("forcePremium", forcePremium);
		try {
			fangan1.put("insurances", new JSONArray(insurance.getInsurances()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	insurancesList.put(fangan1);
    	
    	createEnquiryParams.put("insurancesList", insurancesList);
    	param.put("createEnquiryParams", createEnquiryParams);
    	
    	for(Map.Entry<String, Object> p : param.entrySet()){
    		System.out.println(p.getKey()+"------"+p.getValue());
    	}
    	
    	JSONObject resultJson = HttpClientUtil.httpGet("http://wechat.bac365.com:8081/carRisk/car_risk/carApi/createEnquiry", param);
    	
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