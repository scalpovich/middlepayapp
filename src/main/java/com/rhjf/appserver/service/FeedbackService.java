package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.FeedbackDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

public class FeedbackService {

	
	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	
	public void Feedback(TabLoginuser user , RequestData request , ResponseData  response){
		log.info("用户：" + user.getLoginID() + "提出意见反馈: " + request.getMessage());
		
		FeedbackDB.saveFeedback(new Object[]{UtilsConstant.getUUID() , user.getID() , request.getPayerPhone() , request.getMessage()});
		
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
