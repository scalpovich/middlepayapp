package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MakeCipherText;



/**
 *   修改密码
 * @author a
 *
 */
public class UpdatePasswordService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void UpdatePassword(LoginUser user , RequestData reqdata , ResponseData respdata){
		
		String loginpwd = reqdata.getLoginPwd();
		
		String newLoginpwd = reqdata.getNewLoginPwd();
		
		String passwd = MakeCipherText.calLoginPwd(reqdata.getLoginID(),user.getLoginPwd(), reqdata.getSendTime());
		
		if(!passwd.equals(reqdata.getLoginPwd())){
			logger.info("用户" + user.getLoginID() + "密码错误, 上送密码：" + loginpwd + ", 平台计算密码:" + passwd);
			respdata.setRespCode(RespCode.PasswordError[0]);
			respdata.setRespDesc(RespCode.PasswordError[1]);
			return;
		}
		
		String initKey= LoadPro.loadProperties("config","protectINDEX");  // 5
		String initKey2= LoadPro.loadProperties("config","TMKINDEX");     //  1
		
		String password = MakeCipherText.MakeLoginPwd(initKey2,newLoginpwd,initKey);
		
		int ret = LoginUserDAO.updatePassword(new Object[]{password , user.getLoginID()});
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.remove(Constant.cacheName, user.getLoginID() + "UserInfo");
		
		if(ret > 0){
			logger.info("用户:" + user.getLoginID() + "修改密码成功");
			respdata.setRespCode(RespCode.SUCCESS[0]);
			respdata.setRespDesc(RespCode.SUCCESS[1]);
		}else{
			logger.info("用户:" + user.getLoginID() + "修改密码失败");
			respdata.setRespCode(RespCode.ServerDBError[0]);
			respdata.setRespDesc(RespCode.ServerDBError[1]);
		}
	}
}
