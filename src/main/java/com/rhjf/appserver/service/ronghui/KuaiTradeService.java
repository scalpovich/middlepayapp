package com.rhjf.appserver.service.ronghui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.AgentDAO;
import com.rhjf.appserver.db.AppConfigDAO;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.PayOrderDAO;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.service.creditcard.KuaiTradeInterfaceService;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.OpenProductUtil;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

public class KuaiTradeService implements KuaiTradeInterfaceService{
LoggerTool log  = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void send(LoginUser loginUser,RequestData reqData , ResponseData repData){
		
		log.info("用户"+  loginUser.getLoginID() + "发起支付请求") ;
		
		/**  获取支付类型 **/
		String payChannel = reqData.getPayChannel();
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		if(loginUser.getBankInfoStatus()!=1||loginUser.getPhotoStatus()!=1){
			repData.setRespCode("F002");
			repData.setRespDesc("该用户没有通过审核无法进行交易"); 
			return ;
		}
		
		/**  查询结算卡信息 **/
		Map<String,Object> bankInfoMap =  null;
		Object bankInfoObj = ehcache.get(Constant.cacheName, loginUser.getID()  + "userbankinfo");
		if(bankInfoObj == null){
			bankInfoMap = LoginUserDAO.getUserBankCard(loginUser.getID());
			if(bankInfoMap!=null && !bankInfoMap.isEmpty()){
				ehcache.put(Constant.cacheName,  loginUser.getID()  + "userbankinfo", bankInfoMap);
			}
		}else{
			bankInfoMap = (Map<String,Object>)bankInfoObj;
		}
		
		if(bankInfoMap!=null&&!bankInfoMap.isEmpty()){
			Integer totalAmount =  PayOrderDAO.dayTradeAmount(new Object[]{loginUser.getLoginID() , DateUtil.getNowTime(DateUtil.yyyyMMdd)});
			if("".equals(UtilsConstant.ObjToStr(bankInfoMap.get("SettleCreditCard")))){
				//  没有通过信用卡鉴权
				if(totalAmount+Integer.parseInt(reqData.getAmount()) > 3000000 ){
					repData.setRespCode("F003");
					repData.setRespDesc("当前单日限额3万元，提供信用卡信息可提升至20万元，是否提供？"); 
					return ;
				}
			}else{
				if(totalAmount+Integer.parseInt(reqData.getAmount()) > 20000000 ){
					repData.setRespCode("F003");
					repData.setRespDesc("当前单日限额20万元"); 
					return ;
				}
			}
		}
		
		
		
		/**  查询代理商交易配置信息 **/
		Map<String,Object> agentconfigmap = null;
		Object agentConfigobj = ehcache.get(Constant.cacheName , loginUser.getAgentID() + payChannel + "agentConfig");
		
		if(agentConfigobj == null){
			log.info("缓存读取代理商交易信息失败，将从数据库中读取: 交易类型：" + payChannel + "代理商ID：" + loginUser.getAgentID()); 
			agentconfigmap = AgentDAO.agentConfig(new Object[]{loginUser.getAgentID() ,payChannel });
			if(agentconfigmap == null || agentconfigmap.isEmpty()){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置信息不完整, 对应代理商ID：" +  loginUser.getAgentID());
				repData.setRespCode(RespCode.AgentTradeConfigError[0]);
				repData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}else{
			log.info("缓存中读取代理商交易信息[成功] ,  交易类型：" + payChannel + "代理商ID：" + loginUser.getAgentID());
			agentconfigmap = (Map<String,Object> ) agentConfigobj;
		}
		
		
		/** 查询用户交易配置信息  **/
		Map<String,Object> map = null;
		Object obj = ehcache.get(Constant.cacheName, loginUser.getID() + payChannel + "userConfig");
		if(obj == null){
			log.info("缓存读取用户支付配置信息失败，从数据中读取， 用户：" + loginUser.getID() + " , 支付类型:" + payChannel);
			map = TradeDAO.getUserConfig(new Object[]{ loginUser.getID() , payChannel});
			if(map==null||map.isEmpty()){
				// ID,UserID,PayChannel,SaleAmountMax,SaleAmountMaxDay,T1SaleRate,T0SaleRate,T1SettlementRate,T0SettlementRate
				String id = UtilsConstant.getUUID();
				String userid = loginUser.getID();
				map = TradeDAO.getUserConfig(new Object[]{ loginUser.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[]{id,userid,payChannel,0,0,map.get("T1SaleRate"),map.get("T0SaleRate"),map.get("T1SettlementRate"),map.get("T0SettlementRate")});
				int x = TradeDAO.saveUserConfig(list)[0];
				if(x < 0 ){
					log.info("用户：" + loginUser.getID() + "支付类型：" + payChannel + "系统为查到该类型配置信息" );
					repData.setRespCode(RespCode.TradeTypeConfigError[0]);
					repData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					map = TradeDAO.getUserConfig(new Object[]{ loginUser.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, loginUser.getID() + payChannel + "userConfig" , map);
		}else{
			log.info("用户：" + loginUser.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作");
			map = (Map<String,Object>) obj;
			obj = null;
		}
		
		
		/**  获取交易商户  **/
		Map<String,Object> merchantMap = TradeDAO.getMerchantInfo(new Object[]{loginUser.getID() , payChannel});
		if(merchantMap==null||merchantMap.isEmpty()){
			
			merchantMap = TradeDAO.getMerchantInfo(new Object[]{loginUser.getID() , "1"});
			
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
				
				TradeDAO.saveMerchant(new Object[]{MerchantID,MerchantName,signKey,desKey,QueryKey,loginUser.getID(),payChannel});
				
				merchantMap.put("MerchantID",MerchantID);
				merchantMap.put("SignKey",signKey);
				merchantMap.put("DESKey",desKey);
				
			}else{
				log.info(loginUser.getLoginID() + "获取商户信息失败");
				repData.setRespCode(RespCode.MerchantNoConfig[0]);
				repData.setRespDesc(RespCode.MerchantNoConfig[1]); 
				return ;
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
		
		String encrypt = Constant.T1;
		
		String nowtime = DateUtil.getNowTime(DateUtil.HHmm);
		
		Integer nowHour = Integer.parseInt(nowtime.split("-")[0]);
		
		/** T0交易开始小时和分钟 **/
		String KuaiT0Time = UtilsConstant.ObjToStr(tradeConfig.get("KuaiT0Time"));
		
		Integer startHour = Integer.parseInt(KuaiT0Time.split("-")[0]);
		/** T0交易结束小时和分钟 **/
		Integer EndHour = Integer.parseInt(KuaiT0Time.split("-")[1]);
		
		
		/**
		 * 判断当前交易时间段是否属于T0 交易时间段
		 * 如果该时间段不能发送T0交易将改为T1交易
		 */
		if(nowHour>=startHour&&nowHour<EndHour){
			log.info("属于T0交易时间段");
			encrypt = Constant.T0;
		}else{
			log.info("当前时间：" + nowHour + "不属于交易时间段");
			repData.setRespCode(RespCode.TradeTimeError[0]);
			repData.setRespDesc(RespCode.TradeTimeError[1]); 
			return ;
		}
		
		/** 判断交易金额是否小于T0最低金额 **/
		/*String t0minAmount = UtilsConstant.ObjToStr(tradeConfig.get("T0MinAmount"));
		if(Integer.parseInt(reqData.getAmount()) < Integer.parseInt(t0minAmount)){
			log.info("交易金额：" + reqData.getAmount() + "T0最小金额" + t0minAmount + "将交易转换为T1交易");
			encrypt = Constant.T1;
		}*/
		
		String feeRate = map.get("T1SettlementRate").toString();
		
		if(Constant.T1.equals(encrypt)){
			// ChannelRate , AgentRate , MerchantRate , T0ChannelRate , T0AgentRate , T0MerchantRate
			if("".equals(UtilsConstant.ObjToStr(agentconfigmap.get("AgentRate")))
					||"".equals(UtilsConstant.ObjToStr(agentconfigmap.get("AgentRate")))){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置 [ T1 ] 信息不完整 , 对应代理商ID：" +  loginUser.getAgentID());
				repData.setRespCode(RespCode.AgentTradeConfigError[0]);
				repData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}else{
			
			feeRate = map.get("T0SettlementRate").toString();
			
			if("".equals(UtilsConstant.ObjToStr(agentconfigmap.get("T0AgentRate")))
					||"".equals(UtilsConstant.ObjToStr(agentconfigmap.get("T0MerchantRate")))){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置 [ T0 ] 信息不完整 , 对应代理商ID：" +  loginUser.getAgentID());
				repData.setRespCode(RespCode.AgentTradeConfigError[0]);
				repData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}
		
		
		/** 终端发起的交易日期 **/
		String tradeDate = reqData.getSendTime().substring(0, 8);
		/** 终端发起的交易时间 **/
		String tradeTime = reqData.getSendTime().substring(8);
		
		/** 订单号 **/
		String orderNumber = UtilsConstant.getOrderNumber();
		
		/** 交易商户编号 **/
		String merchantID = merchantMap.get("MerchantID").toString();
		/** 交易签名秘钥 **/
		String signKey = merchantMap.get("SignKey").toString();
		/** 卡信息加密秘钥 **/
		String desKey = merchantMap.get("DESKey").toString();
		
		log.info("获取SignKey:"+ signKey);
		
		
		String url = LoadPro.loadProperties("http", "KUAI_ScanCodeUrl");
		String trxType = Constant.KUAI_ScanCode;
		
		double amount = AmountUtil.div(reqData.getAmount(), "100" , 2);
		Map<String,Object> payrequest = new LinkedHashMap<String,Object>();
		payrequest.put("trxType",trxType);
		payrequest.put("merchantNo",  merchantID);
		payrequest.put("orderNum",orderNumber);
		payrequest.put("amount",amount);
		
		
		Map<String, Object> termKey = TermKeyDAO.selectTermKey(loginUser.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
		String bankCardno = "";
		String cardNo = "";
		try {
			log.info("用户：" + loginUser.getLoginID() + "请求无卡快捷支付请求 , 银行卡卡号：(密文)" + reqData.getBankCardNo());
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(reqData.getBankCardNo(), desckey);
			log.info("用户：" + loginUser.getLoginID() + "请求无卡快捷支付请求 , 银行卡卡号：(原文)" + bankCardno);

			cardNo = DESUtil.encode(desKey,bankCardno);
		} catch (Exception e) {
			e.printStackTrace();
			
			log.error("卡号加密异常", e); 
			
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
		
		boolean isok = false;
		StackTraceElement[] es = Thread.currentThread().getStackTrace();
		for (StackTraceElement e : es) {
			if("com.rhjf.appserver.service.creditcard.DynamicProxy".equals(e.getClassName())){
				log.info("StackTraceElement 集合中 包含  【 com.rhjf.appserver.service.creditcard.DynamicProxy 】"); 
				isok = true;
				break;
			}
		}
		
		String callbackUrl = LoadPro.loadProperties("http" , "WX_ScanCodeCallbackUrl");
		String TradeType = Constant.TradeType[0];
		String creditCardNo = "";
		if(isok){
			log.info("用户执行信用卡还款交易");
			TradeType = "信用卡还款";
			callbackUrl = LoadPro.loadProperties("http" , "CreditCardNotiy");
			
			
			try {
				log.info("用户：" + loginUser.getLoginID() + "请求信用卡还款 , 银行卡卡号：(密文)" + reqData.getCreditCardNo());
				String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
				creditCardNo = DES3.decode(reqData.getCreditCardNo(), desckey);
				log.info("用户：" + loginUser.getLoginID() + "请求信用卡还款收款卡号 , ：(原文)" + creditCardNo);

			} catch (Exception e) {
				e.printStackTrace();
				
				log.error("卡号加密异常", e); 
				
				repData.setRespCode(RespCode.SYSTEMError[0]);
				repData.setRespDesc(RespCode.SYSTEMError[1]);
				return ;
			}
			
		}
		
		/** 向数据库插入初始化数据 **/
		int ret = TradeDAO.tradeInit(new Object[]{UtilsConstant.getUUID(),reqData.getAmount() ,
				DateUtil.getNowTime(DateUtil.yyyyMMdd),DateUtil.getNowTime(DateUtil.HHmmss),
				tradeDate,tradeTime , reqData.getSendSeqId(), TradeType , 
				encrypt, loginUser.getID(),payChannel, feeRate ,merchantID,orderNumber , creditCardNo ,bankCardno , loginUser.getAgentID(), "RONGHUI"});
		
		if(ret < 1 ){
			log.info("数据库保存信息失败");
			repData.setRespCode(RespCode.ServerDBError[0]);
			repData.setRespDesc(RespCode.ServerDBError[1]);
			return ;
		}
		
	    payrequest.put("goodsName",loginUser.getMerchantName());
	    payrequest.put("callbackUrl",callbackUrl);
	    payrequest.put("serverCallbackUrl", callbackUrl);
		payrequest.put("orderIp", "10.10.20.187");
		payrequest.put("cardNo", cardNo);
		payrequest.put("payerPhone", reqData.getPayerPhone());
		payrequest.put("kuaiType","PAY");
		payrequest.put("encrypt",encrypt);
		
		StringBuffer str = new StringBuffer("#");
		
		for (String  key : payrequest.keySet()) {
			str.append(payrequest.get(key)); 
			str.append("#");
		}
		
		String sign = MD5.sign( str + signKey , StringEncoding.UTF_8);
		payrequest.put("sign", sign);
		
		log.info("请求报文:" + payrequest.toString());
		
		try {
			String content = HttpClient.post(url, payrequest, "1");
			JSONObject json = JSONObject.fromObject(content);
			log.info("响应报文："+json.toString());
			String retCode = "";
			
			if(json.has("respCode")){
				retCode = json.getString("respCode");
			}else if(json.has("retCode")){
				retCode = json.getString("retCode");
			}
			
			String msg = "";
			
			if(json.has("respMsg")){
				msg = json.getString("respMsg");
			}else if(json.has("msg")){
				msg = json.getString("msg");
			}else if(json.has("retMsg")){
				msg = json.getString("retMsg");
			}
			
			//无卡快捷支付
			if (encrypt.equals(Constant.T0)) {
				if (retCode.equals(Constant.payRetCode)) {
					repData.setRespCode(RespCode.SUCCESS[0]);
					repData.setRespDesc(RespCode.SUCCESS[1]);
					repData.setOrderNumber(orderNumber);
					repData.setMerchantNo(merchantID);
				} else {
					repData.setRespCode(retCode);
					repData.setRespDesc(msg);
				}
			} else {
				/** T1返回状态码0000 **/
				if (retCode.equals(Constant.payRetCode)) {
					repData.setRespCode(RespCode.SUCCESS[0]);
					repData.setRespDesc(RespCode.SUCCESS[1]);
					repData.setOrderNumber(orderNumber);
					repData.setMerchantNo(merchantID);
				} else {
					repData.setRespCode(retCode);
					repData.setRespDesc(msg);
				}
			}
			
			
			if(retCode.equals("8221")){
				log.info("银行：" + "" + "为开通快捷服务");
				repData.setRespCode("01");
				repData.setRespDesc("交易金额受限，需再次绑定银行卡信息。");
			}
			
		} catch (Exception e) {
			log.error("请求获取发生异常:"  + e.getMessage() , e); 
			repData.setRespCode(RespCode.HttpClientError[0]);
			repData.setRespDesc(RespCode.HttpClientError[1]);
		}
	}
	

	public void confirm(LoginUser loginUser,RequestData reqData , ResponseData repData){
		Map<String,Object> payrequest = new LinkedHashMap<String,Object>();
		/**  获取交易商户  **/
		Map<String,Object> merchantMap = TradeDAO.getMerchantInfo(new Object[]{loginUser.getID() ,  "4"});
		if(merchantMap==null||merchantMap.isEmpty()){
			log.info(loginUser.getLoginID() + "获取商户信息失败");
			repData.setRespCode(RespCode.MerchantNoConfig[0]);
			repData.setRespDesc(RespCode.MerchantNoConfig[1]); 
			return ;
		}
		
		String signKey = merchantMap.get("SignKey").toString();
		String url = LoadPro.loadProperties("http", "KUAI_Confirm");
       
		payrequest.put("trxType","OnlineKuaiPayConfirm");
		payrequest.put("merchantNo", reqData.getMerchantNo());
		payrequest.put("orderNum",reqData.getOrderNumber());
		payrequest.put("smsVerifyCode", reqData.getSmsCode());
		StringBuffer str = new StringBuffer("#");
		for (String key : payrequest.keySet()) {
			str.append(payrequest.get(key));
			str.append("#");
		}
		String sign = MD5.sign(str + signKey, StringEncoding.UTF_8);
		payrequest.put("sign", sign);
		log.info("请求报文:" + payrequest.toString());
		
		try {
			String content = HttpClient.post(url, payrequest, "1");
			log.info("响应报文："+ content);
			JSONObject json = JSONObject.fromObject(content);
			String retCode=json.getString("respCode");
			String msg=json.getString("respMsg");
			if(retCode.equals(Constant.payRetCode)){
				repData.setRespCode(RespCode.SUCCESS[0]);
				repData.setRespDesc(RespCode.SUCCESS[1]);
			}else{
				repData.setRespCode(retCode);
				repData.setRespDesc(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("请求获取发生异常:"  + e.getMessage()); 
			repData.setRespCode(RespCode.HttpClientError[0]);
			repData.setRespDesc(RespCode.HttpClientError[1]);
		}
	}
}
