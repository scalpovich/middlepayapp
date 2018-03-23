package com.rhjf.appserver.service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.OpenKuaiDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.auth.AuthUtil;
import com.rhjf.appserver.util.auth.Author;

import java.util.Map;

/**
 *   开通无卡快捷
 * @author a
 *
 */
public class OpenKuaiService {


	LoggerTool logger = new LoggerTool(this.getClass());
	
	
	@SuppressWarnings("unchecked")
	public void openKuai(TabLoginuser user ,RequestData reqData , ResponseData repData){
		
		logger.info("用户：" + user.getLoginID() + "请求开通无卡快捷支付请求 , 开通银行卡卡号：(密文)" + reqData.getBankCardNo());
		
		String payChannel = "4";
		
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		
		Map<String,Object> agentconfigmap = null;
		Object agentConfigobj = ehcache.get(Constant.cacheName, user.getAgentID()  + payChannel +"agentConfig");
		
		if(agentConfigobj == null){
			logger.info("缓存读取代理商交易信息失败，将从数据库中读取: 交易类型："+payChannel+" , 代理商ID：" + user.getAgentID()); 
			agentconfigmap = AgentDB.agentConfig(new Object[]{user.getAgentID() ,payChannel });
			if(agentconfigmap == null || agentconfigmap.isEmpty()){
				logger.info("用户：" + user.getLoginID() + "对应代理商交易类型： "+payChannel+" 配置信息不完整, 对应代理商ID：" +  user.getAgentID());
				repData.setRespCode(RespCode.AgentTradeConfigError[0]);
				repData.setRespDesc(RespCode.AgentTradeConfigError[1]); 
				return ;
			}
		}

		Map<String, Object> termKey = TermkeyDB.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");

		String bankCardNo ;
		try {
			String descKey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardNo = DES3.decode(reqData.getBankCardNo(), descKey);
			logger.info("用户：" + user.getLoginID() + "请求开通无卡快捷支付请求 , 开通银行卡卡号：(原文)" + bankCardNo);
		} catch (Exception e) {
			logger.error("开号加密异常" , e);
			repData.setRespCode(RespCode.SYSTEMError[0]);
			repData.setRespDesc(RespCode.SYSTEMError[1]);
			return ;
		}

		Map<String,String> reqMap = AuthUtil.authentication(user.getName(),bankCardNo,user.getIDCardNo() , reqData.getPayerPhone());
		logger.info("鉴权三要素:" + user.getName() + " , bankCardNo: " + bankCardNo + " , IDcardNumber : " + user.getIDCardNo() + " , 手机号: " + reqData.getPayerPhone() + " 鉴权结果：" + reqMap.toString());
		if(reqMap.get("respCode").equals(Author.SUCESS_CODE)){
			int ret = OpenKuaiDB.save(new Object[]{UtilsConstant.getUUID(),user.getID(),bankCardNo,reqData.getPayerPhone()
					, "3" , "00" , "" , reqData.getCvn2() ,reqData.getExpired() ,"3" , "",});

			if(ret > 0 ){
				repData.setRespCode("01");
				repData.setRespDesc("该卡片已经开通过快捷支付功能");
				return ;
			}
		}else{
			repData.setRespCode(reqMap.get("respCode"));
			repData.setRespDesc(reqMap.get("respMsg"));
			logger.info(user.getLoginID()+"入网异常：鉴权失败");
		}
	}
}
