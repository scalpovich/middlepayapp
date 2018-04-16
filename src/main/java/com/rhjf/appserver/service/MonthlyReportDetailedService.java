package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

/**
 *    月报详细数据
 * @author hadoop
 *
 */
public class MonthlyReportDetailedService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	
	public void MonthlyReportDetailed(LoginUser user,RequestData request , ResponseData response){
		
		/**  支付类型  **/
		String paychannel = request.getPayChannel();
		/**   交易年月   **/
		StringBuffer stringBuffer = new StringBuffer(request.getTradeDate());	
		
		if(request.getTradeDate().length() < 6){
			stringBuffer.insert(4, "0");
		}
		
		
		Integer page = request.getPage();
		Integer pageSize = 20;
		
		if(page == null){
			pageSize = 200;
			page = 0;
		}
		
		
		List<Map<String,Object>> list = PayOrderDAO.monthlyReportDetailed(user.getID(), stringBuffer.toString(), paychannel ,  page*pageSize , pageSize);
		
		
		Integer count  = PayOrderDAO.monthlyReportDetailedCount(user.getID(), stringBuffer.toString(), paychannel);
		
		
		response.setList(JSONArray.fromObject(list).toString());
		response.setPage(page + 1);
		response.setTotalCount(count);
		
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
