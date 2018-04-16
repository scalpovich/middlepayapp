package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MakeCipherText;
import com.rhjf.appserver.util.UtilsConstant;

public class LoginService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	/**
	 *   用户登录
	 * @param reqData
	 * @param repData
	 */
	public void Login(RequestData reqData , ResponseData repData){
		
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Object obj = ehcache.get(Constant.cacheName,reqData.getLoginID() + "UserInfo" );
		
		LoginUser user ;
		if(obj == null){
			logger.info("查询数据库");
			user = LoginUserDAO.LoginuserInfo(reqData.getLoginID());
			if(user == null){
				logger.info("未查到用户 " + reqData.getLoginID() +"信息");
				repData.setRespCode(RespCode.userDoesNotExist[0]);
				repData.setRespDesc(RespCode.userDoesNotExist[1]);
				return ;
			}
			ehcache.put(Constant.cacheName, user.getLoginID() + "UserInfo" , user);
		}else{
			logger.info("查询缓存");
			user = (LoginUser) obj;
		}
		
		logger.info("用户" +  user.getLoginID() + "登录");
		
		String passwd = MakeCipherText.calLoginPwd(reqData.getLoginID(),user.getLoginPwd(), reqData.getSendTime());
		
		if(!passwd.equals(reqData.getLoginPwd())){
			logger.info("用户" + user.getLoginID() + "密码错误, 上送密码：" + reqData.getLoginPwd() + ", 平台计算密码:" + passwd);
			repData.setRespCode(RespCode.PasswordError[0]);
			repData.setRespDesc(RespCode.PasswordError[1]);
			return;
		}
		
		int nRef = LoginUserDAO.updateUserLoginInfo(new Object[]{DateUtil.getNowTime(DateUtil.yyyyMMddHHmmss) ,reqData.getTerminalInfo() , user.getLoginID()});
		
		if(nRef == 0){
	    	logger.info("终端号：" + reqData.getTerminalInfo() + " ,用户名："+ user.getLoginID()+"登录失败");
	    	repData.setRespCode(RespCode.ServerDBError[0]);
	    	repData.setRespDesc(RespCode.ServerDBError[1]);
	    	return ;
	    }
		
		//获取终端主密钥
	    Map<String,Object> map = TermKeyDAO.selectTermKey(user.getID());
	    
	    if(map == null || map.isEmpty()){
	    	TermKeyDAO.allocationTermk(user.getID());
	    	map = TermKeyDAO.selectTermKey(user.getID());
	    }
	    
	    
	    String initKey = LoadPro.loadProperties("jmj", "3");
	    
	    String tmk = "";
		try {
			tmk = DESUtil.bcd2Str(DESUtil.decrypt3(UtilsConstant.ObjToStr(map.get("TermTmkKey")), initKey));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	    
		//初始化返回包的信息
	    repData.setTerminalInfo(tmk);
	    repData.setBankCardNo(user.getBankCardNo());
	    repData.setBankName(user.getBankName());
	    
	    logger.info("用户"+ user.getLoginID()+"获取终端主秘钥:" +UtilsConstant.ObjToStr(map.get("TermTmkKey"))); 
	    
	    ehcache.clear(Constant.cacheName); 
	    
		//组返回给终端的报文
	    repData.setRespCode(RespCode.SUCCESS[0]);
	    repData.setRespDesc(RespCode.SUCCESS[1]);
	    
	    logger.info("用户"+ user.getLoginID()+"登录成功");
	}
}
