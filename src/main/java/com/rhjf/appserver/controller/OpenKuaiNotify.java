package com.rhjf.appserver.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.service.OpenKuaiNotifyService;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/OpenKuaiNotify")
public class OpenKuaiNotify {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	@Autowired
	private OpenKuaiNotifyService openKuaiNotifyService;
	
	@RequestMapping("")
	@ResponseBody
	public Object openKuaiNotify(HttpServletRequest request){
		
		Map<String,String> map2 = new HashMap<String,String>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map2.put(paramName, paramValue);
				}
			}
		}
		if(map2==null||map2.isEmpty()){
			logger.info("回调报文为空");
			return  RespCode.notifyfail;
		}
		logger.info("接收上游回调, 回调内容:" + map2.toString());

		JSONObject json = JSONObject.fromObject(map2);

		String respCode = json.getString("respCode");

		if (Constant.payRetCode.equals(respCode)) {
			String orderNum = json.getString("orderNum");

			int x = openKuaiNotifyService.updateOpenKuaiStatus(new Object[] { orderNum });

			if (x < 1) {
				return "fail";
			}
		}

		return Constant.orderStatus;
	}

}
