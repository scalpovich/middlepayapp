package com.rhjf.appserver.util;

import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;


import net.sf.json.JSONObject;


public class PushUtil {
	
	
	private static LoggerTool log = new LoggerTool(PushUtil.class);
	
	private static String url = "http://msg.umeng.com/api/send";
	private static final String appkey = "58cf47f23eae251a77000e75";
	private static final String appMasterSecret = "ccejkhq1fozqt4xyoia0ct4ni11xhjwy";

	
	private static final String androidAppkey = "58d356c73eae252d80000d45";
	
	private static final String androidappMasterSecret = "dt5voxjoruxbcjjkzlsf85kf7qqujlrp";
	
	public static void iosSend(String title ,String content , String deviceToken , String type){
	
		JSONObject json = new JSONObject();
		
		json.put("timestamp", System.currentTimeMillis());
		json.put("production_mode" , "true");
		json.put("appkey", appkey);
		
		JSONObject payload = new JSONObject();
		
		JSONObject aps = new JSONObject();

		aps.put("sound", "default");
//		aps.put("content-available", 1);
		aps.put("mutable-content" , 1);
		aps.put("badge", 0);
		aps.put("category", "audio");
		
		JSONObject alert = new JSONObject();
		alert.put("title", title);
		alert.put("subtitle", "");
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
			String newURL = url + "?sign=" + sign;
			log.info(json.toString()); 
			log.info(HttpClient.xml(newURL, json.toString()));
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
		
		if(!"2".equals(type)){
			body.put("after_open", "go_app");
		}
		
		
		payload.put("body", body);
		
		json.put("payload", payload);
		
		json.put("production_mode", "true");
		json.put("description", "description");
		
		String sign = MD5.sign("POST" + url + json.toString() + androidappMasterSecret, "utf-8");
		String newURL = url + "?sign=" + sign;
		
		try {
			log.info( HttpClient.xml(newURL, json.toString()));
			
		} catch (IOException e) {
			log.error("发送通知异常", e); 
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
//		PushUtil.androidSend("标题", "当前收款100元", "Ar1nyOIaQE-ScoWrCNPCQccSiaTx328z-gIlU2afn6Lt,AkrHpP_B2hjw6_9QcHP6QQIihPyg9asNEijeiD79kSZx,Au6sh5olI_LJRydfqQwxjsNHsM3MnISGy2_y25EzGj3B", "1");

		for (int i = 0 ; i < 100 ; i ++ ) {
			
			PushUtil.iosSend("标题", "当前收款1000元", "345ad30874b2ad10af8b108951799176d31655bd4a7b20c2d086630bd318e20a", "1");

			
			Thread.sleep(30000);
		}
	}
	
}
