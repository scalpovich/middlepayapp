package com.rhjf.appserver.service;

import java.util.List; 
import java.util.Map;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

/**
 *    用户查询费率
 * @author a
 *
 */
public class QueryUserFeeRateService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void QueryUserFeeRate(LoginUser user, RequestData reqdata,ResponseData respdata){
		
		logger.info("用户" + user.getLoginID() + "查询费率信息");
		
		List<Map<String,Object>> list = null;
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Object obj = ehcache.get(Constant.cacheName, user.getLoginID() + "FeeRate" );
		if(obj == null){
			logger.info(user.getLoginID() + "从数据库查询费率信息");
			list = TradeDAO.getUserFeeRate(new Object[]{user.getID()});
			ehcache.put(Constant.cacheName, user.getLoginID() + "FeeRate" , list);
		}else{
			logger.info("================== " + user.getLoginID() +"费率从缓存中查询");
			list = (List<Map<String,Object>>) obj;
		}
		
		logger.info("用户" + user.getLoginID() + "费率信息：" + JSONArray.fromObject(list));
		
		respdata.setTranslist(JSONArray.fromObject(list).toString()); 
		respdata.setRespCode(RespCode.SUCCESS[0]);
		respdata.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
