package com.freelycar.entity;  

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
	
	
	
	
	

import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**  
 *  
 */  
@Entity
@Table
public class Reciver  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String reciver;  //收件人;
	
    private String phone;  //手机号码;
	
    private String provincesCities;  //省市;
	
    private Integer clientId;  //属于哪个客户;
    
    private String openId;
    
    private String orderId;//订单的id
    
    
    //在订单里面有这里只做返回
    @Transient
    private String expressNumber;  //快递单号;
	
    @Transient
    private String expressCompany;  //快递公司;
	
    @Transient
    private String remark;  //备注;
    
    
	
    private String adressDetail;  //详细地址;
    /********** constructors ***********/  
    public Reciver() {  
      
    }  
  
    public Reciver(String reciver, String phone, String provincesCities, Integer clientId, String adressDetail) {  
        this.reciver = reciver;  
        this.phone = phone;  
        this.provincesCities = provincesCities;  
        this.clientId = clientId;  
        this.adressDetail = adressDetail;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
    public String getReciver() {  
        return reciver;  
    }  
  
    public void setReciver(String reciver) {  
        this.reciver = reciver;  
    }  
      
	
    public String getPhone() {  
        return phone;  
    }  
  
    public void setPhone(String phone) {  
        this.phone = phone;  
    }  
      
	
    public String getProvincesCities() {  
        return provincesCities;  
    }  
  
    
    public String getOrderId() {
		return orderId;
	}
    
    public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
    
    public void setProvincesCities(String provincesCities) {  
        this.provincesCities = provincesCities;  
    }  
      
	
    public Integer getClientId() {  
        return clientId;  
    }  
  
    public void setClientId(Integer clientId) {  
        this.clientId = clientId;  
    }  
      
	
    public String getAdressDetail() {  
        return adressDetail;  
    }  
  
    public void setAdressDetail(String adressDetail) {  
        this.adressDetail = adressDetail;  
    }  
      
    

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Reciver [id=" + id 
				+ ", reciver=" + reciver
				+ ", phone=" + phone
				+ ", provincesCities=" + provincesCities
				+ ", clientId=" + clientId
				+ ", adressDetail=" + adressDetail
				+ "]";
    }

}