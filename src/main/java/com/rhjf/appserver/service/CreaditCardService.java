package com.rhjf.appserver.service;

import java.text.SimpleDateFormat; 
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.CreditCardDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabBankConfig;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.LoggerTool;

import net.sf.json.JSONArray;


public class CreaditCardService {
	
	
	LoggerTool logger = new LoggerTool(this.getClass());
	/**
	 * 申请信用卡
	 * @param reqData
	 * @param repData
	 * @return
	 */
	public void applyForCard(LoginUser loginuser,RequestData reqData , ResponseData repData){
		try {
			logger.info("进入申请信用卡方法-----------------阿里克里");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance(); 
			calendar.add(Calendar.MONTH, -3);    //得到前3个月 
			String time = sdf2.format(calendar.getTime());
			boolean flag = CreditCardDAO.findCardApplyRecord(reqData.getBankId(), reqData.getIdNumber(), time);
			if(flag){
				repData.setRespCode("A3");
				repData.setRespDesc("此身份证90天内已经申请过");
			}
			String ID = UUID.randomUUID().toString();
			int rsNet = CreditCardDAO.insertCardApplyRecord(ID,reqData.getPhoneNumber(), reqData.getIdNumber(), reqData.getRealName(),"2222", sdf.format(new Date()),reqData.getAgencyNumber(),reqData.getBankId());
			if(rsNet ==0){
				repData.setRespCode("A2");
			}else{
				repData.setRespCode("00");
				TabBankConfig tbc  = CreditCardDAO.getTabBanConfigInfo(reqData.getBankId());
				repData.setRespDesc("交易成功");
				repData.setBankUrl(tbc.getBankUrl());
			}
		} catch (Exception e) {
			repData.setRespCode("A1");
			repData.setRespDesc("系统异常");
			e.printStackTrace();
			logger.error("申请信用卡系统异常 ExceptionMessage:"+e.getMessage());
		}
		
	}	
	
	/**
	 * 获取信用卡开通银行列表
	 * @param reqData
	 * @param repData
	 */
	public void GetBank(LoginUser loginuser,RequestData reqData , ResponseData repData){
		try {
			List<TabBankConfig> list = CreditCardDAO.getBankList();
			repData.setBankList(JSONArray.fromObject(list).toString()); 
			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
		} catch (Exception e) {
			repData.setRespCode("A3");
			repData.setRespDesc("系统异常");
			e.printStackTrace();
			logger.error("申请信用卡系统异常 ExceptionMessage:"+e.getMessage());
		}
				
	}	
	
	public void myCardShare(LoginUser loginuser,RequestData reqData , ResponseData repData){
		
		try {
			// 当前页数
			Integer page = reqData.getPage();
			Integer pageSize = 20;
			
			if(page == null){
				pageSize = 200;
				page = 0;
			}
			
			List<Map<String, Object>> list = CreditCardDAO.myCardShare(loginuser.getID(), page*pageSize , pageSize);
			
			Integer count = CreditCardDAO.myCardShareCount(loginuser.getID());
			
			repData.setList(JSONArray.fromObject(list).toString());
			repData.setPage(page + 1);
			repData.setTotalCount(count);
			repData.setRespCode(RespCode.SUCCESS[0]);
			repData.setRespDesc(RespCode.SUCCESS[1]);
		} catch (Exception e) {
			repData.setRespCode("A3");
			repData.setRespDesc("系统异常");
			e.printStackTrace();
			logger.error("获取信用卡分润 ExceptionMessage:"+e.getMessage());
		}
	}

}
