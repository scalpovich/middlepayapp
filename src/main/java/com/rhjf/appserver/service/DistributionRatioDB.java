package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.db.DBBase;

public class DistributionRatioDB extends DBBase{
	
	
	public static Map<String,Object> profitMap(){
		String sql = "select * from tab_distribution_ratio";
		return queryForMap(sql, null);
	}

}
