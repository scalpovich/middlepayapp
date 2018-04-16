package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.UserProfitDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

/**
 *   用户收益列表查询
 * @author hadoop
 *
 */
public class UserProfitListService {

	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	
	public void UserProfitList(LoginUser user , RequestData request , ResponseData response){
		log.info("用户：" + user.getLoginID() + "查询类型为：" + request.getTradeCode() + "分润列表"); 
		
		// 当天页数
		Integer page = request.getPage();
		
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
		
		List<Map<String,String>> list = UserProfitDAO.profitlist(user.getID(), request.getTradeCode(), page*pageSize , pageSize);
		Integer count  = UserProfitDAO.profitCount(user.getID() ,  request.getTradeCode());
		
		log.info("用户：" + user.getLoginID() + "分润类型为：" + request.getTradeCode() + "的分润总条数为：" + count); 
		
		response.setList(JSONArray.fromObject(list).toString()); 
		response.setPage(page + 1);
		response.setTotalCount(count);
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
