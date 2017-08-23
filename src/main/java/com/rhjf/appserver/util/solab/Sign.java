package com.rhjf.appserver.util.solab;

import java.io.IOException;     
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import com.rhjf.appserver.db.MposDB;
import com.rhjf.appserver.util.EncrptMerchine;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.solab.communicate.Config;
import com.rhjf.appserver.util.solab.communicate.UnionPayCommunicate;
import com.rhjf.appserver.util.solab.iso8583.IsoMessage;
import com.rhjf.appserver.util.solab.iso8583.IsoType;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;


public class Sign {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	protected UnionPayCommunicate unionPayCommunicate = new UnionPayCommunicate();

	Config config = Config.getInstance();
	
	
	String organization = "284473885";
	
	public int send() throws Exception{
		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			logger.error("读取翰鑫配置失败");
			return 1;
		}
		
		String retCode = "";
		String bankIndex = prop.getProperty("DBINDEX");
		String bankTMKIndex = prop.getProperty("bankPosTmkIndex");
		
//		String seqID = UtilsConstant.getOrderNumber();
		
		ThreadLocalRandom ran = ThreadLocalRandom.current();
		Integer seqID = ran.nextInt(100000, 999999);
		
//		String merchantId = bankInfo.getPlatMerchantID();
//		String terminalId = bankInfo.getPlatTermNo();
//		BankKey bankKey = DBOperation.selectBankKey(merchantId, terminalId);
		IsoMessage m_UnionReqMessage = Sign_MakeBuf(seqID.toString(), "000001");
		IsoMessage m_UnionRspMessage = unionPayCommunicate.UnionCommunicate(m_UnionReqMessage, 1);

		if (m_UnionRspMessage != null) {
			retCode = m_UnionRspMessage.getField(39).toString();
			if (retCode.equals("00")) {
				// 获取批次号
				String batchNo = m_UnionRspMessage.getField(60).toString().substring(2, 8);
				String filed62 = m_UnionRspMessage.getField(62).toString();
				String pinKey = filed62.substring(0, 32);
				String macKey = filed62.substring(32 + 8, 32 + 8 + 16);

				// 转密钥
				EncrptMerchine encrptMerchine = new EncrptMerchine();
				// 转pinkey
				String pinKeyDB = encrptMerchine.ChangeKeyFromToProkey(bankIndex, bankTMKIndex, pinKey);
				// 转macKey
				String macDB = encrptMerchine.ChangeKeyFromToProkey(bankIndex, bankTMKIndex, macKey);
				
				System.out.println("macBD:" + macDB);
				// 更新密钥
				return MposDB.updateBankKey(pinKeyDB ,macDB , batchNo );
			}
		}
		return 3;
	}
	
	public IsoMessage Sign_MakeBuf(String seqID , String batchNo) throws Exception {
		MessageFactory mfact = config.getMessFact8583();
		IsoMessage m_UnionReqMessage = mfact.newMessage(0x0800);
		m_UnionReqMessage.setBinary(true);
		m_UnionReqMessage.setValue(11, seqID, IsoType.NUMERIC, 6);
		m_UnionReqMessage.setValue(33, organization, IsoType.LLVAR, organization.length());
		m_UnionReqMessage.setValue(60, "00" + batchNo + "003", IsoType.LLLHEX, 11);
		m_UnionReqMessage.setValue(63, "001", IsoType.LLLVAR, 3);
		return m_UnionReqMessage;	
	}
}
