package com.rhjf.appserver.util;

import java.util.Map;
import java.util.TreeMap;

import com.rhjf.appserver.constant.Constant;

import net.sf.json.JSONObject;

public class OpenProductUtil {

	
	static LoggerTool log = new LoggerTool(OpenProductUtil.class);
	
	public static JSONObject openProduct(String productType , String MerchantID , String T0SaleRate , String T1SaleRate){
		
		log.info("商户号: " + MerchantID + "开通商品：" + productType); 
		
		String OPENURL = LoadPro.loadProperties("http", "OPENPRODUCT_URL");
		
		Map<String,Object> obj = new TreeMap<String,Object>();
		
		obj.put("productType", productType);
		obj.put("merchantNo",MerchantID);
		obj.put("t0Fee", Double.valueOf(T0SaleRate)/10.0);
		obj.put("t1Fee", Double.valueOf(T1SaleRate)/10.0);
		
		log.info("开通产品需要签名的字符串：" +  JSONObject.fromObject(obj).toString() + Constant.REPORT_SIGN_KEY); 
		
		String sign = MD5.sign(JSONObject.fromObject(obj).toString() + Constant.REPORT_SIGN_KEY, "utf-8").toUpperCase();
		obj.put("sign", sign);
		
		
		log.info("商户：" + MerchantID + "开通产品请求报文:" + obj.toString());
		
		String content =  HttpClient.post(OPENURL, obj, null);
		
		log.info("商户：" + MerchantID + "开通产品响应报文：" + content); 
		
		JSONObject result_obj = JSONObject.fromObject(content);
		
		return result_obj;
	}
}
