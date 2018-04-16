package com.rhjf.appserver.service.ronghui;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.AgentDAO;
import com.rhjf.appserver.db.OpenKuaiDAO;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.*;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *   开通无卡快捷
 * @author a
 *
 */
public class OpenKuaiService {


	LoggerTool logger = new LoggerTool(this.getClass());
	
	
	@SuppressWarnings("unchecked")
	public void openKuai(LoginUser user ,RequestData reqData , ResponseData repData){
		
		logger.info("用户：" + user.getLoginID() + "请求开通无卡快捷支付请求 , 开通银行卡卡号：(密文)" + reqData.getBankCardNo());
		
		String payChannel = "4";
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Map<String,Object> agentconfigmap = null;
		Object agentConfigobj = ehcache.get(Constant.cacheName, user.getAgentID()  + payChannel +"agentConfig");
		
		if(agentConfigobj == null){
			logger.info("缓存读取代理商交易信息失败，将从数据库中读取: 交易类型："+payChannel+" , 代理商ID：" + user.getAgentID()); 
			agentconfigmap = AgentDAO.agentConfig(new Object[]{user.getAgentID() ,payChannel });
			if(agentconfigmap == null || agentconfigmap.isEmpty()){
				logger.info("用户：" + user.getLoginID() + "对应代理商交易类型： "+payChannel+" 配置信息不完整, 对应代理商ID：" +  user.getAgentID());
				repData.setRespCode(RespCode.AgentTradeConfigError[0]);
				repData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}

		/** 查询用户交易配置信息  **/
		Map<String,Object> map = null;
		Object obj = ehcache.get(Constant.cacheName, user.getID() + payChannel + "userConfig");
		if(obj == null){
			logger.info("缓存读取用户支付配置信息失败，从数据中读取， 用户：" + user.getID() + " , 支付类型:" + payChannel);
			map = TradeDAO.getUserConfig(new Object[]{ user.getID() ,payChannel});
			if(map==null||map.isEmpty()){
				// ID,UserID,PayChannel,SaleAmountMax,SaleAmountMaxDay,T1SaleRate,T0SaleRate,T1SettlementRate,T0SettlementRate
				String id = UtilsConstant.getUUID();
				String userid = user.getID();
				map = TradeDAO.getUserConfig(new Object[]{ user.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[]{id,userid,payChannel,0,0,map.get("T1SaleRate"),map.get("T0SaleRate"),map.get("T1SettlementRate"),map.get("T0SettlementRate")});
				int x = TradeDAO.saveUserConfig(list)[0];
				if(x < 0 ){
					logger.info("用户：" + user.getID() + "支付类型：" + payChannel + "系统为查到该类型配置信息" );
					repData.setRespCode(RespCode.TradeTypeConfigError[0]);
					repData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					map = TradeDAO.getUserConfig(new Object[]{ user.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, user.getID() + payChannel + "userConfig" , map);
		}else{
			logger.info("用户：" + user.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作");
			map = (Map<String,Object>) obj;
			obj = null;
		}
		
		
		/**  获取交易商户  **/
		Map<String,Object> merchantMap = TradeDAO.getMerchantInfo(new Object[]{user.getID() , payChannel});
		if(merchantMap==null||merchantMap.isEmpty()){
			
			merchantMap = TradeDAO.getMerchantInfo(new Object[]{user.getID() , "1"});
			
			String MerchantID = UtilsConstant.ObjToStr(merchantMap.get("MerchantID"));
			
			JSONObject openjson = OpenProductUtil.openProduct("KUAI", MerchantID , map.get("T0SaleRate").toString(), map.get("T1SaleRate").toString()); 
			
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
				
				TradeDAO.saveMerchant(new Object[]{MerchantID,MerchantName,signKey,desKey,QueryKey,user.getID(),payChannel});
				
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
			userconfigmap = TradeDAO.getUserConfig(new Object[]{ user.getID() , payChannel});
			if(userconfigmap==null||userconfigmap.isEmpty()){
				String id = UtilsConstant.getUUID();
				String userid = user.getID();
				userconfigmap = TradeDAO.getUserConfig(new Object[]{ user.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[] { id, userid, payChannel, 0, 0, userconfigmap.get("T1SaleRate"), userconfigmap.get("T0SaleRate"), 
						userconfigmap.get("T1SettlementRate"), userconfigmap.get("T0SettlementRate") });
				int x = TradeDAO.saveUserConfig(list)[0];
				if(x < 0 ){
					logger.info("用户：" + user.getID() + "支付类型："+payChannel+"系统为查到该类型配置信息" );
					repData.setRespCode(RespCode.TradeTypeConfigError[0]);
					repData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					userconfigmap = TradeDAO.getUserConfig(new Object[]{ user.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, user.getID() + payChannel + "userConfig" , userconfigmap);
		}else{
			logger.info("用户：" + user.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作");
			userconfigmap = (Map<String,Object>) obj;
			obj = null;
		}
		
		Map<String, Object> termKey = TermKeyDAO.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
//		Map<String,Object> merchantMap = TradeDAO.getMerchantInfo(new Object[]{user.getID(),1});
		Map<String,Object> queryMap = new TreeMap<>();
		String bankCardno = "";
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(reqData.getBankCardNo(), desckey);
			logger.info("用户：" + user.getLoginID() + "请求开通无卡快捷支付请求 , 开通银行卡卡号：(原文)" + bankCardno);
			
			queryMap.put("accNo",  DESUtil.encode(Constant.REPORT_DES3_KEY , bankCardno));
		} catch (Exception e) {
			logger.error("开号加密异常" , e); 
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
		
		queryMap.put("merchantNo" ,merchantMap.get("MerchantID"));
		
		String querysign = MD5.sign(JSONObject.fromObject(queryMap).toString() + Constant.REPORT_QUERY_KEY , StringEncoding.UTF_8);
		queryMap.put("sign", querysign.toUpperCase());
		
		logger.info("银行卡卡号：" + bankCardno + "开通无卡快捷报文：" + queryMap.toString());
		
		try {
			logger.info("查询银行卡号:" + bankCardno + "是否开通无卡快捷");
			String queryContent = HttpClient.post(LoadPro.loadProperties("http", "OPENKUAIQUERY_URL"), queryMap, null); 
			
			logger.info("查询银行卡号:" + bankCardno + "是否开通无卡快捷 响应报文：" + queryContent);
 			
			JSONObject queryjson = JSONObject.fromObject(queryContent);
			
			String respCode = queryjson.getString("respCode");
			
			//  查询交易配置信息
			
//			Map<String,Object> tradeConfig = null;
//			obj = ehcache.get(Constant.cacheName, "tradeConfig");
//			if(obj == null){
//				logger.info("缓存中获取交易配置信息失败,从数据库中查询");
//				tradeConfig = AppConfigDAO.getTradeConfig();
//				ehcache.put(Constant.cacheName, "tradeConfig", tradeConfig); 
//			}else{
//				logger.info("缓存查询交易配置信息");
//				tradeConfig = (Map<String,Object>) obj;
//				obj = null;
//			}
			
//			String nowtime = DateUtil.getNowTime(DateUtil.HHmm);
//			
//			Integer nowHour = Integer.parseInt(nowtime.split("-")[0]);
//			
//			String KuaiT0Time = UtilsConstant.ObjToStr(tradeConfig.get("KuaiT0Time"));
//			
//			/** T0交易开始小时 **/
//			Integer startHour = Integer.parseInt(KuaiT0Time.split("-")[0]);
//			/** T0交易结束小时 **/
//			Integer EndHour = Integer.parseInt(KuaiT0Time.split("-")[1]);
			
//			logger.info("快捷T0交易时间段:" + KuaiT0Time + ", 当前时间:" + nowHour);
			
			if(Constant.payRetCode.equals(respCode)){
				String result = queryjson.getString("result");
				
				String orderID = UtilsConstant.getOrderNumber();
				
				OpenKuaiDAO.save(new Object[]{UtilsConstant.getUUID(),user.getID(),bankCardno,reqData.getPayerPhone()
						, result , "00" , orderID , reqData.getCvn2() ,reqData.getExpired() ,result , orderID,});
				
				if(!"3".equals(result)){
					logger.info("银行卡卡号：" + bankCardno + "需要调用开通请求");
					
					String serverCallBackURL = LoadPro.loadProperties("http", "OPENKUAI_NOTIFY");
					
					map  = new TreeMap<String,Object>();
					map.put("accNo", DESUtil.encode(Constant.REPORT_DES3_KEY , bankCardno));
					map.put("merchantNo", merchantMap.get("MerchantID"));
					map.put("orderNum", orderID);
					if(!UtilsConstant.strIsEmpty(reqData.getPayerPhone())){
						map.put("phone",reqData.getPayerPhone());
					}        
					map.put("serverCallBackUrl", serverCallBackURL);
//					map.put("callBackUrl", "https://gateway.chinaepay.com/trans/frontTransResTokenURL");
					if(!UtilsConstant.strIsEmpty(reqData.getCvn2())){
						map.put("cvn2", reqData.getCvn2());
					}else{
						map.put("cvn2", "675");
					}
					
					if(!UtilsConstant.strIsEmpty(reqData.getExpired())&&reqData.getExpired().length() == 4){
						StringBuffer sbf = new StringBuffer();
						sbf.append(reqData.getExpired().substring(2));
						sbf.append(reqData.getExpired().substring(0,2));
						map.put("expired", sbf.toString());
					}else{
						map.put("expired", "2112");
					}
					
					map.put("encrypt", "T0");
					
//					if(("0".equals(result)||"2".equals(result))&&nowHour>=startHour&&nowHour<EndHour){
//						/**
//						 *  当前属于T0时间段， 没有开通任何类型 或者只开通了T1 将请求开通T0
//						 */
//						map.put("encrypt", "T0");
//					}else if(("1".equals(result)||"0".equals(result))&&(nowHour<startHour||nowHour+1 > EndHour)){
//						/**
//						 *  当前属于T1时间段， 没有开通任何类型 或者只开通了T0 将请求开通T1
//						 */
//						map.put("encrypt", "T1");
//						status = 1;
//					}else{
//						logger.info("银行卡卡号：" + reqData.getBankCardNo() + " 已经开通无卡快捷功能");
//						repData.setRespCode("01");
//						repData.setRespDesc("该卡片已经开通过快捷支付功能");
//						return ;
//					}
					
					logger.info(JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY); 
					
					String sign = MD5.sign(JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY , StringEncoding.UTF_8);
					map.put("sign", sign.toUpperCase());
					String content = HttpClient.post(LoadPro.loadProperties("http", "OPENKUAI_URL"), map, null);
					logger.info("银行卡卡号：" + reqData.getBankCardNo() + "开通快捷响应:" + content);
					
					if(content.length() > 200){
						repData.setRespCode(RespCode.SUCCESS[0]);
						repData.setRespDesc(content);
//						if(status == 0){
//							repData.setTerminalInfo("交易金额受限，需再次绑定银行卡信息。");
//						}else{
//							repData.setTerminalInfo("交易金额受限，需再次绑定银行卡信息。"); 
//						}
					}else{
						JSONObject js = JSONObject.fromObject(content);
						if("2021".equals(js.getString("respCode"))){
							repData.setRespCode("01");
						}else{
							repData.setRespCode(js.getString("respCode"));
						}
						repData.setRespDesc(js.getString("respMsg")); 
					}
					
				}else{
					logger.info("银行卡卡号：" + reqData.getBankCardNo() + " 已经开通无卡快捷功能");
					repData.setRespCode("01");
					repData.setRespDesc("该卡片已经开通过快捷支付功能");
				}
			} else {
				repData.setRespCode(queryjson.getString("respCode"));
				repData.setRespDesc(queryjson.getString("respMsg"));
			}
		} catch (Exception e1) {
			logger.error("开通银行卡异常" + e1.getMessage(), e1); 
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
	}
}
