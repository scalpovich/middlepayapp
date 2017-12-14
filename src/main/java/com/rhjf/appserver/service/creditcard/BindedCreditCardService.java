package com.rhjf.appserver.service.creditcard;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.BankCodeDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.db.UserCreditCardDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.auth.AuthUtil;
import com.rhjf.appserver.util.auth.Author;


/**
 *  用户添加信用卡
 * @author hadoop
 *
 */
public class BindedCreditCardService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void bindedCreditCard(TabLoginuser user , RequestData request , ResponseData response){
		
		log.info("用户：" + user.getLoginID() + "添加信用卡"); 
		
		String creditCard = request.getCreditCardNo();
		String cvn2 = request.getCvn2();
		String expired = request.getExpired();
		String payerPhone = request.getPayerPhone();
		String tradeDate = request.getTradeDate();
		String name =  user.getName();
		
		String bankSubbranch = request.getBankSubbranch();

		String bankCode = "";
		String bankSymbol = "";
		String bankName = "";
		
		
		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");
		
		String bankCardno = "";
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(creditCard, desckey);
			log.info("用户：" + user.getLoginID() + " 添加收款信用卡 ：  密文：" + creditCard + " 原文，" + bankCardno); 
		} catch (Exception e) {
			log.error("卡号加密异常 ：" , e); 
			response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}
		
		Map<String,Object> creditCardBin = BankCodeDB.creditCardBin(new Object[] {bankCardno});
		if(creditCardBin == null || creditCardBin.isEmpty()){
			response.setRespCode(RespCode.BankCardInfoErroe[0]);
			response.setRespDesc("暂不支持该银行卡");
			return ;
		}
		
		
		bankName = UtilsConstant.ObjToStr(creditCardBin.get("BankName"));
		bankSymbol = UtilsConstant.ObjToStr(creditCardBin.get("BankSymbol"));
		bankCode = UtilsConstant.ObjToStr(creditCardBin.get("unite_bank_no"));
		bankSubbranch = UtilsConstant.ObjToStr(creditCardBin.get("BankBranch"));
		String bankProv = UtilsConstant.ObjToStr(creditCardBin.get("BankProv"));
		String bankCity = UtilsConstant.ObjToStr(creditCardBin.get("BankCity"));
		
		
		log.info("用户：" + user.getLoginID() + "添加信用卡 ,  卡号：" + bankCardno + " , cvn2:" + cvn2 + " , 有效期: " + expired + " , 预留手机号：" + payerPhone + " , 还款日:" + tradeDate ); 
		
		String id = UtilsConstant.getUUID();
		
		Map<String,String> reqMap = AuthUtil.authentication(user.getName(), bankCardno , user.getIDCardNo() , payerPhone);
		log.info("用户：" + user.getLoginID() + "添加信用卡 ,  卡号：" + bankCardno + "鉴权报文：" + reqMap.toString());
		if(reqMap.get("respCode").equals(Author.SUCESS_CODE)){
			log.info("用户：" + user.getLoginID() + "添加信用卡 ,  卡号：" + bankCardno + "鉴权通过， 保存记录");
			
			UserCreditCardDB.saveUserCreditCard(new Object[]{id, user.getID() , name ,bankCardno , bankName , bankSubbranch ,bankCode ,
					bankProv , bankCity , bankSymbol, cvn2 ,expired ,payerPhone ,tradeDate , cvn2 ,expired ,payerPhone , tradeDate });
			response.setRespCode(RespCode.SUCCESS[0]);
			response.setRespDesc(RespCode.SUCCESS[1]);
		}else{
			log.info("用户：" + user.getLoginID() + "添加信用卡 ,  卡号：" + bankCardno + "没有鉴权通过");
			response.setRespCode(RespCode.BankCardInfoErroe[0]);
			response.setRespDesc(RespCode.BankCardInfoErroe[1]);
		}
	}
}
