package com.rhjf.appserver.service.jifu;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.OpenKuaiDAO;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.db.UserBankCardDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.service.creditcard.KuaiTradeInterfaceService;
import com.rhjf.appserver.util.AESCBCUtil;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hadoop
 * @version 1.0 2018年3月19日 - 下午2:25:38
 */
public class KuaiTradeService implements KuaiTradeInterfaceService {

	private LoggerTool log = new LoggerTool(this.getClass());



	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rhjf.appserver.service.creditcard.KuaiTradeInterfaceService#send(com.rhjf.appserver.model.LoginUser, com.rhjf.appserver.model.RequestData,
	 * com.rhjf.appserver.model.ResponseData)
	 */
	@Override
	public void send(LoginUser user, RequestData reqData, ResponseData repData) {

		JSONObject head = new JSONObject();

		String txnCode = "312001";

		Map<String, Object> termKey = TermKeyDAO.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");

		String bankCardNo;
		try {
			log.info("用户：" + user.getLoginID() + "请求无卡快捷支付请求 , 银行卡卡号：(密文)" + reqData.getBankCardNo());
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardNo = DES3.decode(reqData.getBankCardNo(), desckey);
			log.info("用户：" + user.getLoginID() + "请求无卡快捷支付请求 , 银行卡卡号：(原文)" + bankCardNo);

		} catch (Exception e) {
			e.printStackTrace();

			log.error("卡号加密异常", e);

			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}

		/** 订单号 **/
		String orderNumber = UtilsConstant.getOrderNumber();

		/** 终端发起的交易日期 **/
		String tradeDate = reqData.getSendTime().substring(0, 8);
		/** 终端发起的交易时间 **/
		String tradeTime = reqData.getSendTime().substring(8);


		Map<String,Object> userconfig  = TradeDAO.getUserConfig(new Object[]{ user.getID() , reqData.getPayChannel()});

		/** 向数据库插入初始化数据 **/
		int ret = TradeDAO.tradeInit(new Object[]{UtilsConstant.getUUID(),reqData.getAmount() ,
				DateUtil.getNowTime(DateUtil.yyyyMMdd),DateUtil.getNowTime(DateUtil.HHmmss),
				tradeDate,tradeTime , reqData.getSendSeqId(), "无卡快捷" ,
				"T0", user.getID(),reqData.getPayChannel() , userconfig.get("T0SaleRate") ,JifuConstant.merchantNo ,orderNumber , "" ,bankCardNo , user.getAgentID() , "JIFU"});

		if(ret < 1 ){
			log.info("数据库保存信息失败");
			repData.setRespCode(RespCode.ServerDBError[0]);
			repData.setRespDesc(RespCode.ServerDBError[1]);
			return ;
		}


		Map<String,Object> bankCardnoMap =  OpenKuaiDAO.getOpenKuai(new Object[]{bankCardNo});


		String nowDate = DateUtil.getNowTime(DateUtil.yyyyMMdd);

		String nowTime = DateUtil.getNowTime(DateUtil.yyyyMMddHHmmss);


		head.put("version", "1.0.0");
		head.put("charset", "UTF-8");
		head.put("partnerNo", JifuConstant.merchantNo);
		head.put("partnerType", "OUTER");
		head.put("txnCode", txnCode);
		head.put("orderId", orderNumber);
		head.put("reqDate", nowDate);
		head.put("reqTime", nowTime);

		log.info("head :" + head.toString());

		JSONObject json = new JSONObject();

//	        String mobile = "18611769740";
//	        String IdCard = "130823199105284010";
//	        String cvv2 = "649";
//	        String calidity = "0819";
//	        String cardHolderName = "张志国";

//	        String payBankCardNo = "6225760013991968";
//	        String inBankCardNo = "6214920207968049";

		Map<String,Object> bankMap = UserBankCardDAO.getBankInfo(user.getID());

		json.put("head", head);
		json.put("CutDate", nowDate);
		json.put("NotifyUrl", JifuConstant.notifyUrl);
		json.put("TxnAmt", reqData.getAmount());
		json.put("t0TxnRate", AmountUtil.div(UtilsConstant.ObjToStr(userconfig.get("T0SaleRate")), "1000", 4) );
		json.put("t0TxnFee", 50);
		json.put("cardHolderName", user.getName());
		json.put("Mobile", reqData.getPayerPhone());
		json.put("IdCard", user.getIDCardNo());
		json.put("cvv2", bankCardnoMap.get("cvn2"));
		json.put("Validity", bankCardnoMap.get("expired"));
		json.put("payBankCardNo", bankCardNo);

		json.put("inBankCardNo", bankMap.get("AccountNo").toString());

		json.put("payBankCode",JifuConstant.map.get(UtilsConstant.ObjToStr(bankCardnoMap.get("bankSymbol"))) );

		json.put("inBankCode", JifuConstant.map.get(UtilsConstant.ObjToStr(bankMap.get("BankSymbol"))));
		json.put("Remark", user.getMerchantName());

		log.info("加密原文：" + json.toString());

		String plainText = null;
		try {
			plainText =  AESCBCUtil.encrypt(json.toString(), JifuConstant.aesKey.getBytes(), JifuConstant.aesKey.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("aes 加密结果:" + plainText);

		String encryptData = MD5.getSha1(json.toString() + JifuConstant.signKey);

		log.info("sha1 结果：" + encryptData);

		Map<String, Object> map = new HashMap<>(16);

		map.put("encryptData", plainText);
		map.put("partnerNo", JifuConstant.merchantNo);
		map.put("signData", encryptData);
		map.put("orderId", orderNumber);
		map.put("ext", user.getMerchantName());

		log.info("请求报文“： " + JSONObject.fromObject(map).toString());

		String content = null;
		log.info("请求地址：" + JifuConstant.url + txnCode);
		content = HttpClient.post(JifuConstant.url + txnCode , map, "1");
		log.info("请求返回报文：" + content);

		JSONObject resultJSON = JSONObject.fromObject(content);

		String resultEncryptData = resultJSON.getString("encryptData");
		String signature = resultJSON.getString("signature");

		String text = AESCBCUtil.decrypt(resultEncryptData , JifuConstant.aesKey.getBytes() , JifuConstant.aesKey.getBytes());
		String resultSign = MD5.getSha1(text + JifuConstant.signKey);

		log.info("响应报文明文：" + text);
		log.info("响应签名："  + signature  + " , 计算签名：" + resultSign);

		resultJSON = JSONObject.fromObject(text);

		head = JSONObject.fromObject(resultJSON.getString("head"));
		String respCode = head.getString("respCode");

		if("000000".equals(respCode)){
			log.info("订单：" + orderNumber  + " ， 成功");
			String workId = head.getString("workId");

			TradeDAO.updateOrderTransactionId(orderNumber , workId);

			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
			repData.setOrderNumber(orderNumber);
			repData.setMerchantNo(workId);

		}else{
			repData.setRespCode(respCode);
			repData.setRespDesc(head.getString("respMsg"));
		}
	}
	
	
	
	public void confirm(LoginUser loginUser,RequestData reqData , ResponseData repData){
		JSONObject head = new JSONObject();

		String txnCode = "312002";

		String orderID = reqData.getOrderNumber();
		String workId = reqData.getMerchantNo();

		String nowDate = DateUtil.getNowTime(DateUtil.yyyyMMdd);
		String nowTime = DateUtil.getNowTime(DateUtil.yyyyMMddHHmmss);

		head.put("version", "1.0.0");
		head.put("charset", "UTF-8");
		head.put("partnerNo", JifuConstant.merchantNo);
		head.put("partnerType", "OUTER");
		head.put("txnCode", txnCode);
		head.put("orderId", orderID);
		head.put("reqDate", nowDate);
		head.put("reqTime", nowTime);

		log.info("head :" + head.toString());

		JSONObject json = new JSONObject();

		json.put("head", head);
		json.put("workId", workId);

		String smsCode = reqData.getSmsCode();

		log.info("获取短信验证码为：" + smsCode);

		json.put("smsCode", smsCode);
		log.info("加密原文：" + json.toString());

		String plainText = null;
		try {
			plainText = AESCBCUtil.encrypt(json.toString(), JifuConstant.aesKey.getBytes(), JifuConstant.aesKey.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("aes 加密结果:" + plainText);

		String encryptData = MD5.getSha1(json.toString() + JifuConstant.signKey);

		log.info("sha1 结果：" + encryptData);

		Map<String, Object> map = new HashMap<>(16);

		map.put("encryptData", plainText);
		map.put("partnerNo", JifuConstant.merchantNo);
		map.put("signData", encryptData);
		map.put("orderId", orderID);
		map.put("ext", loginUser.getMerchantName());

		log.info("请求报文“： " + JSONObject.fromObject(map).toString());

		String content = null;
		log.info("请求地址：" + JifuConstant.url + txnCode);

		content = HttpClient.post(JifuConstant.url + txnCode, map, "1");
		log.info("支付返回报文：" + content);

        JSONObject resultJSON = JSONObject.fromObject(content);

        String resultEncryptData = resultJSON.getString("encryptData");
        String signature = resultJSON.getString("signature");

        String text = AESCBCUtil.decrypt(resultEncryptData, JifuConstant.aesKey.getBytes(), JifuConstant.aesKey.getBytes());
        String resultSign = MD5.getSha1(text + JifuConstant.signKey);

        log.info("响应报文明文：" + text);
        log.info("响应签名：" + signature + " , 计算签名：" + resultSign);
        
        head = JSONObject.fromObject(JSONObject.fromObject(text).getString("head"));
        String respCode = head.getString("respCode");

        if("000000".equals(respCode)){
            log.info("订单：" + orderID  + " ， 成功");
            repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
            
        }else{
        	repData.setRespCode(respCode);
			repData.setRespDesc(head.getString("respMsg"));
        }
	}
}
