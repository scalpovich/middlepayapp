package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.YMFTradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

/**
 *   绑定固定码
 * @author a
 *
 */

public class BindedYMFService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void BindedYMF(LoginUser user , RequestData reqdata, ResponseData respdata){
		
		logger.info("用户" + user.getLoginID() +"绑定固定码 , 固定码地址为：" + reqdata.getQrcodeurl()); 
		
		String qrcodeurl = reqdata.getQrcodeurl();
		
		String code = qrcodeurl.substring(qrcodeurl.indexOf("=") +1 );
		
		Map<String,Object> map = YMFTradeDAO.getYMFCode(new Object[]{code});
		
		if(map!=null&&!map.isEmpty()){
			
			if("1".equals(map.get("Binded"))||null!=map.get("UserID")){
				respdata.setRespCode(RespCode.BindedErrir[0]);
				respdata.setRespDesc("该固定码已经被绑定");
				return ;
			}
			
			String agentID = user.getAgentID();
			
			String qrcodeAgentID = UtilsConstant.ObjToStr(map.get("AgentID")); 
			
			if(agentID.equals(qrcodeAgentID)){
				int ret = YMFTradeDAO.updateBindedInfo(new Object[]{user.getID(),DateUtil.getNowTime(DateUtil.yyyyMMdd) , code});
				
				if(ret > 0){
					logger.info("码数据" + code + "更新绑定信息成功");
					respdata.setRespCode(RespCode.SUCCESS[0]);
					respdata.setRespDesc(RespCode.SUCCESS[1]);
				}else{
					logger.info("码数据" + code + "更新绑定信息失败");
					respdata.setRespCode(RespCode.ServerDBError[0]);
					respdata.setRespDesc(RespCode.ServerDBError[1]);
				}
			} else {
				logger.info("申请固定码代理商与用户代理商不是同一个人无法进行绑定" + reqdata.getQrcodeurl());
				respdata.setRespCode(RespCode.BindedErrir[0]);
				respdata.setRespDesc(RespCode.BindedErrir[1]);
			}
		}else{
			logger.info("码数据" + code + "查询信息失败");
			respdata.setRespCode(RespCode.DATANOTEXISTError[0]);
			respdata.setRespDesc(RespCode.DATANOTEXISTError[1]);
		}
	}
}
