package com.rhjf.appserver.service;

import java.util.Map; 

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AppConfigDAO;
import com.rhjf.appserver.db.AppVersionDAO;
import com.rhjf.appserver.db.UserWalletDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.KeyBean;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *    初始化
 * @author hadoop
 *
 */
public class InitializeService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void init(LoginUser user , RequestData request , ResponseData response){
		
		/*
		 *	 商户钱包信息 
		 */
		Map<String,String> walletmap = UserWalletDAO.UserWalletByUserID(new Object[]{user.getID()});
		if(walletmap!=null &&! walletmap.isEmpty()){
			response.setTotal(walletmap.get("WalletBalance"));
		}
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		Object obj = null;
		String deviceType = request.getDeviceType();
		if(!UtilsConstant.strIsEmpty(deviceType)){
			
			Map<String,Object> map = null;
			obj = ehcache.get(Constant.cacheName,  deviceType + "appversion");
			if(obj==null){
				map = AppVersionDAO.getAppVersionInfo(new Object[]{deviceType});
				ehcache.put(Constant.cacheName,  deviceType +  "appversion", map);
			}else{
				map = (Map<String,Object>) obj;
				obj = null;
			}
			response.setOpen(UtilsConstant.ObjToStr(map.get("Open")));

			/*
			 *   判断入网使用方式
			 *   1、 使用html页面
			 *   其他使用原生
			 */
			if("1".equals(map.get("MerchantInOpen"))){
				String url = LoadPro.loadProperties("config", "reportURL");
				KeyBean keyBean = new KeyBean();
				String key = keyBean.getkeyBeanofStr(user.getLoginID());
				response.setTerminalInfo(url + user.getLoginID() + "&sign=" + key);
			}else{
				response.setTerminalInfo("00");
			}
		}
		
		//  查询交易配置信息
		Map<String,Object> tradeConfig = null;
		obj = ehcache.get(Constant.cacheName, "tradeConfig");
		if(obj == null){
			log.info("缓存中获取交易配置信息失败,从数据库中查询");
			tradeConfig = AppConfigDAO.getTradeConfig();
			ehcache.put(Constant.cacheName, "tradeConfig", tradeConfig);
		}else{
			log.info("缓存查询交易配置信息");
			tradeConfig = (Map<String,Object>) obj;
		}
		
		response.setLargeAmount(UtilsConstant.ObjToStr(tradeConfig.get("LargeAmount"))); 
		
		if(user.getRegisterTime()!=null){
			response.setRegisterDate(user.getRegisterTime().substring(0, 8));
		}else{
			response.setRegisterDate(user.getRegisterTime());
		}
		
		String myQRCode = LoadPro.loadProperties("config", "myqrcodeurl");
		response.setQrCodeUrl(myQRCode + user.getLoginID());
		
		//组返回给终端的报文
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
