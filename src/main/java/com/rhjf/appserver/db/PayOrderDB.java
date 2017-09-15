package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.util.UtilsConstant;

public class PayOrderDB extends DBBase{

	
	
	/**
	 *   显示月报数据
	 */
	public static  List<Map<String,String>> monthlyReport(String userid){
		String sql = "SELECT LEFT(LocalDate,4) as year , SUBSTRING(LocalDate FROM 5 FOR 2) as month , sum(amount) as amount , count(1) as count "
				+ " from tab_pay_order where PayRetCode='0000' and UserID=? GROUP BY LEFT(LocalDate,4) , SUBSTRING(LocalDate FROM 5 FOR 2)  order by month  desc , year desc  ";
		return queryForListString(sql, new Object[]{userid});
	}
	

	/**
	 *    将交易数据导出excel文件发送到用户邮箱
	 * @param userid
	 * @param date
	 * @return
	 */
	public static List<Map<String,Object>> exportTrade(String userid, String date){
		
		String sql = "select  c.MerchantName , c.LoginID , PayChannel ,PayChannelName , a.OrderNumber  , date_format(CONCAT(LocalDate, TradeTime) ,'%Y-%m-%d %H:%i') as tradedate , amount , fee , a.TradeCode , a.MerchantProfit "
				+ "from tab_pay_order as a INNER JOIN tab_pay_channel as b on a.PayChannel = b.ID INNER JOIN  tab_loginuser as c on a.UserID=c.ID "
				+ " where PayRetCode='0000' and  userid=? and left(LocalDate,6)=? ";
		
		return queryForList(sql, new Object[]{userid,date}); 
	}
	
	
	public static List<Map<String,Object>> monthlyReportDetailed(String userid , String date , String paychannel , Integer page ,Integer pageSize){
		
		if(UtilsConstant.strIsEmpty(paychannel)||paychannel.equals("0")){
			String sql = "select PayChannel ,PayChannelName , date_format(CONCAT(LocalDate, TradeTime) ,'%Y-%m-%d %H:%i') as tradedate , amount , fee , "
					+ " case when TradeCode='T0' then 'T0结算' when TradeCode='T1' then 'T1结算' end  as TradeCode "
					+ "from tab_pay_order as a INNER JOIN tab_pay_channel as b on a.PayChannel = b.ID  where PayRetCode='0000' and  userid=? and left(LocalDate,6)=? "
					+ " order by tradedate  desc limit ? , ?";
			
			return queryForList(sql, new Object[]{userid,date , page , pageSize}); 
		}else{
			String sql = "select PayChannel ,PayChannelName , date_format(CONCAT(LocalDate, TradeTime) ,'%Y-%m-%d %H:%i') as tradedate , amount , fee , "
					+ " case when TradeCode='T0' then 'T0结算' when TradeCode='T1' then 'T1结算' end  as TradeCode "
					+ "from tab_pay_order as a INNER JOIN tab_pay_channel as b on a.PayChannel = b.ID  where  PayRetCode='0000' and userid=? and left(LocalDate,6)=?  and PayChannel = ? "
					+ " order by tradedate  desc limit ? , ?";
			
			return queryForList(sql, new Object[]{userid,date , paychannel , page , pageSize}); 
		}
	}
	
	
	public static Integer monthlyReportDetailedCount(String userid , String date , String paychannel){
		
		
		Map<String,Object> map = null;
		
		if(UtilsConstant.strIsEmpty(paychannel)||paychannel.equals("0")){
			String sql = "select count(1) as count "
					+ "from tab_pay_order as a INNER JOIN tab_pay_channel as b on a.PayChannel = b.ID "
					+ " where PayRetCode='0000' and  userid=? and left(LocalDate,6)=? ";
			
			map = queryForMap(sql, new Object[]{userid,date }); 
		}else{
			String sql = "select count(1) as count  "
					+ "from tab_pay_order as a INNER JOIN tab_pay_channel as b on a.PayChannel = b.ID "
					+ " where PayRetCode='0000' and  userid=? and left(LocalDate,6)=?  and PayChannel = ? ";
			
			map = queryForMap(sql, new Object[]{userid,date , paychannel}); 
		}
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
	}
	
	/**
	 *   显示折线数据
	 * @param obj
	 * @return
	 */
	public static List<Map<String,String>> tradingCurvelist(Object[] obj){
		String sql = "select UserID ,  cast(SUBSTRING(LocalDate FROM 5 FOR 2) as SIGNED)  as LocalDate , SUM(Amount) as amount , count(1) as count "
				+ " from tab_pay_order where PayRetCode='0000' and UserID=? and  LEFT(LocalDate , 6)  BETWEEN  ? and ? "
				+ " group by UserID , LEFT(LocalDate , 6)";
		return queryForListString(sql, obj);
	}
	
	
	/** 月报详情综合 **/
	public static Map<String,String> tradeTotal(Object[] obj){
		String sql = "select ifnull(sum(Amount) , 0) as amount , ifnull(sum(Fee) , 0 ) as fee , count(1) as count , ifnull(sum(amount)-sum(fee), 0 ) as pureProfit  "
				+ " from tab_pay_order where  UserID=?  and   LEFT(LocalDate , 6)= ? and  PayRetCode='0000' ";
		return queryForMapStr(sql, obj);
	}
	
	
	/**
	 *   查询用户当天交易金额
	 * @param obj
	 * @return
	 */
	public static Integer dayTradeAmount(Object[] obj){
		
		String sql = "select CONCAT(ifnull(sum(Amount),'0'),'') as amount from tab_pay_order where UserID=? and LocalDate=? and PayRetCode='0000'";
		
		List<Map<String,String>> list = queryForListString(sql, obj);
		
		if(list!=null && list.size() > 0){
			return Integer.parseInt(String.valueOf(list.get(0).get("amount"))); 
		}else{
			return 0;
		}
	}
	
	
	
	/**
	 *   商户查询某天交易详细
	 * @param obj
	 * @return
	 */
	public static List<Map<String,String>> DayTradeDetail(Object[] obj){
		String sql = "select DATE_FORMAT(TradeDate,'%Y-%m-%d') as tradedate , amount , fee , PayChannel , b.PayChannelName , concat(a.TradeCode , '结算') as TradeCode "
				+ " from tab_pay_order as  a INNER JOIN tab_pay_channel as b on a.PayChannel=b.ID "
				+ " where UserID=?  and PayRetCode='0000' and TradeDate=? order by TradeDate desc , TradeTime desc;";
		return queryForListString(sql, obj);
	}
	
	


	/**
	 *   用户查询某天交易详情， 显示查询日期之前三天包括查询日期的折线数据
	 * @param obj
	 * @return
	 */
	public static List<Map<String ,String>> DayTradeLineChart(Object[] obj){
		
		String sql = "select TradeDate as LocalDate , amount from tab_usertrade_statistical"
				+ " where Userid=? and TradeDate BETWEEN ? and ?";
		
		return queryForListString(sql, obj);
	}
	
	
	/**
	 *  查询当天的交易金额
	 * @param obj
	 * @return
	 */
	public static Map<String,String> ToDayTradeLineChart(Object[] obj){
		String sql = "select ifnull(TradeDate , ?) as LocalDate , ifnull(sum(Amount) , 0) as amount ,ifnull(sum(Fee) , 0) as fee , count(1) as count  , ifnull(sum(Amount)-sum(Fee) , 0) as profit from tab_pay_order "
				+ " where  PayRetCode='0000' and  Userid=? and TradeDate=?";
		return queryForMapStr(sql, obj);
	}
	
	
	
	
	/**
	 *  查询某天的交易金额 ， 手续费 交易笔数
	 * @param obj
	 * @return
	 */
	public static Map<String,String> DayTradeTotalData(Object[] obj){
		String sql = "select TradeDate as LocalDate , amount ,  fee , TradeCount as  count , profit from tab_usertrade_statistical "
				+ " where  Userid=? and TradeDate=?";
		return queryForMapStr(sql, obj);
	}
	
	
	
}
