package com.rhjf.appserver.service;

import java.util.HashMap;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AuthenticationDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.UserBankCardDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.auth.Author;



/**
 *   用户添加新用卡卡号
 * @author hadoop
 *
 */
public class AddCreditCardNoService {

	private LoggerTool log = new LoggerTool(this.getClass());
	
	public void AddCreditCardNo(TabLoginuser user , RequestData request , ResponseData response){
		  //  查询商户信息
        
        //  查询商户结算卡信息
		Map<String,Object> userBankCard = LoginUserDB.getUserBankCard(user.getID());

        String settleCreditCard = request.getCreditCardNo();


        /**
         *  用户信用卡 卡号鉴权
         */
        boolean flag = authencation(UtilsConstant.ObjToStr(userBankCard.get("AccountName")) , UtilsConstant.ObjToStr(user.getIDCardNo())
        		,UtilsConstant.ObjToStr(settleCreditCard));
        
        if(flag){
            log.info("商户：" + user.getLoginID() + "信用鉴权没有通过 , 卡号:" + settleCreditCard);
            response.setRespCode(RespCode.BankCardInfoErroe[0]);
            response.setRespDesc(RespCode.BankCardInfoErroe[1]);
            return ;
        }

        UserBankCardDB.addCreditCardNo(new Object[]{settleCreditCard , user.getID()});

        response.setRespCode(RespCode.SUCCESS[0]);
        response.setRespDesc(RespCode.SUCCESS[1]);

	}
	
	
    public  boolean authencation( String name , String IDCardNo ,String bankCardNo){
        Map<String, Object> bankAuthencationMan = AuthenticationDB.bankAuthenticationInfo(new Object[]{bankCardNo});
        if (bankAuthencationMan == null || bankAuthencationMan.isEmpty()) {

            log.info("未查到卡号：" + bankCardNo + "的鉴权信息");

            Map<String, String> authMap = new HashMap<String, String>();
            AuthService authService = new AuthService();
            authMap.put("accName", name);
            authMap.put("cardNo", bankCardNo);
            authMap.put("certificateNo", IDCardNo);
            Map<String, String> reqMap = authService.authKuai(authMap);

            log.info("新商户：鉴权，" + authMap.toString() + "鉴权结果:" + reqMap.toString());
            if (!reqMap.get("respCode").equals(Author.SUCESS_CODE)) {
                log.info("业务员新增用户： 银行信息鉴权没有通过");

                return true;
            } else {
                log.info("鉴权通过。将" + bankCardNo +"保存数据库");
                AuthenticationDB.addAuthencationInfo(new Object[]{UtilsConstant.getUUID() , IDCardNo , name , bankCardNo , "00" , reqMap.get("respMsg") });
            }
        } else {
            if (!name.equals(bankAuthencationMan.get("RealName")) || !IDCardNo.equals(bankAuthencationMan.get("IdNumber"))) {
                log.info("业务员新增用户：银行信息鉴权没有通过");
                return true;
            }else{
                log.info("卡号：" + bankCardNo + "查询到历史鉴权数据,并且信息一致");
            }
        }
        return false;
    }
}
