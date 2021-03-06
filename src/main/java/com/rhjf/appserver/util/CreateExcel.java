package com.rhjf.appserver.util;

import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CreateExcel {

	
	
	/**
	 * 
	 * @param title    excel 标题
	 * @param list     excel 内容
	 * @param pathName    文件保存路径
	 * @param fileName    文件名
	 * @throws IOException
	 */
	public static  void createExcel( String[] title , List<Object[]> list ,String pathName , String  fileName) throws IOException{
		if(!new File(pathName).exists()){
			new File(pathName).mkdirs();
		}
		
		   // 创建Excel的工作书册 Workbook,对应到一个excel文档  
	    HSSFWorkbook wb = new HSSFWorkbook();  
	  
	    // 创建Excel的工作sheet,对应到一个excel文档的tab  
	    HSSFSheet sheet = wb.createSheet();  
	  
	    // 创建Excel的sheet的一行  
	    HSSFRow row = sheet.createRow(0);  
	    
	    HSSFCell cell = row.createCell(0); 
	    cell.setCellValue(title[0]);
    	
	    /** 设置标题 **/
	    for (int i = 1; i < title.length; i++) {
		    cell = row.createCell(i);
		    cell.setCellValue(title[i]);
		}
	    
	    
	    for(int i=0 ; i< list.size() ; i++ ){
	    	
			row = sheet.createRow(i + 1);
			
			for (int j = 0; j < list.get(i).length; j++) { 
				cell = row.createCell(j);
				cell.setCellValue(list.get(i)[j]==null?"":list.get(i)[j].toString());
			}
	    }
	    FileOutputStream os = new FileOutputStream(pathName + fileName +".xls");  
	    wb.write(os);  
	    os.close();  
	    wb.close();
	}
}
