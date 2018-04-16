package com.rhjf.appserver.service.creditcard;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AgentDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;


/**
 *   信用卡还款计算手续费
 * @author hadoop
 *
 */
public class ReckonCreditCardRepayFeeService {
	

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void reckonCreditCardRepayFee(LoginUser user , RequestData request , ResponseData response){
		
		log.info("用户计算信用卡换卡手续费 ， 用户登录账号：" + user.getLoginID() + " ， 交易金额：" + request.getAmount());
		
		String payChannel = "4";
		
		Map<String,Object> userConfig = TradeDAO.getUserConfig(new Object[]{user.getID() , payChannel});
		
		int fee = AgentDAO.makeFeeRounding(request.getAmount(), Double.valueOf(userConfig.get("T0SaleRate").toString())  , 0) + 20;
		int amount = Integer.parseInt(request.getAmount())-fee;
	
		log.info("用户：" + user.getLoginID() + "计算信用卡还款手续费 ， 交易金额：" + request.getAmount() + "手续费：" + fee + " ,  到账金额:" + amount);
		
		response.setFee(String.valueOf(fee));
		response.setFeeBalance(String.valueOf(amount)); 
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}

}
