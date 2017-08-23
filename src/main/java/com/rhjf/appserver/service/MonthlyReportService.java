package com.rhjf.appserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *   月报数据
 * @author hadoop
 *
 */
public class MonthlyReportService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void MonthlyReport(TabLoginuser user , RequestData request, ResponseData respdata){
		
		
		logger.info("用户：" + user.getLoginID() + "查询交易月报数据");
		
		List<Map<String,String>> monthlyReportlist = PayOrderDB.monthlyReport(user.getID());
		
		Map<String,List<Map<String,String>>> map = new HashMap<>(); 
		
		for (int i = 0; i < monthlyReportlist.size(); i++) {
			Map<String,String> map2 = monthlyReportlist.get(i);
			
			String year = map2.get("year").toString();
			
			List<Map<String,String>> list = map.get(year);
			
			if(list == null){
				list = new ArrayList<>();
			}
			map2.remove("year");
			map2.put("month", Integer.toString(Integer.parseInt(map2.get("month").toString()))); 
			map2.put("amount", map2.get("amount"));
			map2.put("count", map2.get("count"));
			list.add(map2);
			map.put(year, list);
		}
		
		JSONArray array = new JSONArray();
		
		for (String year : map.keySet()) {
			List<Map<String,String>> data = map.get(year);
			JSONObject json = new JSONObject();
			json.put("year", year);
			json.put("content", JSONArray.fromObject(data));
			array.add(json);
		}
		
		respdata.setList(array.toString()); 
		logger.info("用户：" + user.getLoginID() + "交易月报数据：" + array.toString()); 
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
	}
}
