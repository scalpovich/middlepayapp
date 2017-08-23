package com.rhjf.appserver.util;

import java.text.SimpleDateFormat; 
import java.util.Date;

public class DateUtil {

	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	
	public static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

	public static String yyyyMMdd = "yyyyMMdd";
	
	public static String HHmmss = "HHmmss";
	
	
	public static String HHmm = "HH-mm";
	
	

	/**
	 *   获取当前时间时间
	 * @param format
	 * @return
	 */
	public static String getNowTime(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
		
	}
	public static String getDateTimeTemp() {
		java.text.SimpleDateFormat d = new java.text.SimpleDateFormat();
		d.applyPattern(yyyyMMddHHmmss);
		java.util.Date nowdate = new java.util.Date();
		String str_date = d.format(nowdate);
		return str_date;
	}
	
}
