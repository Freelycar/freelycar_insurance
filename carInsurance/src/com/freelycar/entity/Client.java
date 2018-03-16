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
public class Client  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String openId;  //微信openId;
	
    private String nickName;  //微信名称;
	
    private String headImg;  //微信的头像;
	
    private String ownerName;  //车主姓名;
	
    private String licenseNumber;  //车牌号码;
	
    private String phone;  //手机号码;
	
    private String idCard;  //身份证号码;
	
    private boolean transfer;  //是否过户;
    private boolean toubao;  //是否投保;
    
    private String source;
	
    private Long transferTime;  //过户日期;
	
    private Long insuranceDeadline;  //保险到期;
    
    @Transient
    private String contactAddress;
    /********** constructors ***********/  
    public Client() {  
      
    }  
  
    public Client(String openId, String nickName, String headImg, String ownerName, String licenseNumber, String phone, String idCard, boolean transfer, Long transferTime, Long insuranceDeadline) {  
        this.openId = openId;  
        this.nickName = nickName;  
        this.headImg = headImg;  
        this.ownerName = ownerName;  
        this.licenseNumber = licenseNumber;  
        this.phone = phone;  
        this.idCard = idCard;  
        this.transfer = transfer;  
        this.transferTime = transferTime;  
        this.insuranceDeadline = insuranceDeadline;  
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
  
    public String getContactAddress() {
		return contactAddress;
	}
    
    
    public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
    
    public void setOpenId(String openId) {  
        this.openId = openId;  
    }  
      
	
    public String getNickName() {  
        return nickName;  
    }  
  
    public void setNickName(String nickName) {  
        this.nickName = nickName;  
    }  
      
    
    public String getSource() {
		return source;
	}
    
    
    public void setSource(String source) {
		this.source = source;
	}
	
    public String getHeadImg() {  
        return headImg;  
    }  
  
    public void setHeadImg(String headImg) {  
        this.headImg = headImg;  
    }  
      
	
    public String getOwnerName() {  
        return ownerName;  
    }  
  
    public void setOwnerName(String ownerName) {  
        this.ownerName = ownerName;  
    }  
      
	
    public String getLicenseNumber() {  
        return licenseNumber;  
    }  
  
    public void setLicenseNumber(String licenseNumber) {  
        this.licenseNumber = licenseNumber;  
    }  
      
	
    public String getPhone() {  
        return phone;  
    }  
  
    public void setPhone(String phone) {  
        this.phone = phone;  
    }  
      
	
    public String getIdCard() {  
        return idCard;  
    }  
  
    public void setIdCard(String idCard) {  
        this.idCard = idCard;  
    }  
      
	
    public boolean getTransfer() {  
        return transfer;  
    }  
  
    public void setTransfer(boolean transfer) {  
        this.transfer = transfer;  
    }  
      
	
    public Long getTransferTime() {  
        return transferTime;  
    }  
  
    public void setTransferTime(Long transferTime) {  
        this.transferTime = transferTime;  
    }  
      
	
    public Long getInsuranceDeadline() {  
        return insuranceDeadline;  
    }  
  
    public void setInsuranceDeadline(Long insuranceDeadline) {  
        this.insuranceDeadline = insuranceDeadline;  
    }  
      

    public boolean isToubao() {
		return toubao;
	}
    
    public void setToubao(boolean toubao) {
		this.toubao = toubao;
	}
    
    
	@Override
	public String toString() {
		return "Client [id=" + id 
				+ ", openId=" + openId
				+ ", nickName=" + nickName
				+ ", headImg=" + headImg
				+ ", ownerName=" + ownerName
				+ ", licenseNumber=" + licenseNumber
				+ ", phone=" + phone
				+ ", idCard=" + idCard
				+ ", transfer=" + transfer
				+ ", transferTime=" + transferTime
				+ ", insuranceDeadline=" + insuranceDeadline
				+ "]";
    }

}