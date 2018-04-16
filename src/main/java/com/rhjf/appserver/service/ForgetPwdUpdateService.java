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
import com.rhjf.appserver.util.UtilsConstant;


/**
 * 忘记密码  修改密码
 * @author hadoop
 *
 */
public class ForgetPwdUpdateService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void ForgetPwdUpdate(LoginUser user , RequestData reqdata , ResponseData respdata){
		
		logger.info("用户：" + user.getLoginID() + "忘记密码，开始找回操作");
		
		String newLoginpwd = reqdata.getLoginPwd();
		
		
		if(UtilsConstant.strIsEmpty(newLoginpwd)){
			logger.info("用户：" + user.getLoginID() + "没有填写登录密码");
			respdata.setRespCode(RespCode.ParamsError[0]);
			respdata.setRespDesc(RespCode.ParamsError[1]);
			return ;
		}
		
		String initKey= LoadPro.loadProperties("config","protectINDEX");
		String initKey2= LoadPro.loadProperties("config","TMKINDEX");
		
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
