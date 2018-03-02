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
public class InsuranceList  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private Integer insuranceId;  //险种编号;
	
    private String insuranceName;  //险种中文名称;
	
    private Float quotesPrice;  //保费;
	
    private String amountStr;  //保额(车损险显示车损定价,其余显示保额);
	
    private Float price;  //保额的数字形式;
    /********** constructors ***********/  
    public InsuranceList() {  
      
    }  
  
    public InsuranceList(Integer insuranceId, String insuranceName, Float quotesPrice, String amountStr, Float price) {  
        this.insuranceId = insuranceId;  
        this.insuranceName = insuranceName;  
        this.quotesPrice = quotesPrice;  
        this.amountStr = amountStr;  
        this.price = price;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public Integer getInsuranceId() {  
        return insuranceId;  
    }  
  
    public void setInsuranceId(Integer insuranceId) {  
        this.insuranceId = insuranceId;  
    }  
      
	
    public String getInsuranceName() {  
        return insuranceName;  
    }  
  
    public void setInsuranceName(String insuranceName) {  
        this.insuranceName = insuranceName;  
    }  
      
	
    public Float getQuotesPrice() {  
        return quotesPrice;  
    }  
  
    public void setQuotesPrice(Float quotesPrice) {  
        this.quotesPrice = quotesPrice;  
    }  
      
	
    public String getAmountStr() {  
        return amountStr;  
    }  
  
    public void setAmountStr(String amountStr) {  
        this.amountStr = amountStr;  
    }  
      
	
    public Float getPrice() {  
        return price;  
    }  
  
    public void setPrice(Float price) {  
        this.price = price;  
    }  
      

	@Override
	public String toString() {
		return "InsuranceList [id=" + id 
				+ ", insuranceId=" + insuranceId
				+ ", insuranceName=" + insuranceName
				+ ", quotesPrice=" + quotesPrice
				+ ", amountStr=" + amountStr
				+ ", price=" + price
				+ "]";
    }

}