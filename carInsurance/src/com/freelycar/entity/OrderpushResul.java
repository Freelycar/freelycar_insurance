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
public class OrderpushResul  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String orderId;  //订单编号;
	
    private String licenseNumber;  //核保车牌号码;
	
    private Integer state;  //状态(2-核保成功,3-核保失败,16-核保通过待确认);
	
    private String underwritingJson;  //核保信息。具体格式参考示例 有二维码;
	
    private Integer underwritingPriceCent;  //核保总价。单位(分);
    /********** constructors ***********/  
    public OrderpushResul() {  
      
    }  
  
    public OrderpushResul(String orderId, String licenseNumber, Integer state, String underwritingJson, Integer underwritingPriceCent) {  
        this.orderId = orderId;  
        this.licenseNumber = licenseNumber;  
        this.state = state;  
        this.underwritingJson = underwritingJson;  
        this.underwritingPriceCent = underwritingPriceCent;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public String getOrderId() {  
        return orderId;  
    }  
  
    public void setOrderId(String orderId) {  
        this.orderId = orderId;  
    }  
      
	
    public String getLicenseNumber() {  
        return licenseNumber;  
    }  
  
    public void setLicenseNumber(String licenseNumber) {  
        this.licenseNumber = licenseNumber;  
    }  
      
	
    public Integer getState() {  
        return state;  
    }  
  
    public void setState(Integer state) {  
        this.state = state;  
    }  
      
	
    public String getUnderwritingJson() {  
        return underwritingJson;  
    }  
  
    public void setUnderwritingJson(String underwritingJson) {  
        this.underwritingJson = underwritingJson;  
    }  
      
	
    public Integer getUnderwritingPriceCent() {  
        return underwritingPriceCent;  
    }  
  
    public void setUnderwritingPriceCent(Integer underwritingPriceCent) {  
        this.underwritingPriceCent = underwritingPriceCent;  
    }  
      

	@Override
	public String toString() {
		return "OrderpushResul [id=" + id 
				+ ", orderId=" + orderId
				+ ", licenseNumber=" + licenseNumber
				+ ", state=" + state
				+ ", underwritingJson=" + underwritingJson
				+ ", underwritingPriceCent=" + underwritingPriceCent
				+ "]";
    }

}