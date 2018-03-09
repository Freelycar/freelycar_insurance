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
public class Admin  
{  
    /********** attribute ***********/  
     //主键 自增
    @Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
    private Integer id;
	
    private String username;  //用户名;
	
    private String password;  //密码;
    /********** constructors ***********/  
    public Admin() {  
      
    }  
  
    public Admin(String username, String password) {  
        this.username = username;  
        this.password = password;  
    }  
  
    /********** get/set ***********/  
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
	
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
      
	
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
      

	@Override
	public String toString() {
		return "Admin [id=" + id 
				+ ", username=" + username
				+ ", password=" + password
				+ "]";
    }

}