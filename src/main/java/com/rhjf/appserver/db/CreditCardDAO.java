package com.rhjf.appserver.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.model.TabBankConfig;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


public class CreditCardDAO extends DBBase{
	

	LoggerTool logger = new LoggerTool(this.getClass());

	/**
	 * 校验在一段时间内 是否重复申请信用卡
	 * @param bank_type
	 * @param id_number
	 * @param create_time
	 * @return
	 */
	public static boolean findCardApplyRecord(String BankID,String ApplicantIDCardNo ,String CreateTime ){
		boolean flag = false;
		String sql = "select * from tab_card_apply_record where BankID = ? and ApplicantIDCardNo =? and CreateTime > ?";
		List<Map<String,Object>> list = queryForList(sql, new Object[]{BankID,ApplicantIDCardNo,CreateTime});

		if(list!=null&&list.size()>0){
			flag =true;
		}else{
			flag =false;
		}
		return flag;
	}
	
	public static int insertCardApplyRecord(String ID,String phone_number,String id_number,String real_name,String tjr_user_id,String create_time,String agency_number,String bank_id

			){
		String sql="insert into tab_card_apply_record(ID,ApplicantIDCardNo,ApplicantPhone,ApplicantName,UserID,OrganID,CreateTime,BankID) values (?,?,?,?,?,?,?,?)";
		int nRet= executeSql(sql, new Object[]{ID,id_number,phone_number,real_name,tjr_user_id,agency_number,create_time,bank_id});
		if(nRet==0){
		}
		return nRet;
	}
	
	/**
	 * 获取信用卡开通银行列表
	 * @return
	 */
	public static List<TabBankConfig> getBankList(){
		
		String sql = "select * from tab_bank_config where BankStatus = 1";
		List<TabBankConfig> list =  new ArrayList<TabBankConfig>();
		List<Map<String,Object>> list2 = queryForList(sql, null);
		
		for (Map<String, Object> map : list2) {
			try {
				list.add(UtilsConstant.mapToBean(map, TabBankConfig.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	public static TabBankConfig getTabBanConfigInfo(String id){
		
		String sql = "select * from tab_bank_config where ID=?";
		try {
			
			Map<String, Object> map = queryForMap(sql, new Object[]{id});
			if(map != null && map.size() > 0&&!map.isEmpty()) { 
				TabBankConfig tbc  = UtilsConstant.mapToBean(map, TabBankConfig.class);
				return tbc;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	
	public static List<Map<String, Object>> myCardShare(String userId , Integer page , Integer pageSize){
		String sql = "select * from tab_splitting_detail where agent_id =? order by create_time desc limit ? , ?";
		List<Map<String,Object>> list = queryForList(sql, new Object[]{userId,page,pageSize});
		return list;
	}
	
	
	public static Integer myCardShareCount(String userId ){
		String sql = "select count(1) as count  from tab_splitting_detail where agent_id =?";
		Map<String,Object> map = queryForMap(sql, new Object[]{userId});
		if(map!=null&&!map.isEmpty()){
			return Integer.parseInt(map.get("count").toString());
		}
		return 0;
	}
	
	
	
	public static Map<String,Object> myCardFeeAmount(String userId){
		String sql = "select * from tab_capital where creditmer_id =?";
		Map<String, Object> map = queryForMap(sql, new Object[]{userId} );
		return map;
	}

}
