package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.BankCodeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.util.LoggerTool;

/**
 *     根据卡号获取银行名称
 * @author hadoop
 *
 */
public class BankBinInfoService {

	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void BankBinInfo(RequestData request ,ResponseData response){
		
		String bankCardNo = request.getBankCardNo();
		
		log.info("根据卡bin获取卡信息 ,接收到的卡bin 号码：" + bankCardNo);
		
		Map<String,Object> binMap = BankCodeDAO.bankBinMap(new Object[]{bankCardNo});
		
		if(binMap == null || binMap.isEmpty()){
			response.setRespCode("01");
			response.setRespDesc("暂不支持该银行卡类型"); 
			log.info("卡bin：" + bankCardNo + "未匹配到信息"); 
			return ;
		}
		
		response.setBankName(binMap.get("bankName").toString());
		response.setBankNo(binMap.get("bankCode").toString());
		response.setCardName(binMap.get("cardName").toString());
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
