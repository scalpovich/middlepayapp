package com.rhjf.appserver.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.rhjf.appserver.db.DBBase;


public class Test extends DBBase{

	public static void main(String[] args) throws ParseException {

		Calendar c = Calendar.getInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
		// 开始时间必须小于结束时间
		Date beginDate = dateFormat1.parse("20170323");
		Date endDate = dateFormat1.parse("20170913");
		Date date = beginDate;
		while (!date.equals(endDate)) {
			
			String shijian = dateFormat1.format(date);
			
			String sql = "insert into tab_usertrade_statistical (ID, UserID , Amount , TradeDate , Fee , TradeCount) "
					+ "select UPPER(UUID()) , a.ID , ifnull(sum(b.Amount) , 0) , ?  ,   ifnull(sum(b.Fee),0) , count(b.ID) as count "
					+ " from tab_loginuser as a left JOIN (select *  from tab_pay_order where TradeDate=? and PayRetCode='0000' ) as b"
					+ " on a.ID=b.UserID  where DATE_FORMAT(a.RegisterTime,'%Y%m%d')<=?  GROUP BY a.id ";
			
			executeSql(sql, new Object[]{shijian,shijian,shijian});
			
			c.setTime(date);
			c.add(Calendar.DATE, 1); // 日期加1天
			date = c.getTime();
		}
	}
}
