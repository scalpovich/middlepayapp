package com.rhjf.appserver.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.db.YMFTradeDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.MatrixToImageWriter;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *   用户申请固定码
 * @author a
 *
 */
public class ApplyYMFService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void ApplyYMF(TabLoginuser user , RequestData reqdata , ResponseData respData){
		logger.info("用户：" + user.getLoginID() + "申请到账类型" +  reqdata.getTradeCode() + "的固定码"); 
		
		
		if(user.getPhotoStatus()!=1||user.getBankInfoStatus()!=1){
			
			logger.info("用户：" + user.getLoginID() + "信息没有通过审核，无法申请固定码");
			
			respData.setRespCode(RespCode.MerchantInfoStatusError[0]);
			respData.setRespDesc(RespCode.MerchantInfoStatusError[1]);
			return ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		
		
		List<Map<String,Object>> ymflist = YMFTradeDB.getUserYMFlist(new Object[]{user.getID() ,reqdata.getTradeCode()});
		
		if(ymflist!=null&&ymflist.size()>0){
			respData.setRespCode(RespCode.BindedErrir[0]);
			respData.setRespDesc("用户已经拥有到账类型为" + reqdata.getTradeCode() + "得固定码,此次申请无效");
			return ;
		}
		
		boolean isok = true;
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		Map<String,Object> agentMap = AgentDB.agentInfo(new Object[]{user.getAgentID()});
		String AgentMobile = UtilsConstant.ObjToStr( agentMap.get("AgentPhone")); 
		
		// 二维码图片保存路径
		String outputpath = LoadPro.loadProperties("config", "imgpath") + "ymf" + File.separator + user.getLoginID() +  File.separator;
		
		if(!new File(outputpath).exists()){
			logger.info(user.getLoginID() + "保存二维码图片的文件夹不存在，将创建文件 ，文件夹名称为该用户的手机号");
			new File(outputpath).mkdirs();
		}
		
		String YMFUrl = LoadPro.loadProperties("config", "YMFUrl");
		
		List<Map<String,Object>> ratelist = TradeDB.getUserFeeRate(new Object[]{user.getID()});
		
		// ID,Code,UserID,Valid,PayChannel,Binded,AgentID,TradeCode,Rate,GenDate,BindedDate,AgentProfit,SettlementRate
		for(int i = 0 ; i < ratelist.size() ; i++){
			Map<String,Object> ratemap = ratelist.get(i);
			String PayChannel = UtilsConstant.ObjToStr(ratemap.get("ID"));
			if("1".equals(PayChannel)||"2".equals(PayChannel)){
				// 6位随机数
				int randomNumber = random.nextInt(100000,999999);
				StringBuilder sbd = new StringBuilder(AgentMobile + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + randomNumber);
				String re_md5 = MD5.sign(sbd.toString() + UtilsConstant.ObjToStr(agentMap.get("ID")), "UTF-8");
				
				String ID = UtilsConstant.getUUID();
				String Code = re_md5.substring(0,6).concat(sbd.toString()).toUpperCase();
				String UserID = user.getID();
				String Valid = "1";
				String Binded = "1";
				String AgentID = user.getAgentID();
				String TradeCode = reqdata.getTradeCode();
				String Rate = UtilsConstant.ObjToStr(ratemap.get("T1SaleRate")); 
				String SettlementRate =  UtilsConstant.ObjToStr(ratemap.get("T1SettlementRate")); 
				if(Constant.T0.equals(TradeCode)){
					Rate = UtilsConstant.ObjToStr(ratemap.get("T0SaleRate")); 
					SettlementRate =  UtilsConstant.ObjToStr(ratemap.get("T0SettlementRate")); 
				}
				String AgentProfit = "1";
				
				String text = YMFUrl + Code; 
				
				MatrixToImageWriter.encode(text, new File(outputpath + File.separator + Code + ".jpg"), "", false);
				
				YMFTradeDB.applyymf(new Object[]{ID,Code,UserID,Valid,null,Binded,AgentID,TradeCode,Rate,date,date,AgentProfit,SettlementRate});
				isok = false;
				break;
			}
		}
		
		if(isok){
			logger.info("用户：" + user.getLoginID() + "申请固定码失败，赔率配置错误"); 
			
			respData.setRespCode(RespCode.BindedErrir[0]);
			respData.setRespDesc("申请失败，用户费率配置错误，请联系客服人员");
		}else{
			respData.setRespCode(RespCode.SUCCESS[0]);
			respData.setRespDesc(RespCode.SUCCESS[1]);
		}
	}
}
