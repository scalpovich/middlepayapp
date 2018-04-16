package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;

public class TradeCodeService {

	
	LoggerTool log = new LoggerTool(this.getClass());
	
	public void TradeCode(LoginUser user , RequestData reqdata,ResponseData respdata){
		log.info("用户" + user.getLoginID() + "修改到账类型：" + reqdata.getTradeCode());
		
		
		int x = LoginUserDAO.updateTradeCode(new Object[]{reqdata.getTradeCode() , user.getID()});
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName); 
		
		if(x > 0){
			log.info("用户" + user.getLoginID() + "修改到账类型成功");
			respdata.setRespCode(RespCode.SUCCESS[0]);
			respdata.setRespDesc(RespCode.SUCCESS[1]);
		}else{
			log.info("用户" + user.getLoginID() + "修改到账类型-------失败");
			respdata.setRespCode(RespCode.ServerDBError[0]);
			respdata.setRespDesc(RespCode.ServerDBError[1]);
		}
	}
}
