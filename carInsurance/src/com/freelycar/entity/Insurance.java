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
public class Insurance  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String policyNo;  //保单号;
	
    private String licenseNumber;  //车牌号码;
	
    private Integer insuranceCompanyId;  //保险公司ID;
	
    private String insuranceCompany;  //保险公司名称;
	
    private String insuranceEndTime;  //终保日期;
	
    private String insuranceBeginTime;  //起保险日期;
    
    @Transient
    private String forceInsuranceStartTime;
	
    private boolean commercial;  //是否为商业险;
	
    private String insurances;  //商业险有险种列表方案！;
	
    private String totalOpenId;  //属于同一个人的openId;
	
    private String totalLicenseNumber;  //同一个车牌，怕一个openId有多个车;
    /********** constructors ***********/  
    public Insurance() {  
      
    }  
  
    public Insurance(String policyNo, String licenseNumber, Integer insuranceCompanyId, String insuranceCompany, String insuranceEndTime, String insuranceBeginTime, boolean commercial, String insurances, String totalOpenId, String totalLicenseNumber) {  
        this.policyNo = policyNo;  
        this.licenseNumber = licenseNumber;  
        this.insuranceCompanyId = insuranceCompanyId;  
        this.insuranceCompany = insuranceCompany;  
        this.insuranceEndTime = insuranceEndTime;  
        this.insuranceBeginTime = insuranceBeginTime;  
        this.commercial = commercial;  
        this.insurances = insurances;  
        this.totalOpenId = totalOpenId;  
        this.totalLicenseNumber = totalLicenseNumber;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public String getPolicyNo() {  
        return policyNo;  
    }  
  
    public void setPolicyNo(String policyNo) {  
        this.policyNo = policyNo;  
    }  
      
	
    public String getLicenseNumber() {  
        return licenseNumber;  
    }  
  
    public void setLicenseNumber(String licenseNumber) {  
        this.licenseNumber = licenseNumber;  
    }  
      
	
    public Integer getInsuranceCompanyId() {  
        return insuranceCompanyId;  
    }  
  
    public void setInsuranceCompanyId(Integer insuranceCompanyId) {  
        this.insuranceCompanyId = insuranceCompanyId;  
    }  
      
	
    public String getInsuranceCompany() {  
        return insuranceCompany;  
    }  
  
    public void setInsuranceCompany(String insuranceCompany) {  
        this.insuranceCompany = insuranceCompany;  
    }  
      
	
    public String getInsuranceEndTime() {  
        return insuranceEndTime;  
    }  
  
    public void setInsuranceEndTime(String insuranceEndTime) {  
        this.insuranceEndTime = insuranceEndTime;  
    }  
      
	
    public String getInsuranceBeginTime() {  
        return insuranceBeginTime;  
    }  
  
    public void setInsuranceBeginTime(String insuranceBeginTime) {  
        this.insuranceBeginTime = insuranceBeginTime;  
    }  
      
	
    public boolean getCommercial() {  
        return commercial;  
    }  
  
    public void setCommercial(boolean commercial) {  
        this.commercial = commercial;  
    }  
      
	
    public String getInsurances() {  
        return insurances;  
    }  
  
    public void setInsurances(String insurances) {  
        this.insurances = insurances;  
    }  
      
	
    public String getTotalOpenId() {  
        return totalOpenId;  
    }  
  
    public void setTotalOpenId(String totalOpenId) {  
        this.totalOpenId = totalOpenId;  
    }  
      
	
    public String getTotalLicenseNumber() {  
        return totalLicenseNumber;  
    }  
  
    public void setTotalLicenseNumber(String totalLicenseNumber) {  
        this.totalLicenseNumber = totalLicenseNumber;  
    }  
    
    public String getForceInsuranceStartTime() {
		return forceInsuranceStartTime;
	}
    
    public void setForceInsuranceStartTime(String forceInsuranceStartTime) {
		this.forceInsuranceStartTime = forceInsuranceStartTime;
	}

	@Override
	public String toString() {
		return "Insurance [id=" + id 
				+ ", policyNo=" + policyNo
				+ ", licenseNumber=" + licenseNumber
				+ ", insuranceCompanyId=" + insuranceCompanyId
				+ ", insuranceCompany=" + insuranceCompany
				+ ", insuranceEndTime=" + insuranceEndTime
				+ ", insuranceBeginTime=" + insuranceBeginTime
				+ ", commercial=" + commercial
				+ ", insurances=" + insurances
				+ ", totalOpenId=" + totalOpenId
				+ ", totalLicenseNumber=" + totalLicenseNumber
				+ "]";
    }

}