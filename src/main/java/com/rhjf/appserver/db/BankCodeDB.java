package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;


/**
 * 用户联行号信息
 * 
 * @author a
 *
 */

public class BankCodeDB extends DBBase {
	
	
	public static Map<String, Object> bankBinMap(Object[] obj) {
		String sql = "select * from tab_pay_binverify where verifyCode = SUBSTRING(?,1,verifyLength)";
		return queryForMap(sql, obj);
	}
	
	
	public static Map<String,Object> bankInfo(Object[] obj){
//		String sql = "select * from  tab_pay_bankcode  where BankName like  CONCAT('%', ? ,'%')  and BankBranch like CONCAT('%', ? ,'%') "
//				+ " and ? like CONCAT(BankProv ,'%')  and  ? like  CONCAT(BankCity ,'%')";
		String sql = "select * from  tab_pay_bankcode  where BankName like  CONCAT('%', ? ,'%')  and BankBranch=? "
				+ " and ? like CONCAT(BankProv ,'%')  and  ? like  CONCAT(BankCity ,'%')";
		List<Map<String,Object>> list =  queryForList(sql, obj);
		if(list!=null&&list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	 
	
	/**
	 *  更具信用卡卡号获取银行信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> creditCardBin(Object[] obj){
		String sql = "select tpb.BankName , tpb.BankBranch , tpb.BankSymbol ,unite_bank_no , BankProv , BankCity"
				+ " from tab_cardinfo c ,unite_bank u , tab_pay_bankcode as tpb "
				+ " where substring(c.bankid,2,3)=substring(u.unite_bank_no,1,3) and  Substring(?,1,length(CardBin))=CardBin and tpb.BankCode=unite_bank_no";
		return queryForMap(sql, obj);
	}	
	
	
	/**
	 *   获取支行名称
	 * @param map
	 * @return
	 */
	public static List<Map<String,Object>> bankBranchList(Map<String,String> map){
		String sql = "SELECT BankBranch from tab_pay_bankcode"
				+ " where  BankName like CONCAT(CONCAT('%' , ?) , '%') and ? like CONCAT(CONCAT('%' , BankProv) , '%') "
				+ " and  ? like CONCAT(CONCAT('%' , bankCity),'%')";
		return queryForList(sql, new Object[]{map.get("bankName") , map.get("bankProv") , map.get("bankCity")});
	}
}
