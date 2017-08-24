package com.rhjf.appserver.db;

import java.util.List;  
import java.util.Map;

import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LoginUserDB extends DBBase{
	
	
	/**
	 *  查询商户是否在平台入网成功
	 */
	public static boolean merchantPortalStatus(String LoginID){
		String sql = "select * from tab_loginuser where loginID=? and BankInfoStatus='1' ";
		Map<String,Object> map = queryForMap(sql, new Object[]{LoginID});
		if(map != null && !map.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 *   用户注册
	 * @param obj
	 * @return
	 */
	public static int registerUser(Object[] obj){
		String sql = "insert into tab_loginuser(ID,LoginID,LoginPwd,ThreeLevel, TwoLevel , OneLevel, AgentID , RegisterTime , SalesManID,UserType) values (?,?,?,?,?,?,?,?,?,?)";
		return  executeSql(sql, obj);
	}
	
	
	/**
	 *    根据id用户用户信息
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public static TabLoginuser getLoginuserInfo(String ID) throws Exception{
		String sql = "select * from tab_loginuser where ID=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{ID});
		return UtilsConstant.mapToBean(map, TabLoginuser.class);
	}
	
	
	/**
	 *    根据手机号获取用户信息
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public static TabLoginuser LoginuserInfo(String Loginid){
		String sql = "select * from tab_loginuser where LoginID=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{Loginid});
		if(map == null || map.isEmpty()){
			return null;
		}
		try {
			return UtilsConstant.mapToBean(map, TabLoginuser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *   查询登陆的手机号是否存在
	 * @param loginuser
	 * @return
	 */
	public static boolean getLoginUserInfo(TabLoginuser loginuser){
		String sql = "select * from tab_loginuser where LoginID=?";
		Map<String,Object> map = queryForMap(sql, new Object[]{loginuser.getLoginID()});
		if(map!=null&&!map.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 *   查询用户结算信息
	 * @param userID
	 * @return
	 */
	public static Map<String,Object> getUserBankCard(String userID){
		String sql = "select * from tab_pay_userbankcard where UserID=?";
		return queryForMap(sql, new Object[]{userID});
	}
	
	/**
	 *   更新对应商户  用户信息报中的分润总额
	 * @param list
	 * @return
	 */
	public static int[] merchantProfit(List<Object[]> list){
		String sql = "update tab_loginuser set FeeAmount=FeeAmount+?  ,  FeeBalance=FeeBalance+? where ID=?";
		return executeBatchSql(sql, list);
	}
	
	
	/** 
	 *   完善用户信息
	 * @param obj
	 * @return
	 */
	public static int updateUserInfo(Object[] obj){
		String sql = "update tab_loginuser set  Name=?, IDCardNo=? , BankCardNo=? ,BankName=? ,BankSubbranch=? ,MerchantName=? ,State=? ,Address=? ,Email=? , "
				+ " BankInfoStatus=2  where LoginID=?";
		return executeSql(sql, obj);
	}
	
	
	/**
	 *    H5完善用户信息
	 * @param obj
	 * @return
	 */
	public static int h5updateUserInfo(Object[] obj){
		String sql = "update tab_loginuser set MerchantTypeValue=?, Name=?, IDCardNo=? ,MerchantName=? ,State=? , City=?,Region=? ,BusinessLicense=? ,Address=? ,Email=? , "
				+ " MerchantBillName = ? , merchantPersonName= ? , BankInfoStatus=2  where LoginID=?";
		return executeSql(sql, obj);
	}
	
	/**
	 *   保存结算卡信息
	 * @param obj
	 * @return
	 */
	public static int saveOrUpBankInfo(Object[] obj){
		String sql = "insert into tab_pay_userbankcard (ID,UserID,AccountName,AccountNo,BankBranch,BankProv,BankCity,BankCode,BankName,SettleCreditCard,SettleBankType)"
				+ " value(?,?,?,?,?,?,?,?,?,?,?)"
				+ "on duplicate key update AccountName=?,AccountNo=?,BankBranch=?,BankProv=?,BankCity=?,BankCode=?,BankName=?,SettleCreditCard=?,SettleBankType=?";
		return executeSql(sql, obj);
	}
	
	
	
	/**
	 *   完善照片信息
	 * @param obj
	 * @return
	 */
	public static int updatePhotoInfo(Object[] obj){
		String sql = "update tab_loginuser set HandheldIDPhoto=? , IDCardFrontPhoto=?,IDCardReversePhoto=? , BankCardPhoto=? , BusinessPhoto=? , "
				+ " PhotoStatus=1 where LoginID=?";
		return executeSql(sql, obj);
	}
	
	
	/**
	 *   上游报件成功以后，将状态修该成通过审核
	 * @param obj
	 * @return
	 */
	public static int updateUserBankStatus(Object[] obj){
		String sql = "update tab_loginuser set BankInfoStatus = ? , PhotoStatus = ?  where LoginID=?";
		return executeSql(sql, obj);
	}
	

	
	/**
	 *   查询商户类型
	 * @return
	 */
	public static List<Map<String,Object>> merchantTypeList(){
		String sql = "select * from tab_pay_merchanttype";
		return queryForList(sql, null);
	}
	
	
	
	/**
	 *   更新用户登录信息
	 * @param obj
	 * @return
	 */
	public static int updateUserLoginInfo(Object[] obj){
		String sql = "update tab_loginuser set LastLoginTime=? , LoginPSN=? where LoginID=?";
		return executeSql(sql, obj);
	}
	
	/**
	 *   修改密码
	 * @param obj
	 * @return
	 */
	public static int updatePassword(Object[] obj){
		String sql = "update tab_loginuser set LoginPwd=? where LoginID=?";
		return executeSql(sql, obj);
	}
	

	/**
	 *    查询下线商户（发展商户）
	 * @param userID
	 * @return
	 */
	public static String getMyMerchant(String userID) {
		String sql="SELECT LoginID,ifnull(Name , '') as Name ,MerchantLeve,RegisterTime,BankInfoStatus,PhotoStatus,ThreeLevel,TwoLevel,OneLevel "
				+ " from tab_loginuser where (ThreeLevel =? or TwoLevel=? or OneLevel=?)";

		List<Map<String,Object>> list = queryForList(sql, new Object[]{userID,userID,userID});
		JSONArray jsonArray=new JSONArray();
		
		for (Map<String,Object> map : list) {
			JSONObject json = new JSONObject();
			json.put("userId", map.get("LoginID"));
			json.put("name", map.get("Name"));
			json.put("registerTime", map.get("RegisterTime"));
			json.put("accountStatus", map.get("BankInfoStatus"));
			json.put("photoStatus", map.get("PhotoStatus"));
			json.put("level", map.get("MerchantLeve"));
			if(map.get("ThreeLevel")!=null && map.get("ThreeLevel").equals(userID)){
				json.put("topLevel", "1");
			}
			if(map.get("TwoLevel")!=null && map.get("TwoLevel").equals(userID)){
				json.put("topLevel", "2");
			}
			if(map.get("OneLevel")!=null && map.get("OneLevel").equals(userID)){
				json.put("topLevel", "3");
			}
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}
	
	
	/**
	 *   用户查询商户信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> merchantInfo(Object[] obj){
		String sql = "select  MerchantName , MerchantTypeValue , MerchantBillName , Name, LoginID,Email,BusinessLicense,"
				+ "IDCardNo,State,City,Region, Address,AccountName,AccountNo,"
				+ "SettleBankType,b.BankName,b.BankBranch,BankProv,BankCity,"
				+ " b.BankCode, ifnull(b.SettleCreditCard , '') as SettleCreditCard ,  BankInfoStatus , PhotoStatus from tab_loginuser as a left JOIN"
				+ "  tab_pay_userbankcard as b on a.ID=b.UserID where a.id=?";
		return queryForMap(sql, obj);
	}

	/*保存商户信息*/
	public static int[] saveMerchantInfo(List<Object[]> list){
		
		String sql = "insert into tab_pay_merchant(MerchantID,MerchantName,UserTime,SignKey,DESKey,QueryKey,UserID,PayType)values(?,?,now(),?,?,?,?,?)";
		return executeBatchSql(sql, list);
		
	}
	
	/**
	 *   更新商户到账类型
	 * @param obj
	 * @return
	 */
	public static int updateTradeCode(Object[] obj){
		String sql = "update tab_loginuser set TradeCode=? where ID=?";
		return executeSql(sql, obj);
	}
	
	
	
	/**
	 *  用户更改联系邮箱
	 * @param obj
	 * @return
	 */
	public static int updateUserEmail(Object[] obj){
		String sql = "update tab_loginuser set Email=? where ID=? ";
		return executeSql(sql, obj);
	}
	
	
	public static Map<String, Object> userLevelUpCount(int merchantlevel){
		
		String sql = "select * from tab_upgrades where MerchantLevel =?";
		return  queryForMap(sql, new Object[]{merchantlevel});
		
	}
	
	public static int getUserCount(String userID){
		String sql = "select count(*) from tab_loginuser where ThreeLevel=? and BankInfoStatus=? and PhotoStatus=?";
		
		return executeSql(sql, new Object[]{userID,"1","1"});
	}
	
	public static int updateUserLev(int mechantLev,String userId){
		
		String sql  = "update tab_loginuser set MerchantLeve=? where ID=?";
		
		return executeSql(sql, new Object[]{mechantLev,userId});
	}
	
	public static int updateUserRate(int mechantLev,String userId){
		
		String sql = "update tab_user_config c,tab_pay_channel_usertype_cfg u,tab_loginuser us set c.SaleAmountMax=u.SaleAmountMax,"
				+ "c.T0SettlementRate=u.T0SaleRate,c.T1SettlementRate=u.T1SettlementRate,us.MerchantLeve=? where  us.ID=c.UserID "
				+ "and us.ID=? and u.MerchantLevel=?";
		return executeSql(sql, new Object[]{mechantLev,userId});
	}
	
	
	
}
