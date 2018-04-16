package com.rhjf.appserver.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


/**
 * @author hadoop
 */
public class KuaiTradeService {
	LoggerTool log = new LoggerTool(this.getClass());

	public void send(LoginUser loginUser, RequestData reqData, ResponseData repData) {

		Map<String,Object> routeMap = null;
		
		try {
			Class<?> payRouteDBClass = Class.forName("com.rhjf.appserver.db.PayRouteDAO");
			Object obj = payRouteDBClass.newInstance();
			routeMap = (Map<String,Object>) payRouteDBClass.getMethod("routeMap", String.class).invoke(obj, reqData.getPayChannel());
			
			String channelID = UtilsConstant.ObjToStr(routeMap.get("ChannelID"));
			
			Class<?> kuaiTradeServiceClass = Class.forName("com.rhjf.appserver.service." + channelID.toLowerCase().trim() + ".KuaiTradeService");
			
			Method method = kuaiTradeServiceClass.getMethod("send", new Class[]{LoginUser.class ,RequestData.class , ResponseData.class });
			
			method.invoke(kuaiTradeServiceClass.newInstance(), loginUser , reqData , repData);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void confirm(LoginUser loginUser, RequestData reqData, ResponseData repData) {
		String orderNumber = reqData.getOrderNumber();
		PayOrder order = TradeDAO.getPayOrderInfo(orderNumber);
		
		try{
			Class<?> kuaiTradeServiceClass = Class.forName("com.rhjf.appserver.service." + order.getChannelID().trim().toLowerCase() + ".KuaiTradeService");
			Method method = kuaiTradeServiceClass.getMethod("confirm", new Class[]{LoginUser.class ,RequestData.class , ResponseData.class });
			method.invoke(kuaiTradeServiceClass.newInstance(), loginUser , reqData , repData);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
