package com.rhjf.appserver.db;

import java.util.Map;

import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;

public class TermKeyDAO extends  DBBase{

	/**
	 *   为新注册用户分配秘钥
	 * @param UserID
	 * @return
	 */
	public static int allocationTermk(String UserID){
		
		String termTmkKey = MD5.md5(UtilsConstant.getUUID(), "UTF-8").toUpperCase();
		String tmk = MD5.md5(UtilsConstant.getUUID(), "UTF-8").toUpperCase();
		
		String sql="insert into tab_termkey(ID,UserID,TmkKey,TermTmkKey) values(?,?,?,?)";

		return executeSql(sql, new Object[]{UtilsConstant.getUUID(), UserID,tmk,termTmkKey});
	}
	
	
	/**
	 *   查询用户的termkey
	 * @param userID
	 * @return
	 */
	public static Map<String,Object> selectTermKey(String userID){
		String sql = "select ID,UserID,MacKey,PinKey,TDKey , TmkKey,TermTmkKey from tab_termkey  where UserID=?";
		return queryForMap(sql, new Object[]{userID});
	}
	
	
	public static int updateKey(String userID,String macKey , String pinkey , String tdkey){
		String sql="update tab_termkey set MacKey=? ,  PinKey=? , TDKey=?  where UserID=? ";
		return executeSql(sql, new Object[]{macKey , pinkey , tdkey , userID});
	}
	
}
