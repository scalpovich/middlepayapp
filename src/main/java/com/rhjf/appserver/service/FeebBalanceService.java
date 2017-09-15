package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.CapitalDB;
import com.rhjf.appserver.db.UserProfitDB;
import com.rhjf.appserver.db.UserWalletDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *   用户收益接口
 * @author hadoop
 *
 */
public class FeebBalanceService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void FeebBalance(TabLoginuser user , RequestData request , ResponseData response){
		
		log.info("用户：" + user.getLoginID() + "查询距离上划款后的收益金额");
		
		String available_amount  = "0";
		Map<String,String> capitalMap = CapitalDB.getCapitalByUserID(new Object[]{user.getID()});
		if(capitalMap != null && !capitalMap.isEmpty()){
			available_amount =  UtilsConstant.strIsEmpty(capitalMap.get("capitalMap"))?"0":capitalMap.get("capitalMap");
		}
		
		Map<String,String> profitMap = UserProfitDB.userProfitTotal(user.getID());
		
		Map<String,String> walletMap = UserWalletDB.UserWalletByUserID(new Object[]{user.getID()});
		
		// 可划入钱宝总金额
		response.setTotal(String.valueOf(Integer.parseInt(user.getFeeBalance()) + Integer.parseInt(available_amount))); 
		
		//  获取收益总金额
		response.setFeeAmount(user.getFeeAmount());
		
		// 连续滑款天数
		response.setTotalCount(Integer.parseInt(walletMap.get("ContinuedDays")));
		
		// 信用卡划入钱包金额
		response.setCardFeeBlance(available_amount);
		// 拓客收益
		response.setTokerAmount(profitMap.get("toker"));
		// 返利收益
		response.setAntiAmount(profitMap.get("anti"));

		log.info("用户：" + user.getLoginID() + "总金额：" + response.getTotal() + "，信用卡：" + available_amount + " , 拓客：" + profitMap.get("toker") + " , 返利: " + profitMap.get("anti"));
		
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
