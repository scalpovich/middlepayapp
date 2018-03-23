package com.rhjf.appserver.service.jifu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hadoop
 * @version 1.0
 * 2018年3月19日 - 下午3:39:27
 * 2018
 */
public class JifuConstant {


	/**
	 *   测试参数
	 */

//	public static String url = "http://ip:19085/rest/api/";
//
//	public static String merchantNo = "";
//
//	public static String aesKey = "";
//
//	public static String signKey = "0000000000000000";
//
//	public static String notifyUrl = "http://111.207.6.230:8085/appserver/tradenotify/jifu";


	/**
	 *   生产参数
	 */

	public static Map<String,String> map = null;
	
	static{
		map = new HashMap<>();

		map.put("ICBC", "102");
		map.put("ABC", "103");
		map.put("BOC", "104");
		map.put("CCB", "105");
		map.put("BOCO", "301");
		map.put("ECITIC", "302");
		map.put("CEB", "303");
		map.put("HXB", "304");
		map.put("CMBC", "305");
		map.put("CGB", "306");
		map.put("SDB", "307");
		map.put("CMBCHINA", "308");
		map.put("CIB", "309");
		map.put("SPDB", "310");
		map.put("POST", "403");
		map.put("BCCB", "313100000013");
		map.put("SHYH", "325290000012");
		map.put("TJYH", "313110000017");
		
		map.put("WZYH", "412");
		map.put("GUAZYH", "413");
		map.put("DLYH", "420");
		map.put("HZYH", "423");
		map.put("NJYH", "424");
		map.put("JINZYH", "439");
		map.put("CQYH", "441");
		map.put("HAEBYH", "442");
		map.put("LZYH", "447");
		map.put("NCYH", "448");
		map.put("LJYH", "492");
		map.put("JSBCHINA", "508");
		map.put("SHRCB", "1401");
		map.put("CQRCB", "1413");
	}
}
