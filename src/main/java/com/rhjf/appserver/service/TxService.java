package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.WithdrawDepositDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

public class TxService {
	
	LoggerTool logger = new LoggerTool(this.getClass());

	public void send(LoginUser loginuser,RequestData reqData , ResponseData repData){
		if(reqData.getAmount()==null ){
			logger.info("提现金额为空");
			repData.setRespCode(RespCode.TXAMOUNTError[0]);
			repData.setRespDesc(RespCode.TXAMOUNTError[1]);
	    	return  ;
		}
		if(Long.parseLong(reqData.getAmount())==0 ){
			logger.info("提现金额无效");
			repData.setRespCode(RespCode.TXAMOUNTError[0]);
			repData.setRespDesc(RespCode.TXAMOUNTError[1]);
	    	return  ;
		}
		
		
		//提现
		int nRet= WithdrawDepositDAO.tx(loginuser.getID(), reqData.getAmount(),reqData.getSendSeqId(),reqData.getTxType());
		if(nRet==2){
			logger.info("余额不足");
			repData.setRespCode(RespCode.TXAMOUNTNOTENOUGH[0]);
			repData.setRespDesc(RespCode.TXAMOUNTNOTENOUGH[1]);
	    	return  ;
		}
		else if(nRet==3 || nRet==4){
			logger.info("系统故障,nRet="+nRet);
			repData.setRespCode(RespCode.ServerDBError[0]);
			repData.setRespDesc(RespCode.ServerDBError[1]);
	    	return  ;
		}
		else if(nRet==1){
			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
		}
		else{
			repData.setRespCode(RespCode.ServerDBError[0]);
			repData.setRespDesc(RespCode.ServerDBError[1]);
		}
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.remove(Constant.cacheName, loginuser.getLoginID() + "UserInfo");
	}
	
	public void getTxRecord(LoginUser loginuser,RequestData reqData , ResponseData repData){
		
		// 当前页数
		Integer page = reqData.getPage();
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
		
		List<Map<String, Object>> list = WithdrawDepositDAO.getTxRecordList(loginuser.getID(), reqData.getTxType(), page*pageSize , pageSize);
		
		Integer count = WithdrawDepositDAO.getTxRecordCount(loginuser.getID(), reqData.getTxType());
		
		repData.setList(JSONArray.fromObject(list).toString());
		repData.setPage(page + 1);
		repData.setTotalCount(count);
		repData.setRespCode(RespCode.SUCCESS[0]);
		repData.setRespDesc(RespCode.SUCCESS[1]);
	}

}
