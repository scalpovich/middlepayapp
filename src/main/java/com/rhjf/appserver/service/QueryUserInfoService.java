package com.rhjf.appserver.service;

import java.util.Map; 

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.BankCodeDB;
import com.rhjf.appserver.db.CapitalDB;
import com.rhjf.appserver.db.CreaditCardDB;
import com.rhjf.appserver.db.DevicetokenDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


public class QueryUserInfoService {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void QueryUserInfo(TabLoginuser user , RequestData reqData , ResponseData respData){
		
		logger.info(user.getLoginID() + "查询个人信息");
		
		//初始化返回包的信息
		respData.setLoginID(user.getLoginID());
		respData.setName(user.getName());

		respData.setFeeAmount(user.getFeeAmount());
		respData.setFeeBalance(user.getFeeBalance());
		
		respData.setiDCardNo(user.getIDCardNo()); 
		
		
		/*
		 *    商户结算卡信息
		 */
		Map<String,Object> bankInfoMap = LoginUserDB.getUserBankCard(user.getID());
		if(bankInfoMap!=null&&!bankInfoMap.isEmpty()){
			respData.setBankCardNo(UtilsConstant.ObjToStr(bankInfoMap.get("AccountNo")));
			respData.setBankName(UtilsConstant.ObjToStr(bankInfoMap.get("BankName"))); 
			respData.setBankSubbranch(UtilsConstant.ObjToStr(bankInfoMap.get("BankBranch"))); 
			respData.setBankProv(UtilsConstant.ObjToStr(bankInfoMap.get("BankProv")));
			respData.setBankCity(UtilsConstant.ObjToStr(bankInfoMap.get("BankCity")));
			respData.setCreditCardNo(UtilsConstant.ObjToStr(bankInfoMap.get("SettleCreditCard")));
			
			// 银联号   bankName,unite_bank_no
			Map<String,Object> bankmap = BankCodeDB.bankBinMap(new Object[]{UtilsConstant.ObjToStr(bankInfoMap.get("AccountNo"))});
			respData.setBankNo(UtilsConstant.ObjToStr(bankmap.get("bankCode"))); 
		}
		
		/*
		 *	 商户钱包信息 
		 */
//		Map<String,String> walletmap = UserWalletDB.UserWalletByUserID(new Object[]{user.getID()});
//		if(walletmap!=null &&! walletmap.isEmpty()){
//			respData.setTotal(walletmap.get("WalletBalance"));
//		}
		
		String available_amount  = "0";
		Map<String,String> capitalMap = CapitalDB.getCapitalByUserID(new Object[]{user.getID()});
		if(capitalMap != null && !capitalMap.isEmpty()){
			available_amount =  UtilsConstant.strIsEmpty(capitalMap.get("available_amount"))?"0":capitalMap.get("available_amount");
		}
		
		Integer total = Integer.parseInt(user.getFeeBalance()) + Integer.parseInt(available_amount);
		respData.setTotal(String.valueOf(total)); 
		
		respData.setAccountStatus(user.getAccountStatus());
		
		respData.setPhotoStatus(user.getPhotoStatus());
		respData.setBankInfoStatus(user.getBankInfoStatus());
		respData.setiDCardNo(user.getIDCardNo());
		respData.setLevel(user.getMerchantLeve());
		respData.setAddress(user.getAddress());
		
		respData.setHandheldIDPhoto(user.getHandheldIDPhoto());
		respData.setiDCardFrontPhoto(user.getIDCardFrontPhoto());
		respData.setiDCardReversePhoto(user.getIDCardReversePhoto());
		respData.setBankCardPhoto(user.getBankCardPhoto());
		respData.setBusinessPhoto(user.getBusinessPhoto());
		
		respData.setState(user.getState());
		respData.setEmail(user.getEmail()); 
		
		respData.setRemarks(user.getRemarks());
		
		respData.setTradeCode(user.getTradeCode());
		
		
		Map<String, Object> map = CreaditCardDB.myCardFeeAmount(user.getID());
		if(map!=null && !map.isEmpty()){
			respData.setCardFeeAmount(map.get("aggregate_amount").toString());
			respData.setCardFeeBlance(map.get("available_amount").toString());
		}else{
			respData.setCardFeeAmount("0");
			respData.setCardFeeBlance("0");
		}
		
		
		 /** 获取ios设备token **/
	    String deviceToken = reqData.getDeviceToken();
    	String deviceType = reqData.getDeviceType();
    	DevicetokenDB.saveOrUpToken(new Object[]{user.getID() , deviceToken , deviceType  , deviceToken ,deviceType });
	    
		
//		if(user.getPhoto1()!=null && !user.getPhoto1().equals("")){
//			out.setImageData1(LoadPro.loadProperties("config", "imageurl")+changeUrl(UtilKey.getInstance().decryption(user.getPhoto1())));
//		}
//		if(user.getPhoto2()!=null && !user.getPhoto2().equals("")){
//			out.setImageData2(LoadPro.loadProperties("config", "imageurl")+changeUrl(UtilKey.getInstance().decryption(user.getPhoto2())));
//		}
//		if(user.getPhoto3()!=null && !user.getPhoto3().equals("")){
//			out.setImageData3(LoadPro.loadProperties("config", "imageurl")+changeUrl(UtilKey.getInstance().decryption(user.getPhoto3())));
//		}
		
		respData.setMerchantName(user.getMerchantName());
		//组返回给终端的报文
		respData.setRespCode(RespCode.SUCCESS[0]);
		respData.setRespDesc(RespCode.SUCCESS[1]);
	}
	
	
	
	public String changeUrl(String url){
		String[] urlBuffer=url.split("/");
		return urlBuffer[urlBuffer.length-2]+"/"+urlBuffer[urlBuffer.length-1];
	}
}
