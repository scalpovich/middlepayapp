package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;


public class BankConfigDB extends DBBase{

	public static List<Map<String,String>> getBankList(){
		String sql = "select tbc.ID , tbc.BankName as bankName , tbc.BankUrl as bankUrl , tbc.BankStatus as bankStatus , tcar.count from "
				+ "tab_bank_config as tbc LEFT JOIN (select BankID , count(1) as count  from tab_card_apply_record  GROUP BY BankID ) as tcar on tbc.ID=tcar.BankID "
				+ "where BankStatus = 1";
		return queryForListString(sql, null);
	}
}
