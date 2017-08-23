package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;


/**
 * 用户联行号信息
 * 
 * @author a
 *
 */

public class BankCodeDB extends DBBase {
	
	
	public static Map<String, Object> bankBinMap(Object[] obj) {
		String sql = "select * from tab_pay_binverify where verifyCode = SUBSTRING(?,1,verifyLength)";
		return queryForMap(sql, obj);
	}
	
	
	public static Map<String,Object> bankInfo(Object[] obj){
		String sql = "select * from  tab_pay_bankcode  where BankName like  CONCAT('%', ? ,'%')  and BankBranch like CONCAT('%', ? ,'%') "
				+ " and ? like CONCAT(BankProv ,'%')  and  ? like  CONCAT(BankCity ,'%')";
		List<Map<String,Object>> list =  queryForList(sql, obj);
		System.out.println(list); 
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
	 
	
	
	/**
	 *   获取支行名称
	 * @param map
	 * @return
	 */
	public static List<Map<String,Object>> bankBranchList(Map<String,String> map){
		String sql = "SELECT BankBranch from tab_pay_bankcode"
				+ " where  BankName like CONCAT(CONCAT('%' , #{bankName}) , '%') and #{bankProv} like CONCAT(CONCAT('%' , BankProv) , '%') "
				+ " and  #{bankCity} like CONCAT(CONCAT('%' , bankCity),'%')";
		return queryForList(sql, new Object[]{map.get("bankName") , map.get("bankProv") , map.get("bankCity")});
	}
}
