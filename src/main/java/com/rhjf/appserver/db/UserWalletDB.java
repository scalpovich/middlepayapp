package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.UtilsConstant;

public class UserWalletDB extends DBBase {

	/**
	 *    查询用户钱包信息
	 * @param obj
	 * @return
	 */
	public static Map<String,String> UserWalletByUserID(Object[] obj){
		String sql = "select ID,UserID , WalletBalance , date(LastWithdrawDate) as LastWithdrawDate , ContinuedDays"
				+ " from tab_user_wallet where UserID=?";
		return queryForMapStr(sql, obj);
	}
	
	
	/**
	 *   将收益转入钱包
	 * @param user
	 * @param available_amount
	 * @return
	 */
	public static int[] trunwallet(TabLoginuser user , String available_amount , Integer ContinuedDays){
		String updateLoginUserFeeBalance = "update tab_loginuser set FeeBalance=0 where ID='" +user.getID()+ "'";
		String updateUserWalletFeebalance = "update tab_user_wallet set WalletBalance=WalletBalance+'"+(Integer.parseInt(user.getFeeBalance())+Integer.parseInt(available_amount))+"' , "
				+ "LastWithdrawDate=now() ,  ContinuedDays="+ContinuedDays+" where UserID='" + user.getID() + "'";
		String updateCapital = "update tab_capital set available_amount=0 where creditmer_id='" + user.getID() + "'";
		String insertTurnWallet = "insert into tab_user_turnwallter (ID,UserID , TurnAmount , TurnDateTime) values "
				+ "('"+UtilsConstant.getUUID()+"' , '" + user.getID() +"' ,  ' " +(Integer.parseInt(user.getFeeBalance())+Integer.parseInt(available_amount))+ "' , now())";
		String updateProftStatus = "update tab_user_profit set ProfitStatus=1 where UserID='" + user.getID() + "'";
		String updateCreditProfitStatus = "update tab_splitting_detail set ProfitStatus=1 where customer_id='" + user.getID() + "'";
		return executeBatchSql(new String[]{updateLoginUserFeeBalance ,updateUserWalletFeebalance ,updateCapital ,  insertTurnWallet ,updateProftStatus , updateCreditProfitStatus});
	}
	
	
	/**
	 *   在钱包表中保存一条记录 
	 * @param obj
	 * @return
	 */
	public static int saveUserWallet(Object[] obj){
		String sql = "insert into tab_user_wallet (ID,UserID , WalletBalance , LastWithdrawDate , ContinuedDays) values "
				+ " (?,?,0,now(),0)";
		return executeSql(sql, obj);
	}
	
	
	/**
	 *   用户提现操作
	 * @return
	 */
	public static int[] drawMoney(Integer drawAmount , String userID , String termSerno){
		String updatewalletBalance = "update tab_user_wallet set WalletBalance=WalletBalance-"+drawAmount+" where UserID='"+userID+"'";
		String insertWithraw = "INSERT INTO tab_withdraw(applyMoney,applyUserID,applyDate,termserno,TxType) VALUES("+drawAmount+",'"+userID+"',NOW(),'"+termSerno+"',0)";
		return executeBatchSql(new String[]{updatewalletBalance ,insertWithraw});
	}
	
	
	
}
