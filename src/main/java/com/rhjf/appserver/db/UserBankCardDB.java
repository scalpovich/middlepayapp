package com.rhjf.appserver.db;


public class UserBankCardDB extends DBBase{

	
	
	public static int addCreditCardNo(Object[] obj){
		String sql = "update tab_pay_userbankcard set SettleCreditCard=? where UserID=?";
		return executeSql(sql, obj);
	}
}
