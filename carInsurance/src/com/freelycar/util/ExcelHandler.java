package com.freelycar.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelHandler {
	public static void main(String[] args) throws FileNotFoundException {
		List<List<String>> analyzeExcel = analyzeExcel("C:\\Users\\Administrator\\Documents\\Tencent Files\\448267696\\FileRecv\\单选.xlsx");
		
		for(List<String> row : analyzeExcel){
			for(String cell : row){
				System.out.println(cell);
			}
		}
	}
	
	//默认从0行开始
	private static final int DEFAULT_FROM_ROW = 0;
	
	//默认读取sheet1
	private static final String DEFAULT_SHEET = "Sheet1";
	
	/**
	 * 默认方法
	 * @param filepath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> analyzeExcel(String filepath) {
		return analyzeExcel(filepath, DEFAULT_FROM_ROW,DEFAULT_SHEET);
	}
	/**
	 * 从fromRow开始 读取
	 * @param filepath
	 * @param fromRow
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> analyzeExcel(String filepath, int fromRow) {
		return analyzeExcel(filepath, fromRow, DEFAULT_SHEET);
	}
	
	/**
	 * 从fromRow开始 读取 sheetName的excel
	 * @param filepath
	 * @param sheetName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> analyzeExcel(String filepath,String sheetName) {
		return analyzeExcel(filepath, DEFAULT_FROM_ROW, sheetName);
	}
	
    public static List<List<String>> analyzeExcel(String filepath,int fromRow,String sheetName) {
    	List<List<String>> result = null;
    	Workbook workbook = null;
    	NPOIFSFileSystem fs = null;
    	OPCPackage pkg = null;
    	Iterator<Row> rowIterator = null;
    	
		try {
			if (filepath.endsWith(".xls")) {
				fs = new NPOIFSFileSystem(new File(filepath));
				//workbook = new HSSFWorkbook(fs.getRoot(),true);
				workbook = WorkbookFactory.create(fs);
				HSSFSheet sheet = (HSSFSheet) workbook.getSheet(sheetName);
				rowIterator = sheet.iterator();
			} else if (filepath.endsWith(".xlsx")) {
				pkg = OPCPackage.open(new File(filepath));
				//workbook = new XSSFWorkbook(pkg);
				workbook = WorkbookFactory.create(pkg);
				XSSFSheet sheet =  (XSSFSheet) workbook.getSheet(sheetName);
				rowIterator = sheet.iterator();
			} else {
				return Collections.emptyList();
			}

			result = new ArrayList<List<String>>();
			List<String> rowData = null;
			int row_index = 1;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row_index++ < fromRow){
					continue;//过滤第fromRow行
				}
				//https://www.hellojava.com/article/1649
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
				int lastCellNum = row.getLastCellNum();
				
				if(lastCellNum!=physicalNumberOfCells){
					//throw new ExcelAnalyzeException(row_index+"");
					throw new RuntimeException();
				}
				

				Iterator<Cell> cellIterator = row.cellIterator();
				rowData = new ArrayList<String>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					//统一转成String
					HSSFDataFormatter hdf = new HSSFDataFormatter();
					rowData.add(hdf.formatCellValue(cell));
				}
				result.add(rowData);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				if(fs != null){
					fs.close();
				}
				if(pkg != null){
					pkg.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
    
    
    public static HSSFWorkbook getInstanceOfWb(){
    	HSSFWorkbook wb = new HSSFWorkbook();
    	return wb;
    }
    
    
    /**
     * 生成excel 并下载
     * @param titles
     * @param list
     * @param response
     */
    public static void generateExcelSheet(HSSFWorkbook wb, List<String> titles, List<List<String>> list) {

	    // 第一步，创建一个webbook，对应一个Excel文件  
	    //HSSFWorkbook wb = new HSSFWorkbook();
	    
    	// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet 
    	HSSFSheet sheet = wb.createSheet();
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        //style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       
        //设置表头字段名
        HSSFCell cell;
        int m=0;
        for (String fieldName : titles) {
			sheet.setDefaultColumnStyle(m, style);
        	cell = row.createCell(m);
            cell.setCellValue(fieldName);              
            m++;
        }
        
        for (int i = 0,rowlen = list.size(); i < rowlen; i++) {  
            row = sheet.createRow(i + 1);
            List<String> rowItems = list.get(i);
            for(int j = 0,collen = rowItems.size(); j < collen; j++){
                row.createCell(j).setCellValue(rowItems.get(j));
            }
        }  
        
	}
	
    
	private static void downExcel(HSSFWorkbook wb,HttpServletResponse response){
		// 第六步，实现文件下载保存  
    	try {
			response.setHeader("content-disposition", "attachment;filename="
					+ URLEncoder.encode("导出文件", "utf-8") + ".xls");

			OutputStream out = response.getOutputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write( baos);
			byte[] xlsBytes = baos.toByteArray();
			out.write( xlsBytes);
			out.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
