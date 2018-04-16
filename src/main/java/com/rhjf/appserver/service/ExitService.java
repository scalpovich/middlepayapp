package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.DeviceTokenDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;

/**
 * @author hadoop
 */
public class ExitService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void exit(LoginUser user , RequestData reqdata , ResponseData respdata){
		
		logger.info("用户" + user.getLoginID() + "退出程序"); 
		
		String loginID = user.getID();
		
		
		logger.info("清除缓存中所有内容");
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
		
		DeviceTokenDAO.delkToken(new Object[]{loginID});
		
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
