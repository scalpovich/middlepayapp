package com.rhjf.appserver.controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/requestentry")
@ResponseBody
public class RequestEntryController {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	@RequestMapping(value = "" , method = {RequestMethod.POST })
	public Object RequestEntry(HttpServletRequest request ,HttpServletResponse resp){
		
		
		StringBuffer sbf = new StringBuffer();
		Enumeration<String> hearkey = request.getHeaderNames();
		while (hearkey.hasMoreElements()) {
			String key = hearkey.nextElement();
			sbf.append(key);
			sbf.append("=");
			sbf.append(request.getHeader(key));
			sbf.append("&");
		}
		
		logger.userinfo(sbf.toString()); 
		
		
		logger.info("终端用户发送请求");
		ResponseData respData = new ResponseData();
		
		String loginID = null;
		
		try{
			
			String reqContent = request.getParameter("data") ;
			
			if(UtilsConstant.strIsEmpty(reqContent)){
				logger.info("终端请求报文为空,停止交易");
				respData.setRespCode(RespCode.ParamsError[0]);
				respData.setRespDesc(RespCode.ParamsError[1]);
				return paraFilterReturn(respData);
			}
			logger.info("请求报文：" + reqContent.replace("\n", "").replace(" ", "")); 
			Map<String,Object> map = UtilsConstant.jsonToMap(JSONObject.fromObject(reqContent)); 
			RequestData requestData = UtilsConstant.mapToBean(map, RequestData.class);
			TabLoginuser loginuser = null;
			/** 获取登录手机号 **/
			loginID = requestData.getLoginID();
			
			/** 发送时间  **/
			if(UtilsConstant.strIsEmpty(requestData.getSendTime())){
				logger.info("发送时间sendTime为空");
				respData.setRespCode(RespCode.ParamsError[0]);
				respData.setRespDesc(RespCode.ParamsError[1]);
				return paraFilterReturn(respData);
			}
			
			respData.setSendSeqID(requestData.getSendTime());
			
			/** 请求交易类型 **/
			String Txndir = requestData.getTxndir();
			if(UtilsConstant.strIsEmpty(Txndir)){
				logger.info("交易类型txndir为空");
				respData.setRespCode(RespCode.TxndirError[0]);
				respData.setRespDesc(RespCode.TxndirError[1]);
				return respData;
			}
			String trade = LoadPro.loadProperties("trade", Txndir);
			if(UtilsConstant.strIsEmpty(trade)){
				logger.info("交易类型：" + Txndir + ", 系统为配置该交易类型"); 
				respData.setRespCode(RespCode.TxndirError[0]);
				respData.setRespDesc(RespCode.TxndirError[1]);
				return paraFilterReturn(respData);
			}
			respData.setTxndir(Txndir);
			
			/** 终端流水号 **/
			String sendSeqID = requestData.getSendSeqId();
			if(UtilsConstant.strIsEmpty(sendSeqID)){
				logger.info("终端流水号sendSeqId为空");
				respData.setRespCode(RespCode.ParamsError[0]);
				respData.setRespDesc(RespCode.ParamsError[1]);
				return paraFilterReturn(respData);
			}
			respData.setSendSeqID(sendSeqID);
			
			/**  终端登录信息 **/
			String loginPSN = requestData.getTerminalInfo();
			if(UtilsConstant.strIsEmpty(loginPSN)){
				logger.info("登录信息(PSN)terminalInfo为空");
				respData.setRespCode(RespCode.ParamsError[0]);
				respData.setRespDesc(RespCode.ParamsError[1]);
				return  paraFilterReturn(respData);
			}
			respData.setTerminalInfo(loginPSN);
			
			String className = trade.split(",")[0];
			String funName = trade.split(",")[1];
			String isNeedLogin = trade.split(",")[2];
			String isNeedMac = trade.split(",")[3];
			
			logger.info("获取trade配置信息-------trade：" + trade);
			logger.info("获取trade配置信息-------className：" + className);
			logger.info("获取trade配置信息-------funName：" + funName);
			logger.info("获取trade配置信息-------isNeedLogin：" + isNeedLogin);
			
			//  需要登录信息
			if(isNeedLogin.equals("1")){
				
				logger.info("需要登录信息，登录的手机号为" + loginID); 
				
				EhcacheUtil ehcache = EhcacheUtil.getInstance();
				
				Object obj = ehcache.get(Constant.cacheName, loginID + "UserInfo" );
				if(obj == null){
					logger.info("查询数据库");
					loginuser = LoginUserDB.LoginuserInfo(loginID);
					if(loginuser == null){
						logger.info("未查到用户 " + loginID +"信息");
						respData.setRespCode(RespCode.userDoesNotExist[0]);
						respData.setRespDesc(RespCode.userDoesNotExist[1]);
						return  paraFilterReturn(respData);
					}
					ehcache.put(Constant.cacheName, loginID + "UserInfo" , loginuser);
				}else{
					logger.info("查询缓存");
					loginuser = (TabLoginuser) obj;
				}
				
				if("0".equals(loginuser.getNeedLogin())){
					logger.info("账号：" + loginuser.getLoginID() + " needLogin 状态为0 不可登陆");
					respData.setRespCode(RespCode.LOGINError[0]);
					respData.setRespDesc("账号无法登陆，请联系客服人员");
					return  paraFilterReturn(respData);
				}
				
				
				if("SALESMAN".equals(loginuser.getUserType())){
					logger.info("账号：" + loginuser.getLoginID() + " , 身份为业务员"); 
					respData.setRespCode(RespCode.LOGINError[0]);
					respData.setRespDesc("该账号没有权限进行此操作");
					return  paraFilterReturn(respData);
				}
				
				
				if(!Txndir.startsWith("A")&&!Txndir.startsWith("G")){
					if(!requestData.getTerminalInfo().equals(loginuser.getLoginPSN())){
						logger.info(loginID + "被其他设备登录 , 终端上传: " + requestData.getTerminalInfo() + ",数据库保存" +loginuser.getLoginPSN() );
						respData.setRespCode(RespCode.LOGINError[0]);
						respData.setRespDesc(RespCode.LOGINError[1]);
						return  paraFilterReturn(respData);
					}
				}
				
				if (isNeedMac.equals("1")) {
					// 计算mac
					String mac = makeMac(JSONObject.fromObject(reqContent), loginuser);
					if (!mac.equals(requestData.getMac())) {
						logger.info("验证mac失败，终端上送mac=[" + requestData.getMac() + "],平台计算mac=" + mac);
						respData.setRespCode(RespCode.SIGNMACError[0]);
						respData.setRespDesc(RespCode.SIGNMACError[1]);
						return respData;
					}else{
						logger.info("mac校验通过 == " + mac);
					}
				}
				
				//获取 函数入口
				Class<?> cls = Class.forName("com.rhjf.appserver.service." + className);
				Method m = cls.getDeclaredMethod(funName,new Class[]{ TabLoginuser.class , RequestData.class , ResponseData.class});
	
				m.invoke(cls.newInstance(), loginuser , requestData , respData);
			}else{
				//获取 函数入口
				Class<?> cls = Class.forName("com.rhjf.appserver.service." + className);
				Method m = cls.getDeclaredMethod(funName,new Class[]{RequestData.class,ResponseData.class});
	
				m.invoke(cls.newInstance(), requestData,respData);
			}
			/** 如果请求需要校验mac 那么响应报文中也需要添加mac字段. 加密数据为返回的报文 **/
			if(isNeedMac.equals("1")){
				String mac = makeMac(JSONObject.fromObject(respData), loginuser);
				logger.info("响应报文中的mac" + mac);
				respData.setMac(mac);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e); 
		}
		Object obj =  paraFilterReturn(respData);
		logger.info("响应报文：" + loginID  + " ======== "  + respData.getTxndir()  + "---" + obj.toString());
		return obj;
	}
	
	
	/**
	 *    去除参数提中 value 为null 的字段
	 * @param obj
	 * @return
	 */
	public Object paraFilterReturn(Object obj) {
		
		Map<String, Object> sArray = UtilsConstant.jsonToMap(JSONObject.fromObject(obj));
		Map<String, Object> sArray2 = new HashMap<String, Object>();
		if (sArray == null || sArray.size() <= 0) {
			return "";
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key) + "";
			if ((value == null || value.equals(""))) {
				continue;
			}
			sArray2.put(key + "", value);
		}
		return JSONObject.fromObject(sArray2);
	}
	
	
	/**
	 *   计算mac
	 * @param json
	 * @param user
	 * @return
	 */
    public String makeMac(JSONObject json,TabLoginuser user){
    	
    	Map<String, Object> contentData = UtilsConstant.jsonToMap(json);
    	
		String macStr = "";
		Object[] key_arr = contentData.keySet().toArray();
		Arrays.sort(key_arr);
		for (Object key : key_arr) {
			Object value = contentData.get(key);
			if (value != null&&!UtilsConstant.strIsEmpty(value.toString())){ 
				if (!key.equals("mac")&&!key.equals("signImg")) {
					macStr += value.toString();
				}
			}
		}
		logger.info("计算mac原文:" + macStr);
		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		String rMac = DESUtil.mac(macStr, UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
		return rMac;
    }
}
