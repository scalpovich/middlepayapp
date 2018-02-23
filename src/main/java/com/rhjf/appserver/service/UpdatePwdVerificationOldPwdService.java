package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MakeCipherText;

/**
 *   修改密码 验证老密码是否正确
 * @author hadoop
 *
 */
public class UpdatePwdVerificationOldPwdService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	
	public void UpdatePwdVerificationOldPwd(TabLoginuser user , RequestData reqdata , ResponseData respdata){
		String loginpwd = reqdata.getLoginPwd();
//		String newLoginpwd = reqdata.getNewLoginPwd();
		String passwd = MakeCipherText.calLoginPwd(reqdata.getLoginID(),user.getLoginPwd(), reqdata.getSendTime());
		
		if(!passwd.equals(reqdata.getLoginPwd())){
			log.info("用户" + user.getLoginID() + "密码错误, 上送密码：" + loginpwd + ", 平台计算密码:" + passwd);
			respdata.setRespCode(RespCode.PasswordError[0]);
			respdata.setRespDesc(RespCode.PasswordError[1]);
			return;
		}else{
			log.info("用户" + user.getLoginID() + "密码正确：" + loginpwd); 
		}
		
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
