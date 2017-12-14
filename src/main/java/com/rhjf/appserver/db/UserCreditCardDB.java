package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

/**
 *    用户信用卡
 * @author hadoop
 *
 */
public class UserCreditCardDB extends DBBase{

	
	/**
	 *   添加信用卡
	 * @param obj
	 * @return
	 */
	public static int saveUserCreditCard(Object[] obj){
		String sql = "insert into tab_pay_usercreditcard (id,userID,name,bankCardNo,bankName,bankSubbranch,bankCode,bankProv,bankCity,bankSymbol,cvn2,expired,payerPhone,repayDate,createDate)"
				+ "values  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now()) on duplicate key update "
				+ "cvn2=? , expired=? , payerPhone=? , repayDate=? , createDate=now() , active=1";
		return executeSql(sql , obj);
	}
	
	
	
	public static int delCreditCard(Object[] obj){
		String sql = "update tab_pay_usercreditcard set active=0 where userID=? and bankCardNo=?";
		return executeSql(sql, obj);
	}
	
	
	public static List<Map<String,Object>> creditCardList(Object[] obj){
		String sql = "select * from tab_pay_usercreditcard where userID=? and active=1 order by createDate desc";
		return queryForList(sql, obj);
	}
	
	
	
	/**
	 *   更新信用卡还款日
	 * @param obj
	 * @return
	 */
	public static int updateRepayDay(Object[] obj){
		String sql = "update tab_pay_usercreditcard set repayDate=? where userID=? and bankCardNo=?";
		return executeSql(sql , obj);
	}
}
