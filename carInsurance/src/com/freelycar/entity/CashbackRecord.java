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
public class CashbackRecord  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private Integer clientId;  //客户id;
	
    private String payee;  //收款人姓名;
	
    private String account;  //收款人卡;
	
    private String bankname;  //开户行;
    
    private String orderId;
    /********** constructors ***********/  
    public CashbackRecord() {  
      
    }  
  
    public CashbackRecord(Integer clientId, String payee, String account, String bankname) {  
        this.clientId = clientId;  
        this.payee = payee;  
        this.account = account;  
        this.bankname = bankname;  
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
      
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
    public String getPayee() {  
        return payee;  
    }  
  
    public void setPayee(String payee) {  
        this.payee = payee;  
    }  
      
	
    public String getAccount() {  
        return account;  
    }  
  
    public void setAccount(String account) {  
        this.account = account;  
    }  
      
	
    public String getBankname() {  
        return bankname;  
    }  
  
    public void setBankname(String bankname) {  
        this.bankname = bankname;  
    }  
      

	@Override
	public String toString() {
		return "CashbackRecord [id=" + id 
				+ ", clientId=" + clientId
				+ ", payee=" + payee
				+ ", account=" + account
				+ ", bankname=" + bankname
				+ "]";
    }

}