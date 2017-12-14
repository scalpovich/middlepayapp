package com.rhjf.appserver.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.AppconfigDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.PayOrderDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.db.UserBankCardDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.AmountUtil;
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

public class TradeService {

	LoggerTool log  = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void send(TabLoginuser loginUser,RequestData reqData , ResponseData responseData){
		
		log.info("用户"+  loginUser.getLoginID() + "发起支付请求") ;
		
		if("AGENT".equals(loginUser.getUserType())){
			responseData.setRespCode(RespCode.SystemConfigError[0]);
			responseData.setRespDesc("该用户为代理商商户，无法进行交易操作"); 
			return;
		}
		
		
		if(loginUser.getBankInfoStatus()!=1||loginUser.getPhotoStatus()!=1){
			responseData.setRespCode("F002");
			responseData.setRespDesc("该用户没有通过审核无法进行交易"); 
			return ;
		}
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();

		
		/**  查询结算卡信息 **/
		Map<String,Object> bankInfoMap =  null;
		Object bankInfoObj = ehcache.get(Constant.cacheName, loginUser.getID()  + "userbankinfo");
		if(bankInfoObj == null){
			bankInfoMap = LoginUserDB.getUserBankCard(loginUser.getID());
			if(bankInfoMap!=null && !bankInfoMap.isEmpty()){
				ehcache.put(Constant.cacheName,  loginUser.getID()  + "userbankinfo", bankInfoMap);
			}
		}else{
			bankInfoMap = (Map<String,Object>)bankInfoObj;
		}
		
		if(bankInfoMap!=null&&!bankInfoMap.isEmpty()){
			Integer totalAmount =  PayOrderDB.dayTradeAmount(new Object[]{loginUser.getLoginID() , DateUtil.getNowTime(DateUtil.yyyyMMdd)});
			if("".equals(UtilsConstant.ObjToStr(bankInfoMap.get("SettleCreditCard")))){
				//  没有通过信用卡鉴权
				if(totalAmount+Integer.parseInt(reqData.getAmount()) > 3000000 ){
					responseData.setRespCode("F003");
					responseData.setRespDesc("当前单日限额3万元，提供信用卡信息可提升至20万元，是否提供？"); 
					return ;
				}
			}else{
				if(totalAmount+Integer.parseInt(reqData.getAmount()) > 20000000 ){
					responseData.setRespCode("F003");
					responseData.setRespDesc("当前单日限额20万元"); 
					return ;
				}
			}
		}
		
		/**  获取支付类型 **/
		String payChannel = reqData.getPayChannel();
		
		Map<String,Object> agentconfigmap = null;
		Object agentConfigobj = ehcache.get(Constant.cacheName, loginUser.getAgentID() + payChannel + "agentConfig");
		
		if(agentConfigobj == null){
			log.info("缓存读取代理商交易信息失败，将从数据库中读取: 交易类型：" + payChannel + "代理商ID：" + loginUser.getAgentID()); 
			agentconfigmap = AgentDB.agentConfig(new Object[]{loginUser.getAgentID() ,payChannel });
			if(agentconfigmap == null || agentconfigmap.isEmpty()){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置信息不完整, 对应代理商ID：" +  loginUser.getAgentID());
				responseData.setRespCode(RespCode.AgentTradeConfigError[0]);
				responseData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
			ehcache.put(Constant.cacheName, loginUser.getAgentID() + payChannel + "agentConfig", agentconfigmap);
		}else{
			log.info("缓存中读取代理商交易信息[成功] ,  交易类型：" + payChannel + "代理商ID：" + loginUser.getAgentID());
			agentconfigmap = (Map<String,Object>) agentConfigobj;
		}
		
		/** 查询用户交易配置信息  **/
		Map<String,Object> map = null;
		Object obj = ehcache.get(Constant.cacheName, loginUser.getID() + payChannel + "userConfig");
		if(obj == null){
			
			log.info("缓存读取用户支付配置信息失败，从数据中读取， 用户：" + loginUser.getID() + " , 支付类型:" + payChannel);
			map = TradeDB.getUserConfig(new Object[]{ loginUser.getID() , payChannel});
			if(map==null||map.isEmpty()){
				
				log.info("支付类型" + payChannel + "读取数据库失败, 复制支付类型为1的配置信息");
				String id = UtilsConstant.getUUID();
				String userid = loginUser.getID();
				map = TradeDB.getUserConfig(new Object[]{ loginUser.getID() , "1"});
				
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[]{id,userid , payChannel , 0 , 0 , map.get("T1SaleRate") , map.get("T0SaleRate"),
						map.get("T1SettlementRate"),map.get("T0SettlementRate")});
				int x = TradeDB.saveUserConfig(list)[0];
				if(x < 0 ){
					log.info("用户：" + loginUser.getID() + "支付类型：" + payChannel + " 支付配置信息复制失败,停止交易操作" );
					responseData.setRespCode(RespCode.TradeTypeConfigError[0]);
					responseData.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					map = TradeDB.getUserConfig(new Object[]{ loginUser.getID() , payChannel});
				}
			}
			ehcache.put(Constant.cacheName, loginUser.getID() + payChannel +"userConfig" , map);
		}else{
			map = (Map<String,Object>) obj;
			
			log.info("用户：" + loginUser.getID() + " , 支付类型:" + payChannel + "缓存读取信息成功 继续操作" + map.toString());
			obj = null;
		}
		
		/**  获取交易商户  **/
		Map<String,Object> merchantMap = TradeDB.getMerchantInfo(new Object[]{loginUser.getID() ,  payChannel}); 
		if(merchantMap==null||merchantMap.isEmpty()){
			merchantMap = TradeDB.getMerchantInfo(new Object[]{loginUser.getID() , "1"}); 
			String MerchantID = UtilsConstant.ObjToStr(merchantMap.get("MerchantID"));
			
			String productType = "QQPAY";
			if(payChannel.equals(Constant.payChannelQQScancode)){
				productType = "QQPAY";
			}else if(payChannel.equals(Constant.payChannelunionQRCode)){
				productType = "UnionPay";
			}else if(payChannel.equals(Constant.payChannelJDScancode)){
				productType = "JD";
			}
			
			JSONObject openjson = OpenProductUtil.openProduct(productType, MerchantID , map.get("T0SaleRate").toString(), map.get("T1SaleRate").toString()); 
			
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
				
				TradeDB.saveMerchant(new Object[]{MerchantID,MerchantName,signKey,desKey,QueryKey,loginUser.getID(),payChannel});
				
//				merchantMap.put("MerchantID",MerchantID);
//				merchantMap.put("SignKey",signKey);
//				merchantMap.put("DESKey",desKey);
				
				merchantMap = TradeDB.getMerchantInfo(new Object[]{loginUser.getID() , payChannel}); 
				
				if(merchantMap == null || merchantMap.isEmpty()){
					log.info(loginUser.getLoginID() + "获取 " + payChannel + "商户信息失败");
					responseData.setRespCode(RespCode.MerchantNoConfig[0]);
					responseData.setRespDesc(RespCode.MerchantNoConfig[1]); 
					return ;
				}
			}else{
				log.info(loginUser.getLoginID() + "获取商户信息失败");
				responseData.setRespCode(RespCode.MerchantNoConfig[0]);
				responseData.setRespDesc(RespCode.MerchantNoConfig[1]); 
				return ;
			}
		}
		
		//  查询交易配置信息
		Map<String,Object> tradeConfig = null;
		obj = ehcache.get(Constant.cacheName, "tradeConfig");
		if(obj == null){
			log.info("缓存中获取交易配置信息失败,从数据库中查询");
			tradeConfig = AppconfigDB.getTradeConfig(); 
			ehcache.put(Constant.cacheName, "tradeConfig", tradeConfig);
		}else{
			log.info("缓存查询交易配置信息");
			tradeConfig = (Map<String,Object>) obj;
		}
		
		
		String encrypt = loginUser.getTradeCode();
		
		
		log.info("用户：" + loginUser.getLoginID() + "发送[ " + reqData.getTradeCode() + " ]交易"); 
		
		if(!UtilsConstant.strIsEmpty(reqData.getTradeCode())){
			encrypt = reqData.getTradeCode() ;
		}
		
		
		log.info("用户：" + loginUser.getLoginID() + "发送[ " + encrypt + " ]交易"); 
		
		String nowtime = DateUtil.getNowTime(DateUtil.HHmm);
		
		Integer nowHour = Integer.parseInt(nowtime.split("-")[0]);
		Integer nowMinute = Integer.parseInt(nowtime.split("-")[1]);
		
		/** T0交易开始小时和分钟 **/
		Integer startHour = Integer.parseInt(UtilsConstant.ObjToStr(tradeConfig.get("T0StartHour")));
		Integer startMinute = Integer.parseInt(UtilsConstant.ObjToStr(tradeConfig.get("T0StartMinute")));
		/** T0交易结束小时和分钟 **/
		Integer EndHour = Integer.parseInt(UtilsConstant.ObjToStr(tradeConfig.get("T0EndHour")));
		Integer EndMinute = Integer.parseInt(UtilsConstant.ObjToStr(tradeConfig.get("T0EndMinute")));
		
		/**
		 * 判断当前交易时间段是否属于T0 交易时间段
		 * 如果该时间段不能发送T0交易将改为T1交易
		 */
		if(nowHour<startHour||nowHour>EndHour){
			log.info("属于 [ T1 ] 交易时间段");
			encrypt = Constant.T1;
		}else if((nowHour==startHour&&nowMinute<startMinute)||(nowHour==EndHour&&nowMinute>EndMinute)){
			log.info("属于 [ T1 ] 交易时间段");
			encrypt = Constant.T1;
		}else{
			log.info("属于 [ T0 ] 交易间段");
		}
		
		/** 判断交易金额是否小于T0最低金额 **/
		String t0minAmount = UtilsConstant.ObjToStr(tradeConfig.get("T0MinAmount"));
		if(Integer.parseInt(reqData.getAmount()) < Integer.parseInt(t0minAmount)){
			log.info("交易金额：" + reqData.getAmount() + "T0最小金额" + t0minAmount + "将交易转换为T1交易");
			encrypt = Constant.T1;
		}
		
		String feeRate = map.get("T1SettlementRate").toString();
		
		/** 根据交易类型判断代理商费率的完整性，如果代理商只存在T0费率，商户发起T1交易将不通过 **/
		if(Constant.T1.equals(loginUser.getTradeCode())){
			// ChannelRate , AgentRate , MerchantRate , T0ChannelRate , T0AgentRate , T0MerchantRate
			if("".equals(UtilsConstant.ObjToStr(agentconfigmap.get("AgentRate")))||"".equals(UtilsConstant.ObjToStr(agentconfigmap.get("AgentRate")))){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置 [ T1 ] 信息不完整 , 对应代理商ID：" +  loginUser.getAgentID());
				responseData.setRespCode(RespCode.AgentTradeConfigError[0]);
				responseData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}else{
			
			feeRate = map.get("T0SettlementRate").toString();
			
			if("".equals(UtilsConstant.ObjToStr(agentconfigmap.get("T0AgentRate")))||"".equals(UtilsConstant.ObjToStr(agentconfigmap.get("T0MerchantRate")))){
				log.info("用户：" + loginUser.getLoginID() + "对应代理商交易类型：" + payChannel + "配置 [ T0 ] 信息不完整 , 对应代理商ID：" +  loginUser.getAgentID());
				responseData.setRespCode(RespCode.AgentTradeConfigError[0]);
				responseData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
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
		
		
		/** 默认交易交易类型为 微信扫码支付 **/
		String url = LoadPro.loadProperties("http" , "WX_ScanCodeUrl");
		String trxType = Constant.WX_ScanCode;
		
		/** 如果支付类型为支付宝扫码支付 **/
		if(reqData.getPayChannel().equals(Constant.payChannelAliScancode)){
			trxType =  Constant.Ali_ScanCode;
			url = LoadPro.loadProperties("http", "Alipay_ScanCodeUrl");
		} else
		
		/** 如果支付类型为qq扫码支付 **/
		if(reqData.getPayChannel().equals(Constant.payChannelQQScancode)){
			trxType =  Constant.QQ_ScanCode;
			url = LoadPro.loadProperties("http", "QQ_ScanCodeUrl");
		} else
		
		/** 如果支付类型为 银联二维码 **/
		if(reqData.getPayChannel().equals(Constant.payChannelunionQRCode)){
			trxType =  Constant.UNION_QRCode;
			url = LoadPro.loadProperties("http", "UNIONQRCODE_ScanCodeUrl");
			//  银联二维码 只支持T1 交易
			encrypt = Constant.T1;   
		}
		
		
		/** 向数据库插入初始化数据 **/
		int ret = TradeDB.tradeInit(new Object[]{UtilsConstant.getUUID(),reqData.getAmount() ,
				DateUtil.getNowTime(DateUtil.yyyyMMdd),DateUtil.getNowTime(DateUtil.HHmmss),
				tradeDate,tradeTime , reqData.getSendSeqId(), Constant.TradeType[0] ,
				encrypt, loginUser.getID(),payChannel, feeRate , merchantID,orderNumber , "" , ""  , loginUser.getAgentID()});
		
		if(ret < 1 ){
			log.info("数据库保存信息失败");
			responseData.setRespCode(RespCode.ServerDBError[0]);
			responseData.setRespDesc(RespCode.ServerDBError[1]);
			return ;
		}
		
		
		double amount = AmountUtil.div(reqData.getAmount(), "100" , 2);
		Map<String,Object> payrequest = new LinkedHashMap<String,Object>();
		payrequest.put("trxType",trxType);
		payrequest.put("merchantNo",  merchantID);
		payrequest.put("orderNum",orderNumber);
		payrequest.put("amount",amount);
		
		/** 如果交易类型为T0 将交易手续费上传 **/
		if(encrypt.equals(Constant.T0)){
			String T0SaleRate = UtilsConstant.ObjToStr(map.get("T0SaleRate"));
			
			if(UtilsConstant.strIsEmpty(T0SaleRate)){
				log.info("用户"+ loginUser.getLoginID() + "没有配置支付类型(编号)" + responseData.getPayChannel() + "的T0费率.");
				responseData.setRespCode(RespCode.SystemConfigError[0]);
				responseData.setRespDesc(RespCode.SystemConfigError[1]);
				return ;
			}
			// T0 附加费用
			int T0additional;
			try {
				T0additional = Integer.parseInt(UtilsConstant.ObjToStr(tradeConfig.get("T0AttachFee")));
			} catch (NumberFormatException e) {
				log.error("格式化T0代付费用失败:" + e.getMessage() + "将代付费用默认设置为2元");
				T0additional = 200;
			}
			int T0fee = AgentDB.makeFeeFurther(reqData.getAmount(), Double.valueOf(T0SaleRate)  , 0) + T0additional ; 
			payrequest.put("t0Fee", AmountUtil.div(T0fee + "" , "100" , 2 ));
		}
		
		payrequest.put("goodsName",loginUser.getMerchantName());
		payrequest.put("serverCallbackUrl", LoadPro.loadProperties("http" , "WX_ScanCodeCallbackUrl"));
		payrequest.put("orderIp","1.1.1.1");
		
		
		Map<String,Object> bankMap = UserBankCardDB.getBankInfo(loginUser.getID());
		
		
		/**  上报结算信息  **/
		if(encrypt.equals(Constant.T0)){
			String toibkn = bankMap.get("BankCode").toString();
			payrequest.put("toibkn", toibkn);
			
			try {
				String cardNo = DESUtil.encode(desKey,bankMap.get("AccountNo").toString());
				String idCardNo = DESUtil.encode(desKey,  loginUser.getIDCardNo());
				String payerName = DESUtil.encode(desKey,bankMap.get("AccountName").toString());
				
				payrequest.put("cardNo", cardNo);
				payrequest.put("idCardNo", idCardNo);
				payrequest.put("payerName", payerName);
				
			} catch (Exception e) {
				e.printStackTrace();
				responseData.setRespCode(RespCode.SYSTEMError[0]);
				responseData.setRespDesc(RespCode.SYSTEMError[1]);
				return ;
			}
			payrequest.put("phoneNumber", loginUser.getLoginID());
		}
		
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
			log.info("请求获取二维码响应体:" + content);
			
			JSONObject json = JSONObject.fromObject(content);
			String retCode = "";
			
			if(json.has("respCode")){
				retCode = json.getString("respCode");
			}else if(json.has("retCode")){
				retCode = json.getString("retCode");
			}
			
			/** T0 交易如果成功将返回 10000  **/
			if(retCode.equals(Constant.T0RetCode) || retCode.equals(Constant.payRetCode) ){
				responseData.setQrCodeUrl(json.getString("qrCode")); 
				responseData.setRespCode(RespCode.SUCCESS[0]);
				responseData.setRespDesc(RespCode.SUCCESS[1]);
				responseData.setRate(feeRate);
			}else if("000004".equals(retCode)){
				responseData.setRespCode(retCode);
				responseData.setRespDesc("通道配置异常");
			}else if("7001".equals(retCode)){
				responseData.setRespCode(retCode);
				responseData.setRespDesc("创建订单失败");
			}else{
				responseData.setRespCode(retCode);
				if(json.has("msg")){
					responseData.setRespDesc(json.getString("msg"));
				}else if(json.has("retMsg")){
					responseData.setRespDesc(json.getString("retMsg"));
				}else if(json.has("respMsg")){
					responseData.setRespDesc(json.getString("respMsg"));
				}
			}
		} catch (Exception e) {
			log.error("请求获取二维码发生异常:"  + e.getMessage() , e); 
			responseData.setRespCode(RespCode.HttpClientError[0]);
			responseData.setRespDesc(RespCode.HttpClientError[1]);
			return ;
		}
	}
}
