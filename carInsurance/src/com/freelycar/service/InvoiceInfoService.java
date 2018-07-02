package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.InvoiceInfoDao;
import com.freelycar.entity.InvoiceInfo;
import com.freelycar.util.RESCODE;
import com.freelycar.util.Tools;
import org.springframework.util.StringUtils;

/**  
 *  
 */  
@Transactional
@Service
public class InvoiceInfoService  
{  
    /********** 注入InvoiceInfoDao ***********/  
    @Autowired
	private InvoiceInfoDao invoiceInfoDao;
    
    
    
    
    
    //增加一个InvoiceInfo
    public Map<String,Object> saveInvoiceInfo(InvoiceInfo invoiceInfo){
		if(Tools.isEmpty(invoiceInfo.getInvoiceTitle())){
			return RESCODE.INVOICE_TITLE_EMPTY.getJSONRES();
		}
    	
		if(Tools.isEmpty(invoiceInfo.getInvoiceType())){
			return RESCODE.INVOICE_TYPE_EMPTY.getJSONRES();
		}
    	
		if(Tools.isEmpty(invoiceInfo.getPhone())){
			return RESCODE.INVOICE_PHONE_EMPTY.getJSONRES();
		}
		
		if(invoiceInfo.getClientId()==null ||invoiceInfo.getClientId() == 0){
			return RESCODE.USER_NOT_EXIST.getJSONRES();
		}
    	
		invoiceInfoDao.saveInvoiceInfo(invoiceInfo);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
    
    
    public Map<String, Object> getInvoiceInfoByOpenId(String openId){
    	InvoiceInfo invoiceInfoByOpenId = invoiceInfoDao.getInvoiceInfoByOpenId(openId);
    	if(invoiceInfoByOpenId != null){
    		return RESCODE.SUCCESS.getJSONRES(invoiceInfoByOpenId);
    	}
    	return RESCODE.NOT_FOUND.getJSONRES(invoiceInfoByOpenId);
    }
    
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listInvoiceInfo(InvoiceInfo invoiceInfo, int page,int number){
	    int from = (page-1)*number;
	    List<InvoiceInfo> listPage = invoiceInfoDao.listInvoiceInfo(invoiceInfo,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = invoiceInfoDao.getInvoiceInfoCount(invoiceInfo);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除InvoiceInfo
	public Map<String,Object> removeInvoiceInfoById(String id){
		boolean res =  invoiceInfoDao.removeInvoiceInfoById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新InvoiceInfo
	public Map<String,Object> updateInvoiceInfo(InvoiceInfo invoiceInfo){
	    invoiceInfoDao.updateInvoiceInfo(invoiceInfo);
	    return RESCODE.SUCCESS.getJSONRES();
	}

	/**
	 * 根据orderId获取发票信息
	 * @param orderId
	 * @return
	 */
	public InvoiceInfo getInvoiceInfoByOrderId(String orderId){
		if (StringUtils.isEmpty(orderId)) {
			return null;
		}
		return invoiceInfoDao.getInvoiceInfoByOrderId(orderId);
	}

}