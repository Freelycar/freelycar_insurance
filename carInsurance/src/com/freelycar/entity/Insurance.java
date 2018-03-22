package com.freelycar.entity;  

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	
    private String ownerName;  //车牌号码;
    private String licenseNumber;  //车牌号码;
	
    private Integer insuranceCompanyId;  //保险公司ID;
	
    private String insuranceCompany;  //保险公司名称;
	
    private String insuranceEndTime;  //终保日期;
	
    private String insuranceBeginTime;  //起保险日期;
    
    @Transient
    private String forceInsuranceStartTime;
	
    private boolean commercial;  //是否为商业险;
	
    @Lob
    private String insurances;  //商业险有险种列表方案！;
	
    private String totalOpenId;  //属于同一个人的openId;
	
    private String totalLicenseNumber;  //同一个车牌，怕一个openId有多个车;
    
    private String price;//该险种的钱
    
    
    
    //核保	
    public static class ProposalEntity{
    	private String offerId;
    	private String ownerName;
    	private String idCard;
    	private String phone;
    	private String contactAddress;
    	private String licenseNumber;
    	
    	
    	//收款人信息
	    private String payee;  //收款人姓名;
	    private String account;  //收款人卡;
	    private String bankname;  //开户行;
	    private String openId;//微信openId
	    
	    
	    //保单收获信息
	    private String reciver;  //收件人;
	    private String reciverPhone;  //手机号码;
	    private String provincesCities;  //省市;
	    private String addressDetail;  //详细;
	    
	    
	    //发票信息
	    private String invoiceType;  //发票信息;
	    private String invoiceTitle;  //发表抬头;
	    private String invoicePhone;  //手机号码;
	    
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
		public String getReciverPhone() {
			return reciverPhone;
		}
		public void setReciverPhone(String reciverPhone) {
			this.reciverPhone = reciverPhone;
		}
		public String getProvincesCities() {
			return provincesCities;
		}
		public void setProvincesCities(String provincesCities) {
			this.provincesCities = provincesCities;
		}
		public String getAddressDetail() {
			return addressDetail;
		}
		public void setAddressDetail(String addressDetail) {
			this.addressDetail = addressDetail;
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
		public String getInvoicePhone() {
			return invoicePhone;
		}
		public void setInvoicePhone(String invoicePhone) {
			this.invoicePhone = invoicePhone;
		}
		public String getLicenseNumber() {
			return licenseNumber;
		}
    	public void setLicenseNumber(String licenseNumber) {
			this.licenseNumber = licenseNumber;
		}
		public String getOfferId() {
			return offerId;
		}
		public void setOfferId(String offerId) {
			this.offerId = offerId;
		}
		public String getOwnerName() {
			return ownerName;
		}
		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}
		public String getIdCard() {
			return idCard;
		}
		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getContactAddress() {
			return contactAddress;
		}
		public void setContactAddress(String contactAddress) {
			this.contactAddress = contactAddress;
		}
		@Override
		public String toString() {
			return "ProposalEntity [offerId=" + offerId + ", ownerName=" + ownerName + ", idCard=" + idCard + ", phone="
					+ phone + ", contactAddress=" + contactAddress + ", licenseNumber=" + licenseNumber + ", payee="
					+ payee + ", account=" + account + ", bankname=" + bankname + ", openId=" + openId + ", reciver="
					+ reciver + ", reciverPhone=" + reciverPhone + ", provincesCities=" + provincesCities
					+ ", addressDetail=" + addressDetail + ", invoiceType=" + invoiceType + ", invoiceTitle="
					+ invoiceTitle + ", invoicePhone=" + invoicePhone + "]";
		}
		
		
    }
    
    
    
    
    //询价
    public static class QueryPriceEntity{
		private String cityCode;
		private String cityName;
		private String openId;
		private String forceInsuranceStartTime;
		private String insuranceCompanyId;
		private String insurances;
		private String licenseNumber;
		private String ownerName;
		private String insuranceBeginTime;
		
		private boolean transfer;
		private String transferTime;
		
		public String getTransferTime() {
			return transferTime;
		}
		
		public void setTransferTime(String transferTime) {
			this.transferTime = transferTime;
		}
		
		public boolean isTransfer() {
			return transfer;
		}
		
		public void setTransfer(boolean transfer) {
			this.transfer = transfer;
		}
		public String getInsuranceBeginTime() {
			return insuranceBeginTime;
		}
		
		public void setInsuranceBeginTime(String insuranceBeginTime) {
			this.insuranceBeginTime = insuranceBeginTime;
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
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public String getForceInsuranceStartTime() {
			return forceInsuranceStartTime;
		}
		public void setForceInsuranceStartTime(String forceInsuranceStartTime) {
			this.forceInsuranceStartTime = forceInsuranceStartTime;
		}
		public String getInsuranceCompanyId() {
			return insuranceCompanyId;
		}
		public void setInsuranceCompanyId(String insuranceCompanyId) {
			this.insuranceCompanyId = insuranceCompanyId;
		}
		public String getInsurances() {
			return insurances;
		}
		public void setInsurances(String insurances) {
			this.insurances = insurances;
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

		@Override
		public String toString() {
			return "QueryPriceEntity [cityCode=" + cityCode + ", cityName=" + cityName + ", openId=" + openId
					+ ", forceInsuranceStartTime=" + forceInsuranceStartTime + ", insuranceCompanyId="
					+ insuranceCompanyId + ", insurances=" + insurances + ", licenseNumber=" + licenseNumber
					+ ", ownerName=" + ownerName + ", insuranceBeginTime=" + insuranceBeginTime + ", transfer="
					+ transfer + ", transferTime=" + transferTime + "]";
		}
		
		
	}
    
    
    /********** constructors ***********/  
    public Insurance() {  
      
    }  
  
    class InsurancesList{
    	
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
      
    public String getOwnerName() {
		return ownerName;
	}
    
    public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
    
    
    public String getPrice() {
		return price;
	}
    
    public void setPrice(String price) {
		this.price = price;
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