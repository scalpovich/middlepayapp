package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.YMFTradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class QueryYMFService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void QueryYMF(LoginUser user , RequestData reqdata , ResponseData respdata){
		
		
		
		logger.info("用户：" + user.getLoginID() + "查询到账类型" +  reqdata.getTradeCode() + "的固定码"); 
		
		List<Map<String,String>> list = YMFTradeDAO.getUserYMFlist(new Object[]{user.getID(),reqdata.getTradeCode()});
		
		String YMFUrl = LoadPro.loadProperties("config", "YMFUrl");
		
		JSONArray  jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++){
			JSONObject json = new JSONObject();
			Map<String,String> map = list.get(i);
			
			// Code,UserID,Valid,PayChannel,Binded,AgentID,TradeCode,Rate ,PayChannelName"
			json.put("Code", YMFUrl +  map.get("Code"));
			json.put("UserID", map.get("UserID"));
			json.put("Valid", map.get("Valid"));
			json.put("PayChannel", map.get("PayChannel"));
			json.put("Binded", map.get("Binded"));
			json.put("AgentID", map.get("AgentID"));
			json.put("TradeCode", map.get("TradeCode"));
			json.put("Rate", map.get("Rate"));
			json.put("PayChannelName", map.get("PayChannelName")==null?"微信/支付宝":map.get("PayChannelName"));
			
			jsonArray.add(json);
		}
		respdata.setTranslist(jsonArray.toString());
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
