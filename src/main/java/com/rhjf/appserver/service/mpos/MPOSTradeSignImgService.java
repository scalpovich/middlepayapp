package com.rhjf.appserver.service.mpos;

import java.io.File;
import java.io.IOException;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.Image64Bit;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;

/**
 *   mpos  交易上传签名图片
 * @author hadoop
 *
 */
public class MPOSTradeSignImgService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void MPOSTradeSignImg(TabLoginuser user , RequestData request , ResponseData response){
		
		logger.info("用户：" + user.getLoginID() + "上传交易签名图片");
		
		
		/** 获取签名图片 **/
		String signImg = request.getSignImg();
		
		/** 签名图片保存路径 **/
		String imgPath = LoadPro.loadProperties("config", "mposSignimgpath") + user.getLoginID() + File.separator + DateUtil.getNowTime(DateUtil.yyyyMMdd) + File.separator ; 
		
		/** 如果路径不存在 将创建 **/
		if(!new File(imgPath).exists()){
			new File(imgPath).mkdirs();
		}
		String postfix = ".jpg";
		try {
			Image64Bit.geneRateImage(signImg.replaceAll("\n", "").replace("\t", ""), imgPath  + request.getRefetno() + postfix);
			
			response.setRespCode(RespCode.SUCCESS[0]);
			response.setRespDesc(RespCode.SUCCESS[1]);
			return ;
		} catch (IOException e) {
			e.printStackTrace();
			response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
		}
	}
}
