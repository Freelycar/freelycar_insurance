package com.freelycar.entity;  

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
	
	
	
	

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**  
 *  
 */  
@Entity
@Table
public class InvoiceInfo  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private Integer clientId;  //客户id;
	
    private String invoiceType;  //发票信息;
	
    private String invoiceTitle;  //发表抬头;
	
    private String phone;  //手机号码;
    /********** constructors ***********/  
    public InvoiceInfo() {  
      
    }  
  
    public InvoiceInfo(Integer clientId, String invoiceType, String invoiceTitle, String phone) {  
        this.clientId = clientId;  
        this.invoiceType = invoiceType;  
        this.invoiceTitle = invoiceTitle;  
        this.phone = phone;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public Integer getClientId() {  
        return clientId;  
    }  
  
    public void setClientId(Integer clientId) {  
        this.clientId = clientId;  
    }  
      
	
    public String getInvoiceType() {  
        return invoiceType;  
    }  
  
    public void setInvoiceType(String invoiceType) {  
        this.invoiceType = invoiceType;  
    }  
      
	
    public String getInvoiceTitle() {  
        return invoiceTitle;  
    }  
  
    public void setInvoiceTitle(String invoiceTitle) {  
        this.invoiceTitle = invoiceTitle;  
    }  
      
	
    public String getPhone() {  
        return phone;  
    }  
  
    public void setPhone(String phone) {  
        this.phone = phone;  
    }  
      

	@Override
	public String toString() {
		return "InvoiceInfo [id=" + id 
				+ ", clientId=" + clientId
				+ ", invoiceType=" + invoiceType
				+ ", invoiceTitle=" + invoiceTitle
				+ ", phone=" + phone
				+ "]";
    }

}