package com.rhjf.appserver.db;

import java.util.Map;

public class ChannelConfigDAO extends DBBase{

	
	
	public static Map<String,Object> getChannelConfig(int payChannelID , String ChannelID){
		String sql = "select * from tab_channel_config where PayChannel = ? and ChannelID=?";
		return queryForMap(sql, new Object[]{payChannelID , ChannelID});
	}
}
