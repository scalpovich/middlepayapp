package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


/**
 *    用户修改邮箱
 * @author hadoop
 *
 */
public class UpdateUserEmailService {

	LoggerTool log = new LoggerTool(this.getClass());
	
	public void UpdateUserEmail(TabLoginuser user , RequestData request, ResponseData response){
		log.info("用户：" + user.getLoginID() + "修改邮箱, " + request.getEmail()); 
		
		if(UtilsConstant.strIsEmpty(request.getEmail())){
			response.setRespCode(RespCode.ParamsError[0]);
			response.setRespDesc(RespCode.ParamsError[1]);
			return ;
		}
		
		int x = LoginUserDB.updateUserEmail(new Object[]{request.getEmail() , user.getID()});
		
		EhcacheUtil cache = EhcacheUtil.getInstance();
		cache.cachesize(Constant.cacheName);
		
		if(x > 0 ){
			response.setRespCode(RespCode.SUCCESS[0]);
			response.setRespDesc(RespCode.SUCCESS[1]);
		}else{
			response.setRespCode(RespCode.ServerDBError[0]);
			response.setRespDesc(RespCode.ServerDBError[1]);
		}
	}
}
