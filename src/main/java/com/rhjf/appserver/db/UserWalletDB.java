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
		/** 将用户表可以转入余额更新为0 **/
		String updateLoginUserFeeBalance = "update tab_loginuser set FeeBalance=0 where ID='" +user.getID()+ "'";
		/**  更新钱包中的可提现余额 （用户表余额和信用卡余额） **/
		String updateUserWalletFeebalance = "update tab_user_wallet set WalletBalance=WalletBalance+'"+(Integer.parseInt(user.getFeeBalance())+Integer.parseInt(available_amount))+"' , "
				+ "LastWithdrawDate=now() ,  ContinuedDays="+ContinuedDays+" where UserID='" + user.getID() + "'";
		/** 将信用卡收益更新成0 **/
		String updateCapital = "update tab_capital set available_amount=0 where creditmer_id='" + user.getID() + "'";
		/**  保存一条转入记录 **/
		String insertTurnWallet = "insert into tab_user_turnwallter (ID,UserID , TurnAmount , TurnDateTime) values "
				+ "('"+UtilsConstant.getUUID()+"' , '" + user.getID() +"' ,  ' " +(Integer.parseInt(user.getFeeBalance())+Integer.parseInt(available_amount))+ "' , now())";
		/** 将收益状态更改成1 **/
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
	 *    用户从钱包中提现操作
	 * @return
	 */
	public static int[] drawMoney(Integer drawAmount , String userID , String termSerno , String bankCardNo , String orderNumber){
		String updatewalletBalance = "update tab_user_wallet set WalletBalance=WalletBalance-"+drawAmount+" where UserID='"+userID+"'";
		String insertWithraw = "INSERT INTO tab_withdraw(applyMoney,applyUserID,applyDate,termserno,TxType , AccountNo , OrderNumber) VALUES("+(drawAmount-100)+",'"+userID+"',NOW(),'"+termSerno+"',0 , '" + bankCardNo +"' , '" +orderNumber+ "')";
		return executeBatchSql(new String[]{updatewalletBalance ,insertWithraw});
	}
	
	
	
}
