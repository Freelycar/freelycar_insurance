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
public class QuoteRecord  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private Integer clientId;  //客户id;
	
    private String openId;  //客户的openId;
	
    private String offerId;//变价编号
    
    private String offerDetail;//报价详细
    
    private String licenseNumber;  //车牌号;
	
    private String ownerName;  //车主名称;
	
    private String insurances;  //报价方案;
	
    private Long crateTime;  //报价时间;
	
    private String cityCode;  //城市编码;
	
    private String cityName;  //城市名称;
	
    private String insuranceCompanyName;  //保险公司编号多加用逗号分隔;
	
    private String insurancesList;  //多方案险种列表;
	
    private Integer insuranceStartTime;  //商业险起保日期（时间戳）;
	
    private Integer forceInsuranceStartTime;  //交强险起保日期（时间戳）;
	
    private Integer transferDate;  //过户日期（时间戳）;
	
    private String requestHeader;  //第三方信息(可以是任意信息比如;
    
    private int state;
    /********** constructors ***********/  
    public QuoteRecord() {  
      
    }  
  
    public QuoteRecord(Integer clientId, String openId, String licenseNumber, String ownerName, String insurances, Long crateTime, String cityCode, String cityName, String insuranceCompanyName, String insurancesList, Integer insuranceStartTime, Integer forceInsuranceStartTime, Integer transferDate, String requestHeader) {  
        this.clientId = clientId;  
        this.openId = openId;  
        this.licenseNumber = licenseNumber;  
        this.ownerName = ownerName;  
        this.insurances = insurances;  
        this.crateTime = crateTime;  
        this.cityCode = cityCode;  
        this.cityName = cityName;  
        this.insuranceCompanyName = insuranceCompanyName;  
        this.insurancesList = insurancesList;  
        this.insuranceStartTime = insuranceStartTime;  
        this.forceInsuranceStartTime = forceInsuranceStartTime;  
        this.transferDate = transferDate;  
        this.requestHeader = requestHeader;  
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
      
	
    public String getOpenId() {  
        return openId;  
    }  
  
    public void setOpenId(String openId) {  
        this.openId = openId;  
    }  
      
	
    public String getLicenseNumber() {  
        return licenseNumber;  
    }  
  
    public void setLicenseNumber(String licenseNumber) {  
        this.licenseNumber = licenseNumber;  
    }  
      
	
    public String getOwnerName() {  
        return ownerName;  
    }  
  
    public void setOwnerName(String ownerName) {  
        this.ownerName = ownerName;  
    }  
      
	
    public String getInsurances() {  
        return insurances;  
    }  
  
    public void setInsurances(String insurances) {  
        this.insurances = insurances;  
    }  
      
	
    public Long getCrateTime() {  
        return crateTime;  
    }  
  
    public void setCrateTime(Long crateTime) {  
        this.crateTime = crateTime;  
    }  
      
	
    public String getCityCode() {  
        return cityCode;  
    }  
  
    public void setCityCode(String cityCode) {  
        this.cityCode = cityCode;  
    }  
      
	
    public String getCityName() {  
        return cityName;  
    }  
  
    public void setCityName(String cityName) {  
        this.cityName = cityName;  
    }  
      
	
    public String getInsuranceCompanyName() {  
        return insuranceCompanyName;  
    }  
  
    public void setInsuranceCompanyName(String insuranceCompanyName) {  
        this.insuranceCompanyName = insuranceCompanyName;  
    }  
      
	
    public String getInsurancesList() {  
        return insurancesList;  
    }  
  
    public void setInsurancesList(String insurancesList) {  
        this.insurancesList = insurancesList;  
    }  
      
	
    public Integer getInsuranceStartTime() {  
        return insuranceStartTime;  
    }  
  
    public void setInsuranceStartTime(Integer insuranceStartTime) {  
        this.insuranceStartTime = insuranceStartTime;  
    }  
      
	
    public Integer getForceInsuranceStartTime() {  
        return forceInsuranceStartTime;  
    }  
  
    public void setForceInsuranceStartTime(Integer forceInsuranceStartTime) {  
        this.forceInsuranceStartTime = forceInsuranceStartTime;  
    }  
      
	
    public Integer getTransferDate() {  
        return transferDate;  
    }  
  
    public void setTransferDate(Integer transferDate) {  
        this.transferDate = transferDate;  
    }  
      
	
    public String getRequestHeader() {  
        return requestHeader;  
    }  
  
    public void setRequestHeader(String requestHeader) {  
        this.requestHeader = requestHeader;  
    }  
      

    public int getState() {
		return state;
	}
    
    public void setState(int state) {
		this.state = state;
	}
    
    public String getOfferId() {
		return offerId;
	}
    
    public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
    
    public String getOfferDetail() {
		return offerDetail;
	}
    
    public void setOfferDetail(String offerDetail) {
		this.offerDetail = offerDetail;
	}
    
    
    
	@Override
	public String toString() {
		return "QuoteRecord [id=" + id 
				+ ", clientId=" + clientId
				+ ", openId=" + openId
				+ ", licenseNumber=" + licenseNumber
				+ ", ownerName=" + ownerName
				+ ", insurances=" + insurances
				+ ", crateTime=" + crateTime
				+ ", cityCode=" + cityCode
				+ ", cityName=" + cityName
				+ ", insuranceCompanyName=" + insuranceCompanyName
				+ ", insurancesList=" + insurancesList
				+ ", insuranceStartTime=" + insuranceStartTime
				+ ", forceInsuranceStartTime=" + forceInsuranceStartTime
				+ ", transferDate=" + transferDate
				+ ", requestHeader=" + requestHeader
				+ "]";
    }

}