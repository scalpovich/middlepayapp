package com.rhjf.appserver.db;

/**
 *   用户结算卡
 * @author hadoop
 *
 */
public class PayUserBankCardDB extends  DBBase{

	
	
	public static int updateUserCreditCard(Object[] obj){
		String sql = "update tab_pay_userbankcard set SettleCreditCard = ? where UserID=?";
		return executeSql(sql, obj);
	}
}
