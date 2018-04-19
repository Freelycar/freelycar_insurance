package com.freelycar.service; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.freelycar.dao.CashbackRecordDao;
import com.freelycar.dao.ClientDao;
import com.freelycar.dao.InsuranceDao;
import com.freelycar.dao.InvoiceInfoDao;
import com.freelycar.dao.OrderDao;
import com.freelycar.dao.QuoteRecordDao;
import com.freelycar.dao.ReciverDao;
import com.freelycar.entity.CashbackRecord;
import com.freelycar.entity.Client;
import com.freelycar.entity.Insurance;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.InvoiceInfo;
import com.freelycar.entity.QuoteRecord;
import com.freelycar.entity.Reciver;
import com.freelycar.util.Constant;
import com.freelycar.util.HttpClientUtil;
import com.freelycar.util.INSURANCE;
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
    
    @Autowired
    private  OrderDao orderDao;
    
    @Autowired
    private QuoteRecordDao qrdao;
    
    @Autowired
    private ClientDao clientDao;
    
    @Autowired
    private ReciverDao reciverDao;
    
    @Autowired
    private InvoiceInfoDao invoiceInfoDao;
    
    @Autowired
    private CashbackRecordDao cashbackDao;
    
    private static final String LUOTUOKEY = Constant.LUOTUOKEY;
    private static final String LUOTUO_INTERFACE_BASEURL = Constant.LUOTUO_INTERFACE_URL;
    
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public Map<String, Object> queryLastYear(Client client){
    	if(Tools.isEmpty(client.getOpenId())){
    		return RESCODE.USER_OPENID_EMPTY.getJSONRES();
    	}
    	if(Tools.isEmpty(client.getOwnerName())){
    		return RESCODE.USER_NAME_EMPTY.getJSONRES();
    	}
    	if(Tools.isEmpty(client.getLicenseNumber())){
    		return RESCODE.USER_LICENSENUMBER_EMPTY.getJSONRES();
    	}
    	
    	
    	//先去查是不是老用户
    	List<Client> clientByOpenIdList = clientDao.getClientByOpenIdList(client.getOpenId());
    	if(clientByOpenIdList.isEmpty()){
    		return RESCODE.USER_NO_PHONE.getJSONRES();
    	}
    	
    	//这里假如他使用别的人信息去查 用自己的手机号查询续保
    	//循环出来 已有的phone
    	for(Client c : clientByOpenIdList){
    		if(Tools.notEmpty(c.getPhone())){
    			client.setPhone(c.getPhone());
    			break;
    		}
    	}
    	
    	
    	Map<String,Object> param = new HashMap<>();
    	param.put("api_key", LUOTUOKEY);
    	param.put("ownerName", client.getOwnerName());
    	param.put("licenseNumber", client.getLicenseNumber());
    	param.put("carTypeCode", Tools.isEmpty(client.getCarTypeCode())?"02":client.getCarTypeCode());
    	
    	JSONObject resultJson = HttpClientUtil.httpGet(LUOTUO_INTERFACE_BASEURL+"queryLatestPolicy", param);
    	
    	
    	System.out.println("查询续保结果："+resultJson);
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			JSONObject data = null;
				try { data = new JSONObject(resultJson.getString("data"));
				} catch (JSONException e) { 
					e.printStackTrace();
					return RESCODE.LUOTUO_REPONSE_ERROR.getJSONRES();
				}
				
				
				/*if(data.length()==0){
					return RESCODE.USER_NAME_LICENSENUMBER_NOT_FOUND.getJSONRES();
				}*/
				
    			JSONArray array = new JSONArray();
    			if(data.has("ciInfo")){//交强险
    				JSONObject ciInfo = data.getJSONObject("ciInfo");
    				array.put(ciInfo);
    			}else{
    				//没有交强险
    				System.out.println("查询不到交强险.........");
    				return RESCODE.REQUEST_BAOJIA_EXCEPTION.getJSONRES("查询不到交强险");
    			}
    			
    			if(data.has("biInfo")){//商业险 
    				JSONObject biInfo = data.getJSONObject("biInfo");
    				array.put(biInfo);
    			}
    			List<Insurance> result = new ArrayList<>();
    			for(int i=0;i<array.length();i++){
    				JSONObject info = array.getJSONObject(i);
    				
    				String policyNo = info.getString("policyNo");
    				String insuranceCompany = info.getString("insuranceCompany");
    				int insuranceCompanyId = info.getInt("insuranceCompanyId");
    				String insuranceBeginTime = info.getString("insuranceBeginTime");
    				String insuranceEndTime = info.getString("insuranceEndTime");
    				try {
    					insuranceEndTime = String.valueOf(format.parse(insuranceEndTime).getTime()/1000);
    					if(i==0){
    						System.out.println("交强险时间：>>>"+insuranceEndTime);
    					}else{
    						System.out.println("商业险时间：>>>"+insuranceEndTime);
    					}
					} catch (ParseException e) {
						e.printStackTrace();
					}
    				
    				String totalAmount = info.getString("totalAmount");
    				String insuranceJson = info.has("insurances")?info.getJSONArray("insurances").toString():null;
    				
    				//根据车主姓名和车牌 保持只有一条查询续保记录
    				synchronized (InsuranceService.class) {
    					Insurance insurance = insuranceDao.getInsuranceByNameLiceAndState(client.getOwnerName(), client.getLicenseNumber(), i==1);
    					if(insurance == null){
    						insurance = new Insurance();
    					}
    					
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
    					insurance.setPrice(totalAmount);
    					saveUpdateInsurance(insurance);
    					result.add(insurance);
    				}
    			}
    			
    			
    			//证明用户身份真实有效
    			synchronized (InsuranceService.class) {
    				Client exist = clientDao.getClientByOpenIdAndLicenseNumber(client.getOpenId(),client.getLicenseNumber());
    				if(exist == null){
    					//x询价的时候 插入客户的信息//初始状态 呆报价
    					clientDao.saveClient(client);
    				}else{
    					//数据库有数据 填充其他信息 这边要注意
    					exist.setOwnerName(client.getOwnerName());
    					//exist.setQuoteState(INSURANCE.QUOTESTATE_NO_YIBAOJIA.getCode()+"");
    					clientDao.saveClient(exist);
    				}
    				
    				return RESCODE.SUCCESS.getJSONRES(result);
    			}
    			
    		}
    	}
    	
    	return RESCODE.NOT_FOUND.getJSONRES();
    	
    	
    }
    
    
    //查询价格
    public Map<String,Object> queryPrice(Insurance.QueryPriceEntity entity){
    	Map<String,Object> param = new HashMap<>();
    	param.put("api_key", LUOTUOKEY);
    	
    	//通过openId 查手机号
    	Client clientByOpenId = clientDao.getClientByOpenIdAndLicenseNumber(entity.getOpenId(), entity.getLicenseNumber());
    	
    	if(clientByOpenId == null){
    		return RESCODE.SMS_PHONE_EMPTY.getJSONRES("没有openId为"+entity.getOpenId()+"的用户");
    	}
    	
    	if(Tools.isEmpty(clientByOpenId.getPhone())){
    		return RESCODE.SMS_PHONE_EMPTY.getJSONRES("没有openId为"+entity.getOpenId()+"的用户没有手机号");
    	}
    	
    	param.put("mobilePhone", clientByOpenId.getPhone());
    	
    	JSONObject createEnquiryParams = new JSONObject();
    	createEnquiryParams.put("licenseNumber", entity.getLicenseNumber());//
    	createEnquiryParams.put("carTypeCode", Tools.isEmpty(entity.getCarTypeCode())?"02":entity.getCarTypeCode());//
    	createEnquiryParams.put("ownerName", entity.getOwnerName());//
    	createEnquiryParams.put("cityCode", entity.getCityCode());//前端写死
    	//obj.put("cityName", cityName);//cityName可以不传
    	createEnquiryParams.put("insuranceCompanyName", entity.getInsuranceCompanyId());//保险公司编号多加用逗号分隔
    	createEnquiryParams.put("insuranceStartTime", Tools.isEmpty(entity.getInsuranceStartTime())?0:entity.getInsuranceStartTime());//
    	
    	String forceInsuranceStartTime = entity.getForceInsuranceStartTime();
    	createEnquiryParams.put("forceInsuranceStartTime", forceInsuranceStartTime);//
    	createEnquiryParams.put("transferDate", !entity.isTransfer()?"0":entity.getTransferTime());//
    	String requestHeader = Tools.uuid()+"HLDD";
    	createEnquiryParams.put("requestHeader", requestHeader);//
    	
    	
    	JSONArray insurancesList = new JSONArray();
    	JSONObject fangan1 = new JSONObject();
    	fangan1.put("schemeName", "方案1");
    	JSONObject forcePremium = new JSONObject();
    	forcePremium.put("isToubao", "1");
    	fangan1.put("forcePremium", forcePremium);
		try {
			fangan1.put("insurances", new JSONArray(entity.getInsurances()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	insurancesList.put(fangan1);
    	
    	createEnquiryParams.put("insurancesList", insurancesList);
    	param.put("createEnquiryParams", createEnquiryParams);
    	
    	for(Map.Entry<String, Object> p : param.entrySet()){
    		System.out.println(p.getKey()+"------"+p.getValue());
    	}
    	
    	JSONObject resultJson = HttpClientUtil.httpGet(LUOTUO_INTERFACE_BASEURL+"createEnquiry", param);
    	
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			//询价成功
    			
    			QuoteRecord qr = new QuoteRecord();
    			qr.setOpenId(entity.getOpenId());
    			qr.setCityCode(entity.getCityCode());
    			qr.setCityName(entity.getCityName());
    			qr.setClientId(clientByOpenId.getId());
    			qr.setCrateTime(System.currentTimeMillis());
    			
    			if(Tools.notEmpty(forceInsuranceStartTime)){
    				qr.setForceInsuranceStartTime(Integer.parseInt(forceInsuranceStartTime));
    			}
    			
    			qr.setInsuranceCompanyName("人保车险");
    			qr.setInsurances(entity.getInsurances());
    			qr.setInsuranceStartTime(Integer.parseInt(entity.getForceInsuranceStartTime()));
    			qr.setLicenseNumber(entity.getLicenseNumber());
    			qr.setOwnerName(entity.getOwnerName());
    			qr.setRequestHeader(requestHeader);
    			qr.setTransferDate(0);
    			qrdao.saveQuoteRecord(qr);
    			return RESCODE.SUCCESS.getJSONRES();
    		}
    	}
    	return RESCODE.FAIL.getJSONRES();
	}
    
    
    //提交核保
    public Map<String,Object> submitProposal(Insurance.ProposalEntity entity){
    	System.out.println("核保的参数"+entity.toString());
    	
    	if(Tools.isEmpty(entity.getOpenId())){
    		return RESCODE.USER_OPENID_EMPTY.getJSONRES();
    	}
    	
    	
    	//增加/更新收款人信息
    	Reciver reciverByOpenId = reciverDao.getReciverByOrderId(entity.getOfferId());
    	if(reciverByOpenId ==null ){
    		reciverByOpenId = new Reciver();
    	}
    	reciverByOpenId.setOrderId(entity.getOfferId());
    	reciverByOpenId.setOpenId(entity.getOpenId());
    	reciverByOpenId.setPhone(entity.getReciverPhone());
    	reciverByOpenId.setProvincesCities(entity.getProvincesCities());
    	reciverByOpenId.setAdressDetail(entity.getAddressDetail());
    	reciverDao.saveUpdateReciver(reciverByOpenId);
    	
    	//更新车主的idcard
    	if(Tools.isEmpty(entity.getLicenseNumber())){
    		return RESCODE.USER_LICENSENUMBER_EMPTY.getJSONRES();
    	}
    	
    	Client clientByOpenId = clientDao.getClientByOpenIdAndLicenseNumber(entity.getOpenId(), entity.getLicenseNumber());
    	if(clientByOpenId == null){
    		return RESCODE.USER_NOT_EXIST.getJSONRES();
    	}
    	clientByOpenId.setLicenseNumber(entity.getLicenseNumber());
    	
    	
    	
    	//收款信息
    	CashbackRecord cashbackRecordByOpenId = cashbackDao.getCashbackRecordByOrderId(entity.getOfferId());
    	if(cashbackRecordByOpenId ==null ){
    		cashbackRecordByOpenId = new CashbackRecord();
    	}
    	cashbackRecordByOpenId.setOrderId(entity.getOfferId());
    	cashbackRecordByOpenId.setOpenId(entity.getOpenId());
    	cashbackRecordByOpenId.setAccount(entity.getAccount());
    	cashbackRecordByOpenId.setBankname(entity.getBankname());
    	cashbackRecordByOpenId.setPayee(entity.getPayee());
    	cashbackDao.saveUpdateCashbackRecord(cashbackRecordByOpenId);
    	
    	//发票信息
    	InvoiceInfo invoiceInfoByOpenId = invoiceInfoDao.getInvoiceInfoByOpenId(entity.getOpenId());
    	if(invoiceInfoByOpenId ==null ){
    		invoiceInfoByOpenId = new InvoiceInfo();
    	}
    	invoiceInfoByOpenId.setOpenId(entity.getOpenId());
    	invoiceInfoByOpenId.setInvoiceTitle(entity.getInvoiceTitle());
    	invoiceInfoByOpenId.setInvoiceType(entity.getInvoiceType());
    	invoiceInfoByOpenId.setPhone(entity.getInvoicePhone());
    	invoiceInfoDao.saveUpdateInvoiceInfo(invoiceInfoByOpenId);
    	
    	
    	Map<String,Object> param = new HashMap<>();
    	param.put("api_key", LUOTUOKEY);
    	
    	JSONObject params = new JSONObject();
    	System.out.println("#######"+entity.getOfferId());
    	params.put("orderId", entity.getOfferId());/////
    	params.put("insuredName", entity.getOwnerName());/////
    	params.put("insuredIdNo", entity.getIdCard());/////
    	params.put("insuredPhone", entity.getPhone());////
    	params.put("customerName", entity.getOwnerName());////
    	params.put("customerPhone", entity.getPhone());/////
    	params.put("customerIdNo", entity.getIdCard());/////
    	params.put("contactName", entity.getOwnerName());////
    	params.put("contactPhone", entity.getPhone());////
    	
    	JSONObject address = new JSONObject();
    	address.put("acceptProvince", "");
    	address.put("contactAddressDetail",entity.getProvincesCities()+ entity.getAddressDetail());
    	address.put("address", entity.getAddressDetail());
    	address.put("acceptProvinceName", entity.getProvincesCities());
    	params.put("contactAddress", address.toString());/////
    	params.put("imageJson", "");///
    	
    	
    	JSONObject invoiceInfo = new JSONObject();
    	invoiceInfo.put("isInvoice", "0");
    	params.put("invoiceInfo", invoiceInfo.toString());////
    	
    	params.put("ownerIdCard", entity.getIdCard());//////
    	params.put("ownerMobilePhone", entity.getPhone());//
    	
    	param.put("params", params);
    	
    	
    	for(Map.Entry<String, Object> p : param.entrySet()){
    		System.out.println(p.getKey()+"------"+p.getValue());
    	}
    	JSONObject resultJson = HttpClientUtil.httpGet(LUOTUO_INTERFACE_BASEURL+"submitProposal", param);
    	
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			//提交核保成功
    			System.out.println("提交核保成功");
    			
    			//保证同一个offerId 生成一个订单
    			synchronized (InsuranceService.class) {
    				boolean save = false;
    				InsuranceOrder inorder = orderDao.getOrderByOrderId(entity.getOfferId());
    				if(inorder == null){
    					save = true;
    					inorder = new InsuranceOrder();
    				}
    				inorder.setCreateTime(System.currentTimeMillis());
    				inorder.setInsuredIdNo(entity.getIdCard());
    				inorder.setInsuredPhone(entity.getPhone());
    				inorder.setInsureName(entity.getOwnerName());
    				inorder.setLicenseNumber(entity.getLicenseNumber());
    				inorder.setOrderId(entity.getOfferId());
    				inorder.setOpenId(entity.getOpenId());
    				inorder.setState(INSURANCE.QUOTESTATE_HEBAOING.getCode());
    				inorder.setStateString(INSURANCE.QUOTESTATE_HEBAOING.getName());
    				inorder.setCreateTime(System.currentTimeMillis());
    				String saveId = orderDao.saveUpdateOrder(inorder,save);
    				return RESCODE.SUCCESS.getJSONRES(save?saveId:inorder.getId());
				}
    			
    			
    		}
    	}
    	return RESCODE.FAIL.getJSONRES();
    }
    
    
    //7、确认是否承保接口
    public Map<String,Object> confirmChengbao(String orderId){
    	
    	Map<String,Object> param = new HashMap<>();
    	param.put("api_key", LUOTUOKEY);
    	System.out.println("##确认是否承保接口#####"+orderId);
    	param.put("orderId", orderId);
    	JSONObject resultJson = HttpClientUtil.httpGet(LUOTUO_INTERFACE_BASEURL+"confirmChengbao", param);
    	
    	if(resultJson.has("errorMsg")){
    		String msg = resultJson.getJSONObject("errorMsg").getString("code");
    		if("success".equals(msg)){
    			//确认承保成功示例
    			
    			return RESCODE.SUCCESS.getJSONRES();
    		}else{
    			//请求失败
    			return RESCODE.FAIL.getJSONRES(msg);
    		}
    	}
    	return RESCODE.FAIL.getJSONRES();
    }
    
    
    //根据姓名 车牌 和商业险还是交强险来定
    public Insurance getInsuranceByNameLiceAndState(String ownerName,String licenseNumber,boolean commercial){
    	Insurance insurance = insuranceDao.getInsuranceByNameLiceAndState(ownerName, licenseNumber, commercial);
    	return insurance;
    }
    
	
    //增加一个Insurance
    public Map<String,Object> saveInsurance(Insurance insurance){
    	insuranceDao.saveInsurance(insurance);
    	return RESCODE.SUCCESS.getJSONRES();
    }
	
    //增加一个Insurance
    public Map<String,Object> saveUpdateInsurance(Insurance insurance){
    	insuranceDao.saveUpdateInsurance(insurance);
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