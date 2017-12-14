package com.rhjf.appserver.db;

import java.util.Map;

public class TurnWalletDB extends DBBase{

	
	/**
	 *   查询用户一共向钱包转入了多少钱
	 * @param userID
	 * @return
	 */
	public static Integer turnWalletTotalAmount(String userID){
		String sql = "select ifnull(sum(TurnAmount) , '0') as TurnAmount"
				+ " from tab_user_turnwallter where UserID = ?";
		Map<String,String> map = queryForMapStr(sql, new Object[]{userID});
		try {
			return Integer.parseInt(map.get("TurnAmount"));
		} catch (Exception e) {
			return 0;
		}
	}
	
}
