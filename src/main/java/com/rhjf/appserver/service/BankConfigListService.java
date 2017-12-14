package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.BankConfigDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;

import net.sf.json.JSONArray;

public class BankConfigListService {

	
	public void BankConfigList(RequestData request ,  ResponseData response){
		
		List<Map<String,String>> list = BankConfigDB.getBankList();
		
		response.setList(JSONArray.fromObject(list).toString());

		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
