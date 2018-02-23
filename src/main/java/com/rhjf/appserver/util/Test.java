package com.rhjf.appserver.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.DBBase;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.util.auth.AuthUtil;


public class Test extends DBBase{
	
	private static int fib(int n) throws Exception {  
	    if (n == 0) return 0;  
	    if (n == 1) return 1;  
	    int z = fib(n-1);
	    int x = fib(n-2);
	    System.out.println(n + ":" + z + " == " + x ); 
	    return z + fib(n-2);  
	}  
	
	public List<String> getBetweenDates(String tradeDate) throws Exception{

		
		List<String> dataList = new ArrayList<>();
		
		String startTime = DateUtil.getDateAgo(tradeDate, 3, DateUtil.yyyyMMdd);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dBegin = sdf.parse(startTime);
		Date dEnd = sdf.parse(tradeDate);

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
			System.out.println(sdf.format(calBegin.getTime())); 
			dataList.add(sdf.format(calBegin.getTime()));
		}
		return dataList;
	}
	
	

	public static void main(String[] args) throws Exception {

		
		
		
		Map<String, String> content = AuthUtil.authentication("林惠娥", "6214850212903782", "350521197906116546", "13917828049");
//				
		System.out.println(content.toString());
		
		
		
//		System.out.println(DESUtil.decode("pDOm9MY2z5M0fgWUSpMk6E3H" , "S3pb66qM69N4a395VSqA0/oX+EYHvQbk" )); 
//		System.out.println(DESUtil.decode("pDOm9MY2z5M0fgWUSpMk6E3H" , "wpansn+HS1SJAcaIWu+fodLX5HvUDDHl")); 
//		System.out.println(DESUtil.decode("pDOm9MY2z5M0fgWUSpMk6E3H" , "qsQ0aCPFLD6M4R0dXIf/Ew==")); 
//		System.out.println(UtilsConstant.getUUID());
		
//		System.out.println(fib(5));
		
		
//		for (int i = 1; i < 15; i++) {
//			String day = "0";
//			if(i < 10){
//				day = "0" + i;
//			}else{
//				day = "" + i;
//			}
//			
//			System.out.println("tar -czvf trade.log.2017-09-"+day+".tar.gz  Trade.log.2017-09-"+day+"-*");
//		}
		
		
		
//		Calendar c = Calendar.getInstance();
//		DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
//		// 开始时间必须小于结束时间
//		Date beginDate = dateFormat1.parse("20170323");
//		Date endDate = dateFormat1.parse("20170913");
//		Date date = beginDate;
//		while (!date.equals(endDate)) {
//			
//			String shijian = dateFormat1.format(date);
//			
//			String sql = "insert into tab_usertrade_statistical (ID, UserID , Amount , TradeDate , Fee , TradeCount) "
//					+ "select UPPER(UUID()) , a.ID , ifnull(sum(b.Amount) , 0) , ?  ,   ifnull(sum(b.Fee),0) , count(b.ID) as count "
//					+ " from tab_loginuser as a left JOIN (select *  from tab_pay_order where TradeDate=? and PayRetCode='0000' ) as b"
//					+ " on a.ID=b.UserID  where DATE_FORMAT(a.RegisterTime,'%Y%m%d')<=?  GROUP BY a.id ";
//			
//			executeSql(sql, new Object[]{shijian,shijian,shijian});
//			
//			c.setTime(date);
//			c.add(Calendar.DATE, 1); // 日期加1天
//			date = c.getTime();
//		}
	}
}
