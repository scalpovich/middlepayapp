package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.UserProfitDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;


/**
 *    商户列表点击详情
 * @author hadoop
 *
 */
public class MyMerchantDetailedService {
	
	
	public void MyMerchantDetailed(TabLoginuser user , RequestData request , ResponseData response){
		
		String merchantID = request.getMerchantID();
		
		Integer count = LoginUserDB.merchantTokerCount(user.getID(), merchantID);
		
		response.setTotalCount(count);
		
		String total = UserProfitDB.merchantTokerProfit(user.getID(), merchantID);
		
		response.setTotal(total);
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}

}
