package com.rhjf.appserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
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
	
	public void MonthlyReport(LoginUser user , RequestData request, ResponseData respdata){
		
		
		logger.info("用户：" + user.getLoginID() + "查询交易月报数据");
		
		List<Map<String,String>> monthlyReportlist = PayOrderDAO.monthlyReport(user.getID());
		
		Map<String,List<Map<String,String>>> map = new LinkedHashMap<>(); 
		
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
	
	
	public void sortMap(Map<String, String> map) {
		// 获取entrySet
		Set<Map.Entry<String, String>> mapEntries = map.entrySet();

		for (Entry<String, String> entry : mapEntries) {
			System.out.println("key:" + entry.getKey() + "   value:" + entry.getValue());
		}

		// 使用链表来对集合进行排序，使用LinkedList，利于插入元素
		List<Map.Entry<String, String>> result = new LinkedList<>(mapEntries);
		// 自定义比较器来比较链表中的元素
		Collections.sort(result, new Comparator<Entry<String, String>>() {
			// 基于entry的值（Entry.getValue()），来排序链表
			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {

				return o1.getValue().compareTo(o2.getValue());
			}

		});

		// 将排好序的存入到LinkedHashMap(可保持顺序)中，需要存储键和值信息对到新的映射中。
		Map<String, String> linkMap = new LinkedHashMap<>();
		for (Entry<String, String> newEntry : result) {
			linkMap.put(newEntry.getKey(), newEntry.getValue());
		}
		// 根据entrySet()方法遍历linkMap
		for (Map.Entry<String, String> mapEntry : linkMap.entrySet()) {
			System.out.println("key:" + mapEntry.getKey() + "  value:" + mapEntry.getValue());
		}
	}

	
}
