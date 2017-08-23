package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

public class OpenKuaiDB extends DBBase{

	
	public static  Map<String,Object> getOpenKuai(Object[] obj){
		String sql = "select * from tab_openkuai where bankCardNo=?";
		return queryForMap(sql, obj);
	}
	
	
	public static int save(Object[] obj){
		String sql = "INSERT INTO tab_openkuai (ID,UserID,bankCardNo,createDate,encrypt,statusCode) VALUES (?,?,?,now(),?,?) "
				+ " ON DUPLICATE KEY UPDATE encrypt=?";
		return executeSql(sql, obj);
	}
	
	
	public static List<Map<String,Object>> kuaiCardlist(Object[] obj){
		String sql = "select a.*  , b.bankName  from tab_openkuai as a INNER JOIN tab_pay_binverify as b"
				+ " on b.verifyCode = SUBSTRING(a.bankCardNo,1,b.verifyLength) where UserID=? and encrypt!=0";
		return queryForList(sql, obj);
	}
	
}
