package com.rhjf.appserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.AppconfigDB;
import com.rhjf.appserver.db.OpenKuaiDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author hadoop
 */
public class KuaiCardListService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	
	@SuppressWarnings("unchecked")
	public void kuaiCardList(TabLoginuser user,RequestData reqData , ResponseData repData){
		
		
		logger.info("-----用户：" + user.getLoginID() + "请求开通快捷银行卡列表");
		
		
		Object obj = null;
		//  查询交易配置信息
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Map<String,Object> tradeConfig = null;
		obj = ehcache.get(Constant.cacheName, "tradeConfig");
		if(obj == null){
			logger.info("缓存中获取交易配置信息失败,从数据库中查询");
			tradeConfig = AppconfigDB.getTradeConfig(); 
		}else{
			logger.info("缓存查询交易配置信息");
			tradeConfig = (Map<String,Object>) obj;
		}
		
		String nowtime = DateUtil.getNowTime(DateUtil.HHmm);
		
		Integer nowHour = Integer.parseInt(nowtime.split("-")[0]);
		
		
		String KuaiT0Time = UtilsConstant.ObjToStr(tradeConfig.get("KuaiT0Time"));
		
		/** T0交易开始小时 **/
		Integer startHour = Integer.parseInt(KuaiT0Time.split("-")[0]);
		/** T0交易结束小时 **/
		Integer EndHour = Integer.parseInt(KuaiT0Time.split("-")[1]);
		
		
		Integer t0Result = 1;
		
		if(nowHour>=startHour&&nowHour<EndHour){
			t0Result = 0;
		}
		
		List<Map<String,Object>> noOpenList = OpenKuaiDB.kuaiCardlistNoOpen(new Object[]{user.getID()});
		if(noOpenList!=null && noOpenList.size() > 0){
			
			/**  获取交易商户  **/
			Map<String,Object> merchantMap = TradeDB.getMerchantInfo(new Object[]{user.getID() , "4"}); 

//			Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
//			String initKey = LoadPro.loadProperties("config", "DBINDEX");
			
			List<Object[]> objlist = new ArrayList<Object[]>();
			
			for (int i = 0; i < noOpenList.size() ; i++) {
				
				Map<String,Object> queryMap = new TreeMap<>();
				
				String bankCardno = "";
				try {
					bankCardno = UtilsConstant.ObjToStr(noOpenList.get(i).get("bankCardNo"));
					logger.info("用户：" + user.getLoginID() + "请求查询无卡快捷支付请求 , 银行卡卡号：(原文)" + bankCardno);
					queryMap.put("accNo",  DESUtil.encode(Constant.REPORT_DES3_KEY , bankCardno));
				} catch (Exception e) {
					e.printStackTrace();
					repData.setRespCode(RespCode.SYSTEMError[0]);
					repData.setRespDesc(RespCode.SYSTEMError[1]);
					return ;
				}
				
				queryMap.put("merchantNo" ,merchantMap.get("MerchantID"));
				
				String querysign = MD5.sign(JSONObject.fromObject(queryMap).toString() + Constant.REPORT_QUERY_KEY , StringEncoding.UTF_8);
				queryMap.put("sign", querysign.toUpperCase());
				
				logger.info("银行卡卡号：" + bankCardno + "查询是否开通无卡快捷报文：" + queryMap.toString());
				
				logger.info("查询银行卡号:" + bankCardno + "是否开通无卡快捷");
				String queryContent = HttpClient.post(LoadPro.loadProperties("http", "OPENKUAIQUERY_URL"), queryMap, null); 
				
				logger.info("查询银行卡号:" + bankCardno + "是否开通无卡快捷 响应报文：" + queryContent);
	 			
				JSONObject queryjson = JSONObject.fromObject(queryContent);
				
				String respCode = queryjson.getString("respCode");
				
				if(Constant.payRetCode.equals(respCode)){
					String result = queryjson.getString("result");
					if(!"0".equals(result)){
						Object[] params = new Object[]{result , user.getID() , bankCardno};
						objlist.add(params);
					}
				}
			}
			if(objlist.size() > 0){
				OpenKuaiDB.updateBankCardResult(objlist);
			}
		}
		
		List<Map<String,Object>> list = OpenKuaiDB.kuaiCardlist(new Object[]{user.getID() });
		
		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
		logger.info("卡号列表：" + JSONArray.fromObject(list));
		
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			JSONArray array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject json = new JSONObject();
				Map<String,Object> map = list.get(i);
				json.put("cardno", DES3.encode(UtilsConstant.ObjToStr(map.get("bankCardNo")), desckey));
				/**
				 * 	代表是否可做交易 0 可做交易， 1 不可做交易
				 * 
				 * 	0 未开通 
				 * 	1 开通T0 
				 * 	2 开通T1 
				 * 	3 T1 与T0 都开通
				 * 
				 */
//				String encrypt = UtilsConstant.ObjToStr(map.get("encrypt"));
				String status = "0";
				
//				if("0".equals(encrypt)){
//					status = "0";
//				}
				
//				if(t0Result==1){
//					// t1 时间段
//					if("2".equals(encrypt)||"3".equals(encrypt)){
//						status = "0";
//					}
//				}else if(t0Result==0){
//					// t0 时间段
//					if("1".equals(encrypt)||"3".equals(encrypt)){
//						status = "0";
//					}
//				}
				logger.info("当前到账时间段： " +t0Result+ " , 卡号："+UtilsConstant.ObjToStr(map.get("bankCardNo")) + ",当前卡号交易状态：" + status);
				
				json.put("payerPhone", UtilsConstant.ObjToStr(map.get("payerPhone")));
				json.put("cardName", map.get("cardName").toString());
				json.put("bankCode", UtilsConstant.ObjToStr(map.get("bankCode")));
				json.put("status", status);
				json.put("bankName", UtilsConstant.ObjToStr( map.get("bankName")).substring(0, 4));
 				array.add(json); 
			}
			repData.setTranslist(array.toString()); 
			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
		} catch (Exception e) {
			e.printStackTrace();
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
	}
}
