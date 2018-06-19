package com.freelycar.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table
public class Client {
    /********** attribute ***********/
    /**
     * 主键 自增
     */
    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer id;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 微信名称
     */
    private String nickName;
    /**
     * 微信的头像
     */
    private String headImg;
    /**
     * 车主姓名
     */
    private String ownerName;
    /**
     * 车牌号码
     */
    private String licenseNumber;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 是否过户
     */
    private Boolean transfer;
    /**
     * 是否投保
     */
    private Boolean toubao;
    /**
     * 是否返现
     */
    private Boolean cashback;
    /**
     * 来源
     */
    private String source;
    /**
     * 报价状态
     */
    private String quoteState;
    private Integer quoteStateCode;
    /**
     * 过户日期
     */
    private Long transferTime;
    /**
     * 获取报价时间
     */
    private Long leastQueryTime;
    /**
     * 开发平台的unionId
     */
    private String unionId;
    /**
     * 最新报价时间
     */
    @Transient
    private Long leastQuoteTime;
    /**
     * 强制险保险到期
     */
    private Long insuranceDeadline;
    /**
     * 最新订单时间
     */
    @Transient
    private Long leastOrderTime;

    @Transient
    private String contactAddress;
    /**
     * 号牌种类(01 大型车、02 小型车)
     */
    @Transient
    private String carTypeCode;

    /********** constructors ***********/
    public Client() {

    }

    public Client(String openId, String nickName, String headImg, String ownerName, String licenseNumber, String phone, String idCard, Boolean transfer, Long transferTime, Long insuranceDeadline) {
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

    public Integer getQuoteStateCode() {
        return quoteStateCode;
    }

    public void setQuoteStateCode(Integer quoteStateCode) {
        this.quoteStateCode = quoteStateCode;
    }

    public Long getLeastQuoteTime() {
        return leastQuoteTime;
    }

    public void setLeastQuoteTime(Long leastQuoteTime) {
        this.leastQuoteTime = leastQuoteTime;
    }

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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
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


    public Long getLeastQueryTime() {
        return leastQueryTime;
    }

    public void setLeastQueryTime(Long leastQueryTime) {
        this.leastQueryTime = leastQueryTime;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }


    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
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


    public Boolean isToubao() {
        return toubao;
    }

    public void setToubao(Boolean toubao) {
        this.toubao = toubao;
    }

    public String getQuoteState() {
        //自定义枚举类意思
    	/*if(Tools.notEmpty(this.quoteState)){
    		
    	}
    	return INSURANCE.getQuotestateName(Integer.parseInt(this.quoteState));*/
        return quoteState;
    }

    public void setQuoteState(String quoteState) {
        this.quoteState = quoteState;
    }

    public Boolean isCashback() {
        return cashback;
    }

    public void setCashback(Boolean cashback) {
        this.cashback = cashback;
    }


    public Long getLeastOrderTime() {
        return leastOrderTime;
    }

    public void setLeastOrderTime(Long leastOrderTime) {
        this.leastOrderTime = leastOrderTime;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
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