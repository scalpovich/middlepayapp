package com.rhjf.appserver.service.creditcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.PayOrderDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;


/**
 *    查询信用卡还款记录
 * @author hadoop
 *
 */
public class CreditCardRepayRecordService {

	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void creditCardRepayRecord(TabLoginuser user , RequestData request , ResponseData response){
		
		
		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
		String bankCardno = "";
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(request.getCreditCardNo(), desckey);
			log.info("用户：" + user.getLoginID() + " 添加收款信用卡 ：  密文：" + request.getCreditCardNo() + " 原文，" + bankCardno); 
		} catch (Exception e) {
			log.error("卡号加密异常 ：" , e); 
			response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
		
		List<Map<String,Object>> recordList = PayOrderDB.creditCardRepayRecordList(new Object[]{bankCardno  , user.getID()});
		JSONArray array = new JSONArray();
		
		Map<String,List<Map<String,Object>>> map = new HashMap<>();
		
		for (int i = 0; i < recordList.size(); i++) {
			
			Map<String,Object> recordMap = recordList.get(i);
			
			String month =  recordMap.get("createMonth").toString();
			List<Map<String,Object>> list = map.get(month);
			if(list == null){
				list = new ArrayList<>();
			}
			recordMap.put("Amount", AmountUtil.sub(recordMap.get("OrderAmount").toString(),recordMap.get("fee").toString()) - 20); 
			recordMap.put("CreateDate", recordMap.get("CreateDate").toString());
			recordMap.put("BankSymbol", recordMap.get("BankSymbol").toString());
			recordMap.put("status" , "还款成功");
			list.add(recordMap);
			map.put(month, list);
		}
		
		for (String year : map.keySet()) {
			List<Map<String,Object>> data = map.get(year);
			JSONObject json = new JSONObject();
			json.put("date", year);
			json.put("content", JSONArray.fromObject(data));
			array.add(json.toString());
		}
		
		response.setList(array.toString());
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
		
	}
	
	
	
}
