package com.rhjf.appserver.service;


import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.KeyBean;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONObject;

public class MerchantReportService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void merchantReportURL(LoginUser user ,  RequestData reqData , ResponseData repdata){
		
		logger.info("用户" + user.getLoginID() +  "请求访问入网资料填写页面"); 
		
		
		if(user.getBankInfoStatus()==2||user.getBankInfoStatus()==1){
			repdata.setRespCode(RespCode.MerchantInfoError[0]);
			repdata.setRespDesc(RespCode.MerchantInfoError[1]);
			return;
		}
		
		
		String url = LoadPro.loadProperties("config", "reportURL");
		
		KeyBean keyBean = new KeyBean();
		
		
		
		String key = keyBean.getkeyBeanofStr(user.getLoginID());
		
		//  分享链接
		repdata.setTerminalInfo(url + user.getLoginID() + "&sign=" + key);
			
		repdata.setRespCode(RespCode.SUCCESS[0]);
		repdata.setRespDesc(RespCode.SUCCESS[1]);
	}
	
	
	public void getMerchantReportInfo(LoginUser user ,  RequestData reqData , ResponseData repdata){
		logger.info("用户" + user.getLoginID() +  "查看入网资料"); 
		
		String url = LoadPro.loadProperties("config", "reportInfoURL");
		//  分享链接
		repdata.setTerminalInfo(url + user.getLoginID());
			
		repdata.setRespCode(RespCode.SUCCESS[0]);
		repdata.setRespDesc(RespCode.SUCCESS[1]);
	}
	
	
	
	
	public void MerchantReportInfo(LoginUser user ,  RequestData reqData , ResponseData repdata){
		logger.info("用户" + user.getLoginID() +  "查看入网资料"); 
		
		Map<String,Object> map = LoginUserDAO.merchantInfo(new Object[]{user.getID()});
		//  BankInfoStatus , PhotoStatus 
		repdata.setList(JSONObject.fromObject(map).toString());
		
		repdata.setBankInfoStatus(Integer.parseInt(map.get("BankInfoStatus").toString())); 
		repdata.setPhotoStatus(Integer.parseInt(map.get("PhotoStatus").toString())); 
			
		repdata.setRespCode(RespCode.SUCCESS[0]);
		repdata.setRespDesc(RespCode.SUCCESS[1]);
	}
}
