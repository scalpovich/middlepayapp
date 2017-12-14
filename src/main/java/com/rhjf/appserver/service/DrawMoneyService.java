package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.UserBankCardDB;
import com.rhjf.appserver.db.UserProfitDB;
import com.rhjf.appserver.db.UserWalletDB;
import com.rhjf.appserver.db.WithdrawDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *    钱包提现
 * @author hadoop
 *
 */
public class DrawMoneyService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void DrawMoney(TabLoginuser user , RequestData request , ResponseData response){
		
		log.info("用户：" + user.getLoginID() + "发起提现请求");
		
		Map<String ,String> userwalletMap = UserWalletDB.UserWalletByUserID(new Object[]{user.getID()});
		
		if(request.getAmount()==null ){
			log.info("提现金额为空");
			response.setRespCode(RespCode.TXAMOUNTError[0]);
			response.setRespDesc(RespCode.TXAMOUNTError[1]);
	    	return ;
		}
		if(Long.parseLong(request.getAmount())==0){
			log.info("提现金额无效");
			response.setRespCode(RespCode.TXAMOUNTError[0]);
			response.setRespDesc(RespCode.TXAMOUNTError[1]);
	    	return ;
		}
		
		String WalletBalance = UtilsConstant.strIsEmpty(userwalletMap.get("WalletBalance"))?"0":userwalletMap.get("WalletBalance");
		
		if(Integer.parseInt(WalletBalance) < Integer.parseInt(request.getAmount())){
			log.info("余额不足 , 用户：" + user.getLoginID() + "剩余金额:" + WalletBalance + " , 提现金额：" + request.getAmount());
			response.setRespCode(RespCode.TXAMOUNTNOTENOUGH[0]);
			response.setRespDesc(RespCode.TXAMOUNTNOTENOUGH[1]);
	    	return ;
		}
		
		/**  获取用户结算卡信息 **/
		Map<String,Object> map = UserBankCardDB.getBankInfo(user.getID());
		
		String orderNumber = UtilsConstant.getOrderNumber();
		
		int[] ret = UserWalletDB.drawMoney(Integer.parseInt(request.getAmount()), user.getID(), request.getSendSeqId() ,
					UtilsConstant.ObjToStr(map.get("AccountNo")) , orderNumber );
		
		if(ret == null){
			log.info("执行的sql中返回的数据包含0");
	    	response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
	    
		try {
			JSONObject json = new JSONObject();
			json.put("orderNumber", orderNumber);
			json.put("dfType", "TX");
//			RabbitmqSend.sendMessage(json.toString());
//			Runtime.getRuntime().exec("java -jar /daifu.jar > /log.log");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setFeeBalance(String.valueOf(Integer.parseInt(WalletBalance) - Integer.parseInt(request.getAmount())));
    	response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
	}
	
	
	/**
	 *   提现列表
	 * @param user
	 * @param request
	 * @param response
	 */
	public void DrawMoneylist(TabLoginuser user , RequestData request , ResponseData response){
		
		String tradeDate = request.getTradeDate();
		
		log.info("用户：" + user.getLoginID() + "查询月份为：" + tradeDate + "提现记录");
		
		StringBuffer sbf = new StringBuffer(tradeDate);

        sbf.insert(4,"-");
        log.info("插入 - 的日期:" + sbf.toString());
		
		// 当前页数
		Integer page = request.getPage();
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
				
		List<Map<String,String>> list = WithdrawDB.getTxRecordList(user.getID(), sbf.toString() , page, pageSize);
		
		String totalAmount = UserProfitDB.monthProfitTotalAmount(new Object[]{ user.getID() ,tradeDate});
		
		response.setList(JSONArray.fromObject(list).toString());
		response.setTotal(totalAmount);
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
