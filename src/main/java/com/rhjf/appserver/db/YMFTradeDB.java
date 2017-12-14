package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

/**
 *    固定码交易
 * @author a
 *
 */
public class YMFTradeDB extends DBBase{

	
	/**
	 *     查询码数据
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> getYMFCode(Object[] obj){
		String sql = "select * from tab_ymf_qrcode where Code=?";
		return queryForMap(sql, obj);
	}
	
	
	/**
	 *   查询固定码列表
	 * @param obj
	 * @return
	 */
	public static List<Map<String,String>> getUserYMFlist(Object[] obj){
		String sql = "select Code,UserID,Valid,PayChannel,Binded,AgentID,TradeCode,Rate ,PayChannelName"
				+ " from tab_ymf_qrcode as qrcode left join  tab_pay_channel as tpc on qrcode.PayChannel=tpc.ID"
				+ " where qrcode.Valid=1 and UserID=? and TradeCode=?";
		return queryForListString(sql, obj);
	}
	
	
	/**
	 *   更新固定码绑定状态
	 * @param obj
	 * @return
	 */
	public static int updateBindedInfo(Object[] obj){
		String sql = "update tab_ymf_qrcode set UserID=? , Binded=1 , BindedDate=? where Code=?";
		return executeSql(sql, obj);
	}
	
	

	public static int applyymf(Object[] obj){
		String sql = "insert into tab_ymf_qrcode (ID,Code,UserID,Valid,PayChannel,Binded,AgentID,TradeCode,Rate,GenDate,BindedDate,AgentProfit,SettlementRate) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	
	
	
}
