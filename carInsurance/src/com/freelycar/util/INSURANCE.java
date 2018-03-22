package com.freelycar.util;

public enum INSURANCE {

	INSURANCE_NO_1("车辆损失险"), 
	INSURANCE_NO_2("第三者责任险"), 
	INSURANCE_NO_3("全车盗抢险"), 
	INSURANCE_NO_4("司机座位责任险"), 
	INSURANCE_NO_5("乘客座位责任险"), 
	INSURANCE_NO_6("玻璃单独破碎险"),
	INSURANCE_NO_7("车身划痕损失险"),
	INSURANCE_NO_8("自燃损失险"), 
	INSURANCE_NO_9("发动机涉水损失险"), 
	INSURANCE_NO_10("指定专修厂特约条款"), 
	INSURANCE_NO_11("无法找到第三方特约险"),
	
	
	
	//报价状态
	QUOTESTATE_NO_0(0,"待报价"),
	QUOTESTATE_NO_1(1,"已报价"),
	QUOTESTATE_NO_2(2,"核保失败"),
	QUOTESTATE_NO_3(3,"核保成功"),
	QUOTESTATE_NO_4(4,"待配送"),
	QUOTESTATE_NO_5(5,"已配送"),
	QUOTESTATE_NO_6(6,"待签收"),
	QUOTESTATE_NO_7(7,"已投保");

	private int code;// 险种名称
	private String name;// 险种名称
	private INSURANCE(String name) {
		this.name = name;
	}
	private INSURANCE(int code,String name) {
		this.code = code;
		this.name = name;
	}
	
	//根据number得到名称
	public static String getInsuranceTypeName(int number){
		return INSURANCE.valueOf("INSURANCE_NO_"+number).name;
	}
	
	//根据state来得到报价状态的中文意思
	public static String getQuotestateName(int number){
		return INSURANCE.valueOf("QUOTESTATE_NO_"+number).name;
	}
	
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
