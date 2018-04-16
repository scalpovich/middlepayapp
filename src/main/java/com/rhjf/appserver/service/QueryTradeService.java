package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.MposDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class QueryTradeService {

	
	
	
	
	/**
	 *   交易查询
	 * @param loginUser
	 * @param reqData
	 * @param respData
	 */
	public void QueryTrade( LoginUser loginUser , RequestData reqData , ResponseData respData){
		
		// 当前页数
		Integer page = reqData.getPage();
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
		
		List<Map<String,Object>> list = null;
		
		Integer count = 0;
		
		JSONObject amountJSON = null;
		
		if("6".equals(reqData.getPayChannel())){
			list = MposDAO.mposTradeList(new Object[]{loginUser.getID()  , page*pageSize , pageSize});
			count  = MposDAO.userQueryTradeCount(new Object[]{loginUser.getID() });
			amountJSON = MposDAO.userQueryTradeTotal(new Object[]{loginUser.getID() });
			
		}else{
			list = TradeDAO.userQueryTradeList(new Object[]{loginUser.getID() , reqData.getPayChannel() , page*pageSize , pageSize});
			count  = TradeDAO.userQueryTradeCount(new Object[]{loginUser.getID() , reqData.getPayChannel()});
			amountJSON = TradeDAO.userQueryTradeTotal(new Object[]{loginUser.getID() , reqData.getPayChannel()});
		}
		
		
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
		respData.setTranslist(jsonArray.toString()); 
		respData.setPage(page + 1);
		respData.setTotalCount(count);
		
		respData.setToday(amountJSON.getString("today"));
		respData.setTotal(amountJSON.getString("total"));
		
		respData.setRespCode(RespCode.SUCCESS[0]);
		respData.setRespDesc(RespCode.SUCCESS[1]);
	}
}
