package com.freelycar.service; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freelycar.dao.AdminDao;
import com.freelycar.entity.Admin;
import com.freelycar.util.RESCODE;  
/**  
 *  
 */  
@Transactional
@Service
public class AdminService  
{  
    /********** 注入AdminDao ***********/  
    @Autowired
	private AdminDao adminDao;
    
	//注册一个User
    public Map<String,Object> registerAdmin(Admin admin){
        List<Admin> list = adminDao.listAdmin(admin,0,0);
        if(list==null || list.size()==0){
            adminDao.saveAdmin(admin);
            return RESCODE.SUCCESS.getJSONRES();
        }else{
            return RESCODE.USER_EXIST.getJSONRES();
        }
    }
    
    
    //修改密码
    public Map<String,Object> modifyPass(int userid, String oripass, String newpass){
        Admin admin = adminDao.getAdminById(userid);
        String pass = admin.getPassword();
        if (pass.equals(oripass)) {
        	Admin e = new Admin();
        	e.setPassword(newpass);
        	e.setId(userid);
        	adminDao.updateAdmin(e);
        	return RESCODE.SUCCESS.getJSONRES(admin);
        } else {
        	return RESCODE.USER_NAMEPAS_ERROR.getJSONRES();
        }
    }
    
    //登陆
    public Map<String,Object> login(String username, String password){
        Admin admin = adminDao.findAdminByUserAndPass(username, password);
        if (admin==null) {
        	return RESCODE.NOT_FOUND.getJSONRES();
        } else {
        	return RESCODE.SUCCESS.getJSONRES(admin);
        }
    }
    
    
    
    
    //增加一个Admin
    public Map<String,Object> saveAdmin(Admin admin){
		
		adminDao.saveAdmin(admin);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	
	/**
		分页查询
	 * @param page 从第几页开始查询
	 * @param number 每页数量
	 * @return
	 */
	public Map<String,Object> listAdmin(Admin admin, int page,int number){
	    int from = (page-1)*number;
	    List<Admin> listPage = adminDao.listAdmin(admin,from, number);
	    if(listPage !=null && !listPage.isEmpty()){
	    	long count = adminDao.getAdminCount(admin);
			return RESCODE.SUCCESS.getJSONRES(listPage,(int)Math.ceil(count/(float)number),count);
		} 
		return RESCODE.NOT_FOUND.getJSONRES();
    }
	
	
	//根据id删除Admin
	public Map<String,Object> removeAdminById(String id){
		boolean res =  adminDao.removeAdminById(id);
		if (res) {
			return RESCODE.SUCCESS.getJSONRES();
		} else {
			return RESCODE.DELETE_FAIL.getJSONRES();
		}
	}
	
	//更新Admin
	public Map<String,Object> updateAdmin(Admin admin){
	    adminDao.updateAdmin(admin);
	    return RESCODE.SUCCESS.getJSONRES();
	}
    
}