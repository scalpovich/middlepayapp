package com.rhjf.appserver.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


@Service
public class SignService {
	
	LoggerTool log = new LoggerTool(this.getClass());
	
	public void send(LoginUser user , RequestData reqData , ResponseData respData) throws Exception{
		
		
		log.info("用户 : " + user.getLoginID() + "进行签到操作"); 
		
		
		//生成密钥  tmkIndex = 3  dbIndex = 2
		String tmkIndex = LoadPro.loadProperties("config", "TMKINDEXDB");
		String dbIndex = LoadPro.loadProperties("config", "DBINDEX");
		
		Map<String,Object> termKey = TermKeyDAO.selectTermKey(user.getID());
		HashMap<String, String> mackey = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);
		HashMap<String, String> pinKey = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);
		HashMap<String, String> tdKey = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);

		
		respData.setTerminalInfo(mackey.get("keyTerm"));
		
		//记录数据库  
		int i = TermKeyDAO.updateKey(user.getID(),  mackey.get("keyDB") ,  pinKey.get("keyDB") ,  tdKey.get("keyDB"));
		
		respData.setSecretKey(pinKey.get("keyTerm")  + pinKey.get("checkCode") + mackey.get("keyTerm")  + mackey.get("checkCode") + tdKey.get("keyTerm") + tdKey.get("checkCode"));
		
		log.info("用户 : " + user.getLoginID() + "获取秘钥" + pinKey.get("keyTerm")  + pinKey.get("checkCode") + mackey.get("keyTerm")  + mackey.get("checkCode") + tdKey.get("keyTerm") + tdKey.get("checkCode"));
		
		if(user.getRegisterTime()!=null){
			respData.setRegisterDate(user.getRegisterTime().substring(0, 8));
		}else{
			respData.setRegisterDate(user.getRegisterTime());
		}
		
		
		if(i > 0){
			log.info("用户 : " + user.getLoginID() + "签到成功");
			respData.setRespCode(RespCode.SUCCESS[0]);
			respData.setRespDesc(RespCode.SUCCESS[1]);
		} else{
			log.info("用户 : " + user.getLoginID() + "签到失败");
			respData.setRespCode(RespCode.ServerDBError[0]);
			respData.setRespDesc(RespCode.ServerDBError[1]);
		}
	}	

	private HashMap<String, String> GetKey(String tmkIndex, String tmkEncry, String dbIndex) {
		/**
		 *  3=AB123CD564ABE85FDB42342364ABE85F
		 *	2=22222222222222222222222222222222
		 */
		
		Random random = new Random();
		HashMap<String, String> keyMap = new HashMap<String, String>();
		char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			StringBuffer ret = new StringBuffer();
			for (int i = 0; i < 32; i++) {
				ret.append(String.valueOf(codeSequence[random.nextInt(16)]));
			}

			String data = ret.toString();
//			initKey     AB123CD564ABE85FDB42342364ABE85F
//			dbInitKey   22222222222222222222222222222222
			String initKey = LoadPro.loadProperties("jmj", tmkIndex);
			String dbInitKey = LoadPro.loadProperties("jmj", dbIndex);
			// 解密TMK
			String tmk = DESUtil.bcd2Str(DESUtil.decrypt3(tmkEncry, initKey));
			
			//  获取校验码
			String checkCode = DESUtil.bcd2Str(DESUtil.encrypt3("0000000000000000", data));
			
			// 生成下发给终端的密钥
			String keyTerm = DESUtil.bcd2Str(DESUtil.encrypt3(data, tmk));
			// 生成存放到数据的密钥
			String keyDB = DESUtil.bcd2Str(DESUtil.encrypt3(data, dbInitKey));
			keyMap.put("keyTerm", keyTerm);
			keyMap.put("keyDB", keyDB);
			keyMap.put("checkCode", checkCode.substring(0, 8));
			
//			System.out.println("key:"+key);
//			System.out.println("initKey:"+initKey);
//			System.out.println("dbInitKey:"+dbInitKey);
//			System.out.println("tmk:"+tmk);
//			System.out.println("keyTerm:"+keyTerm);
//			System.out.println("keyDB:"+keyDB);
			
		} catch (Exception e) {
			return null;
		}
		return keyMap;
	}
}
