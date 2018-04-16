package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.CapitalDAO;
import com.rhjf.appserver.db.UserProfitDAO;
import com.rhjf.appserver.db.UserWalletDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *   用户收益接口
 * @author hadoop
 *
 */
public class FeebBalanceService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void FeebBalance(LoginUser user , RequestData request , ResponseData response){
		
		log.info("用户：" + user.getLoginID() + "查询距离上划款后的收益金额");
		
		
		/**  信用卡收益 **/
		String available_amount  = "0";
		String aggregate_amount = "0";
		Map<String,String> capitalMap = CapitalDAO.getCapitalByUserID(new Object[]{user.getID()});
		if(capitalMap != null && !capitalMap.isEmpty()){
			available_amount =  UtilsConstant.strIsEmpty(capitalMap.get("available_amount"))?"0":capitalMap.get("available_amount");
			aggregate_amount =  UtilsConstant.strIsEmpty(capitalMap.get("aggregate_amount"))?"0":capitalMap.get("aggregate_amount");
		}
		
		Map<String,String> profitMap = UserProfitDAO.userProfitTotal(user.getID());
		
		Map<String,String> walletMap = UserWalletDAO.UserWalletByUserID(new Object[]{user.getID()});
		
		Integer ContinuedDays = 0;
		
		if(walletMap != null){
			ContinuedDays = Integer.parseInt(walletMap.get("ContinuedDays")==null?"0":walletMap.get("ContinuedDays"));
		}
		
		
	
		//  获取收益总金额
		Integer total = Integer.parseInt(user.getFeeAmount()) + Integer.parseInt(aggregate_amount);
		
		log.info("user.getFeeAmount():" + user.getFeeAmount());
		log.info("available_amount:" + available_amount);
		
		//  总收益（包括信用卡 拓客 交易返利）
		response.setFeeAmount(String.valueOf(total));
		
		// 信用卡划入钱包金额
		response.setCardFeeBlance(available_amount);
		// 拓客收益
		response.setTokerAmount(profitMap.get("toker"));
		// 返利收益
		response.setAntiAmount(profitMap.get("anti"));
		
		
		//可划入钱宝总金额
		response.setTotal(String.valueOf(Integer.parseInt(user.getFeeBalance()) + Integer.parseInt(available_amount))); 
		
		
		// 连续滑款天数
		response.setTotalCount(ContinuedDays);
		

		JSONArray array = new JSONArray();
		// 返利收益
		JSONObject json = new JSONObject();
		json.put("amount", profitMap.get("anti"));
		json.put("open", "0".equals(profitMap.get("anti"))?"1":"0");
		json.put("type", "1");
		
		array.add(json);
		
		json = new JSONObject();
		json.put("amount", profitMap.get("toker"));
		json.put("open", "0".equals(profitMap.get("toker"))?"1":"0");
		json.put("type", "2");
		
		array.add(json);
		
		json = new JSONObject();
		json.put("amount", available_amount);
		json.put("open", "0".equals(available_amount)?"1":"0");
		json.put("type", "3");
		
		array.add(json);
		
		response.setList(array.toString());
		log.info("用户：" + user.getLoginID() + "总金额：" + response.getTotal() + "，信用卡：" + available_amount + " , 拓客：" + profitMap.get("toker") + " , 返利: " + profitMap.get("anti"));
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
		
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
