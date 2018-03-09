package com.freelycar.util;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MulFileUpload {
	private List<MultipartFile> files;

	
	//上传图片
    public static String savePic(MultipartFile mf){
        if(mf.isEmpty())
        	return null;
    	
        Tools.createDir(Constant.UPLOAD_FOLDER);
    	
        String fileName = mf.getOriginalFilename();
        // 获取后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."),
                fileName.length());
        
        String newName = System.currentTimeMillis()+suffix;
        
        try {
            mf.transferTo(new File(Constant.UPLOAD_FOLDER + newName));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return newName;
    }
    
    //上传Excel
    public static List<List<String>> saveExcel(MultipartFile mf) {
        if(mf.isEmpty())
            return null;
        
        Tools.createDir(Constant.UPLOAD_FOLDER);
        
        String fileName = mf.getOriginalFilename();
        // 获取后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."),
                fileName.length());
        
        String newName = System.currentTimeMillis()+suffix;
        
        try {
            mf.transferTo(new File(Constant.UPLOAD_FOLDER + newName));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        List<List<String>> analyzeExcel = ExcelHandler.analyzeExcel(Constant.UPLOAD_FOLDER + newName);
        
        return analyzeExcel;
    }
	

    
    
	public List<MultipartFile> getFiles() {

		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
}
