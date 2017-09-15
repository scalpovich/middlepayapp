package com.rhjf.appserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

/**
 *     查询某天交易详细数据
 * @author hadoop
 *
 */
public class DayTradeDetailService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	/**
	 * 查询某天交易详细数据
	 * @param user
	 * @param request
	 * @param response
	 */
	public void DayTradeDetail(TabLoginuser user , RequestData request ,  ResponseData response){
		
		log.info("用户：" + user.getLoginID() + "查询：" + request.getTradeDate() + "交易详细");
		
		String tradeDate = request.getTradeDate();
		
		String userID = user.getID();
		
		List<Map<String,String>> tradelist = PayOrderDB.DayTradeDetail(new Object[]{userID , tradeDate});
		
		response.setList(JSONArray.fromObject(tradelist).toString());
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
	
	
	
	/**
	 *    查询日交易详细 显示折线数据
	 * @param user
	 * @param request
	 * @param response
	 */
	public void DayTradeLineChart(TabLoginuser user , RequestData request ,  ResponseData response){
		String tradeDate = request.getTradeDate();
		
		log.info("用户：" + user.getLoginID() + "请求折线数据. 查询日期:" + tradeDate );
		
//        StringBuffer sbf = new StringBuffer(tradeDate);
//
//        sbf.insert(4,"-");
//        sbf.insert(7,"-");
//        log.info("插入 - 的日期:" + sbf.toString());
		try {
			String startTime = DateUtil.getDateAgo(tradeDate , 3 , DateUtil.yyyyMMdd);
			log.info("查询的开始日期：" + startTime);
            log.info("查询的结束日期：" + tradeDate);
            
            
            List<Map<String,String>> list = PayOrderDB.DayTradeLineChart(new Object[]{user.getID() , startTime , tradeDate});
            
            if(tradeDate.equals(DateUtil.getNowTime(DateUtil.yyyyMMdd))){
            	Map<String,String> todayTrade = PayOrderDB.ToDayTradeLineChart(new Object[]{tradeDate , user.getID() , tradeDate});
            	Map<String,String> map = new HashMap<String,String>();
            	//s LocalDate , sum(Amount) as amount
            	map.put("LocalDate", todayTrade.get("LocalDate"));
            	map.put("amount", todayTrade.get("amount"));
            	list.add(map);
            	
            	response.setFee(todayTrade.get("fee"));
            	response.setTotalCount(Integer.parseInt(todayTrade.get("count")));
            	response.setTotal(todayTrade.get("amount"));
            	response.setPureProfit(todayTrade.get("profit"));
            }else{
            	
            	Map<String,String> todayTrade = PayOrderDB.DayTradeTotalData(new Object[]{ user.getID() , tradeDate});

            	response.setFee(todayTrade.get("fee"));
            	response.setTotalCount(Integer.parseInt(todayTrade.get("count")));
            	response.setTotal(todayTrade.get("amount"));
            	response.setPureProfit(todayTrade.get("profit"));
            }
            
        	response.setList(JSONArray.fromObject(list).toString());
        	
    		response.setRespCode(RespCode.SUCCESS[0]);
    		response.setRespDesc(RespCode.SUCCESS[1]);
            
		} catch (Exception e) {
			log.error("查询数据出现异常:", e); 
			response.setRespCode(RespCode.SYSTEMError[0]);
    		response.setRespDesc(RespCode.SYSTEMError[1]);
		}
	}
	
	
	
}