package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;


/**
 *     查询商户
 * @author a
 *
 */
public class MyMerchantService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void MyMerchant(TabLoginuser user, RequestData reqdata, ResponseData respdata) {
		
		
		logger.info("用户" + user.getLoginID() + "查询发展商户"); 

		// 查询交易
		String returnString = LoginUserDB.getMyMerchant(user.getID());

		respdata.setLoginID(reqdata.getLoginID());
		respdata.setTranslist(returnString);
		// 组返回给终端的报文
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
	}
}
