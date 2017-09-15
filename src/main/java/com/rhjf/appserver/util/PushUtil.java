package com.rhjf.appserver.util;

import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;


import net.sf.json.JSONObject;


public class PushUtil {
	
	
	private static LoggerTool log = new LoggerTool(PushUtil.class);
	
	private static String url = "http://msg.umeng.com/api/send";
	private static final String appkey = "59ad1251c62dca7ccb00071a";
	private static final String appMasterSecret = "ezcfogsow6bysilnh0lssm1yixwhbijv";

	
	private static final String androidAppkey = "59ae7454310c93467d000288";
	
	private static final String androidappMasterSecret = "psivnf3hwzzmgfivd1rfj2zktt5acay9";
	
	public static void iosSend(String title ,String content , String deviceToken , String type){
	
		JSONObject json = new JSONObject();
		
		json.put("timestamp", System.currentTimeMillis());
		json.put("production_mode" , "false");
		json.put("appkey", appkey);
		
		JSONObject payload = new JSONObject();
		
		JSONObject aps = new JSONObject();

		aps.put("sound", "default");
		aps.put("content-available", 1);
		aps.put("badge", 1);
		
		if("2".equals(type)){
			aps.put("badge", 0);
		}
		
		JSONObject alert = new JSONObject();
		alert.put("title", title);
		alert.put("subtitle", title);
		alert.put("body", content);
		aps.put("alert", alert);
		
		payload.put("aps", aps);
		payload.put("type", type);
		
		json.put("payload", payload);
		json.put("device_tokens", deviceToken);
		json.put("type", "unicast");
		
        String sign = null;
		try {
			System.out.println(("POST" + url + json.toString() + appMasterSecret)); 
			sign = DigestUtils.md5Hex(("POST" + url + json.toString() + appMasterSecret).getBytes("utf8"));
			url = url + "?sign=" + sign;
			log.info(json.toString()); 
			log.info(HttpClient.xml(url, json.toString()));
		} catch (IOException e1) {
			log.error("发送push通知异常："  , e1);
		}

		
	}
	
	
	
	public static void androidSend(String title , String content , String deviceToken , String type){
		JSONObject json = new JSONObject();
		json.put("appkey", androidAppkey);
		json.put("timestamp", System.currentTimeMillis());
		json.put("type", "listcast");
		json.put("device_tokens", deviceToken);
		
		JSONObject payload = new JSONObject();
		
		payload.put("display_type", "notification");  //  notification-通知，message-消息

		if("2".equals(type)){
			payload.put("display_type", "message"); 
		}
		
		JSONObject body = new JSONObject();
		body.put("ticker",title);
		body.put("title", title);
		body.put("text", content);
		
		body.put("custom", content);
		
		payload.put("body", body);
		
		json.put("payload", payload);
		
		json.put("production_mode", "false");
		json.put("description", "description");
		
		String sign = MD5.sign("POST" + url + json.toString() + androidappMasterSecret, "utf-8");
		url = url + "?sign=" + sign;
		
		try {
			log.info( HttpClient.xml(url, json.toString()));
			
		} catch (IOException e) {
			log.error("发送通知异常", e); 
		}
	}
	
	public static void main(String[] args) {
		PushUtil.androidSend("标题", "内容", "AlDXcAtK72x4-hV2v9Jf8oCGIKDB6QTVNhLS7aWvfNOd,"
				+ "AkrHpP_B2hjw6_9QcHP6QQLWYFY6V9sBAjz_sSmdn8S6,AhfLFegDDOKxyRnQZOW6hdoLiR9-b6r9vAMgYC2ZnRWg", "2");
	}
	
}
