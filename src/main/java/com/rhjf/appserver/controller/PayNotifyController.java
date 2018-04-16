package com.rhjf.appserver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rhjf.appserver.db.AppConfigDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.service.FeeComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.DeviceTokenDAO;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.SalesManDAO;
import com.rhjf.appserver.db.UserProfitDAO;
import com.rhjf.appserver.model.Fee;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.PushUtil;
import com.rhjf.appserver.util.RabbitmqSend;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;


/**
 *   支付通知入口
 * @author a
 *
 */

@Controller
@RequestMapping("/paynotify")
@ResponseBody
public class PayNotifyController {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	@Autowired
	private FeeComputeService notifyService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("")
	public Object notify(HttpServletRequest request) { 
		
		Map<String,String> map2 = new HashMap<String,String>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map2.put(paramName, paramValue);
				}
			}
		}
		if(map2==null||map2.isEmpty()){
			logger.info("回调报文为空");
			return  RespCode.notifyfail;
		}
		logger.info("接收上游回调, 回调内容:" + map2.toString()); 
		
		StringBuffer text = new StringBuffer("#");
		/**  拼接加密字符串  **/
		for (String key : Constant.notifyParams) {
			if(map2.containsKey(key)){
				String value = map2.get(key);
				if(UtilsConstant.strIsEmpty(value)||"sign".equals(key)){ 
					continue;
				}
				text.append(value);
				text.append("#");
			}
		}
		/** 查询商户信息  **/
		String merchantID = map2.get("r1_merchantNo");
		String paytype = "1";
		if("Alipay".equals(map2.get("r5_business").toString())){
			paytype = "2";
		}else if("QQ".equals(map2.get("r5_business").toString())){
			paytype = "3";
		}else if("B2C".equals(map2.get("r5_business").toString())){
			paytype = "4";
		}else if("UnionPay".equals(map2.get("r5_business").toString())){
			paytype = "5";
		}
		
		
		Map<String,Object> map =  TradeDAO.getMerchantInfo(merchantID,paytype);
		/**  计算签名 **/
		String serverSign = MD5.sign(text.append(map.get("SignKey")).toString(), StringEncoding.UTF_8);
		String reqSign = map2.get("sign");
		
		if(!serverSign.equals(reqSign)){
			logger.info("平台计算签名：" + serverSign + ", 通知上传签名：" + reqSign);
			return RespCode.notifyfail;
		}

		/** 获取订单号  **/
		String orderNumber = map2.get("r2_orderNumber");

		/**  查询订单信息 **/
		PayOrder order = TradeDAO.getPayOrderInfo(orderNumber);
		
		if(order==null){
			logger.info("订单号：" + orderNumber + "未查到订单信息");
			return RespCode.notifyfail;
		}
		
		if(Constant.payRetCode.equals(order.getPayRetCode())){
			logger.info("订单号：" + orderNumber + "已经成功支付");
			return RespCode.notifySuccess;
		}
		
		String retCode = map2.get("retCode");
		String orderStatus = map2.get("r8_orderStatus");
		
		logger.info("订单：" + orderNumber + "状态为 : retCode= " + retCode + " , orderStatus=" + orderStatus);
		
		if((Constant.payRetCode.equals(retCode)&&Constant.orderStatus.equals(orderStatus))||
				(Constant.T0RetCode.equals(retCode)&&Constant.orderStatus.equals(orderStatus))){
			logger.info("订单 ：" + orderNumber  + "支付成功");
			String retMsg = "支付成功";
			
			retCode = Constant.payRetCode;
			
			if(map2.containsKey("retMsg")){
				retMsg = map2.get("retMsg");
			}
			
			LoginUser loginUser = null;
			try {
				loginUser = LoginUserDAO.getLoginuserInfo(order.getUserID());
			} catch (Exception e) {
				logger.info(e.getMessage()); 
				return RespCode.notifyfail;
			}
			
			Fee fee = notifyService.calProfit(order, loginUser);
			
			if(fee==null){
				logger.info("订单：" +  orderNumber + "计算手续费失败");
				return RespCode.notifyfail;
			}
			
			int updateRet = TradeDAO.updatePayOrderPayRetCode(new Object[]{retCode ,retMsg ,fee.getMerchantFee() , fee.getMerchantprofit() , order.getID()});
			if(updateRet < 1){
				logger.info("订单号：" + orderNumber + "更新数据库失败"); 
				return RespCode.notifyfail;
			}
			
			String tonken = DeviceTokenDAO.getDeviceToken(loginUser.getID());
			logger.info("============================订单编号:" + order.getOrderNumber() + "支付成功，用户tonken:" + tonken + "开始发送push");
			if(!UtilsConstant.strIsEmpty(tonken)){
				
				String content = "爱码付收款金额 " + AmountUtil.div(order.getAmount(), "100") + "元"; 
				
				PushUtil.iosSend("收款通知" , content, tonken , "1");
				PushUtil.androidSend("收款通知", content, tonken, "1");
			}
			
			
			// ID,UserID,TradeID,Fee,AgentID,AgentProfit,TwoAgentID,TwoAgentProfit,DistributeProfit,PlatformProfit
			int x = TradeDAO.saveProfit(new Object[]{
					UtilsConstant.getUUID(),loginUser.getID(), order.getID() ,fee.getMerchantFee(),fee.getAgentID(),fee.getAgentProfit(),
					fee.getTwoAgentID(),fee.getTwoAgentProfit(),
					fee.getDistributeProfit(),fee.getPlatformProfit(),fee.getPlatCostFee()
			});
			if(x < 1){
				logger.info("订单号:"  + orderNumber + "保存收益记录失败");
				return RespCode.notifyfail;
			}
			
			/**  计算三积分销各个商户的利润   **/
			List<Object[]> objs = notifyService.calDistributeProfit(fee, order, loginUser);
			
			logger.info("订单号：" + orderNumber + "三级分销的list长度:" + objs.size());
			
			if(objs.size()>0){
				/**  保存三级分销各个商户的利润  **/
				UserProfitDAO.saveDistributeProfit(objs);
				/** 更新用户信息表中的 分润总额 **/
				List<Object[]> profitlist = new ArrayList<Object[]>();
				for (Object[] objects : objs) {
					
					Integer profit = Integer.parseInt(objects[2].toString());
					
					// 如果三积分销中的用户包含业务员
					if(fee.getSalemsManID()!= null){
						if(objects[1].equals(fee.getSalemsManID())){
							logger.info("订单：" + orderNumber + "交易三级分销包含业务员 " + profit);
							fee.setSalemsDistrubuteProfit(profit); 
						}
					} else {
						logger.info("订单：" + orderNumber + " 交易三级分销没有业务员  " + profit);
					}
					logger.info("交易单号：" + order.getOrderNumber() + "  ====用户分润list中的值：" + Arrays.toString(objects)); 
					profitlist.add(new Object[]{ profit , profit , objects[1]});
				}
				
				// 如果业务从代理商哪里获取利润
				if(fee.getSalemsManID()!= null){
					profitlist.add(new Object[]{ fee.getSalemsGetAgentProfit() , fee.getSalemsGetAgentProfit() , fee.getSalemsManID()});
				} else {
					logger.info("订单：" + order.getOrderNumber() +  "  ，没有业务员");
				}
				
				LoginUserDAO.merchantProfit(profitlist);
			} else {
				logger.info("订单号：" + orderNumber + ",没有产生分润");
			}
			
			if(fee.getSalemsManID()!= null){
				logger.info("订单号：" + orderNumber + " 交易商户 涉及业务员分润"); 
				SalesManDAO.saveSalesManProfit(new Object[]{UtilsConstant.getUUID(),fee.getSalemsManID(),order.getUserID(),order.getID()
						,fee.getSalemsDistrubuteProfit(),fee.getSalemsGetAgentProfit()});
			}
			
			
			if("4".equals(paytype)){
				JSONObject mq = new JSONObject();
				mq.put("orderNumber", orderNumber);
				mq.put("dfType", "Trade");
				
				try {
					
					String delayTime = "30000";
					EhcacheUtil ehcache = EhcacheUtil.getInstance();

					//  查询交易配置信息
					Map<String,Object> tradeConfig = null;
					Object obj = ehcache.get(Constant.cacheName, "tradeConfig");
					if(obj == null){
						logger.info("缓存中获取交易配置信息失败,从数据库中查询");
						tradeConfig = AppConfigDAO.getTradeConfig();
						ehcache.put(Constant.cacheName, "tradeConfig", tradeConfig); 
					}else{
						logger.info("缓存查询交易配置信息");
						tradeConfig = (Map<String,Object>) obj;
					}
					
					if(tradeConfig.get("KuaiDelayTime")!=null){
						delayTime = tradeConfig.get("KuaiDelayTime").toString();
					}
					
					
					RabbitmqSend.sendMessage(mq.toString() , delayTime);
				
//					JSONObject mq = new JSONObject();
//					mq.put("orderNumber", orderNumber);
//					mq.put("dfType", "Trade");
//					Timer timer = new Timer();
//					timer.schedule(new TimerSendRabbitMQ(mq.toString()), 50 * 1000);
				} catch (Exception e) {
					logger.error("执行mq发送队列消息异常：" + e.getMessage() ,  e); 
				}
			}else{
				logger.info("交易订单号：" + orderNumber + "不是快捷交易");
				if(Constant.T0.equals(order.getTradeCode())){
					logger.info("不是快捷并且到账类型为T0调用查询到付状态脚本");
					try {
						Runtime.getRuntime().exec("/usr/local/middlepay/queryt0status.sh");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			EhcacheUtil ehcache = EhcacheUtil.getInstance();
			ehcache.clear(Constant.cacheName);
			 
		}
		return RespCode.notifySuccess;
	}
}


