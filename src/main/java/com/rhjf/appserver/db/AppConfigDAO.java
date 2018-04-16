package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.util.UtilsConstant;

/**
 * @author hadoop
 */
public class AppConfigDAO extends DBBase{
	
	
	
	/**
	 *   获取T0附加手续费
	 * @return
	 */
	public static int t0Additional(){
		String sql = "select * from tab_appconfig";
		Map<String,Object> map = queryForMap(sql, null);
		return Integer.parseInt(UtilsConstant.ObjToStr(map.get("T0AttachFee")));
	}
	
	
	/**
	 *    查询交易配置信息
	 * @return
	 */
	public static Map<String,Object> getTradeConfig(){
		String sql = "select T0StartHour , T0StartMinute , T0EndHour , T0EndMinute , T0AttachFee , T0MinAmount , KuaiT0Time , LargeAmount , KuaiDelayTime from tab_appconfig";
		return queryForMap(sql, null);
	}

}
