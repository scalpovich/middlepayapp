package com.rhjf.appserver.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;


@Service
public class SignService {
	
	LoggerTool log = new LoggerTool(this.getClass());
	
	public void send(TabLoginuser user , RequestData reqData , ResponseData respData) throws Exception{
		
		
		log.info("用户 : " + user.getLoginID() + "进行签到操作"); 
		
		
		//生成密钥
		String tmkIndex = LoadPro.loadProperties("config", "TMKINDEXDB");  //3
		String dbIndex = LoadPro.loadProperties("config", "DBINDEX");	// 2
		
		Map<String,Object> termKey = TermkeyDB.selectTermKey(user.getID());
		HashMap<String, String> MACKEY = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);
		HashMap<String, String> PINKEY = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);
		HashMap<String, String> TDKEY = GetKey(tmkIndex,UtilsConstant.ObjToStr(termKey.get("TermTmkKey")), dbIndex);

		
		respData.setTerminalInfo(MACKEY.get("keyTerm"));
		
		//记录数据库  
		int i = TermkeyDB.updateKey(user.getID(),  MACKEY.get("keyDB") ,  PINKEY.get("keyDB") ,  TDKEY.get("keyDB"));
		
		respData.setSecretKey(PINKEY.get("keyTerm")  + PINKEY.get("checkCode") + MACKEY.get("keyTerm")  + MACKEY.get("checkCode") + TDKEY.get("keyTerm") + TDKEY.get("checkCode"));
		
		
		log.info("用户 : " + user.getLoginID() + "获取秘钥" + PINKEY.get("keyTerm")  + PINKEY.get("checkCode") + MACKEY.get("keyTerm")  + MACKEY.get("checkCode") + TDKEY.get("keyTerm") + TDKEY.get("checkCode"));
		
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
	
	public void init(){
		HashMap<String, String> m = GetKey("3","879E6AA8B1710049B494C45A4397C5A8","2") ;
		System.out.println(m.toString());
	}
	
	public static void main(String[] args) {
		SignService sign = new SignService();
		sign.init();
	}
//	key:82926C4FFE612D30BA4F39E5ECE977BB
//	tmk:6555303FB6D9013F5E22D687B9C10B14    解密 6555303FB6D9013F5E22D687B9C10B14
//	keyTerm:29F330EAA3998C3B9D8F4337058928CE
//	keyDB:7A87504EBE25A40032B39964744CB00E
//	{keyDB=7A87504EBE25A40032B39964744CB00E, keyTerm=29F330EAA3998C3B9D8F4337058928CE}
//	  BA47B342363943A17755EDC7E1A8893D
	
}
