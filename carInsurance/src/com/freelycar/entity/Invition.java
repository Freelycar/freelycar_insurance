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
public class Invition  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String name;  //渠道名称;
	
    private String invcode;  //邀请码;
	
    private String remark;  //备注;
	
    
    private Long createTime;  //创建时间;
    /********** constructors ***********/  
    public Invition() {  
      
    }  
  
    public Invition(String name, String invcode, String remark, Long createTime) {  
        this.name = name;  
        this.invcode = invcode;  
        this.remark = remark;  
        this.createTime = createTime;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
      
	
    public String getInvcode() {  
        return invcode;  
    }  
  
    public void setInvcode(String invcode) {  
        this.invcode = invcode;  
    }  
      
	
    public String getRemark() {  
        return remark;  
    }  
  
    public void setRemark(String remark) {  
        this.remark = remark;  
    }  
      
	
    public Long getCreateTime() {  
        return createTime;  
    }  
  
    public void setCreateTime(Long createTime) {  
        this.createTime = createTime;  
    }  
      

	@Override
	public String toString() {
		return "Invition [id=" + id 
				+ ", name=" + name
				+ ", invcode=" + invcode
				+ ", remark=" + remark
				+ ", createTime=" + createTime
				+ "]";
    }

}