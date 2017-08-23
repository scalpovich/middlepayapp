package com.rhjf.appserver.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.AgentDB;
import com.rhjf.appserver.db.MposDB;
import com.rhjf.appserver.db.TermkeyDB;
import com.rhjf.appserver.db.TradeDB;
import com.rhjf.appserver.model.BankInfo;
import com.rhjf.appserver.model.BankKey;
import com.rhjf.appserver.model.Fee;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.AmountUtil;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.DateUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.EncrptMerchine;
import com.rhjf.appserver.util.solab.EmvTLVInfo;
import com.rhjf.appserver.util.solab.Sign;
import com.rhjf.appserver.util.solab.communicate.Config;
import com.rhjf.appserver.util.solab.communicate.UnionPayCommunicate;
import com.rhjf.appserver.util.solab.iso8583.IsoMessage;
import com.rhjf.appserver.util.solab.iso8583.IsoType;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;

import net.sf.json.JSONObject;

/**
 *    终端mpos交易
 * @author hadoop
 *
 */

public class MPOSTradeService {
	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	protected UnionPayCommunicate unionPayCommunicate = new UnionPayCommunicate();
	
	Config config = Config.getInstance();
	
	public void mposTrade(TabLoginuser user, RequestData request , ResponseData response){
		
		String sendTime = request.getSendTime();
		String sendSeqId = request.getSendSeqId();
		String loginID = request.getLoginID();
		String amount = request.getAmount();
		String bankCardNo = request.getBankCardNo();
		String pin = request.getPin();
		String track2Data = request.getTrack2Data();
		String track3Data = request.getTrack3Data();
		String terminalInfo = request.getTerminalInfo();
		
		logger.info("用户：" + user.getLoginID() + "发起mpos交易请求");
		logger.info("sendTime :" + sendTime);
		logger.info("sendSeqId:" + sendSeqId);
		logger.info("loginID :" + loginID);
		logger.info("amount :" + amount);
		logger.info("bankCardNo :" + bankCardNo);
		logger.info("pin :" + pin);
		logger.info("track2Data :" + track2Data);
		logger.info("track3Data :" + track3Data);
		logger.info("terminalInfo :" + terminalInfo);
		logger.info("icRelatedData :" + request.getIcRelatedData());
		
		
		/** 查询代理商配置信息  **/
		Map<String,Object> agentconfigmap = AgentDB.agentConfig(new Object[]{user.getAgentID() , Constant.payChannelMpos});
		if(agentconfigmap == null || agentconfigmap.isEmpty()){
			logger.info("用户：" + user.getLoginID() + "对应代理商交易类型：" + Constant.payChannelMpos + "配置信息不完整, 对应代理商ID：" +  user.getAgentID());
			response.setRespCode(RespCode.AgentTradeConfigError[0]);
			response.setRespDesc(RespCode.AgentTradeConfigError[1]); 
			return ;
		}
		
		
		/** 查询用户交易配置信息  **/
		Map<String,Object> map = TradeDB.getUserConfig(new Object[]{ user.getID() , Constant.payChannelMpos});
		if(map==null||map.isEmpty()){
			
			logger.info("支付类型" + Constant.payChannelMpos + "读取数据库失败 , 保存用户默认费率， 5‰ 的手续费 ");
			String id = UtilsConstant.getUUID();
			String userid = user.getID();
			
			List<Object[]> list = new ArrayList<Object[]>();
			list.add(new Object[]{id,userid , Constant.payChannelMpos , 0 , 0 , Constant.FeeRate , Constant.FeeRate,
					Constant.FeeRate , Constant.FeeRate });
			int x = TradeDB.saveUserConfig(list)[0];
			if(x < 0 ){
				logger.info("用户：" + user.getID() + "支付类型：" + Constant.payChannelMpos + " 支付配置信息获取失败,停止交易操作" );
				response.setRespCode(RespCode.TradeTypeConfigError[0]);
				response.setRespDesc(RespCode.TradeTypeConfigError[1]); 
				return ;
			}else{
				map = TradeDB.getUserConfig(new Object[]{ userid , Constant.payChannelMpos});
			}
		}
		
		/** 获取 mpos 商户信息 **/
		BankInfo bankInfo = MposDB.getBankInfo(user.getID());
		if(bankInfo == null){
			logger.info("用户：" + user.getLoginID() + " mpos 商户信息不存在."); 
			response.setRespCode(RespCode.TradeTypeConfigError[0]);
			response.setRespDesc(RespCode.TradeTypeConfigError[1]);
			return ;
		}
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		String seqID = Integer.toString(random.nextInt(100000, 999999));
		
		Map<String,Object> termkey = TermkeyDB.selectTermKey(user.getID());
		
		String dbIndex = LoadPro.loadProperties("config", "DBINDEX");	// 2
		
		String merchantId = bankInfo.getPlatMerchantID();
		String terminalId = bankInfo.getPlatTermNo();
		
		System.gc();
		System.runFinalization();
		
		try {
			BankKey bankKey = MposDB.getBankey(merchantId, terminalId);
			
			String sendDate = sendTime.substring(0, 8);
			sendTime = sendTime.substring(8);
			
			IsoMessage m_UnionReqMessage = sendTrade(seqID , request , dbIndex , termkey , bankInfo , bankKey , dbIndex);
			
			int isCredit = MposDB.getCardType(request.getBankCardNo());
			
			
			String supportID = UtilsConstant.getUUID();
			
			int nRet = MposDB.insertConsumeTrade(supportID,user.getID() , merchantId, terminalId, "T1",  request.getAmount(), request.getBankCardNo() 
					 , request.getSendSeqId() , seqID , sendDate, sendTime, "", bankInfo.getNetCode(), bankInfo.getPlatMerchantID(),
					 bankInfo.getPlatTermNo(), "", "", isCredit , "", "", "", "");
			 
			if (nRet < 1) {
				
				logger.info("订单数据保存数据库失败");
				
				response.setRespCode(RespCode.ServerDBError[0]);
				response.setRespCode(RespCode.ServerDBError[1]);
				return;
			}
				   
			IsoMessage m_UnionRspMessage = this.unionPayCommunicate.UnionCommunicate(m_UnionReqMessage);

			if (m_UnionRspMessage != null) {
				String retCode = m_UnionRspMessage.getField(39).toString();
				response.setRespCode(retCode);
				if (retCode.equals("A0")) {
					logger.info("mac错误，重新签到更新秘钥");
					Sign sign = new Sign();
					sign.send();
				} else if (retCode.equals("00")) {
					String ref = "";
					if (m_UnionRspMessage.getField(37) != null) {
						ref = m_UnionRspMessage.getField(37).toString();
					}
					MposDB.updateConsumeTrade(ref, DateUtil.getNowTime("yyyyMMdd"), DateUtil.getNowTime("HHmmss"), "",
							0, retCode, 0, 0, 0, bankKey.getBatchNo(), "", "", merchantId, terminalId, sendDate,
							request.getSendSeqId());
					
					Fee fee = calProfit(seqID, amount, user);
					MposDB.saveMposFee(new Object[]{
							UtilsConstant.getUUID(),user.getID(), supportID ,fee.getMerchantFee(),fee.getAgentID(),fee.getAgentProfit(),
							fee.getTwoAgentID(),fee.getTwoAgentProfit(),
							fee.getPlatformProfit(),fee.getPlatCostFee() });
					
					
					JSONObject json = new JSONObject();
					json.put("merchantName", user.getMerchantName());
					json.put("bankCardNo", request.getBankCardNo());
					json.put("merchantNo", user.getLoginID());
					json.put("terminalno", bankInfo.getPlatTermNo());
					json.put("refetno", ref);
					json.put("date",  DateUtil.getNowTime("yyyy/MM/dd HH:mm:ss"));
					json.put("batchno",  bankKey.getBatchNo());
					json.put("amount", amount);
					response.setList(json.toString());
					
				}
			}
			/** 测试代码块  上线必须删除 **/
			else{
				
				MposDB.updateConsumeTrade("111111111111", DateUtil.getNowTime("yyyyMMdd"), DateUtil.getNowTime("HHmmss"), "",
						0, "00", 0, 0, 0, bankKey.getBatchNo(), "", "", merchantId, terminalId, sendDate,
						request.getSendSeqId());
				
				Fee fee = calProfit(seqID, amount, user);
				MposDB.saveMposFee(new Object[]{
						UtilsConstant.getUUID(),user.getID(), supportID ,fee.getMerchantFee(),fee.getAgentID(),fee.getAgentProfit(),
						fee.getTwoAgentID(),fee.getTwoAgentProfit(),
						fee.getPlatformProfit(),fee.getPlatCostFee() });
				
				
				JSONObject json = new JSONObject();
				json.put("merchantName", user.getMerchantName());
				json.put("bankCardNo", request.getBankCardNo());
				json.put("merchantNo", user.getLoginID());
				json.put("terminalno", bankInfo.getPlatTermNo());
				json.put("refetno", "111111111111");
				json.put("date",  DateUtil.getNowTime("yyyy/MM/dd HH:mm:ss"));
				json.put("batchno",  bankKey.getBatchNo());
				json.put("amount", amount);
				json.put("type", "消费");
				response.setList(json.toString());
				
				response.setRespCode(RespCode.SUCCESS[0]);
				response.setRespDesc(RespCode.SUCCESS[1]);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IsoMessage sendTrade(String SeqId, RequestData request , String termKeyIndex , Map<String,Object> termkey , BankInfo bankInfo , BankKey bankKey , String BankkeyIndex) 
			throws Exception{

		MessageFactory mfact = config.getMessFact8583();
		IsoMessage m_UnionReqMessage = mfact.newMessage(0x0200);
		m_UnionReqMessage.setBinary(true);
		m_UnionReqMessage.setValue(2, request.getBankCardNo(), IsoType.LLNUMERIC, request.getBankCardNo().length());
		m_UnionReqMessage.setValue(3, "000000", IsoType.NUMERIC, 6);
		m_UnionReqMessage.setValue(4, Long.parseLong(request.getAmount()), IsoType.NUMERIC, 12);
		m_UnionReqMessage.setValue(11, SeqId, IsoType.NUMERIC, 6);

		if (request.getPointOfserviceEntryModel() != null) {
			m_UnionReqMessage.setValue(22, request.getPointOfserviceEntryModel() + "0", IsoType.NUMERIC, 4);
		} else {
			if (request.getPin() == null && request.getIcRelatedData() == null) {
				m_UnionReqMessage.setValue(22, "0220", IsoType.NUMERIC, 4);
			} else if (request.getPin() == null && request.getIcRelatedData() != null) {
				m_UnionReqMessage.setValue(22, "0520", IsoType.NUMERIC, 4);
			} else if (request.getPin() != null && request.getIcRelatedData() != null) {
				m_UnionReqMessage.setValue(22, "0510", IsoType.NUMERIC, 4);
			} else {
				m_UnionReqMessage.setValue(22, "0210", IsoType.NUMERIC, 4);
			}
		}
		
		if (request.getCardSerno() != null) {
			m_UnionReqMessage.setValue(23, request.getCardSerno(), IsoType.NUMERIC, 3);
		} else {
			if (request.getIcRelatedData() != null) {
				EmvTLVInfo emu = new EmvTLVInfo();
				emu.AppendData(request.getIcRelatedData());
				String filed23 = emu.getCardSerno();
				m_UnionReqMessage.setValue(23, filed23, IsoType.NUMERIC, 3);
			}
		}
		
		m_UnionReqMessage.setValue(25, "00", IsoType.NUMERIC,2);
		if(request.getPin()!=null){
			m_UnionReqMessage.setValue(26, "06", IsoType.NUMERIC,2);
		}
		
		m_UnionReqMessage.setValue(33, bankKey.getChannelNo(), IsoType.LLVAR, 9);
		
		/**  磁道2数据  **/
		String td2 = EncrptMerchine.DesTrack(UtilsConstant.ObjToStr(termkey.get("TDKey")) , termKeyIndex, request.getTrack2Data());
		
		System.out.println("td2:" + td2);
		
		m_UnionReqMessage.setValue(35, td2 , IsoType.LLHEX , td2.length());
		
		/**  磁道3数据  **/
		if (request.getTrack3Data() != null && request.getTrack3Data().length() != 0) {
			String td3 = EncrptMerchine.DesTrack(UtilsConstant.ObjToStr(termkey.get("TDKey")) , termKeyIndex, request.getTrack3Data());
			m_UnionReqMessage.setValue(36, td3, IsoType.LLLHEX, td3.length());
		}

		m_UnionReqMessage.setValue(41, bankInfo.getPlatTermNo() , IsoType.ALPHA, 8);
		m_UnionReqMessage.setValue(42, bankInfo.getPlatMerchantID() , IsoType.ALPHA, 15);
		m_UnionReqMessage.setValue(49, "156", IsoType.ALPHA, 3);
		
		/** 转pin **/
		if(request.getPin()!=null){
			if(request.getPin()!=null && request.getPin().length()>0){
				String pin = DESUtil.changePin(request.getPin(),request.getBankCardNo(),UtilsConstant.ObjToStr(termkey.get("PinKey")),
						termKeyIndex , bankKey.getPinKey() , BankkeyIndex);
				m_UnionReqMessage.setValue(52, pin, IsoType.ALPHAHEX,16);
			}
		}
		m_UnionReqMessage.setValue(53, "2600000000000000", IsoType.ALPHAHEX, 16);
		
		/** IC卡数据域  **/
		if (request.getIcRelatedData() != null && request.getIcRelatedData().length() != 0) {
			EmvTLVInfo emu = new EmvTLVInfo();
			emu.AppendData(request.getIcRelatedData());
			String filed55 = emu.get55YuStr();
			m_UnionReqMessage.setValue(55, filed55, IsoType.LLLBINARY, filed55.length() / 2);
		}
		
		String filed60 = "";
		if (request.getIcRelatedData() == null) {
			filed60 = "22" + bankKey.getBatchNo() + "000";
		} else {
			filed60 = "22" + bankKey.getBatchNo() + "000501";
		}
		
		m_UnionReqMessage.setValue(60, filed60, IsoType.LLLHEX, filed60.length());
		m_UnionReqMessage.setValue(64, "0000000000000000", IsoType.ALPHAHEX, 16);

		// 计算mac
		ByteBuffer bb = m_UnionReqMessage.writeToBuffer(2);
		byte[] ba8583 = bb.array();
		byte[] bData = Arrays.copyOfRange(ba8583, 13, ba8583.length);
		String mac = EncrptMerchine.bankMac2(DESUtil.bcd2Str(bData), bankKey.getMacKey(), BankkeyIndex);
		m_UnionReqMessage.setValue(64, mac, IsoType.ALPHAHEX, 16);
		return m_UnionReqMessage;
	}
	
	
	/**
	 *   计算手续费
	 * @param order
	 * @param loginUser
	 * @return
	 */
	public Fee calProfit(String seqID ,String amount , TabLoginuser loginUser ){
		
		logger.info("计算订单：" + seqID + "手续费");
		
		Fee fee = new Fee();
		// 交易费率
		String feeRate = "0";
		// 结算费率
		String SettlementRate = "0";
		// t0 附加费用
		int T0additional = 0;
		
		/** 用户费率配置信息 **/
		Map<String,Object> userConfig = TradeDB.getUserConfig(new Object[]{loginUser.getID() ,  Constant.payChannelMpos }); 
		
		/** 代理商信息  **/
		Map<String,Object> agentInfo = AgentDB.agentInfo(new Object[]{loginUser.getAgentID()});
		/**  代理商配置信息  **/
		Map<String,Object> agentConfig = AgentDB.agentConfig(new Object[]{agentInfo.get("ID") , Constant.payChannelMpos}); 
		
		if(agentConfig==null||agentConfig.isEmpty()){
			logger.info("代理商：" + agentInfo.get("ID") + "没有配置支付类型：" +  Constant.payChannelMpos ); 
			return null;
		}
		
		
		/**  商户下放费率   ,   代理商签约成本 ,  渠道成本  **/
		String merchantRate = "0", agentRate = "0" ,  channelRate ="0";
		

		logger.info("订单:" + seqID + "为T1交易"); 
		
		/** 交易类型为T1 **/
		// 交易费率
		feeRate = UtilsConstant.ObjToStr(userConfig.get("T1SaleRate"));
		// 结算费率
		SettlementRate = UtilsConstant.ObjToStr(userConfig.get("T1SettlementRate"));
		//  商户下放费率
		merchantRate = UtilsConstant.ObjToStr(agentConfig.get("MerchantRate"));
		// 代理商成本费率
		agentRate = UtilsConstant.ObjToStr(agentConfig.get("AgentRate"));
		//  渠道成本
		channelRate = UtilsConstant.ObjToStr(agentConfig.get("ChannelRate"));
		
		logger.info(seqID  + "交易费率为：" + feeRate + ",结算费率为：" + SettlementRate + ",商户下放：" + merchantRate  + ",代理商成本：" + agentRate + ",T0渠道成本:" + channelRate + ",T0附加费用：" + T0additional);
			
		/** 商户手续费 **/
		fee.setMerchantFee(AgentDB.makeFeeRounding(amount, Double.valueOf(feeRate)  , 0) + T0additional);
		
		
		/** 商户自己的分润  **/
		int merchantprofit = AgentDB.makeFeeAbandon(amount,AmountUtil.sub(feeRate, SettlementRate), 0);
		
		/** 三级分销总金额  **/
		int distributeProfit = AgentDB.makeFeeAbandon(amount,AmountUtil.sub(SettlementRate, merchantRate), 0);
		
		
		/** 二级代理商商户交易 **/
		if(agentInfo.get("ParentAgentID")!=null&&!agentInfo.get("ParentAgentID").equals(agentInfo.get("ID"))){
			
			logger.info(seqID + "订单的商户的代理商为二级代理商");
			
			//  获得父级代理商ID
			Map<String,Object> agentParent = AgentDB.agentInfo(new Object[]{agentInfo.get("ParentAgentID")});
			//  获取父类代理商配置信息
			Map<String,Object> agentParentConfig = AgentDB.agentConfig(new Object[]{agentParent.get("ID") ,Constant.payChannelMpos });
			//  代理商ID
			fee.setAgentID(agentParent.get("ID").toString());
			//  二级代理商ID
			fee.setTwoAgentID(agentInfo.get("ID").toString());
			
			String parentAgentRate = "0";
			
			parentAgentRate = agentParentConfig.get("AgentRate").toString();
			//  代理商分润
			fee.setAgentProfit(AgentDB.makeFeeAbandon(amount, AmountUtil.sub(agentRate ,parentAgentRate ) ,0));
			//  设置二级代理商分润
			fee.setTwoAgentProfit(AgentDB.makeFeeAbandon(amount, AmountUtil.sub(merchantRate ,agentRate), 0));
			agentRate = parentAgentRate;
		}else{
			logger.info(seqID + "订单商户的代理商为一级代理商");
			// 代理商ID
			fee.setAgentID(agentInfo.get("ID").toString());
			//  代理商分润
			fee.setAgentProfit(AgentDB.makeFeeAbandon(amount, AmountUtil.sub(merchantRate ,agentRate) ,0));
		}
		//计算平台手续费
		fee.setPlatCostFee(AgentDB.makeFeeFurther(amount, Double.valueOf(channelRate),0));
		// 平台收益
		fee.setPlatformProfit(AgentDB.makeFeeFurther(amount,  AmountUtil.sub(agentRate, channelRate),0) + distributeProfit + merchantprofit);
		
		logger.info("订单：" + seqID + "，商户手续费：" + fee.getMerchantFee()  + "，平台收益:" + fee.getPlatformProfit() 
		+ "，平台手续费:" + fee.getPlatCostFee()) ;
		
		
		return fee;
	}
	
	public static void main(String[] args) throws Exception { 
		
//		TabLoginuser user = LoginUserDB.LoginuserInfo("18510978159");
//		
//		RequestData request = new RequestData();
//		ResponseData response = new ResponseData();
//		
//		request.setSendTime("20170730210929");
//		request.setSendSeqId("210929");
//		request.setLoginID("18510978159");
//		request.setAmount("1");
//		request.setBankCardNo("6217730708226277");
//		request.setPin("D3F9E8266B5273E9");
//		request.setTrack2Data("6217730708226277D25152E426D929FD877800");
//		request.setTrack3Data("");
//		request.setTerminalInfo("AB571EF9-DC62-4E4D-AA86-873689BE8B15");
//		request.setIcRelatedData("9F260838EFC2BC91D8A5099F2701809F101307010103A0A804010A0100000000005E65D5549F370462D671399F360200209505088004E0009A031707309C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0F1C89F34030203009F3501229F1E0830223030303034388408A0000003330101019F090200209F4104000000019F631030333032303030300000000000000000");
//		
//		MPOSTradeService mpos = new MPOSTradeService();
//		mpos.mposTrade(user, request, response);
		
		Sign sign = new Sign();
		sign.send();
	}
	
}
