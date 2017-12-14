package com.rhjf.appserver.util.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.db.AuthenticationDB;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

public class AuthUtil {
	
	
	private static LoggerTool log = new LoggerTool(AuthUtil.class);

//	public static Map<String,String> Auth(String name,String bankCardNo,String IDcardNumber){
//		 Map<String, Object> bankAuthencationMan = AuthenticationDB.bankAuthenticationInfo(new Object[]{bankCardNo});
//		 Map<String,String> reqMap=new HashMap<String,String>();
//		 if (bankAuthencationMan == null || bankAuthencationMan.isEmpty()){
//			 Map<String,String> authMap=new HashMap<String,String>();
//				AuthService authService = new AuthService();
//				authMap.put("accName", name);
//				authMap.put("cardNo", bankCardNo);
//				authMap.put("certificateNo", IDcardNumber);
//				reqMap=authService.authKuai(authMap);
//				System.out.println(reqMap.toString());
//				if(reqMap.get("respCode").equals(Author.SUCESS_CODE)){
//				AuthenticationDB.addAuthencationInfo(new Object[]{UtilsConstant.getUUID() , IDcardNumber , name , bankCardNo , "00" , reqMap.get("respMsg") });
//				}
//				return reqMap;
//		 }else{
//			 if (name.equals(bankAuthencationMan.get("RealName")) &&IDcardNumber.equals(bankAuthencationMan.get("IdNumber"))) {
//				 reqMap.put("respCode", Author.SUCESS_CODE);
//				 reqMap.put("respMsg","鉴权成功");
//	             return reqMap;
//	            }else{
//	            reqMap.put("respCode", "001");
//	            reqMap.put("respMsg", "鉴权信息不一致");
//		        return reqMap;
//	            }
//		 }
//	}
	
	
	public static Map<String, String> authentication(String name, String bankCardNo, String IDcardNumber ,  String payerPhone) {
		Map<String, Object> bankAuthencationMan = AuthenticationDB.bankAuthenticationInfo(new Object[] { bankCardNo });
		Map<String, String> reqMap = new HashMap<String, String>();
		if (bankAuthencationMan == null || bankAuthencationMan.isEmpty()) {
			try {
				Map<String,Object> map = new TreeMap<String,Object>();
				map.put("channelNo", Constant.REPORT_CHANNELNO);
				map.put("channelName", Constant.REPORT_CHANNELNAME);
				map.put("orderNo", UtilsConstant.getOrderNumber());
				map.put("cardNo", DESUtil.encode(Constant.REPORT_DES3_KEY,bankCardNo));
				if(!UtilsConstant.strIsEmpty(payerPhone)){
					map.put("mobile", payerPhone);
				}
				
				map.put("name", DESUtil.encode(Constant.REPORT_DES3_KEY,name));
				map.put("idNo", DESUtil.encode(Constant.REPORT_DES3_KEY,IDcardNumber));
				
				String sign = MD5.sign(JSONObject.fromObject(map) + Constant.REPORT_SIGN_KEY, "utf-8").toUpperCase();
				map.put("sign", sign);
				
				String url = LoadPro.loadProperties("http", "AUTH_URL");
				
				System.out.println(map.toString());	
				log.info("鉴权请求地址:" + url);
				log.info("鉴权请求报文：" + map.toString());
				
				String content = HttpClient.post(url, map, null);
				
				log.info("鉴权响应报文：" + content);
				
				JSONObject json = JSONObject.fromObject(content);
				
				if (json.getString("resCode").equals(Constant.payRetCode)) {
					AuthenticationDB.addAuthencationInfo(new Object[] { UtilsConstant.getUUID(), IDcardNumber, payerPhone ,name,bankCardNo, "00", reqMap.get("respMsg") });
					reqMap.put("respCode", Author.SUCESS_CODE);
					reqMap.put("respMsg", "鉴权成功");
					return reqMap;
				}else{
					reqMap.put("respCode", "001");
					reqMap.put("respMsg", json.getString("resMsg"));
					return reqMap;
				}
			} catch (Exception e) {
				log.error("鉴权出现异常：" + e.getMessage() , e);
			}
			return reqMap;
		} else {
			if (name.equals(bankAuthencationMan.get("RealName"))&& IDcardNumber.equals(bankAuthencationMan.get("IdNumber"))) {
				reqMap.put("respCode", Author.SUCESS_CODE);
				reqMap.put("respMsg", "鉴权成功");
				return reqMap;
			} else {
				reqMap.put("respCode", "001");
				reqMap.put("respMsg", "鉴权信息不一致");
				return reqMap;
			}
		}
	}
}
