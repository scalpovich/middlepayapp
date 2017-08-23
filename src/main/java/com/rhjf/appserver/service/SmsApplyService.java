package com.rhjf.appserver.service;

import java.util.Random;

import com.rhjf.appserver.db.SmsApplyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.SmsUtil;

import net.sf.json.JSONObject;

public class SmsApplyService {
	LoggerTool logger = new LoggerTool(this.getClass());

	public ResponseData send(RequestData reqData , ResponseData repData){
		
		//生成验证码
		String smsCode = GetSmsCode();
		int nRet = SmsApplyDB.insertSmsCode(reqData.getLoginID(), smsCode);
		if(nRet==-1){
			logger.info("记录手机号校验码失败，手机号=【"+reqData.getLoginID()+"】");
			repData.setRespCode("96");
			repData.setRespDesc("手机号校验失败");
			return repData;
		}
		//发送短信
		JSONObject json = SmsUtil.sendSMS(reqData.getLoginID(), smsCode,LoadPro.loadProperties("config", "ZhuCe"),"5",LoadPro.loadProperties("config", "APPID"));
		if(json.getInt("code")==0){
			repData.setRespCode("00");
			repData.setRespDesc("短信已发送");
		}else{
			repData.setRespCode("96");
			repData.setRespDesc(json.getString("message"));
		}
		return repData;
	}
	
	public ResponseData restPWd(RequestData reqData , ResponseData repData){
		
		//生成验证码
		String smsCode = GetSmsCode();
		int nRet = SmsApplyDB.insertSmsCode(reqData.getLoginID(), smsCode);
		if(nRet==-1){
			logger.info("记录手机号校验码失败，手机号=【"+reqData.getLoginID()+"】");
			repData.setRespCode("96");
			repData.setRespDesc("手机号校验失败");
			return repData;
		}
		//发送短信
		JSONObject json = SmsUtil.sendSMS(reqData.getLoginID(), smsCode,LoadPro.loadProperties("config", "RestPwd"),"5",LoadPro.loadProperties("config", "APPID"));
		
		if(json.getInt("code")==0){
			repData.setRespCode("00");
			repData.setRespDesc("短信已发送");
		}else{
			repData.setRespCode("96");
			repData.setRespDesc(json.getString("message"));
		}
		return repData;
	}
	
	
	private String  GetSmsCode() {
		Random random = new Random();
		StringBuffer randBuffer = new StringBuffer();
		char[] codeSequence = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9'};
		
		for (int i = 0; i < 4; i++) {
			randBuffer.append(String.valueOf(codeSequence[random.nextInt(10)]));
		}
		return randBuffer.toString();
		
	}	

}
