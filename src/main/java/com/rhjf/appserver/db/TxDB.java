package com.rhjf.appserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class TxDB extends DBBase {
	
	public static int tx(String loginID,String ammount,String termSerno,String txType){
		int seqNo = 0;
		ResultSet rs = null;
		if("0".equals(txType)){
			rs = executeProcedure("TX",loginID,ammount,termSerno,txType);
		}else{
			rs = executeProcedure("CARD_TX",loginID,ammount,termSerno,txType);
		}
		try {
			if(rs==null){
				return 0;
			}
			while(rs.next()) {  
				return rs.getInt("ret");
			}  
		} catch (SQLException e) {	
			e.printStackTrace();
			return 0;
		}
		return seqNo; 
		
	}
	
	
	public static int walletTX(String loginID,String ammount,String termSerno,String txType){
		
		int seqNo = 0;
		ResultSet rs = null;
		rs = executeProcedure("walletwithdraw",loginID,ammount,termSerno,txType);
		try {
			if(rs==null){
				return 0;
			}
			while(rs.next()) {  
				return rs.getInt("ret");
			}  
		} catch (SQLException e) {	
			e.printStackTrace();
			return 0;
		}
		return seqNo; 
	}
	
	
	
	/**
	 * 提现记录
	 * @return
	 */
	public static List<Map<String, Object>> getTxRecordList(String userId,String txType,Integer page, Integer pageSize){
		String sql="select ApplyDate,ApplyMoney,TermSerno,BalanceFlag from tab_withdraw s"
				+ " where  ApplyUserID=? and TxType=? order by ApplyDate desc  limit ?,? ";
		List<Map<String,Object>> list = queryForList(sql, new Object[]{userId,txType,page,pageSize});
		return list;
	}
	
	
	public static Integer getTxRecordCount(String userId,String txType){
		String sql="select count(1) as count from tab_withdraw s where  ApplyUserID=? and TxType=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{userId,txType});
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
		
	}

}
