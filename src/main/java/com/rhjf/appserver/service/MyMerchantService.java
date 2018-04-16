package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


/**
 *     查询商户
 * @author a
 *
 */
public class MyMerchantService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void MyMerchant(LoginUser user, RequestData reqdata, ResponseData respdata) {
		
		String merchantName = UtilsConstant.ObjToStr(reqdata.getMerchantName());
		
		
		logger.info("用户" + user.getLoginID() + "查询发展商户"); 

		// 查询交易
		String returnString = LoginUserDAO.getMyMerchant(new Object[]{user.getID() , "%" + merchantName + "%"});

		respdata.setLoginID(reqdata.getLoginID());
		respdata.setTranslist(returnString);
		// 组返回给终端的报文
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
	}
}
