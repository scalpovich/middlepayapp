package com.rhjf.appserver.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

/**
 * @author hadoop
 */
@Service
public class PayQueryService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void payQuery(PayOrder order , Map<String,Object> merchantMap){
		
		
		logger.info("查询订单:" + order.getOrderNumber() + "代付状态"); 
		
		
		String url = LoadPro.loadProperties("http", "PayQueryUrl");
		
		String signkey = UtilsConstant.ObjToStr(merchantMap.get("QueryKey"));
		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		
		map.put("trxType", "OnlineQuery");
		map.put("r1_merchantNo", order.getMerchantID());
		map.put("r2_orderNumber", order.getOrderNumber());
		
		StringBuffer sbf = new StringBuffer("#");
		for (String key : map.keySet()) {
			sbf.append(map.get(key));
			sbf.append("#");
		}
		
		String str = sbf.append(signkey).toString();
		
		map.put("sign", MD5.sign(str, StringEncoding.UTF_8));
		
		try {
			String content = HttpClient.post(url, map, "1");
			logger.info("订单:" + order.getOrderNumber() + "响应报文:" + content);
			
			JSONObject json = JSONObject.fromObject(content);
			
			String retCode = json.getString("retCode");
			
			if(retCode.equals(Constant.payRetCode)){
				
				String withdrawStatus = json.getString("r9_withdrawStatus");
				
				/** 提现状态成功  **/
				if(withdrawStatus.equals(Constant.orderStatus)){
					logger.info("");
					TradeDAO.updateWithdrawStatus(new Object[]{ retCode , "成功-承兑或交易成功" ,order.getID()});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
