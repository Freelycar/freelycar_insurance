package com.freelycar.controller; 

import com.freelycar.entity.Admin;
import com.freelycar.service.AdminService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**  
 *  
 */  
@RestController
@RequestMapping(value = "/admin")
public class AdminController  
{  
    /********** 注入AdminService ***********/  
    @Autowired
	private AdminService adminService;




	//注册一个Admin
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Map<String,Object> registerAdmin(Admin admin){
        return adminService.registerAdmin(admin);
    }
    
    //修改密码
    @RequestMapping(value = "/modifypass",method = RequestMethod.POST)
    public Map<String,Object> modifyPass(int userid, String oripass, String newpass){
        return adminService.modifyPass(userid, oripass, newpass);
    }
    
    //登陆
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,Object> login(@RequestBody Admin admin){
        return adminService.login(admin.getUsername(), admin.getPassword());
    }

    
    //增加一个Admin
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String,Object> saveAdmin(Admin admin){
		return adminService.saveAdmin(admin);
	}
	
	//查询所有的Admin	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Map<String,Object> listAdmin(Admin admin, int _pageNumber,int _pageSize){
		return adminService.listAdmin(admin, _pageNumber, _pageSize);
	}
	
	//根据id删除Admin
	@RequestMapping(value = "/remove",method = RequestMethod.GET)
	public Map<String,Object> removeAdminById(String id){
		return adminService.removeAdminById(id);
	}
	
	//更新Admin
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String,Object> updateAdmin(Admin admin){
	    return adminService.updateAdmin(admin);
	}
    
}