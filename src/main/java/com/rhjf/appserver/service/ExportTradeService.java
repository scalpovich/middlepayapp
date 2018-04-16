package com.rhjf.appserver.service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.PayOrderDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.CreateExcel;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.email.SendMail;

public class ExportTradeService {

	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void ExportTrade(LoginUser user , RequestData request, ResponseData response){
		
		String tradeDate = request.getTradeDate();
		StringBuffer stringBuffer = new StringBuffer(tradeDate);	
		
		if(tradeDate.length() < 6){
			stringBuffer.insert(4, "0");
		}
 		
		logger.info("用户" +  user.getLoginID() + "获取" + stringBuffer.toString() + "交易数据");
		
		List<Map<String,Object>> list = PayOrderDAO.exportTrade(user.getID() , stringBuffer.toString());
	
		String[] title = {"商户名称" ,"手机号"  , "支付类型" , "订单号" , "交易时间" , "交易金额" , "交易手续费" , "结算周期" , "返利"};
		
		if(!user.getEmail().equals(request.getEmail())){
			//  用户上传的邮箱和系统中保存的邮箱不一致 ， 将用户上传的邮箱更新到系统中
			LoginUserDAO.updateUserEmail(new Object[]{request.getEmail() , user.getID()});
		}
		
		String path = LoadPro.loadProperties("config", "imgpath") + user.getLoginID() + File.separator;
		
		List<Object[]> datalist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			
			//  c.MerchantName , c.LoginID , PayChannel ,PayChannelName , a.OrderNumber  , 
			// date_format(CONCAT(LocalDate, TradeTime) ,'%Y-%m-%d %H:%i') as tradedate , amount , fee , a.TradeCode , a.MerchantProfit 
			
			double amount = AmountUtil.div(map.get("amount").toString(), "100" , 2);
			
			double fee = AmountUtil.div(map.get("fee").toString(),"100" , 2);
			
			double MerchantProfit = AmountUtil.div(map.get("MerchantProfit").toString(), "100" ,2);
			
			
			Object[] obj = new Object[]{map.get("MerchantName")  , map.get("LoginID")  ,map.get("PayChannelName")  ,map.get("OrderNumber")  , map.get("tradedate")
					, amount ,fee ,map.get("TradeCode") ,MerchantProfit };
			datalist.add(obj);
		}
		
		try {
			CreateExcel.createExcel(title, datalist , path ,  user.getLoginID() + stringBuffer.toString());
			
			String emailTile = stringBuffer.toString() + "交易数据";
			String emailContent = "";
			
			String fileName =  path +  user.getLoginID() + stringBuffer.toString() + ".xls";
			
			SendMail.sendMail(emailTile, emailContent, new String[]{request.getEmail()} , null , new String[]{fileName});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
