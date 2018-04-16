package com.rhjf.appserver.service;

import java.util.List; 
import java.util.Map;
import java.util.Set;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.UserProfitDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *    个人收益记录查询
 * @author a
 *
 */
public class QueryProfitService {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void QueryProfit(LoginUser user , RequestData reqData , ResponseData respData){
		
		logger.info("用户：" +  user.getLoginID() + "查询个人收益记录");
		
		// 当天页数
		Integer page = reqData.getPage();
		
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
		
		List<Map<String,Object>> list = UserProfitDAO.userQueryProfitList(new Object[]{user.getID(), page*pageSize , pageSize});
		
		Integer count  = UserProfitDAO.userQueryProfitCount(new Object[]{user.getID()});
		
		
		JSONArray  jsonArray = new JSONArray();
		
		for (int i = 0; i < list.size(); i++){
			JSONObject json = new JSONObject();
			Map<String,Object> map = list.get(i);
			Set<String> keys = map.keySet();
			for (String key : keys) {
				json.put(key, map.get(key));
			}
			jsonArray.add(json);
		}
		
		respData.setSendTime(reqData.getSendTime());
		respData.setSendSeqID(reqData.getSendSeqId()); 
		respData.setTxndir(reqData.getTxndir());
		respData.setTranslist(jsonArray.toString()); 
		respData.setPage(page + 1);
		respData.setTotalCount(count);
		respData.setRespCode(RespCode.SUCCESS[0]);
		respData.setRespDesc(RespCode.SUCCESS[1]);
	}

}
