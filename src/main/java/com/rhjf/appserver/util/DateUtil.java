package com.rhjf.appserver.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	
	public static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

	public static String yyyyMMdd = "yyyyMMdd";
	
	public static String HHmmss = "HHmmss";
	
	public static String HHmm = "HH-mm";

	public static String yyyy_MM_dd = "yyyy-MM-dd";
	
	public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

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
	

	/**
	 *    获取指定当天日之前的几天的日期
	 * @param day
	 * @return
	 */
	public static String getDateAgo(String dateStr , int day , String format) throws Exception{
		Calendar calendar1 = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat(format);
		Date date = sdf1.parse(dateStr);//初始日期
		calendar1.setTime(date);
		calendar1.add(Calendar.DATE, -day);
		String daysAgo = sdf1.format(calendar1.getTime());
		return  daysAgo;
	}
	
	
	
	/**
	 *    获取两个时间内 的所有日期
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static List<String> getBetweenDates(String startTime , String endTime) throws Exception{
		
		List<String> dataList = new ArrayList<>();
		dataList.add(startTime);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dBegin = sdf.parse(startTime);
		Date dEnd = sdf.parse(endTime);

		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			dataList.add(sdf.format(calBegin.getTime()));
		}
		return dataList;
	}
	
	
	
	
	
	/**
     * date2比date1多的天数
     * @param date1    
     * @param date2
     * @return    
     */
    public static long differentDays(Date date1,Date date2){
    	
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(date1);
		// long time1 = cal.getTimeInMillis();
		// cal.setTime(date2);
		// long time2 = cal.getTimeInMillis();
		// long between_days=(time2-time1)/(1000*3600*24);
		//
		// return between_days;
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		// 同一年
		if (year1 != year2){
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				// 闰年
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0){
					timeDistance += 366;
				} else {
					// 不是闰年
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {
			// 不同年
			System.out.println("判断day2 - day1 : " + (day2 - day1));
			return day2 - day1;
		}
    }
	
	
	public static String getPreMonth(String repeatDate) {
		String lastMonth = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		int year = Integer.parseInt(repeatDate.substring(0, 4));
		int day = Integer.parseInt(repeatDate.substring(6));
		String monthsString = repeatDate.substring(4, 6);
		int month;
		if ("0".equals(monthsString.substring(0, 1))) {
			month = Integer.parseInt(monthsString.substring(1, 2));
		} else {
			month = Integer.parseInt(monthsString.substring(0, 2));
		}
		cal.set(year, month, day);
		lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}
	
	
	
}
