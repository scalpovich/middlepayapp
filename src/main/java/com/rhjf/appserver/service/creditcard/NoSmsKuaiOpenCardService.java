package com.rhjf.appserver.service.creditcard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.AppconfigDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.OpenProductUtil;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;


public class NoSmsKuaiOpenCardService {


	private LoggerTool logger = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void noSmsKuaiOpenCard(TabLoginuser user ,RequestData reqData , ResponseData repData){
		
		logger.info("用户：" + user.getLoginID() + "请求开通  无短信 快捷支付 请求 , 开通银行卡卡号：(密文)" + reqData.getBankCardNo());
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		String payChannel = Constant.payNosmsKUAI; 
		
		/** 查询用户交易配置信息  **/
		Map<String,Object> map = null;
		Object obj = ehcache.get(Constant.cacheName, user.getID() + payChannel + "userConfig");
		if(obj == null){
			logger.info("缓存读取用户支付配置信息失败，从数据中读取， 用户：" + user.getID() + " , 支付类型:" + payChannel);
			map = TradeDB.getUserConfig(new Object[]{ user.getID() , payChannel});
			if(map==null||map.isEmpty()){
				// ID,UserID,PayChannel,SaleAmountMax,SaleAmountMaxDay,T1SaleRate,T0SaleRate,T1SettlementRate,T0SettlementRate
				String id = UtilsConstant.getUUID();
				String userid = user.getID();
				map = TradeDB.getUserConfig(new Object[]{ user.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[]{id,userid , payChannel , 0 , 0,map.get("T1SaleRate"),map.get("T0SaleRate"),map.get("T1SettlementRate"),map.get("T0SettlementRate")});
				int x = TradeDB.saveUserConfig(list)[0];
				if(x < 0 ){
					logger.info("用户：" + user.getID() + "支付类型：" + payChannel + "系统为查到该类型配置信息" );
					repData.setRespCode(RespCode.TradeTypeConfigError[0]);
					repData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					map = TradeDB.getUserConfig(new Object[]{ user.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, user.getID() + payChannel + "userConfig" , map);
		}else{
			logger.info("用户：" + user.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作");
			map = (Map<String,Object>) obj;
			obj = null;
		}
		
		
		/**  获取交易商户  **/
		Map<String,Object> merchantMap = TradeDB.getMerchantInfo(new Object[]{user.getID() , payChannel}); 
		if(merchantMap==null||merchantMap.isEmpty()){
			
			merchantMap = TradeDB.getMerchantInfo(new Object[]{user.getID() , "1"}); 
			
			String MerchantID = UtilsConstant.ObjToStr(merchantMap.get("MerchantID"));
			
			JSONObject openjson = OpenProductUtil.openProduct("DIRKUAI", MerchantID , map.get("T0SaleRate").toString(), map.get("T1SaleRate").toString()); 
			
			String respCode = "";
			if(openjson.has("respCode")){
				respCode = openjson.getString("respCode");
			}else if(openjson.has("retCode")){
				respCode =  openjson.getString("retCode");
			}
			
			if ("0000".equals(respCode)) {
				String signKey = openjson.getString("signKey");
				String desKey = openjson.getString("desKey");
				// QueryKey , MerchantName , MerchantID 
				String QueryKey = UtilsConstant.ObjToStr(merchantMap.get("QueryKey"));
				String MerchantName = UtilsConstant.ObjToStr(merchantMap.get("MerchantName"));
				
				TradeDB.saveMerchant(new Object[]{MerchantID,MerchantName,signKey,desKey,QueryKey,user.getID(),payChannel});
				
				merchantMap.put("MerchantID",MerchantID);
				merchantMap.put("SignKey",signKey);
				merchantMap.put("DESKey",desKey);
				
			}else{
				logger.info(user.getLoginID() + "获取商户信息失败");
				repData.setRespCode(RespCode.MerchantNoConfig[0]);
				repData.setRespDesc(RespCode.MerchantNoConfig[1]); 
				return ;
			}
		}
		
		
		/** 查询用户交易配置信息  **/
		Map<String,Object> userconfigmap = null;
		obj = ehcache.get(Constant.cacheName, user.getID() + payChannel +"userConfig");
		if(obj == null){
			
			logger.info("缓存读取用户支付配置信息失败，从数据中读取， 用户：" + user.getID() + " , 支付类型:" + payChannel);
			userconfigmap = TradeDB.getUserConfig(new Object[]{ user.getID() , payChannel});
			if(userconfigmap==null||userconfigmap.isEmpty()){
				String id = UtilsConstant.getUUID();
				String userid = user.getID();
				userconfigmap = TradeDB.getUserConfig(new Object[]{ user.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[] { id, userid, payChannel , 0, 0, userconfigmap.get("T1SaleRate"), userconfigmap.get("T0SaleRate"), 
						userconfigmap.get("T1SettlementRate"), userconfigmap.get("T0SettlementRate") });
				int x = TradeDB.saveUserConfig(list)[0];
				if(x < 0 ){
					logger.info("用户：" + user.getID() + "支付类型：" +payChannel+ "系统为查到该类型配置信息" );
					repData.setRespCode(RespCode.TradeTypeConfigError[0]);
					repData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					userconfigmap = TradeDB.getUserConfig(new Object[]{ user.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, user.getID() + payChannel +"userConfig" , userconfigmap);
		}else{
			logger.info("用户：" + user.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作");
			userconfigmap = (Map<String,Object>) obj;
			obj = null;
		}
		
		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
		String bankCardno = "";
		
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(reqData.getBankCardNo(), desckey);
			logger.info("用户：" + user.getLoginID() + "请求开通无卡快捷支付请求 , 开通银行卡卡号：(原文)" + bankCardno);
		} catch (Exception e) {
			e.printStackTrace();
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
		
		
		try {
			//  查询交易配置信息
			Map<String,Object> tradeConfig = null;
			obj = ehcache.get(Constant.cacheName, "tradeConfig");
			if(obj == null){
				logger.info("缓存中获取交易配置信息失败,从数据库中查询");
				tradeConfig = AppconfigDB.getTradeConfig(); 
				ehcache.put(Constant.cacheName, "tradeConfig", tradeConfig); 
			}else{
				logger.info("缓存查询交易配置信息");
				tradeConfig = (Map<String,Object>) obj;
				obj = null;
			}
			
//			String nowtime = DateUtil.getNowTime(DateUtil.HHmm);
//			Integer nowHour = Integer.parseInt(nowtime.split("-")[0]);
//			
//			String KuaiT0Time = UtilsConstant.ObjToStr(tradeConfig.get("KuaiT0Time"));
//			logger.info("快捷T0交易时间段:" + KuaiT0Time + ", 当前时间:" + nowHour);
			
			String orderID = UtilsConstant.getOrderNumber();
			logger.info("银行卡卡号：" + bankCardno + "需要调用开通请求");

			map = new TreeMap<String, Object>();
			map.put("accNo", DESUtil.encode(Constant.REPORT_DES3_KEY, bankCardno));
			map.put("merchantNo", merchantMap.get("MerchantID"));
			map.put("orderNum", orderID);
			map.put("encrypt", "T0");
			map.put("type", "T001");
			map.put("phone", reqData.getPayerPhone());
			if (!UtilsConstant.strIsEmpty(reqData.getCvn2())) {
				map.put("cvn2", reqData.getCvn2());
			} else {
				map.put("cvn2", "649");
			}

			if (!UtilsConstant.strIsEmpty(reqData.getExpired()) && reqData.getExpired().length() == 4) {
				StringBuffer sbf = new StringBuffer();
				sbf.append(reqData.getExpired().substring(2));
				sbf.append(reqData.getExpired().substring(0, 2));
				map.put("expired", sbf.toString());
			} else {
				map.put("expired", "2210");
			}

			logger.info(JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY);

			String sign = MD5.sign(JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY, StringEncoding.UTF_8);
			map.put("sign", sign.toUpperCase());
			String content = HttpClient.post(LoadPro.loadProperties("http", "OPENKUAI_URL"), map, null);
			logger.info("银行卡卡号：" + reqData.getBankCardNo() + "开通快捷响应:" + content);
			
			JSONObject result = JSONObject.fromObject(content);
			if(Constant.payRetCode.equals(result.getString("respCode"))){
				repData.setRespCode(RespCode.SUCCESS[0]);
				repData.setRespDesc(RespCode.SUCCESS[1]);
			}
		} catch (Exception e1) {
			logger.error("开通银行卡异常" + e1.getMessage(), e1); 
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
	}
}
