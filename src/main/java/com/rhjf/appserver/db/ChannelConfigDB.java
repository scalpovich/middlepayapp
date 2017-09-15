package com.rhjf.appserver.db;

import java.util.Map;

public class ChannelConfigDB extends DBBase{

	
	
	public static Map<String,Object> getChannelConfig(int payChannelID){
		String sql = "select * from tab_channel_config where PayChannel = ?";
		return queryForMap(sql, new Object[]{payChannelID});
	}
}
