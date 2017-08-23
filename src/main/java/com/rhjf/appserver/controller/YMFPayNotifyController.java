package com.rhjf.appserver.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.DevicetokenDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.SalesManDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.db.YMFTradeDB;
import com.rhjf.appserver.model.Fee;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.service.NotifyService;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.PushUtils;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *    固定码支付通知接口
 * @author a
 *
 */
@RestController
@RequestMapping("/YMFPayNotify")
public class YMFPayNotifyController {

	@Autowired
	private NotifyService notifyService;
	
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	@RequestMapping("")
	public Object YMFPayNotify(HttpServletRequest request , HttpServletResponse response) throws Exception{
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
		try {
			/** 查询商户信息  **/
			String merchantID = map2.get("r1_merchantNo");
			
			String bankid = map2.get("r5_business");
			String payType = "1";
			if("Alipay".equals(bankid)){
				payType = "2";
			}
			
			Map<String,Object> map =  TradeDB.getMerchantInfo(merchantID , payType);
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
			PayOrder order = TradeDB.getPayOrderInfo(orderNumber);
			
			if(order==null){
				logger.info("订单号：" + orderNumber + "未查到订单信息");
				return RespCode.notifyfail;
			}
			
			if(Constant.payRetCode.equals(order.getPayRetCode())){
				logger.info("订单号：" + orderNumber + "已经成功支付");
				return RespCode.notifySuccess;
			}
			
			/** 查询固定码信息  **/
			Map<String,Object> qrcode = YMFTradeDB.getYMFCode(new Object[]{order.getYMFCode()});
			if(qrcode==null||map.isEmpty()){
				logger.info("订单号：" + orderNumber + "固定码信息为空");
				return RespCode.notifyfail;
			}
			String retCode = map2.get("retCode");
			String orderStatus = map2.get("r8_orderStatus");
			
			logger.info("订单：" + orderNumber + "状态为 : retCode= " + retCode + " , orderStatus=" + orderStatus);
			
			if((Constant.payRetCode.equals(retCode)&&Constant.orderStatus.equals(orderStatus))||
					(Constant.T0RetCode.equals(retCode)&&Constant.orderStatus.equals(orderStatus))){
				
				String retMsg = "支付成功";
				
				if(map2.containsKey("retMsg")){
					retMsg = map2.get("retMsg");
				}
				TabLoginuser loginUser = LoginUserDB.getLoginuserInfo(order.getUserID());
				
				Fee fee = notifyService.YMFcalProfit(order, loginUser, qrcode);
				
				int updateRet = TradeDB.updatePayOrderPayRetCode(new Object[]{retCode ,retMsg ,fee.getMerchantFee() , fee.getMerchantprofit() , order.getID()});
				
				if(updateRet < 1){
					logger.info("订单号：" + orderNumber + "更新数据库失败"); 
					return RespCode.notifyfail;
				}
				
				String tonken = DevicetokenDB.getDeviceToken(loginUser.getID());
				logger.info("============================订单编号:" + order.getOrderNumber() + "支付成功，用户tonken:" + tonken + "开始发送push");
				if(!UtilsConstant.strIsEmpty(tonken)){
					String content = "您的有一笔固定码交易支付成功，请查看";
					PushUtils.IOSPush(content, tonken);
					PushUtils.AndroidPush("支付通知", content, tonken); 
				}
				
				TradeDB.saveProfit(new Object[]{
						UtilsConstant.getUUID(),loginUser.getID(), order.getID() ,fee.getMerchantFee(),fee.getAgentID(),fee.getAgentProfit(),
						fee.getTwoAgentID(),fee.getTwoAgentProfit(),
						 fee.getDistributeProfit() , fee.getPlatformProfit(),fee.getPlatCostFee()
				});
				
				
				/**  计算三积分销各个商户的利润   **/
				List<Object[]> objs = notifyService.calDistributeProfit(fee, order, loginUser);
				
				logger.info("订单号：" + orderNumber + "三级分销的list长度:" + objs.size());
				
				if(objs.size()>0){
					/**  保存三级分销各个商户的利润  **/
					TradeDB.saveDistributeProfit(objs);
					/** 更新用户信息表中的 分润总额 **/
					List<Object[]> profitlist = new ArrayList<Object[]>();
					for (Object[] objects : objs) {
						
						Integer profit = Integer.parseInt(objects[2].toString());
						
						// 如果三积分销中的用户包含业务员
						if(fee.getSalemsManID()!= null){
							if(objects[1].equals(fee.getSalemsManID())){
								
								logger.info("订单：" + orderNumber + "交易三级分销包含业务员 " + profit);
								
								fee.setSalemsDistrubuteProfit(profit); 
								profit += fee.getSalemsGetAgentProfit();
							}
						} else {
							logger.info("订单：" + orderNumber + " 交易三级分销没有业务员  " + profit);
						}
						
						logger.info("交易单号：" + order.getOrderNumber() + "  ====用户分润list中的值：" + Arrays.toString(objects)); 
						profitlist.add(new Object[]{ profit , profit , objects[1]});
					}
					LoginUserDB.merchantProfit(profitlist);
				} else {
					logger.info("订单号：" + orderNumber + ",没有产生分润");
				}
				
				if(fee.getSalemsManID()!= null){
					logger.info("订单号：" + orderNumber + " 交易商户 涉及业务员分润"); 
					SalesManDB.saveSalesManProfit(new Object[]{UtilsConstant.getUUID(),fee.getSalemsManID(),order.getUserID(),order.getID()
							,fee.getSalemsDistrubuteProfit(),fee.getSalemsGetAgentProfit()});
				}
				
				EhcacheUtil ehcache = EhcacheUtil.getInstance();
				ehcache.clear(Constant.cacheName);
				
			}
			return RespCode.notifySuccess;
		} catch (Exception e) {
			e.printStackTrace();
			return RespCode.notifyfail;
		}
	}
}
