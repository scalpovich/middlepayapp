package com.rhjf.appserver.db;

import java.util.Map;

public class SalesManDAO extends DBBase{

	
	
	public static Map<String,Object> salesManInfo(String ID){
		String sql = "select * from tab_salesman where ID=?";
		return queryForMap(sql, new Object[]{ID});
	}
	
	
	public static int saveSalesManProfit(Object[] obj){
		String sql = "insert into tab_salesman_profit (ID,SalesManID,TradeUserID,TradeID,DistributeProfit,Profit,TradeDate)"
				+ " values (?,?,?,?,?,?,now())";
		return executeSql(sql, obj);
	}
}
