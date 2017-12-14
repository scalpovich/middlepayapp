package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

public class ADListDB extends DBBase{
	
	
	public static List<Map<String,Object>> adlist(){
		String sql = "select * from tab_adlist";
		return queryForList(sql, null);
	}

}
