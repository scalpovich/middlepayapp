package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.SmsApplyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;

/**
 *   忘记密码   修改密码之前校验验证码是否正确 
 * @author hadoop
 *
 */
public class ForgetPwdSmsCodeService {


	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void ForgetPwdSmsCode(TabLoginuser user , RequestData reqdata , ResponseData respdata){
		
		logger.info("用户：" + user.getLoginID() + "忘记密码，开始找回操作");
		
		
		String code = reqdata.getSmsCode();
		
		String smsCode = SmsApplyDB.getSmsCode(new Object[]{user.getLoginID()});
		
		if(smsCode==null||!code.equals(smsCode)){
			logger.info("短息验证码错误:" + reqdata.getLoginID() + "数据库验证码：" + smsCode + "上报验证码：" + code);
			// public static final String[] SMSCodeError = {"E015" , "短息验证码错误，请核对"};
			respdata.setRespCode(RespCode.SMSCodeError[0]);
			respdata.setRespDesc(RespCode.SMSCodeError[1]);
			return ;
		}else{
			SmsApplyDB.delSmsCode(new Object[]{reqdata.getLoginID()});
		}
		
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
		return ;
		
		
	}
}
