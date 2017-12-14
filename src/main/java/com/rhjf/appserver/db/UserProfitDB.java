package com.rhjf.appserver.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfitDB extends DBBase{

	
	
	/** 
	 *   保存三级分销 各个商户的利润
	 * @param obj
	 * @return
	 */
	public static int[] saveDistributeProfit(List<Object[]> obj){
		String sql = "insert ignore  into tab_user_profit (ID,UserID,Amount,TradeTime,TradeID , IncomeNature , ProfitType) "
				+ "values (?,?,?,?,?,?,?)";
		return executeBatchSql(sql, obj);
	}
	
	/**
	 *    终端用户查询自己的收益记录
	 * @param obj
	 * @return
	 */
	public static List<Map<String,Object>> userQueryProfitList(Object[] obj){
		String sql = "select Tradetime,Amount from tab_user_profit where UserID=?  order by TradeTime desc  limit ? , ? ";
		return queryForList(sql, obj);
	}
	
	public static Integer userQueryProfitCount(Object[] obj){
		String sql = "select count(1) as count from tab_user_profit where UserID=? ";
		Map<String,Object> map = queryForMap(sql, obj);
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
	}
	
	
	/**
	 *   查询商户距离上次划入钱包以后 拓客收益和范润收益
	 * @param userID
	 * @return
	 */
	public static Map<String,String> userProfitTotal(String userID){
		String sql = "select ifnull(sum(Amount),'0') as Amount from tab_user_profit where UserID=? and ProfitType=0 and ProfitStatus=0";
		Map<String,String> map = queryForMapStr(sql, new Object[]{userID});
		Map<String,String> profitMap = new HashMap<String,String>();
		profitMap.put("anti", map.get("Amount"));
		sql = "select ifnull(sum(Amount),'0') as Amount from tab_user_profit where UserID=? and ProfitType!=0 and ProfitStatus=0";
		map = queryForMapStr(sql, new Object[]{userID});
		profitMap.put("toker", map.get("Amount"));
		return profitMap;
	}
	
	
	

	
	/**
	 *    商户查询收益列表
	 * @param userID
	 * @param type
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static List<Map<String,String>> profitlist(String userID , String type , Integer page , Integer pageSize){
		
		List<Map<String,String>> list = null;
		String sql = null;
		switch (type) {
		case "1":
			// 交易范润
			sql = "select tpo.PayChannel , tpc.PayChannelName , DATE_FORMAT(CONCAT(tpo.TradeDate,tpo.TradeTime),'%Y-%m-%d %H:%i') as Tradetime ,"
					+ " tpo.Amount , tpo.MerchantProfit , tup.ProfitStatus "
					+ " from tab_user_profit as tup INNER JOIN tab_pay_order as tpo on tup.TradeID=tpo.ID"
					+ " INNER JOIN tab_pay_channel as tpc on tpo.PayChannel=tpc.ID"
					+ " where tup.UserID=? and tup.ProfitType=0 order by tup.TradeTime desc limit ? , ?";
			list = queryForListString(sql, new Object[]{userID , page , pageSize});
			break;

		case "2":
			// 查询拓客收益
			sql = "select tl.MerchantName ,DATE_FORMAT(CONCAT(tpo.TradeDate,tpo.TradeTime),'%Y-%m-%d %H:%i') as Tradetime , tpo.Amount ,"
					+ " tup.Amount as MerchantProfit , tup.ProfitStatus , ProfitType "
					+ " from tab_user_profit as tup INNER JOIN tab_pay_order as tpo on tup.TradeID=tpo.ID INNER JOIN tab_loginuser as tl on tpo.UserID=tl.ID"
					+ " where tup.UserID=? and tup.ProfitType!=0  order by tup.TradeTime desc limit ? , ?";
			list = queryForListString(sql, new Object[]{userID , page , pageSize});
			break;
		case "3":
			// 其他收益
			sql = "select '卡吧收益' as profitName , b.LoginID , b.MerchantName , a.Amount ,ProfitStatus "
					+ " from tab_splitting_detail as a INNER JOIN tab_loginuser as b on a.customer_id=b.ID "
					+ " where a.customer_id=? order by create_time desc limit  ? , ? ";
			list = queryForListString(sql, new Object[]{userID , page , pageSize});
			
			break;
		}
		
		
		return list;
	}
	
	public static Integer profitCount(String userID , String type ){
		Map<String,String> map = null;
		String sql = null;
		switch (type) {
		case "1":
			// 交易范润
			sql = "select count(1) as count from tab_user_profit where UserID=? and ProfitType=0 ";
			map = queryForMapStr(sql, new Object[]{userID});
			break;

		case "2":
			// 查询拓客收益
			sql = "select count(1) as count from tab_user_profit where UserID=? and ProfitType=1";
			map = queryForMapStr(sql, new Object[]{userID});
			break;
		case "3":
			// 其他收益
			sql = "select count(1) as count from tab_splitting_detail where customer_id=? ";
			map = queryForMapStr(sql, new Object[]{userID});
			break;
		}
		Integer count  = 0;
		if(map!=null && !map.isEmpty()){
			count  = Integer.parseInt(map.get("count"));
		}
		return count;
	}
	
	/**
	 *    用户获取某一个月的总收益（三级分销或交易返利）
	 * @param obj
	 * @return
	 */
	public static String monthProfitTotalAmount(Object[] obj){
		String sql = "select ifnull(sum(Amount) , 0) as amount from tab_user_profit where UserID = ? and left(TradeTime , 6) = ?";
		Map<String,String> map = queryForMapStr(sql, obj);
		return map.get("amount");
	}
	
	
	
	
	public static String merchantTokerProfit(String userID , String MerchantID){
		String sql = "select ifnull(sum(Amount) , '0') as amount from tab_user_profit where TradeID in "
				+ " (select ID from tab_pay_order where UserID in (select ID from tab_loginuser "
				+ " where (ThreeLevel=? and TwoLevel=? ) or (TwoLevel=? and OneLevel=?) or ID=? )) and UserID = ?";
		
		Map<String,String> map = queryForMapStr(sql, new Object[]{MerchantID , userID , MerchantID , userID , MerchantID , userID});
		
		return map.get("amount");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
