package com.rhjf.appserver.service.unionpay;

import java.util.Map;
import java.util.TreeMap;

import com.rhjf.appserver.db.UserBankCardDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.AESUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.RSAUtil;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

/**
 *   申请银联 固定码
 * @author hadoop
 *
 */


public class AskUnionpayCodeService {
	
	private LoggerTool log = new LoggerTool(this.getClass());
	
	private String url = "https://smbp.95516.com/xwins/gateway/createQrcode";
	
	public void send(LoginUser user , RequestData request ,  ResponseData response){
		
		String str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMUSnlPpApCGLxkR6CDBGBQHCtDRLpVmlQhvbT2dmIdKTuAjRZyY5qZn26Md8G44lFip/rxf2W94KfdQJ8D7PQpqlEOT3a7yN465ZKEvFtbL2/8GV7UYpQtCT3om3i1G7XrDzhieu3DQvFM8BSEPEycPe5oUCxvwoA//oun+WI8zAgMBAAECgYA0kz8CC8vPWrz95zUSZ/FQhoBwLR5MZU2lLnTqVzz1+vEIAuDzYidGhbam0bnu7dFFxjRbdQbBIkGv5QZ2CZDC2sbC/8n+GWusLiG7ph7o1/OBCXbtMYUnC1Vy2of6v6ZhiOfD+eiX9S135MMvx5utjs82LwtKRGbw7yXZ5mQsIQJBAOSd5xLhHA1QDtw+eUvoW50yyjs2aAy37Rk06xKYigwICcT8BAqnngtaX9rQAmZhSn6LfGtWJp1SNG4cq8n9PEUCQQDcrXoaEYNw2wBG1gJJ/T2k54IHv5ImbFGicvbcl1Itd7n7OpxuaPQg9jqQxy4Ud6/IrupSED8RGtNWn3hq2mEXAkEAoWVka6ymfDOHui8UvOUWQF1J7hGT1V7HsSLtzdwIoUPPedSdGdSJu+QahcSR3StURxA8Tx6r9ibBwvbUxK3VrQJALTcOMZEtEU6N+/WVent8yfD1X4kM9Pp0r4hGtGP83nwNi0AzFRxECQzqaweklWUAi2nrcO0LPwH0E2eRULN1+QJAbShhWLeNjN8uTPeeY9qtlFoRrVZA0RpMMVXxckDyTIfZbig4DIsHenLv5/Fcbdc7iRXA4tW+i+l96kVD9XK0Pw==";
		
		log.info("用户申请银联固定码：" + user.getLoginID());
		
		Map<String,Object> userBankMap = UserBankCardDAO.getBankInfo(user.getID());
		
		String phoneNo = user.getLoginID();
		if(userBankMap.get("PayerPhone") != null){
			phoneNo = UtilsConstant.ObjToStr(userBankMap.get("PayerPhone"));
		}
		
		
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("version", "1.0");
		map.put("encoding", "UTF-8");
		map.put("signMethod", "RSA2");
		map.put("expandcode", "49001100");
		map.put("requestId", UtilsConstant.getOrderNumber());
		JSONObject biz_json = new JSONObject();
		biz_json.put("bankNo", userBankMap.get("BankCode"));
		biz_json.put("certifId", user.getIDCardNo());
		biz_json.put("certifTp", "01");
		biz_json.put("customerNm", user.getName());
		JSONObject info = new JSONObject();
		info.put("accNo", userBankMap.get("AccountNo"));
		info.put("phoneNo", phoneNo);
		biz_json.put("encryptedInfo", AESUtil.encrypt(info.toString(), "W/9BR2KmpsvznFuC4dTGGw=="));
		biz_json.put("expandName", "tjchjiet");
		biz_json.put("merName", user.getMerchantName());
		map.put("bizContent", biz_json.toString());
		
		StringBuffer sbf = new StringBuffer();
		for (String key : map.keySet()) {
			sbf.append(key);
			sbf.append("=");
			sbf.append(map.get(key));
			sbf.append("&");
		}
		
		System.out.println("要去签名的数据" + sbf.toString());
		try {
			map.put("signature", RSAUtil.sign(sbf.toString().getBytes() , str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String result = HttpClient.post(url, map, "1");
		JSONObject json = JSONObject.fromObject(result);
		log.info(json.toString());
		
	}

}
