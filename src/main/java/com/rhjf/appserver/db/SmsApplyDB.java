package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.util.UtilsConstant;

public class SmsApplyDB extends DBBase{
	
	public static int insertSmsCode(String phone,String smsCode){
		//删除以前的校验码
		String sql="insert into tab_smscode(ID,phoneNum,smsCode) values( '" +UtilsConstant.getUUID() + "' ,'"+phone+"','"+smsCode+"')";
		return executeSql(sql, null);
	}

	
	public static String getSmsCode(Object[] object){
		String sql = "select * from tab_smscode where phoneNum=? order by insertTime desc ";
		Map<String,Object> map = queryForMap(sql, object);
		String code = null;
		if(map!=null&&!map.isEmpty()){
			code = UtilsConstant.ObjToStr(map.get("smsCode"));
		}
		
		return code;
	}
	
	
	public static int delSmsCode(Object[] obj){
		String sql = "delete from tab_smscode where phoneNum=?";
		return executeSql(sql, obj);
	}
}
