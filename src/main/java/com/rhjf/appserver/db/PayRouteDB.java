package com.rhjf.appserver.db;

import java.util.Map;

/**
 * @author hadoop
 * @version 1.0
 * 2018年3月19日 - 下午2:54:24
 * 2018
 */
public class PayRouteDB extends DBBase{
	
	
	
	public Map<String,Object> routeMap(String payChannel){
		String sql = "select * from tab_pay_route where payChannel=? and active=1 order by routeLevel asc;";
		return queryForMap(sql, new Object[]{payChannel});
	}

}
