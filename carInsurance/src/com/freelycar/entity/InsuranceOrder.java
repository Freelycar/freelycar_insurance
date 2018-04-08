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
public class InsuranceOrder  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String orderId;  //订单编号/报价编号;
    
    private String openId;//
	
    private String ciPolicyNo;  //交强险保单号;
	
    private Long ciPolicyPrice;  //交强险价格;
	
    private Long taxPrice;  //车船税(单位：分);
	
    private String biPolicyNo;  //商业险保单号;
	
    private Long biPolicyPrice;  //商业险价格;
	
    @Lob
    private String offerDetail;  //报价方案结果详情;
	
    private String insureName;  //被保人姓名;
	
    private String insuredIdNo;  //被保人身份证号;
	
    private String insuredPhone;  //被保人联系方式;
	
    private String licenseNumber;  //车牌号;
	
    private boolean cashback;  //是否返现;
	
    private Long backmoney;  //返现多少钱;
	
    private String invoiceInfo;  //发票信息;
	
    private Integer addressId;  //地址信息;
	
    private int state;  //订单状态
    private String stateString;  //订单状态。没错就是存的String表义;
	
    private Long createTime;  //创建时间;
	
    private Long payTime;  //付款时间;
	
    private Long deliveredTime;  //发货时间;
	
    private Long dealTime;  //成交时间;
    
    private long cashbackTime;
	
    private String expressNumber;  //快递单号;
	
    private String expressCompany;  //快递公司;
	
    private String remark;  //备注;
    
    private long totalPrice;//总价
    
    private String paycodeurl;
    
    private String effectiveTime;//二维有效时间
    
    private long effetiveTimeLong;//时间戳
    
    private String hebaoMessage;//核保的信息
    
    private String source;
    
    private int sourceId;
    
    @Transient
    private QuoteRecord quoteRecord;
    @Transient
    private Reciver reciver;
    
    /********** constructors ***********/  
    public InsuranceOrder() {  
      
    }  
   
    public InsuranceOrder(String orderId, String ciPolicyNo, Long ciPolicyPrice, Long taxPrice, String biPolicyNo, Long biPolicyPrice, String offerDetail, String insureName, String insuredIdNo, String insuredPhone, String licenseNumber, boolean cashback, Long backmoney, String invoiceInfo, Integer addressId, int state, Long createTime, Long payTime, Long deliveredTime, Long dealTime, String expressNumber, String expressCompany, String remark) {  
        this.orderId = orderId;  
        this.ciPolicyNo = ciPolicyNo;  
        this.ciPolicyPrice = ciPolicyPrice;  
        this.taxPrice = taxPrice;  
        this.biPolicyNo = biPolicyNo;  
        this.biPolicyPrice = biPolicyPrice;  
        this.offerDetail = offerDetail;  
        this.insureName = insureName;  
        this.insuredIdNo = insuredIdNo;  
        this.insuredPhone = insuredPhone;  
        this.licenseNumber = licenseNumber;  
        this.cashback = cashback;  
        this.backmoney = backmoney;  
        this.invoiceInfo = invoiceInfo;  
        this.addressId = addressId;  
        this.state = state;  
        this.createTime = createTime;  
        this.payTime = payTime;  
        this.deliveredTime = deliveredTime;  
        this.dealTime = dealTime;  
        this.expressNumber = expressNumber;  
        this.expressCompany = expressCompany;  
        this.remark = remark;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    
    public Reciver getReciver() {
		return reciver;
	}
    
    public void setReciver(Reciver reciver) {
		this.reciver = reciver;
	}
    
    public QuoteRecord getQuoteRecord() {
		return quoteRecord;
	}
    
    public void setQuoteRecord(QuoteRecord quoteRecord) {
		this.quoteRecord = quoteRecord;
	}
    
    public String getHebaoMessage() {
		return hebaoMessage;
	}
    
    public void setHebaoMessage(String hebaoMessage) {
		this.hebaoMessage = hebaoMessage;
	}
    
    public String getSource() {
		return source;
	}
    
    public void setSource(String source) {
		this.source = source;
	}
    
    public int getSourceId() {
		return sourceId;
	}
    
    public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
    
    public String getOpenId() {
		return openId;
	}
    
    public void setOpenId(String openId) {
		this.openId = openId;
	}
    
    public String getEffectiveTime() {
		return effectiveTime;
	}
    
    
    public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
    
    public long getEffetiveTimeLong() {
		return effetiveTimeLong;
	}
    
    
    public void setEffetiveTimeLong(long effetiveTimeLong) {
		this.effetiveTimeLong = effetiveTimeLong;
	}
    
    public long getCashbackTime() {
		return cashbackTime;
	}
    
    public void setCashbackTime(long cashbackTime) {
		this.cashbackTime = cashbackTime;
	}
    
    public long getTotalPrice() {
		return totalPrice;
	}
    
    public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
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
      
	
    public String getCiPolicyNo() {  
        return ciPolicyNo;  
    }  
  
    public void setCiPolicyNo(String ciPolicyNo) {  
        this.ciPolicyNo = ciPolicyNo;  
    }  
      
	
    public String getPaycodeurl() {
		return paycodeurl;
	}
    
    public void setPaycodeurl(String paycodeurl) {
		this.paycodeurl = paycodeurl;
	}
    
    public Long getCiPolicyPrice() {  
        return ciPolicyPrice;  
    }  
  
    public void setCiPolicyPrice(Long ciPolicyPrice) {  
        this.ciPolicyPrice = ciPolicyPrice;  
    }  
      
	
    public Long getTaxPrice() {  
        return taxPrice;  
    }  
  
    public void setTaxPrice(Long taxPrice) {  
        this.taxPrice = taxPrice;  
    }  
      
	
    public String getBiPolicyNo() {  
        return biPolicyNo;  
    }  
  
    public void setBiPolicyNo(String biPolicyNo) {  
        this.biPolicyNo = biPolicyNo;  
    }  
      
	
    public Long getBiPolicyPrice() {  
        return biPolicyPrice;  
    }  
  
    public void setBiPolicyPrice(Long biPolicyPrice) {  
        this.biPolicyPrice = biPolicyPrice;  
    }  
      
	
    public String getOfferDetail() {  
        return offerDetail;  
    }  
  
    public void setOfferDetail(String offerDetail) {  
        this.offerDetail = offerDetail;  
    }  
      
	
    public String getInsureName() {  
        return insureName;  
    }  
  
    public void setInsureName(String insureName) {  
        this.insureName = insureName;  
    }  
      
	
    public String getInsuredIdNo() {  
        return insuredIdNo;  
    }  
  
    public void setInsuredIdNo(String insuredIdNo) {  
        this.insuredIdNo = insuredIdNo;  
    }  
      
	
    public String getInsuredPhone() {  
        return insuredPhone;  
    }  
  
    public void setInsuredPhone(String insuredPhone) {  
        this.insuredPhone = insuredPhone;  
    }  
      
	
    public String getLicenseNumber() {  
        return licenseNumber;  
    }  
  
    public void setLicenseNumber(String licenseNumber) {  
        this.licenseNumber = licenseNumber;  
    }  
      
	
    public boolean getCashback() {  
        return cashback;  
    }  
  
    public void setCashback(boolean cashback) {  
        this.cashback = cashback;  
    }  
      
	
    public Long getBackmoney() {  
        return backmoney;  
    }  
  
    public void setBackmoney(Long backmoney) {  
        this.backmoney = backmoney;  
    }  
      
	
    public String getInvoiceInfo() {  
        return invoiceInfo;  
    }  
  
    public void setInvoiceInfo(String invoiceInfo) {  
        this.invoiceInfo = invoiceInfo;  
    }  
      
	
    public Integer getAddressId() {  
        return addressId;  
    }  
  
    public void setAddressId(Integer addressId) {  
        this.addressId = addressId;  
    }  
      
	
    public int getState() {  
        return state;  
    }  
  
    public void setState(int state) {  
        this.state = state;  
    }  
      
	
    public Long getCreateTime() {  
        return createTime;  
    }  
  
    public void setCreateTime(Long createTime) {  
        this.createTime = createTime;  
    }  
      
	
    public Long getPayTime() {  
        return payTime;  
    }  
  
    public void setPayTime(Long payTime) {  
        this.payTime = payTime;  
    }  
      
	
    public Long getDeliveredTime() {  
        return deliveredTime;  
    }  
  
    public void setDeliveredTime(Long deliveredTime) {  
        this.deliveredTime = deliveredTime;  
    }  
      
	
    public Long getDealTime() {  
        return dealTime;  
    }  
  
    public void setDealTime(Long dealTime) {  
        this.dealTime = dealTime;  
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
      
    
    public String getStateString() {
		return stateString;
	}
    
    public void setStateString(String stateString) {
		this.stateString = stateString;
	}

	@Override
	public String toString() {
		return "Order [id=" + id 
				+ ", orderId=" + orderId
				+ ", ciPolicyNo=" + ciPolicyNo
				+ ", ciPolicyPrice=" + ciPolicyPrice
				+ ", taxPrice=" + taxPrice
				+ ", biPolicyNo=" + biPolicyNo
				+ ", biPolicyPrice=" + biPolicyPrice
				+ ", offerDetail=" + offerDetail
				+ ", insureName=" + insureName
				+ ", insuredIdNo=" + insuredIdNo
				+ ", insuredPhone=" + insuredPhone
				+ ", licenseNumber=" + licenseNumber
				+ ", cashback=" + cashback
				+ ", backmoney=" + backmoney
				+ ", invoiceInfo=" + invoiceInfo
				+ ", addressId=" + addressId
				+ ", state=" + state
				+ ", createTime=" + createTime
				+ ", payTime=" + payTime
				+ ", deliveredTime=" + deliveredTime
				+ ", dealTime=" + dealTime
				+ ", expressNumber=" + expressNumber
				+ ", expressCompany=" + expressCompany
				+ ", remark=" + remark
				+ "]";
    }

}