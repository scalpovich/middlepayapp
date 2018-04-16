package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.util.UtilsConstant;

public class DeviceTokenDAO extends DBBase{

	
	/** 
	 *		保存更新设备信息 
	 * @param obj
	 * @return
	 */
	public static int saveOrUpToken(Object[] obj){
		String sql = "INSERT INTO tab_devicetoken (UserID , DeviceToken , DeviceType ) VALUES (?,?,?)  ON DUPLICATE KEY UPDATE DeviceToken=? , DeviceType=?";
		return executeSql(sql, obj);
	}
	
	/**
	 *   用户退出 删除devicetoken
	 * @param obj
	 * @return
	 */
	public static int delkToken(Object[] obj){
		String sql = "delete from tab_devicetoken where UserID=?";
		return executeSql(sql, obj);
	}
	
	
	
	/**
	 * 
	 *    查询用户deviceToken
	 */
	public static String getDeviceToken(String userID){
		String sql = "select * from tab_devicetoken where UserID=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{userID});
		if(map!=null){
			String deviceToken = UtilsConstant.ObjToStr(map.get("DeviceToken"));
			return deviceToken;
		}
		return null;
	}
}
