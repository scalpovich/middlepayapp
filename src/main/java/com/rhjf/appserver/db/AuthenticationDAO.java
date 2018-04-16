package com.rhjf.appserver.db;

import java.util.ArrayList; 
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.model.AuthenticationRecord;
import com.rhjf.appserver.util.UtilsConstant;

public class AuthenticationDAO extends DBBase{
	
	
	public static int Authentication(Object[] obj){
		String sql = "insert into tab_authentication(ID,UserID,IdNumber,PhoneNumber, RealName , BankCardNo,OrderId,RespCode,RespDesc) values (?,?,?,?,?,?,?,?,?)";
		return  executeSql(sql, obj);
	}
	
	/**
	 * 获取信用卡开通银行列表
	 * @return
	 */
	public static List<AuthenticationRecord> getAuthenticationRecordList(Object[] obj){
		
		String sql = "select * from tab_authentication where RespCode = ? and UserID = ?";
		List<AuthenticationRecord> list =  new ArrayList<AuthenticationRecord>();
		List<Map<String,Object>> list2 = queryForList(sql, obj);
		
		for (Map<String, Object> map : list2) {
			try {
				list.add(UtilsConstant.mapToBean(map, AuthenticationRecord.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	
	public static Map<String,Object> bankAuthenticationInfo(Object[] obj){
		String sql = "select * from tab_authentication where BankCardNo=? and RespCode=00";
		return queryForMap(sql, obj);
	}
	
	
	
	public static int addAuthencationInfo(Object[] obj){
		String sql = "insert into tab_authentication (ID , IdNumber , PhoneNumber , RealName , BankCardNo , RespCode , RespDesc) "
				+ "values (?,?,?,?,?,? ,?)";
		return executeSql(sql, obj);
	}
	
	
	

}
