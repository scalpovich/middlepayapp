package com.rhjf.appserver.db;

import java.util.Map;

/**
 *   信用卡收益
 * @author hadoop
 *
 */
public class CapitalDB extends DBBase{

	
	/**
	 *   查询用户信用卡收益
	 * @param obj
	 * @return
	 */
	public static Map<String,String> getCapitalByUserID(Object[] obj){
		String sql = "select id , aggregate_amount , available_amount , creditmer_id  from tab_capital where creditmer_id=?";
		return queryForMapStr(sql, obj);
	}
}
