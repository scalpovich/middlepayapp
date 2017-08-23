package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.util.UtilsConstant;

public class AppconfigDB extends DBBase{
	
	
	
	/**
	 *   获取T0附加手续费
	 * @return
	 */
	public static int T0additional(){
		String sql = "select * from tab_appconfig";
		Map<String,Object> map = queryForMap(sql, null);
		return Integer.parseInt(UtilsConstant.ObjToStr(map.get("T0AttachFee")));
	}
	
	
	/**
	 *    查询交易配置信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> getTradeConfig(){
		String sql = "select T0StartHour , T0StartMinute , T0EndHour , T0EndMinute , T0AttachFee , T0MinAmount , KuaiT0Time from tab_appconfig";
		return queryForMap(sql, null);
	}

}
