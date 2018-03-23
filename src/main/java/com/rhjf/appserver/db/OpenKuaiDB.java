package com.rhjf.appserver.db;

import java.util.List;
import java.util.Map;

public class OpenKuaiDB extends DBBase{

	
	public static  Map<String,Object> getOpenKuai(Object[] obj){
		String sql = "select * from tab_openkuai where bankCardNo=?";
		return queryForMap(sql, obj);
	}
	
	
	public static int save(Object[] obj){
		String sql = "INSERT INTO tab_openkuai (ID,UserID,bankCardNo,createDate,payerPhone ,encrypt,statusCode,orderID , cvn2 , expired) VALUES (?,?,?,now(),?,?,?,? , ? , ?) "
				+ " ON DUPLICATE KEY UPDATE encrypt=? , orderID=? , createDate=now() ";

		int x = executeSql(sql, obj);

		String sql2 = "update tab_pay_binverify as a , tab_openkuai as b set b.bankSymbol=a.bankCode where left(b.bankCardNo,a.verifyLength)=a.verifyCode and b.bankSymbol is null";

		executeSql(sql2, null);
		return x;
	}



	
	public static List<Map<String,Object>> kuaiCardlist(Object[] obj){
//		String sql = "select * from ( select a.*  , b.bankName , case b.cardName when 'DEBIT_CARD' then '储蓄卡'  when 'CREDIT_CARD' then '信用卡' end as cardName , b.bankCode "
//				+ " from tab_openkuai as a INNER JOIN tab_pay_binverify as b"
//				+ " on b.verifyCode = SUBSTRING(a.bankCardNo,1,b.verifyLength) where UserID=? and encrypt!=0 order by a.createDate desc ) "
//				+ " as z GROUP BY z.bankCardNo  having verifyLength=max(verifyLength)";
		
		String sql = "select z.ID,z.UserID , z.bankCardNo , z.createDate , z.payerPhone , z.encrypt , z.statusCode , z.orderID , x.bankName ,"
				+ " case x.cardName when 'DEBIT_CARD' then '储蓄卡'  when 'CREDIT_CARD' then '信用卡' end as cardName , x.bankCode "
				+ " from ( select a.ID,a.UserID , a.bankCardNo , a.createDate , a.payerPhone , a.encrypt , a.statusCode , a.orderID , max(b.verifyLength) as verifyLength from tab_openkuai as a "
				+ " INNER JOIN tab_pay_binverify as b on b.verifyCode = SUBSTRING(a.bankCardNo,1,b.verifyLength)"
				+ " where UserID=? and encrypt!=0 GROUP BY a.bankCardNo ) as z "
				+ " INNER JOIN tab_pay_binverify as x on x.verifyCode = SUBSTRING(z.bankCardNo,1,z.verifyLength) order by  createDate desc";
		return queryForList(sql, obj);
	}
	
	public static List<Map<String,Object>> kuaiCardlistNoOpen(Object[] obj){
		String sql = "select * from tab_openkuai where encrypt=0 and date(createDate)=date(now()) and  UserID=?";
		return queryForList(sql, obj);
		
	}
	
	
	public static int updateEncrypt(Object[] obj){
		String sql = "update tab_openkuai set encrypt=3 where orderID=?";
		return executeSql(sql, obj);
		
	}
	
	
	
	public static int[] updateBankCardResult(List<Object[]> obj){
		String sql = "update tab_openkuai set encrypt  = ? where UserID=? and bankCardNo = ?";
		return executeBatchSql(sql, obj);
	}
	
	
	public static int updatePayerPhone(Object[] obj){
		String sql = "update tab_openkuai set payerPhone=?  where UserID=? and bankCardNo=?";
		return executeSql(sql, obj);
	}
	
}
