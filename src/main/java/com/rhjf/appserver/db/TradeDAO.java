package com.rhjf.appserver.db;


import java.util.List;
import java.util.Map;

import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

public class TradeDAO extends DBBase{

	
	/**
	 *     查看用户配置信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> getUserConfig(Object[] obj){
		String sql = "select * from tab_user_config where UserID=? and PayChannel=?";
		System.out.println(sql);
		return  queryForMap(sql, obj);
	}
	
	
	/**
	 *    用户查询各种交易类型的费率
	 * @param obj
	 * @return
	 */
	public static List<Map<String,Object>> getUserFeeRate(Object[] obj){
//		String sql = "select tpc.ID,PayChannelName , T1SaleRate , T1SaleRate-T1SettlementRate as T1SettlementRate ,"
//				+ " T0SaleRate , T0SaleRate-T0SettlementRate as T0SettlementRate , SaleAmountMax , SaleAmountMaxDay "
//				+ " from tab_user_config as tuc , tab_pay_channel as tpc"
//				+ " where tuc.PayChannel=tpc.ID and tuc.UserID=?";
		
		String sql = "select tpc.ID,PayChannelName , T1SaleRate ,  T1SettlementRate , T1SaleRate-T1SettlementRate as T1rebate  , "
				+ " T0SaleRate , T0SettlementRate , T0SaleRate-T0SettlementRate as T0rebate , SaleAmountMax , SaleAmountMaxDay "
				+ " from tab_user_config as tuc , tab_pay_channel as tpc"
				+ " where tuc.PayChannel=tpc.ID and tuc.UserID=? and tpc.Active=1";
		return queryForList(sql, obj);
	}
	
	
	/**
	 *   查询交易类型
	 * @return
	 */
	public static List<Map<String,Object>> getPayChannel(){
		String sql = "select * from  tab_pay_channel";
		return queryForList(sql, null);
	}
	
	
	
	public static int[] saveUserConfig(List<Object[]> list){
		String sql = "insert ignore into tab_user_config (ID,UserID,PayChannel,SaleAmountMax,SaleAmountMaxDay,T1SaleRate,T0SaleRate,T1SettlementRate,T0SettlementRate)"
				+ " values (?,?,?,?,?,?,?,?,?)";
		return executeBatchSql(sql, list);
	}
	
	
	/**
	 *   查询商户信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> getMerchantInfo(Object[] obj){
		
		String sql = "select * from tab_pay_merchant where UserID=? and PayType=?";
		Map<String,Object> map = queryForMap(sql, obj);
		if(map==null||map.isEmpty()){
			return null;
		}
		return map;
	}
	
	
	/**
	 *   保存商户信息
	 * @param obj
	 * @return
	 */
	public static int saveMerchant(Object[] obj){
		String sql = "insert into tab_pay_merchant (MerchantID,MerchantName,UserTime,SignKey,DESKey,QueryKey,UserID,PayType) values "
				+ " (?,?,now(),?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	
	/**
	 *    根据商户号查询商户信息
	 * @param merchantID
	 * @return
	 */
	public static Map<String,Object> getMerchantInfo(String merchantID , String payType){
		String sql = "select * from tab_pay_merchant where MerchantID=? and PayType=?";
		return queryForMap(sql, new Object[]{merchantID,payType});
	}
	
	
	/**
	 *    记录交易请求
	 * @param obj
	 * @return
	 */
	public static int tradeInit(Object[] obj){
		String sql = "insert into tab_pay_order (ID,Amount,LocalDate,LocalTimes,TradeDate,TradeTime,TermSerno,TradeType,"
				+ "TradeCode,UserID,PayChannel,FeeRate,MerchantID,OrderNumber, DFBankCardNo ,TradeBankNo , AgentID , ChannelID) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	/**
	 *   初始化固定码交易数据
	 * @param obj
	 * @return
	 */
	public static int YMFTradeInit(Object[] obj){
		String sql = "insert into tab_pay_order (ID,Amount,LocalDate,LocalTimes,TradeDate,TradeTime,TermSerno,"
				+ "TradeType,TradeCode,UserID,PayChannel,MerchantID,OrderNumber,YMFCode , AgentID,ChannelID) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	

	/**
	 *   根据平台订单号查询交易信息
	 * @param orderNumber
	 * @return
	 */
	public static PayOrder  getPayOrderInfo(String orderNumber){
		String sql = "select * from tab_pay_order where orderNumber=?";
		
		Map<String,Object> map = queryForMap(sql, new Object[]{orderNumber});
		
		if(map == null || map.isEmpty()){
			return null;
		}
		PayOrder order = null;
		try {
			order = UtilsConstant.mapToBean(map, PayOrder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}
	
	
	/**
	 * 更新订单支付结果
	 * @param obj
	 * @return
	 */
	public static int updatePayOrderPayRetCode(Object[] obj){
		String sql = "update tab_pay_order set PayRetCode=? , PayRetMsg=? , Fee=? , MerchantProfit=?  where ID=?";
		return executeSql(sql, obj);
	}


	/**
	 *   更新订单支付状态， 默认出款成功
	 * @param obj
	 * @return
	 */
	public static int updatePayOrder(Object[] obj){
		String sql = "update tab_pay_order set PayRetCode=? , PayRetMsg=? , Fee=? , MerchantProfit=?  , T0PayRetCode='0000'  where ID=?";
		return executeSql(sql, obj);
	}


	/**
	 *    更新订单代付结果
	 * @param obj
	 * @return
	 */
	public static int updateWithdrawStatus(Object[] obj){
		String sql = "update tab_pay_order set T0PayRetCode=? , T0PayRetMsg=? where ID=? ";
		return executeSql(sql, obj);
	}
	
	
	/**
	 *   保存收益信息
	 * @param obj
	 * @return
	 */
	public static int saveProfit(Object[] obj){
		String sql = "insert  into tab_platform_profit (ID,UserID,TradeID,Fee,AgentID,AgentProfit,TwoAgentID,TwoAgentProfit,DistributeProfit,PlatformProfit,ChannelProfit)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	
	/***
	 *    终端用户查询自己的交易记录
	 * @param obj
	 * @return
	 */
	public static List<Map<String,Object>> userQueryTradeList(Object[] obj){
		String sql = "select OrderNumber , CONCAT(TradeDate,TradeTime) as TradeTime, Amount, PayRetCode , PayRetMsg , TermSerno, PayChannelName "
				+ " from tab_pay_order as tpo , tab_pay_channel as tpc where tpo.PayChannel=tpc.ID and tpo.UserID=? and tpc.ID=? and  PayRetCode='0000'"
				+ " order by  LocalDate desc ,  LocalTimes  desc limit ? , ?  ";
		return queryForList(sql, obj);
	}
	
	public static Integer userQueryTradeCount(Object[] obj){
		String sql = "select count(1) as count from tab_pay_order as tpo , tab_pay_channel as tpc"
				+ " where tpo.PayChannel=tpc.ID and tpo.UserID=? and tpc.ID=? and  PayRetCode='0000'";
		Map<String,Object> map = queryForMap(sql, obj);
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
	}
	
	
	public static JSONObject userQueryTradeTotal(Object[] obj){
		
		JSONObject json = new JSONObject();
		
		String today = "select IFNULL(SUM(Amount) ,0) as amount from tab_pay_order "
				+ "where PayRetCode='0000' and LocalDate=DATE_FORMAT(now(),'%Y%m%d') and UserID=? and PayChannel=?";
		String todayAmount = UtilsConstant.ObjToStr(queryForMap(today, obj).get("amount"));
		
		String total = "select SUM(Amount) as amount from tab_pay_order "
				+ "where PayRetCode='0000' and  UserID=? and PayChannel =?";
		
		String totalAmount =  UtilsConstant.ObjToStr(queryForMap(total, obj).get("amount"));
		
		json.put("today" , todayAmount);
		json.put("total", totalAmount);
		
		return json;
		
	}



	public static int updateOrderTransactionId(String orderNumber , String transactionID){
		String sql = "update tab_pay_order set TransactionId = ? where OrderNumber=?";
		return executeSql(sql , new Object[]{transactionID , orderNumber });
	}

}
