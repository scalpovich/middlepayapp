package com.rhjf.appserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rhjf.appserver.authen.ApiTrans;
import com.rhjf.appserver.authen.GetDynKey;
import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AuthenticationDAO;
import com.rhjf.appserver.model.AuthenticationRecord;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TJJQResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;
import net.sf.json.JSONArray;


public class AuthenticationService {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	
	public ResponseData send(LoginUser loginuser,RequestData reqData , ResponseData repData){
		if(reqData.getBankCardNo()==null || "".equals(reqData.getBankCardNo())){
			repData.setRespCode(RespCode.ParamsError[0]);
			repData.setRespCode(RespCode.ParamsError[1]);
			return repData;
		}
		if(reqData.getPhoneNumber() == null || "".equals(reqData.getPhoneNumber())){
			repData.setRespCode(RespCode.ParamsError[0]);
			repData.setRespCode(RespCode.ParamsError[1]);
			return repData;
		}
		if(reqData.getRealName() == null || "".equals(reqData.getRealName())){
			repData.setRespCode(RespCode.ParamsError[0]);
			repData.setRespCode(RespCode.ParamsError[1]);
			return repData;
		}
		if(reqData.getIdNumber() == null || "".equals(reqData.getIdNumber())){
			repData.setRespCode(RespCode.ParamsError[0]);
			repData.setRespCode(RespCode.ParamsError[1]);
			return repData;
		}
		Constant.TRADE_TYPE ="0411";
		Map<String, String> map = new HashMap<String, String>();
		map.put("accNo", reqData.getBankCardNo());
		map.put("certificateCode", reqData.getIdNumber());
		map.put("name", reqData.getRealName());
		map.put("nbr", reqData.getPhoneNumber());
		try {
			GetDynKey.getDynKey();
			TJJQResponseData tjresp = ApiTrans.doTrans(map);
			int ret = AuthenticationDAO.Authentication(new Object[]{UtilsConstant.getUUID(),loginuser.getID(),reqData.getIdNumber(),reqData.getPhoneNumber(),
					reqData.getRealName() , reqData.getBankCardNo(),tjresp.getOrderId(),tjresp.getResultCode(),tjresp.getResultDesc()});
			if(tjresp.getResultCode() =="00"){
				if(ret>0){
					logger.info("用户" + reqData.getLoginID() + "鉴权成功");
					repData.setRespCode(RespCode.SUCCESS[0]);
					repData.setRespDesc(RespCode.SUCCESS[1]);
				}else {
					logger.info("插入数据库收银行书为：" + ret + "用户" + reqData.getLoginID() + "鉴权失败");
					repData.setRespCode(RespCode.ServerDBError[0]);
					repData.setRespDesc(RespCode.ServerDBError[1]);
				}
			}else{
				if(ret>0){
					logger.info("上游鉴权失败：" + tjresp.getOrderId() + "用户" + reqData.getLoginID() + "鉴权失败");
					repData.setRespCode(RespCode.TJJQError[0]);
					repData.setRespDesc(tjresp.getResultDesc());
				}else {
					logger.info("插入数据库收银行书为：" + ret + "用户" + reqData.getLoginID() + "鉴权失败");
					repData.setRespCode(RespCode.ServerDBError[0]);
					repData.setRespDesc(RespCode.ServerDBError[1]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			repData.setRespCode(RespCode.ServerDBError[0]);
			repData.setRespDesc(RespCode.ServerDBError[1]);
			e.printStackTrace();
		}
		return repData;
	}
	
	public void getAuthenticationList(LoginUser loginuser,RequestData reqData , ResponseData repData){
		try {
			List<AuthenticationRecord> list = AuthenticationDAO.getAuthenticationRecordList(new Object[]{"00",loginuser.getID()});
			repData.setAuthenList(JSONArray.fromObject(list).toString()); 
			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
		} catch (Exception e) {
			repData.setRespCode(RespCode.SystemConfigError[0]);
			repData.setRespDesc(RespCode.SystemConfigError[1]);
			logger.error("申请信用卡系统异常 ExceptionMessage:"+e.getMessage());
			e.printStackTrace();
		}
	}
	

}
