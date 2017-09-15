package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

public class WithdrawDB extends DBBase{

	
	
	/**
	 * 提现记录
	 * @return
	 */
	public static List<Map<String, String>> getTxRecordList(String userID ,  String ApplyDate ,Integer page, Integer pageSize){
		String sql="select ApplyDate,ApplyMoney,TermSerno,BalanceFlag from tab_withdraw s"
				+ " where  ApplyUserID=? and left(ApplyDate , 7)=? order by ApplyDate desc  limit ?,? ";
		List<Map<String,String>> list = queryForListString(sql, new Object[]{userID ,ApplyDate , page , pageSize});
		return list;
	}
}
