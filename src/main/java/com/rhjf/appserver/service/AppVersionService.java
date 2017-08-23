package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.ADListDB;
import com.rhjf.appserver.db.AppVersionDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *   查询App版本号;
 * @author a
 *
 */
public class AppVersionService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void appVersionInfo(RequestData reqData , ResponseData repData){
	
		/** 平台版本 **/
		if(UtilsConstant.strIsEmpty(reqData.getVersion())){
			repData.setRespCode(RespCode.ParamsError[0]);
			repData.setRespDesc(RespCode.ParamsError[1]); 
			return ;
		}
		Map<String,Object> map = null;
		
		String deviceType = reqData.getDeviceType();
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Object obj = ehcache.get(Constant.cacheName,  deviceType + "appversion");
		if(obj==null){
			map = AppVersionDB.getAppVersionInfo(new Object[]{deviceType});
			ehcache.put(Constant.cacheName,  deviceType +  "appversion", map);
		}else{
			map = (Map<String,Object>) obj;
		}
		repData.setAppInfo(JSONObject.fromObject(map).toString()); 
		repData.setRespCode(RespCode.SUCCESS[0]);
		repData.setRespDesc(RespCode.SUCCESS[1]); 
	}
	
	
	public void MyQRCode(TabLoginuser user ,RequestData reqdata, ResponseData repdata){
		
		logger.info("获取我的二维码"); 
		
		String myQRCode = LoadPro.loadProperties("config", "myqrcodeurl");
		repdata.setQrCodeUrl(myQRCode + user.getLoginID());
		//  分享链接
		repdata.setTerminalInfo(LoadPro.loadProperties("config", "myqrcodeurl") + user.getLoginID());
		
		repdata.setRespCode(RespCode.SUCCESS[0]);
		repdata.setRespDesc(RespCode.SUCCESS[1]);
	}
	
	
	
	public void adlist(RequestData reqdata, ResponseData repdata){
		JSONArray adarray = new JSONArray();
		
		List<Map<String,Object>> list = ADListDB.adlist();
		
		
		for (int i = 0; i < list.size(); i++) {
			adarray.add(list.get(i).get("Content")); 
		}
		
		repdata.setTranslist(adarray.toString());  
		
		repdata.setRespCode(RespCode.SUCCESS[0]);
		repdata.setRespDesc(RespCode.SUCCESS[1]);
	}
	
}
