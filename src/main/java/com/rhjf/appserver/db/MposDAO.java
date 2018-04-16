package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.model.BankInfo;
import com.rhjf.appserver.model.BankKey;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

public class MposDAO extends DBBase{

	/**
	 *   交易表插入数据
	 * @param merchantID
	 * @param termNo
	 * @param tradeCode
	 * @param tradeMoney
	 * @param outAccount
	 * @param termSerno
	 * @param platSerno
	 * @param localTradeDate
	 * @param localTradeTime
	 * @param termBatchNo
	 * @param netCode
	 * @param platMerchantNo
	 * @param platTermNo
	 * @param cardExp
	 * @param cardSerno
	 * @param isCredit
	 * @param referenceNo
	 * @param authCode
	 * @param termExpandData
	 * @param bankExpandData
	 * @return
	 */
	public static int insertConsumeTrade(String ID , String UserID , String merchantID,String termNo,String tradeCode, String tradeMoney, String outAccount, 
			String termSerno,String  platSerno, String localTradeDate, String localTradeTime, String termBatchNo, int netCode,
			 String platMerchantNo, String platTermNo,String cardExp, String cardSerno, int isCredit,String referenceNo, 
			String authCode, String termExpandData,String bankExpandData){
		int T0Flag = 0;
		if (tradeCode.equals("1010")) {
			T0Flag = 1;
			tradeCode = "T002";
		}
		String sql="Insert into Tab_Support ( ID ,UserID , MerchantID, TermNo, TradeCode, TradeMoney, OutAccount, TermSerno, PlatSerno, "
				+ "LocalTradeDate, LocalTradeTime, TermBatchNo, NetCode, PlatTermNo, PlatMerchantNo, CardExp, CardSerno, IsCredit,"
				+ "ReferenceNo, AuthCode, TermExpandData,BankExpandData,T0Flag) values ('"+ID+"','"+UserID+"' , '"+merchantID+"','"+termNo+"','"+tradeCode+"',"
				+ "'"+tradeMoney+"','"+outAccount+"','"+termSerno+"','"+platSerno+"','"+localTradeDate+"','"+localTradeTime+"',"
				+ "'"+termBatchNo+"',"+netCode+",'"+platTermNo+"','"+platMerchantNo+"','"+cardExp+"','"+cardSerno+"',"+isCredit+","
				+ "'"+referenceNo+"','"+authCode+"','"+termExpandData+"','"+bankExpandData+"',"+T0Flag+")";
		return executeSql(sql, null);
	}
	
	
	/**
	 *    借到成功响应   更新交易表数据
	 * @param referenceNo
	 * @param bankDate
	 * @param bankTime
	 * @param settleDate
	 * @param errSource
	 * @param retCode
	 * @param cancelFlag
	 * @param voidFlag
	 * @param refundFlag
	 * @param bankBatchNo
	 * @param bankOrganID
	 * @param bankExpandData
	 * @param merchantID
	 * @param termNo
	 * @param localTradeDate
	 * @param termSerno
	 * @return
	 */
	public static int updateConsumeTrade(String referenceNo,String bankDate,String bankTime, String settleDate, int errSource, 
			String retCode,int cancelFlag, int voidFlag, int refundFlag, String bankBatchNo, String bankOrganID, String bankExpandData,
			String merchantID, String termNo, String localTradeDate, String termSerno){
		
		String sql = "Update Tab_Support set ReferenceNo='" + referenceNo + "', BankDate='" + bankDate + "', BankTime='"
				+ bankTime + "', " + "SettleDate='" + settleDate + "',  ErrSource=" + errSource + ", RetCode='"
				+ retCode + "', CancelFlag=" + cancelFlag + ", " + "VoidFlag=" + voidFlag + ", RefundFlag=" + refundFlag
				+ ", BankBatchNo='" + bankBatchNo + "', BankOrganID='" + bankOrganID + "',BankExpandData='"
				+ bankExpandData + "'" + "where MerchantID='" + merchantID + "' and TermNo='" + termNo
				+ "' and LocalTradeDate='" + localTradeDate + "' and TermSerno='" + termSerno + "'";
		
		return executeSql(sql, null);

	}
	
	public static int saveMposFee(Object[] obj){
		String sql = "insert into tab_mpos_profit (ID,UserID,TradeID,Fee,AgentID,AgentProfit,TwoAgentID,TwoAgentProfit,PlatformProfit,ChannelProfit) "
				+ "values (?,?,?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	
	
	/**
	 *   查询上游秘钥
	 * @param platMerchantID
	 * @param platTermID
	 * @return
	 */
	public static BankKey getBankey(String platMerchantID , String platTermID){
		String sql = "select * from tab_bankkey";
		
		Map<String,Object> map = queryForMap(sql, null);
		
		try {
			return UtilsConstant.mapToBean(map, BankKey.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	
	public static int getCardType(String cardNo) {
		String sql="select CardType from tab_cardinfo where Substring('"+cardNo+"',1,length(CardBin))=CardBin and CardLen=length('"+cardNo+"')";
		
		Map<String,Object> map = queryForMap(sql, null);
		
		return Integer.parseInt(map.get("CardType").toString()); 
	}
	
	/**
	 *     获取商户终端信息
	 * @param platMerchantID
	 * @param platTermID
	 * @return
	 */
	public static BankInfo getBankInfo(String userID){
		String sql = "select PlatMerchantID , PlatTermNo from tab_merbindinfo where UserID=? and Enable=1";
		Map<String,Object> map = queryForMap(sql, new Object[]{userID});
		try {
			return UtilsConstant.mapToBean(map, BankInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	/**
	 *   pos签到以后更新秘钥
	 * @param pinKey
	 * @param macKey
	 * @param batchNo
	 * @param termNo
	 * @param merchantID
	 * @return
	 */
	public static int updateBankKey(String pinKey , String macKey , String batchNo ){
		String sql="update tab_bankkey set PinKey='"+pinKey+"',MacKey='"+macKey+"',BatchNo='"+batchNo+"'";
		return  executeSql(sql , null);
	}
	
	
	public static boolean getBindSN(String sn){
		String sql = "select * from tab_merbindinfo where SN=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{sn});
		if(map == null || map.isEmpty()){
			return false;
		}
		return true;
	}
	
	
	public static Map<String,Object> getMPOSMerchant(String userid){
		String sql = "select * from tab_merbindinfo where UserID=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{userid});
		return map;
	}
	
	public static int BindSN(String userid , String sn){
		String sql = "update tab_merbindinfo set sn = ? where userid=?";
		return executeSql(sql, new Object[]{sn,userid});
	}
	
	public static int saveMposMerchant(Object[] obj){
		String sql = "insert into tab_merbindinfo (UserID,NetCode,PlatMerchantID,PlatTermNo,CustomerNo,Enable,Remarks,SN)"
				+ " values (?,?,?,?,?,?,?,?)";
		return executeSql(sql, obj);
	}
	
	
	public static List<Map<String,Object>> mposTradeList(Object[] obj){
		String sql = "select PlatSerno  as OrderNumber , CONCAT(LocalTradeDate,LocalTradeTime) as TradeTime, TradeMoney as Amount, TermSerno, 'MPOS' as PayChannelName "
				+ " from tab_support  where UserID=? and RetCode='00'"
				+ " order by  LocalTradeDate desc ,  LocalTradeTime  desc limit ? , ?  ";
		
		return queryForList(sql, obj);
		
	}
	
	public static Integer userQueryTradeCount(Object[] obj){
		String sql = "select count(1) as count from tab_support"
				+ " where  UserID=? and  RetCode='00'";
		Map<String,Object> map = queryForMap(sql, obj);
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
	}
	
	
	public static JSONObject userQueryTradeTotal(Object[] obj){
		
		JSONObject json = new JSONObject();
		
		String today = "select IFNULL(SUM(TradeMoney) ,0) as amount from tab_support "
				+ "where RetCode='00' and LocalTradeDate=DATE_FORMAT(now(),'%Y%m%d') and UserID=?";
		String todayAmount = UtilsConstant.ObjToStr(queryForMap(today, obj).get("amount"));
		
		String total = "select SUM(TradeMoney) as amount from tab_support "
				+ "where RetCode='00' and  UserID=? ";
		
		String totalAmount =  UtilsConstant.ObjToStr(queryForMap(total, obj).get("amount"));
		
		json.put("today" , todayAmount);
		json.put("total", totalAmount);
		
		return json;
		
	}
	
}
