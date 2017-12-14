package com.rhjf.appserver.service;

import java.math.BigDecimal; 
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.AppconfigDB;
import com.rhjf.appserver.db.ChannelConfigDB;
import com.rhjf.appserver.db.DevicetokenDB;
import com.rhjf.appserver.db.DistributionRatioDB;
import com.rhjf.appserver.db.SalesManDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.model.Fee;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.PushUtil;
import com.rhjf.appserver.util.UtilsConstant;



@Service
public class NotifyService {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	/**
	 *   计算手续费
	 * @param order
	 * @param loginUser
	 * @return
	 */
	public Fee calProfit(PayOrder order , TabLoginuser loginUser ){
		
		logger.info("计算订单：" + order.getOrderNumber() + "手续费");
		
		Fee fee = new Fee();
		String tradeCode = order.getTradeCode();
		// 交易费率
		String feeRate = "0";
		// 结算费率
		String SettlementRate = "0";
		// t0 附加费用
		int T0additional = 0;
		
		/** 用户费率配置信息 **/
		Map<String,Object> userConfig = TradeDB.getUserConfig(new Object[]{order.getUserID() ,  order.getPayChannel()}); 
		
		/** 代理商信息  **/
		Map<String,Object> agentInfo = AgentDB.agentInfo(new Object[]{loginUser.getAgentID()});
		/**  代理商配置信息  **/
		Map<String,Object> agentConfig = AgentDB.agentConfig(new Object[]{agentInfo.get("ID") , order.getPayChannel()}); 
		
		if(agentConfig==null||agentConfig.isEmpty()){
			logger.info("代理商：" + agentInfo.get("ID") + "没有配置支付类型：" +  order.getPayChannel()); 
			return null;
		}
		
		/**  查询通道成本费率 **/
		Map<String,Object> channelconfigMap = ChannelConfigDB.getChannelConfig(order.getPayChannel());
		if(channelconfigMap==null||channelconfigMap.isEmpty()){
			logger.info("通道成本费率查询异常：支付类型：" + order.getPayChannel()); 
			return null;
		}
		
		boolean flag = false;
		
		
		Map<String,Object> salemsMan  = null;
		/**  交易商户 的业务员ID **/
		String salemsManID = loginUser.getSalesManID();
		if(salemsManID != null && !salemsManID.trim().isEmpty()){
			flag = true;
			salemsMan = SalesManDB.salesManInfo(salemsManID);
			if(salemsMan == null || salemsMan.isEmpty()){
				flag = false;
			}else{
				fee.setSalemsManID(UtilsConstant.ObjToStr(salemsMan.get("ID"))); 
				logger.info("计算订单：" + order.getOrderNumber() + " 包含业务员  ， 业务员ID：" + UtilsConstant.ObjToStr(salemsMan.get("ID")));
			}
 		}
		
		
		/**  商户下放费率   ,   代理商签约成本 ,  渠道成本  **/
		String merchantRate = "0", agentRate = "0" ,  channelRate ="0";
		
		/**  业务员分润费率 **/
		String salesManRate = "0";
		
		/** 交易类型为T0 **/
		if(tradeCode.equals(Constant.T0)){
			
			logger.info("订单:" + order.getOrderNumber() + "为T0交易"); 
			
			//  交易费率  T0SaleRate,T0SettlementRate 
			feeRate =  UtilsConstant.ObjToStr(userConfig.get("T0SaleRate"));
			//  结算费率                                                                                                    
			SettlementRate = UtilsConstant.ObjToStr(userConfig.get("T0SettlementRate"));
			//  T0商户下放
			merchantRate = UtilsConstant.ObjToStr(agentConfig.get("T0MerchantRate"));
			//  T0代理商成本
			agentRate = UtilsConstant.ObjToStr(agentConfig.get("T0AgentRate"));
			//  T0渠道成本
//			channelRate = UtilsConstant.ObjToStr(agentConfig.get("T0ChannelRate"));
			channelRate = UtilsConstant.ObjToStr(channelconfigMap.get("T0ChannelRate"));
			
			//  T0附加手续费
			T0additional = AppconfigDB.T0additional();
			
			if(flag){
				
				//  存在业务员信息
				salesManRate = UtilsConstant.ObjToStr(salemsMan.get("T0FeeRate"));
				
				logger.info("业务员费率值：" + salesManRate );
			}
			
			logger.info(order.getOrderNumber() + "交易费率为：" + feeRate + ",结算费率为：" + SettlementRate + ",T0商户下放：" + merchantRate  + ",代理商成本：" + agentRate + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
			
		}else{
			
			logger.info("订单:" + order.getOrderNumber() + "为T1交易"); 
			
			/** 交易类型为T1 **/
			// 交易费率
			feeRate = UtilsConstant.ObjToStr(userConfig.get("T1SaleRate"));
			// 结算费率
			SettlementRate = UtilsConstant.ObjToStr(userConfig.get("T1SettlementRate"));
			//  商户下放费率
			merchantRate = UtilsConstant.ObjToStr(agentConfig.get("MerchantRate"));
			// 代理商成本费率
			agentRate = UtilsConstant.ObjToStr(agentConfig.get("AgentRate"));
			//  渠道成本
//			channelRate = UtilsConstant.ObjToStr(agentConfig.get("ChannelRate"));
			channelRate = UtilsConstant.ObjToStr(channelconfigMap.get("ChannelRate"));
			
			if(flag){
				//  存在业务员信息
				salesManRate = UtilsConstant.ObjToStr(salemsMan.get("FeeRate"));
				logger.info("业务员费率值：" + salesManRate );
			}
			
			logger.info(order.getOrderNumber()  + "交易费率为：" + feeRate + ",结算费率为：" + SettlementRate + ",商户下放：" + merchantRate  + ",代理商成本：" + agentRate + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
			
		}
		
		/** 商户手续费 **/
		fee.setMerchantFee(AgentDB.makeFeeRounding(order.getAmount(), Double.valueOf(feeRate) , 0) + T0additional);
		/** 商户自己的分润  **/
		fee.setMerchantprofit(AgentDB.makeFeeAbandon(order.getAmount(),AmountUtil.sub(feeRate, SettlementRate), 0));
		
		/** 三级分销总金额  **/
		int distributeProfit = AgentDB.makeFeeAbandon(order.getAmount(),AmountUtil.sub(SettlementRate, merchantRate), 0);
		fee.setDistributeProfit(distributeProfit);
		
		/** 二级代理商商户交易 **/
		if(agentInfo.get("ParentAgentID")!=null&&!agentInfo.get("ParentAgentID").equals(agentInfo.get("ID"))){
			
			logger.info(order.getOrderNumber() + "订单的商户的代理商为二级代理商");
			
			//  获得父级代理商ID
			Map<String,Object> agentParent = AgentDB.agentInfo(new Object[]{agentInfo.get("ParentAgentID")});
			//  获取父类代理商配置信息
			Map<String,Object> agentParentConfig = AgentDB.agentConfig(new Object[]{agentParent.get("ID") , order.getPayChannel()});
			//  代理商ID
			fee.setAgentID(agentParent.get("ID").toString());
			//  二级代理商ID
			fee.setTwoAgentID(agentInfo.get("ID").toString());
			
			String parentAgentRate = "0";
			
			if(tradeCode.equals(Constant.T0)){
				parentAgentRate = agentParentConfig.get("T0AgentRate").toString();
			}else{
				parentAgentRate = agentParentConfig.get("AgentRate").toString();
			}
			
			if(flag){
				//  如果存在业务员
				int salemsGetAgentProfit = AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,salesManRate) ,0);
				
				logger.info("订单："+order.getOrderNumber() + "从代理商获取的收益为： " + salemsGetAgentProfit);
				fee.setSalemsGetAgentProfit(salemsGetAgentProfit);
				merchantRate = salesManRate;
			}
			
			//  代理商分润
			fee.setAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(agentRate ,parentAgentRate ) ,0));
			//  设置二级代理商分润
			fee.setTwoAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,agentRate), 0));
			agentRate = parentAgentRate;
			
		}else{
			logger.info(order.getOrderNumber() + "订单商户的代理商为一级代理商");
			
			if(flag){
				//  如果存在业务员
				int salemsGetAgentProfit = AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,salesManRate) ,0);
				fee.setSalemsGetAgentProfit(salemsGetAgentProfit);
				
				logger.info("订单："+order.getOrderNumber() + "从代理商获取的收益为： " + salemsGetAgentProfit);
				
				merchantRate = salesManRate;
			}
			
			// 代理商ID
			fee.setAgentID(agentInfo.get("ID").toString());
			//  代理商分润
			fee.setAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,agentRate) ,0));
		}
		//计算平台手续费
		fee.setPlatCostFee(AgentDB.makeFeeFurther(order.getAmount(), Double.valueOf(channelRate),0));
		// 平台收益
		fee.setPlatformProfit(AgentDB.makeFeeFurther(order.getAmount(),  AmountUtil.sub(agentRate, channelRate),0));
		
		logger.info("订单：" + order.getOrderNumber() + "，商户手续费：" + fee.getMerchantFee() + " , 商户分润:"
		+ fee.getMerchantprofit() + ", 三级分销总金额:" + distributeProfit + "，平台收益:" + fee.getPlatformProfit() 
		+ "，平台手续费:" + fee.getPlatCostFee() + " ， 业务员从代理商获取的收益：" + fee.getSalemsGetAgentProfit());
		
		return fee;
	}
	
	
	
	/**
	 *     计算三级分销
	 * @param fee
	 * @param loginUser
	 * @return
	 */
	public List<Object[]> calDistributeProfit( Fee fee , PayOrder order ,TabLoginuser loginUser){
		logger.info("开始计算订单编号为：" + order.getOrderNumber() + "的三级分销分润");
		
		List<Object[]> list = new ArrayList<Object[]>();
		
		String one = loginUser.getOneLevel();
		
		String two = loginUser.getTwoLevel();
		
		String three = loginUser.getThreeLevel();
		
		Map<String,Object> profitMap = DistributionRatioDB.profitMap();
		
		int total = fee.getDistributeProfit();

		logger.info("计算订单编号为：" + order.getOrderNumber() + "的三级分销分润总金额：" + total);
		Object[] obj = null;
		if(total > 0){
			/** 上级用户 **/
			if(!UtilsConstant.strIsEmpty(three)){
				int threeProfit = total*Integer.parseInt(UtilsConstant.ObjToStr(profitMap.get("ThreeLeveFee")))/10;
				if(threeProfit > 0){
					String tonken = DevicetokenDB.getDeviceToken(three);
					logger.info("============================订单编号:" + order.getOrderNumber() + "上级用户Token:" + tonken + "开始发送push");
					if(!UtilsConstant.strIsEmpty(tonken)){
						String content = "爱码付为你赚了" + new BigDecimal(threeProfit).divide(new BigDecimal(100),2,RoundingMode.DOWN) + "元分润";
						
//						PushUtils.IOSPush(content, tonken);
//						PushUtils.AndroidPush("分润通知", content, tonken);
//						
						PushUtil.iosSend("收益通知" , content, tonken , "1");
						PushUtil.androidSend("收益通知", content, tonken, "1");
					}
					
					obj = new Object[]{UtilsConstant.getUUID(),three,threeProfit ,order.getTradeDate() + order.getTradeTime(),order.getID() , "上级用户" , 3};
					list.add(obj);
				}
				logger.info("订单编号:" + order.getOrderNumber() + " , 上级用户" + three + "获得分润:" + threeProfit);
			}
			/** 第二级用户  **/
			if(!UtilsConstant.strIsEmpty(two)){
				int twoProfit = total*Integer.parseInt(UtilsConstant.ObjToStr(profitMap.get("TwoLeveFee")))/10;
				if(twoProfit > 0){
					String tonken = DevicetokenDB.getDeviceToken(two);
					logger.info("============================订单编号:" + order.getOrderNumber() + "第二级用户Token:" + tonken + "开始发送push");
					if(!UtilsConstant.strIsEmpty(tonken)){
						String content = "爱码付为你赚了" + new BigDecimal(twoProfit).divide(new BigDecimal(100),2,RoundingMode.DOWN) + "元分润";
						PushUtil.iosSend("收益通知" , content, tonken , "1");
						PushUtil.androidSend("收益通知", content, tonken, "1");
					}
					
					obj = new Object[]{UtilsConstant.getUUID(),two ,twoProfit ,order.getTradeDate() + order.getTradeTime(),order.getID() ,  "二级用户" , 2};
					list.add(obj);
				}
				logger.info("订单编号:" + order.getOrderNumber() + " , 第二级用户" + two + "获得分润:" + twoProfit);
			}
			
			/** 第三级用户  **/
			if(!UtilsConstant.strIsEmpty(one)){
				int oneProfit = total*Integer.parseInt(UtilsConstant.ObjToStr(profitMap.get("OneLeveFee")))/10;
				if(oneProfit > 0){
					String tonken = DevicetokenDB.getDeviceToken(one);
					logger.info("============================订单编号:" + order.getOrderNumber() + "第三级用户Token:" + tonken + "开始发送push");
					if(!UtilsConstant.strIsEmpty(tonken)){
						String content = "爱码付为你赚了" + new BigDecimal(oneProfit).divide(new BigDecimal(100),2,RoundingMode.DOWN) + "元分润";
						
//						PushUtils.IOSPush(content, tonken);
//						PushUtils.AndroidPush("分润通知", content, tonken);
//						
						PushUtil.iosSend("收益通知" , content, tonken , "1");
						PushUtil.androidSend("收益通知", content, tonken, "1");
					}
					
					obj = new Object[]{UtilsConstant.getUUID(),one ,oneProfit , order.getTradeDate() + order.getTradeTime(),order.getID() , "一级用户" , 1};
					list.add(obj);
				}
				logger.info("订单编号:" + order.getOrderNumber() + " , 第三级用户" + one + "获得分润:" + oneProfit);
			}
		}
		
		if(fee.getMerchantprofit() > 0){
			/** 保存商户自己的分润 **/
			String tonken = DevicetokenDB.getDeviceToken(loginUser.getID());
			logger.info("============================订单编号:" + order.getOrderNumber() + "商户自己的token" + tonken + "开始发送push"); 
			if(!UtilsConstant.strIsEmpty(tonken)){
				String content = "爱码付为你赚了" + new BigDecimal(fee.getMerchantprofit()).divide(new BigDecimal(100),2,RoundingMode.DOWN)   + "元";

//				PushUtils.IOSPush(content, tonken);
//				PushUtils.AndroidPush("分润通知", content, tonken);
//				
				PushUtil.iosSend("收益通知" , content, tonken , "1");
				PushUtil.androidSend("收益通知", content, tonken, "1");
			}
			logger.info("订单编号:" + order.getOrderNumber() + " , 商户自己获得分润:" + fee.getMerchantprofit());
			obj = new Object[]{UtilsConstant.getUUID(),loginUser.getID(),fee.getMerchantprofit(), order.getTradeDate() + order.getTradeTime(),order.getID() , "商户反润" , 0};
			list.add(obj);
		}
		return list;
	}
	
	
	
	/**
	 *   计算固定码手续费
	 * @param order
	 * @param loginUser
	 * @param map
	 * @return
	 */
	public Fee YMFcalProfit(PayOrder order , TabLoginuser user , Map<String,Object> qrcode){
		
		logger.info("计算订单" + order.getOrderNumber() + "固定码手续费");
		
		Fee fee = new Fee();
		
		/** 到账类型 **/
		String tradeCode = order.getTradeCode();
		
		/** 固定码的手续费 **/
		String rate = UtilsConstant.ObjToStr(qrcode.get("Rate"));
		
		/** 固定码计算费率 ***/
		String SettlementRate = UtilsConstant.ObjToStr(qrcode.get("SettlementRate"));
		
		/** t0 附加费用 **/
		int T0additional = 0;
		
		/** 代理商信息 **/
		Map<String,Object> agentInfo = AgentDB.agentInfo(new Object[]{user.getAgentID()});
		/** 代理商费率配置信息 **/
		Map<String,Object> agentConfig = AgentDB.agentConfig(new Object[]{agentInfo.get("ID") , order.getPayChannel()});
		
		
		
		/**  查询通道成本费率 **/
		Map<String,Object> channelconfigMap = ChannelConfigDB.getChannelConfig(order.getPayChannel());
		if(channelconfigMap==null||channelconfigMap.isEmpty()){
			logger.info("通道成本费率查询异常：支付类型：" + order.getPayChannel()); 
			return null;
		}
		
		
		
		boolean flag = false;
		
		Map<String,Object> salemsMan  = null;
		/**  交易商户 的业务员ID **/
		String salemsManID = user.getSalesManID();
		if(salemsManID != null && !salemsManID.trim().isEmpty()){
			flag = true;
			salemsMan = SalesManDB.salesManInfo(salemsManID);
			if(salemsMan == null || salemsMan.isEmpty()){
				flag = false;
			}else{
				fee.setSalemsManID(UtilsConstant.ObjToStr(salemsMan.get("ID"))); 
				logger.info("计算订单：" + order.getOrderNumber() + " 包含业务员  ， 业务员ID：" + UtilsConstant.ObjToStr(salemsMan.get("ID")));
			}
 		}
		
		
		/**   代理商签约成本 ,  渠道成本  **/
		String agentRate = "0" , channelRate ="0" , merchantRate = "0";
		
		/**  业务员分润费率 **/
		String salesManRate = "0";
		
		/** 交易类型为T0 **/
		if(tradeCode.equals(Constant.T0)){
			
			logger.info("订单:" + order.getOrderNumber() + "为T0交易"); 
			
			//  T0代理商成本
			agentRate = UtilsConstant.ObjToStr(agentConfig.get("T0AgentRate"));
			//  T0渠道成本
//			channelRate = UtilsConstant.ObjToStr(agentConfig.get("T0ChannelRate"));
			channelRate = UtilsConstant.ObjToStr(channelconfigMap.get("ChannelRate"));
			
			merchantRate = UtilsConstant.ObjToStr(agentConfig.get("T0MerchantRate"));
			
			//  T0附加手续费
			T0additional = AppconfigDB.T0additional();
			
			if(flag){
				
				//  存在业务员信息
				salesManRate = UtilsConstant.ObjToStr(salemsMan.get("T0FeeRate"));
				
				logger.info("业务员费率值：" + salesManRate );
			}
			
			logger.info(order.getOrderNumber() + "交易费率为：" + rate + ", 优惠费率 ：" + SettlementRate + ",代理商成本：" + agentRate + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
			
		}else{
			
			logger.info("订单:" + order.getOrderNumber() + "为T1交易"); 
			
			/** 交易类型为T1 **/
			// 代理商成本费率
			agentRate = UtilsConstant.ObjToStr(agentConfig.get("AgentRate"));
			//  渠道成本
//			channelRate = UtilsConstant.ObjToStr(agentConfig.get("ChannelRate"));
			channelRate = UtilsConstant.ObjToStr(channelconfigMap.get("ChannelRate"));
			//  商户下放费率
			merchantRate = UtilsConstant.ObjToStr(agentConfig.get("MerchantRate"));
			
			
			if(flag){
				//  存在业务员信息
				salesManRate = UtilsConstant.ObjToStr(salemsMan.get("FeeRate"));
				logger.info("业务员费率值：" + salesManRate );
			}
			
			logger.info(order.getOrderNumber()  + "交易费率为：" + rate + ", 优惠费率 ：" + SettlementRate + ",代理商成本：" + agentRate + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
		}
		
		/** 商户手续费 **/
		fee.setMerchantFee(AgentDB.makeFeeRounding(order.getAmount(), Double.valueOf(rate) , 0) + T0additional);
		
		/** 商户自己的分润  **/
		fee.setMerchantprofit(AgentDB.makeFeeAbandon(order.getAmount(),AmountUtil.sub(rate, SettlementRate), 0));
		/** 三级分销总金额  **/
		int distributeProfit = AgentDB.makeFeeAbandon(order.getAmount(),AmountUtil.sub(SettlementRate, merchantRate), 0);
		fee.setDistributeProfit(distributeProfit);
		
		if("1".equals(UtilsConstant.ObjToStr(qrcode.get("AgentProfit")))){
			// 计算代理商分润
			/** 二级代理商商户交易 **/
			if(agentInfo.get("ParentAgentID")!=null&&!agentInfo.get("ParentAgentID").equals(agentInfo.get("ID"))){
				
				logger.info(order.getOrderNumber() + "订单的商户的代理商为二级代理商");
				
				//  获得父级代理商ID
				Map<String,Object> agentParent = AgentDB.agentInfo(new Object[]{agentInfo.get("ParentAgentID")});
				//  获取父类代理商配置信息
				Map<String,Object> agentParentConfig = AgentDB.agentConfig(new Object[]{agentParent.get("ID") , order.getPayChannel()});
				//  代理商ID
				fee.setAgentID(agentParent.get("ID").toString());
				//  二级代理商ID
				fee.setTwoAgentID(agentInfo.get("ID").toString());
				
				String parentAgentRate = "0";
				
				if(tradeCode.equals(Constant.T0)){
					parentAgentRate = agentParentConfig.get("T0AgentRate").toString();
				}else{
					parentAgentRate = agentParentConfig.get("AgentRate").toString();
				}
				
				if(flag){
					//  如果存在业务员
					int salemsGetAgentProfit = AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,salesManRate) ,0);
					
					logger.info("订单："+order.getOrderNumber() + "从代理商获取的收益为： " + salemsGetAgentProfit);
					fee.setSalemsGetAgentProfit(salemsGetAgentProfit);
					merchantRate = salesManRate;
				}
				
				//  代理商分润
				fee.setAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(agentRate ,parentAgentRate ) ,0));
				//  设置二级代理商分润
				fee.setTwoAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,agentRate), 0));
				
				agentRate = parentAgentRate;
			}else{
				logger.info(order.getOrderNumber() + "订单商户的代理商为一级代理商");
				
				if(flag){
					//  如果存在业务员
					int salemsGetAgentProfit = AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,salesManRate) ,0);
					fee.setSalemsGetAgentProfit(salemsGetAgentProfit);
					
					logger.info("订单："+order.getOrderNumber() + "从代理商获取的收益为： " + salemsGetAgentProfit);
					
					merchantRate = salesManRate;
				}
				
				// 代理商ID
				fee.setAgentID(agentInfo.get("ID").toString());
				//  代理商分润
				fee.setAgentProfit(AgentDB.makeFeeAbandon(order.getAmount(), AmountUtil.sub(merchantRate ,agentRate) ,0));
			} 
		} else {
			logger.info(order.getOrderNumber() + "不计算代理商分润, 使用的固定码为：" + UtilsConstant.ObjToStr(qrcode.get("code"))); 
		}
		
		//计算平台手续费
		fee.setPlatCostFee(AgentDB.makeFeeFurther(order.getAmount(), Double.valueOf(channelRate),0));
		// 平台收益
		fee.setPlatformProfit(AgentDB.makeFeeFurther(order.getAmount(),  AmountUtil.sub(agentRate, channelRate),0));
		
		
		logger.info("订单：" + order.getOrderNumber() + "，商户手续费：" + fee.getMerchantFee() + " , 商户分润:"
				+ fee.getMerchantprofit() + ", 三级分销总金额:" + distributeProfit + "，平台收益:" + fee.getPlatformProfit() 
				+ "，平台手续费:" + fee.getPlatCostFee()) ;
		
		return fee;
	} 
	
	
	
	/**
	 *   计算手续费 (信用卡还款)
	 *      不参与三级分销 ， 不参与代理商分润  只计算平台成本和平台收益
	 * @param order
	 * @param loginUser
	 * @return
	 */
	public Fee calProfit(String orderNumber ,PayOrder  order , TabLoginuser loginUser ){
		
		logger.info("计算订单：" + orderNumber + "手续费");
		
		Fee fee = new Fee();
		// 交易费率
		String feeRate = "0";
		
		// t0 附加费用
		int T0additional = 0;
		
		/** 用户费率配置信息 **/
		Map<String,Object> userConfig = TradeDB.getUserConfig(new Object[]{loginUser.getID() ,  order.getPayChannel() }); 
		
		/**    渠道成本   **/
		String channelRate ="0";
		
		/**  查询通道成本费率 **/
		Map<String,Object> channelconfigMap = ChannelConfigDB.getChannelConfig(order.getPayChannel());
		if(channelconfigMap==null||channelconfigMap.isEmpty()){
			logger.info("通道成本费率查询异常：支付类型：" + order.getPayChannel()); 
			return null;
		}
		
		channelRate = UtilsConstant.ObjToStr(channelconfigMap.get("T0ChannelRate"));

		// 交易费率
		feeRate = UtilsConstant.ObjToStr(userConfig.get("T0SaleRate"));
		
		logger.info(orderNumber  + "交易费率为：" + feeRate  + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
			
		/** 商户手续费 **/
		fee.setMerchantFee(AgentDB.makeFeeRounding(order.getAmount(), Double.valueOf(feeRate)  , 0) + T0additional);
		
		//计算平台手续费
		fee.setPlatCostFee(AgentDB.makeFeeFurther(order.getAmount(), Double.valueOf(channelRate),0));
		
		// 平台收益
		fee.setPlatformProfit(AgentDB.makeFeeFurther(order.getAmount() ,  AmountUtil.sub(feeRate, channelRate),0));
		
		logger.info("订单：" + orderNumber + "，商户手续费：" + fee.getMerchantFee()  + "，平台收益:" + fee.getPlatformProfit()  + "，平台手续费:" + fee.getPlatCostFee()) ;
		
		return fee;
	}
}
