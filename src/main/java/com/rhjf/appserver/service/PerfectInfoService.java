package com.rhjf.appserver.service;

import java.util.ArrayList; 
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.db.BankCodeDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.auth.AuthUtil;
import com.rhjf.appserver.util.auth.Author;

import net.sf.json.JSONObject;

/**
 *   终端用户完善资料
 * @author a
 *
 */
public class PerfectInfoService {
	
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void PerfectInfo(TabLoginuser user , RequestData reqData , ResponseData respData) throws Exception{

		
		boolean flag = LoginUserDB.merchantPortalStatus(user.getLoginID());
		
		if(flag){
			logger.info("商户：" + user.getLoginID() + "已经入网成功");
			respData.setRespCode("F001");
			respData.setRespDesc("");
			return ;
		}
		
		// 商户名称
		String merchantName = reqData.getMerchantName();
		
		flag = UtilsConstant.checkMerchantName(merchantName);
		if(flag){
			logger.info("用户："+ user.getLoginID() + "商户名不合法," + merchantName);
			
			respData.setRespDesc(RespCode.MerchantNameError[0]);
			respData.setRespDesc(RespCode.MerchantNameError[1]);
			return ;
		}
		
		// 真实性名
		String name = reqData.getName();
		// 身份证号
		String IDcardNumber = reqData.getIdNumber();
		// 银行名称
//		String bankName = reqData.getBankName();
		// 支行名称
		String bankSubbranch = reqData.getBankSubbranch();
		// 银行卡号
		String bankCardNo = reqData.getBankCardNo();
		// 所在省份
		String state = reqData.getState();
		// 所在城市
		String city = reqData.getCity();
		
		if(city.contains("北京")){
			state = city;
		}else if (city.contains("上海")){
			state = city;
		}else if (city.contains("重庆")){
			state = city;
		}else if (city.contains("天津")){
			state = city;
		}
		
		// 所在区
		String county = reqData.getCounty();
		// 详细地址
		String address = reqData.getAddress() + "" + UtilsConstant.ObjToStr(reqData.getHouseNumber());
		// 邮箱
		String email = "";
		// 营业执照号
		String businessLicense = UtilsConstant.RandCode();
		// 结算人信用卡
		String creditCardNo = reqData.getCreditCardNo();
		// 结算账户性质 对公或对私
		String bankType = "TOPRIVATE";
		// 开户行省份
		String bankProv = reqData.getBankProv();
		// 开户行城市
		String bankCity = reqData.getBankCity();
		// 商户类型
		String merchantType = "PERSON";

		// 银联号   bankName,unite_bank_no
		Map<String,Object> bankmap = BankCodeDB.bankBinMap(new Object[]{bankCardNo});
		if(bankmap == null){
			
			respData.setRespCode(RespCode.BankCardInfoErroe[0]);
			respData.setRespDesc(RespCode.BankCardInfoErroe[1]);
			return ;
		}
		String bankName = UtilsConstant.ObjToStr( bankmap.get("bankName")); 
		String bankCode = "";
		String bankSymbol = "";
		
		int x = LoginUserDB.h5updateUserInfo(new Object[]{merchantType,name,IDcardNumber,merchantName,state,
				city,county,businessLicense,address,email,merchantName,name,user.getLoginID()});
		
		//  根据银行名称 开户行省份 城市 等信息 查询分行名称信息表 
		Map<String,Object> bankinfomap =  BankCodeDB.bankInfo(new Object[]{bankName,bankSubbranch,bankProv,bankCity});
		if(bankinfomap!=null){
			logger.info("查询到的银行信息:" + bankinfomap.toString()); 
			bankCode = bankinfomap.get("BankCode").toString();
			bankSubbranch = bankinfomap.get("BankBranch").toString();
			bankName = bankinfomap.get("BankName").toString();
			bankSymbol = bankinfomap.get("BankSymbol").toString();
		}else{
			bankinfomap = BankCodeDB.bankInfo(new Object[]{bankName,"分行营业部",bankProv,bankCity});
			if(bankinfomap!=null){
				logger.info("查询到的银行信息:" + bankinfomap.toString());
				bankCode = bankinfomap.get("BankCode").toString();
				bankSubbranch = bankinfomap.get("BankBranch").toString();
				bankName = bankinfomap.get("BankName").toString();
				bankSymbol = bankinfomap.get("BankSymbol").toString();
			}else{
				bankinfomap = BankCodeDB.bankInfo(new Object[]{bankName, bankCity + "分行",bankProv,bankCity});
				if(bankinfomap!=null){
					logger.info("查询到的银行信息:" + bankinfomap.toString());
					bankCode = bankinfomap.get("BankCode").toString();
					bankSubbranch = bankinfomap.get("BankBranch").toString();
					bankName = bankinfomap.get("BankName").toString();
					bankSymbol = bankinfomap.get("BankSymbol").toString();
				}else{
					logger.info("没有查询到银行信息，或返回多条数据，将不进行匹配");
				}
			}
		}
		
		LoginUserDB.saveOrUpBankInfo(new Object[]{UtilsConstant.getUUID(),user.getID(),name,bankCardNo,bankSubbranch,
				bankProv,bankCity,bankCode,bankName, bankSymbol ,creditCardNo,bankType,reqData.getPayerPhone() ,name,
				bankCardNo,bankSubbranch,bankProv,bankCity,bankCode,bankName,bankSymbol,creditCardNo,bankType,reqData.getPayerPhone()});
		
		List<Map<String,Object>> payChannelList = TradeDB.getPayChannel();
		
		List<Object[]> userConfig = new ArrayList<Object[]>();
		for (Map<String, Object> map : payChannelList) {
			Object[] obj = new Object[]{UtilsConstant.getUUID(),user.getID(),map.get("ID"),0,0,Constant.FeeRate ,Constant.FeeRate
					,Constant.SettlementRate,Constant.SettlementRate};
			userConfig.add(obj);
		}
		
		TradeDB.saveUserConfig(userConfig);
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
		
		Map<String,Object> BKmap = BankCodeDB.bankBinMap(new Object[]{bankCardNo});
		if(BKmap!=null&&"CREDIT_CARD".equals(UtilsConstant.ObjToStr(BKmap.get("cardName")))){
			logger.info("用户：" +  user.getLoginID() + "填写的结算账号为信用卡, 卡号为：" +  bankCardNo);
			LoginUserDB.updateUserBankStatus(new Object[]{3 , 0 , user.getID()});
			respData.setRespCode(RespCode.AccountNoError[0]);
			respData.setRespDesc(RespCode.AccountNoError[1]);
			return ;
		}
		
	
		if(x < 1){
			respData.setRespCode(RespCode.ServerDBError[0]);
			respData.setRespDesc(RespCode.ServerDBError[1]);
		}
		
		/**********  鉴权   *************************/
		Map<String,String> reqMap = AuthUtil.authentication(name,bankCardNo,IDcardNumber , reqData.getPayerPhone());
		logger.info("鉴权三要素:" + name + " , bankCardNo: " + bankCardNo + " , IDcardNumber : " + IDcardNumber + " , 手机号: " + reqData.getPayerPhone() + " 鉴权结果：" + reqMap.toString());
		if(reqMap.get("respCode").equals(Author.SUCESS_CODE)){
			//鉴权成功，向上游报商户

			int alipaylength = Constant.alipayMCCType.length;
			Random random = new Random(alipaylength-1);
			int index = random.nextInt(alipaylength-1);
			String alipaymcccNumber = Constant.alipayMCCType[index];
			Integer wxmcccNumber = Constant.wxMCCType[index];
//			Map<String,Object> wxmap = h5perfectInfoService.getUserConfig(new Object[]{user.getID(),1});
//			Map<String,Object> alipaymap = h5perfectInfoService.getUserConfig(new Object[]{user.getID(),2});
			
			Map<String,Object> wxmap  = TradeDB.getUserConfig(new Object[]{user.getID(),1});
			Map<String,Object> alipaymap = TradeDB.getUserConfig(new Object[]{user.getID(),2});
			Map<String,Object> map = new TreeMap<String, Object>();
			map.put("channelName", Constant.REPORT_CHANNELNAME);
			map.put("channelNo", Constant.REPORT_CHANNELNO);
			map.put("merchantName", merchantName);
			map.put("merchantBillName", merchantName);
			map.put("installProvince", state);
			map.put("installCity",  city);
			map.put("installCounty", county);
			map.put("operateAddress", address);
			map.put("merchantType", merchantType);
			map.put("businessLicense", businessLicense);
			map.put("legalPersonName", name);
			map.put("legalPersonID", IDcardNumber);
			map.put("merchantPersonName", name);
			map.put("merchantPersonPhone",  reqData.getPayerPhone());
			
			map.put("wxType", wxmcccNumber);
			map.put("wxT1Fee", Double.parseDouble(wxmap.get("T1SaleRate").toString())/10.0);
			map.put("wxT0Fee",  Double.parseDouble(wxmap.get("T0SaleRate").toString())/10.0);
			
			map.put("alipayType", alipaymcccNumber);
			map.put("alipayT1Fee",  Double.parseDouble(alipaymap.get("T1SaleRate").toString())/10.0);
			map.put("alipayT0Fee",  Double.parseDouble(alipaymap.get("T0SaleRate").toString())/10.0);
			
			map.put("bankType", bankType);
			map.put("accountName", name);
			map.put("accountNo", DESUtil.encode(Constant.REPORT_DES3_KEY,bankCardNo));
			map.put("bankName", bankName);
			map.put("bankProv", bankProv);
			map.put("bankCity", bankCity);
			map.put("bankBranch", bankSubbranch);
			map.put("bankCode", bankCode);

			
			logger.info("需要签名的的数据：" + JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY); 
			
			String sign = MD5.sign( JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY , StringEncoding.UTF_8);
			map.put("sign", sign.toUpperCase());
			
			
			logger.info("用户" + user.getLoginID() + "入网请求报文:" + map.toString());
			
			logger.info("入网请求地址: " + Constant.REPORT_URL);
			
			try {
				String content = HttpClient.post(Constant.REPORT_URL, map, null);
				
				logger.info("入网响应报文:" + content);
				JSONObject respJS = JSONObject.fromObject(content);
				
				String respCode = respJS.getString("respCode");
				
				if(Constant.payRetCode.equals(respCode)){
					
					String merchantNo = respJS.getString("merchantNo");// 商户号
					String signKey = respJS.getString("signKey");		//  微信签名秘钥
					String desKey = respJS.getString("desKey");			//  微信des秘钥
					String queryKey = respJS.getString("queryKey");		//  查询秘钥
					
//					String AlipaySignKey = respJS.getString("AlipaySignKey");	// 支付宝签名秘钥
//					String AlipaydesKey = respJS.getString("AlipaydesKey");		// 支付des秘钥
					//MerchantID,MerchantName,SignKey,DESKey,QueryKey,UserID,PayType
					
					LoginUserDB.delUserMerchant(user.getID());
					
					/*
					 *  保存商户秘钥等信息 
					 *
					 */
					List<Object[]> list = new ArrayList<Object[]>();
					Object[] objs = new Object[]{merchantNo,merchantName,signKey,desKey,queryKey,user.getID(),Constant.PayChannelWXScancode};
					list.add(objs);
					objs = new Object[]{merchantNo,merchantName,signKey,desKey,queryKey,user.getID(),Constant.payChannelAliScancode};
					list.add(objs);
					LoginUserDB.saveMerchantInfo(list);
					
					LoginUserDB.updateUserBankStatus(new Object[] { 1, 0, user.getLoginID() });
					
					if (user.getThreeLevel() != null && !user.getThreeLevel().toString().equals("")) {
						logger.info("判断商户上级商户是否需要升级 , 上级商户ID：" + user.getThreeLevel());
						String levelUpResult = this.levelUp(user.getThreeLevel().toString());
						if (levelUpResult.equals("fail")) {
							logger.info("用户上级商户ID：" + user.getThreeLevel() + "升级失败;当前等级--" + user.getMerchantLeve());
						}
					} else {
						logger.info("商户：" + user.getLoginID() + "没有上级商户");
					}
					
					respData.setRespCode("00");
					respData.setRespDesc("入网成功");
				}else{
					LoginUserDB.updateUserBankStatus(new Object[]{0 , 0 , user.getLoginID()});
					logger.info(user.getLoginID()+"入网异常：上游报备失败");
					respData.setRespCode("01");
					respData.setRespDesc(respJS.getString("respMsg"));
				}
			} catch (Exception e) {
				logger.info(user.getLoginID() + "入网异常：" + e.getMessage());
				respData.setRespCode("01");
				respData.setRespDesc(e.getMessage());
			}
		}else{
			respData.setRespCode(reqMap.get("respCode"));
			respData.setRespDesc(reqMap.get("respMsg"));
			logger.info(user.getLoginID()+"入网异常：鉴权失败");
		}
	}
	
//	public Map<String,String> Auth(String name,String bankCardNo,String IDcardNumber){
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
//		
//	}
	
	public String levelUp(String UserID) {
		// 查询出审核用户的上一级用户商户类型和用户ID (topUser)
		try {
			TabLoginuser user = LoginUserDB.getLoginuserInfo(UserID);
			if (user != null) {
				int MerchantLevel = user.getMerchantLeve();

				logger.info("上级商户 " + UserID + "目前等级为：" + MerchantLevel);

				if (MerchantLevel < 2) {
					int userCount = LoginUserDB.getUserCount(UserID);
					logger.info("上级商户：" + UserID + "直接扩展商户 ，  并且通过审核的数量：" + userCount);
					
					Map<String, Object> result = LoginUserDB.userLevelUpCount(MerchantLevel + 1);
					
					int levelUpCount = Integer.parseInt(result.get("UserCount").toString());
					
					logger.info("上级商户：" + UserID + "如果升级需要扩展的人数：" + levelUpCount);
					
					if (userCount >= levelUpCount) {
						
						logger.info("上级商户：" + UserID + "符合升级条件：当前扩展人数：" + userCount + "，系统需要扩展人数：" + levelUpCount ); 
						
						// 升级
						int modifyLoginUserMerchantLevel = LoginUserDB.updateUserLev(MerchantLevel + 1, UserID);
						if (modifyLoginUserMerchantLevel == 0) {
							logger.info("上级商户：" + UserID + "更新商户等级失败");
							
							return "fail";
						}
						// 同时查出升级之后费率（使用升级后商户类型查询费率）
						int statusResult = LoginUserDB.updateUserRate(new Object[]{UserID , MerchantLevel + 1});
						if (statusResult == 0) {
							logger.info("上级商户：" + UserID + "更新商户费率失败");
							return "fail";
						}else{
							logger.info("上级商户：" + UserID + "更新费率反悔收印象行数：" + statusResult);
						}
					}
				}else{
					logger.info("上级商户：" + UserID + "目前等级已经为2 是最高等级，无需升级");
				}
			}else{
				logger.info("上级商户ID:" + UserID + "不存在");
				return "fail";
			}
		} catch (Exception e) {
			logger.info("商户升级失败,错误信息为" + e.getMessage());
			return "fail";
		}
		
		logger.info("上级商户：" + UserID + "升级成功");

		return "success";
	}
	
	
}
