package com.rhjf.appserver.service;

import java.util.List; 
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;

/**
 *    交易月报详细  上端显示三个月的曲线图
 * @author hadoop
 *
 */
public class TradingCurveService {

	LoggerTool  logger = new LoggerTool(this.getClass());
	
	public void TradingCurve(TabLoginuser user , RequestData request , ResponseData response){
		
		
		/** 查询月报的时间 年月 20176 **/
		String tradeDate = request.getTradeDate();
		
		
		int year = Integer.parseInt(tradeDate.substring(0, 4));
		int month = Integer.parseInt(tradeDate.substring(4)); 
		
		String startDate = null , endDate = null;
		
		if(month > 3){
			startDate = year + "0" + (month-3);
			endDate = year + "" + (month-1<10?"0"+(month-1):(month-1));
		}else{
			int newyear = year;
			int newmonth = month;
			if(month - 3 < 1 ){
				month = month + 12;
				year = year - 1;
			}
			startDate = year + "" + (month-3<10?"0"+(month-3):(month-3));
			
			if(newmonth - 1 <= 0){
				newmonth = newmonth + 12;
				newyear = newyear - 1;
			}
			endDate = newyear + "" + (newmonth-1 < 10?"0"+(newmonth-1):(newmonth-1));
		}
		
		List<Map<String,String>> list = PayOrderDB.tradingCurvelist(new Object[]{user.getID(),startDate , endDate});
		
		StringBuffer stringBuffer = new StringBuffer(tradeDate);	
		
		if(tradeDate.length() < 6){
			stringBuffer.insert(4, "0");
		}
		//  sum(Amount) as amount , sum(Fee) as fee , count(1) as count , sum(amount)-sum(fee) as pureProfit 
		Map<String,Object> map = PayOrderDB.tradeTotal(new Object[]{user.getID(),stringBuffer.toString()});
		
		response.setList(JSONArray.fromObject(list).toString());
		response.setTotalCount(Integer.parseInt(map.get("count").toString()));
		response.setTotal(map.get("amount").toString());
		response.setFee(map.get("fee").toString()); 
		response.setPureProfit(map.get("pureProfit").toString()); 
		
		
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
}
