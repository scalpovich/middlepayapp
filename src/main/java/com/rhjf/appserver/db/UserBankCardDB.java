package com.rhjf.appserver.db;

import java.util.Map;

public class UserBankCardDB extends DBBase{

	
	public static int addCreditCardNo(Object[] obj){
		String sql = "update tab_pay_userbankcard set SettleCreditCard=? where UserID=?";
		return executeSql(sql, obj);
	}
	
	
	
	/**
	 *   查询结算信息
	 * @param userID
	 * @return
	 */
	public static Map<String,Object> getBankInfo(String userID){
		String sql = "select * from tab_pay_userbankcard where UserID=?";
		return queryForMap(sql, new Object[]{userID});
	}
}
