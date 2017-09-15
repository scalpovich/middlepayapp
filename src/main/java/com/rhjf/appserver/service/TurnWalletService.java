package com.rhjf.appserver.service;

import java.util.Arrays;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.CapitalDB;
import com.rhjf.appserver.db.UserWalletDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *   将受益余额转入钱包
 * @author hadoop
 *
 */

public class TurnWalletService {
	
	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void TurnWallet(TabLoginuser user , RequestData request , ResponseData response){
		log.info("用户将受益余额转入钱包:"  + user.getLoginID() + "账户余额:" + user.getFeeBalance());
		log.info("查询用户" + user.getLoginID() + "钱包信息");
		
		Map<String,String> capitalMap = CapitalDB.getCapitalByUserID(new Object[]{user.getID()});
		
		String available_amount = "0";
		if(capitalMap!=null && !capitalMap.isEmpty()){
			available_amount = UtilsConstant.strIsEmpty(capitalMap.get("available_amount"))?"0":capitalMap.get("available_amount");
		}
		
		log.info("用户分润余额： " + user.getFeeBalance() + " , 信用卡分润余额：" + available_amount);
		
		if("0".equals(user.getFeeBalance())&&"0".equals(available_amount)){ 
			log.info("用户" + user.getLoginID() + "收益金额为0");
			response.setRespCode(RespCode.TurnWalletError[0]);
			response.setRespDesc("可操作金额为0");
			return ;
		}
		int feeBalance = 0;
		int ContinuedDays = 1;
		Map<String , String> walletMap = UserWalletDB.UserWalletByUserID(new Object[]{user.getID()});
		if(walletMap == null || walletMap.isEmpty()){
			log.info("用户：" + user.getLoginID() + "钱包数据为空， 初始化一条记录 ，所有数据都为0"); 
			UserWalletDB.saveUserWallet(new Object[]{UtilsConstant.getUUID() , user.getID()});
		}else{
			String toDay = DateUtil.getNowTime(DateUtil.yyyy_MM_dd);
//			if(toDay.equals(walletMap.get("LastWithdrawDate"))){
//				log.info("日期：" + toDay + "用户：" + user.getLoginID() + "已经出现划款动作");
//				response.setRespCode(RespCode.TurnWalletError[0]);
//				response.setRespDesc(RespCode.TurnWalletError[1]);
//				return ;
//			}
			try {
				String yesterday = DateUtil.getDateAgo(toDay, 1, DateUtil.yyyy_MM_dd);
				
				log.info("当前日期：" + toDay + ", 前一天的日期：" + yesterday + " , 商户最后一次划入钱包的时间：" + walletMap.get("LastWithdrawDate"));
				
				if(yesterday.equals(walletMap.get("LastWithdrawDate"))){
					ContinuedDays = Integer.parseInt(walletMap.get("ContinuedDays")) + 1;
				}
			} catch (Exception e) {
				log.error("格式化日期出现异常："  , e); 
			}
			
			feeBalance = Integer.parseInt(walletMap.get("WalletBalance"));
			feeBalance = feeBalance + Integer.parseInt(user.getFeeBalance()) + Integer.parseInt(available_amount);
 		}
		
		int[] ret = UserWalletDB.trunwallet(user , available_amount , ContinuedDays);
		int a =  Arrays.binarySearch(ret, 0);
	    if(a > 0){
	    	log.info("执行的sql中返回的数据包含0");
	    	response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
	    }else{
	    	response.setFeeBalance(String.valueOf(feeBalance)); 
	    	response.setRespCode(RespCode.SUCCESS[0]);
			response.setRespDesc(RespCode.SUCCESS[1]);
	    }
	}
}
