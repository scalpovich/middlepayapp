package com.rhjf.appserver.service.creditcard;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.db.UserCreditCardDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateJsonValueProcessor;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


/**
 *    用户信用卡列表
 * @author hadoop
 *
 */
public class CreditCardListService {
	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void  creditCardList(LoginUser user , RequestData request , ResponseData response){
		
		List<Map<String,Object>> list = UserCreditCardDAO.creditCardList(new Object[]{user.getID()});
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		//获取当前天数
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		try {
			
			Map<String, Object> termKey = TermKeyDAO.selectTermKey(user.getID());
			String initKey = LoadPro.loadProperties("config", "DBINDEX");
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);

			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map = list.get(i);
				
				String repayDate = map.get("repayDate").toString();
				
				map.put("bankCardNo" , DES3.encode(UtilsConstant.ObjToStr(map.get("bankCardNo")), desckey));
				
				int repay = Integer.parseInt(repayDate);
				if(repay >= day){
					map.put("difference", repay - day);
				}else{
					String nowtime = DateUtil.getNowTime(DateUtil.yyyyMMdd);
					String lastMonth = DateUtil.getPreMonth(nowtime);
					lastMonth = lastMonth.substring(0, 6) + "" + repay;
			        SimpleDateFormat format = new SimpleDateFormat(DateUtil.yyyyMMdd);
			        try {
						Date date2 = format.parse(lastMonth);
						Date date = format.parse(nowtime);
						
						long difference = DateUtil.differentDays(date, date2);
						map.put("difference", difference);
					} catch (ParseException e) {
						log.error("时间处理异常。" , e);
						map.put("difference" , 0);
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor(DateUtil.yyyy_MM_dd_HH_mm_ss));
        JSONArray jo = JSONArray.fromObject(list , jsonConfig);
        
        response.setList(jo.toString()); 
        response.setRespCode(RespCode.SUCCESS[0]);
        response.setRespDesc(RespCode.SUCCESS[1]);
	}
	
}
