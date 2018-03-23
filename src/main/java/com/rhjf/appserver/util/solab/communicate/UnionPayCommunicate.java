package com.rhjf.appserver.util.solab.communicate;

import java.nio.ByteBuffer; 
import java.text.ParseException;


import com.rhjf.appserver.util.BytesTool;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.solab.iso8583.IsoMessage;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;

public class UnionPayCommunicate {

	private Config config = Config.getInstance();
	private IsoMessage m_UnionRspMessage;
	
	
	LoggerTool log = new LoggerTool(this.getClass());

	public UnionPayCommunicate() {
	}

	public UnionPayCommunicate getInstance() {
		return new UnionPayCommunicate();
	}


	public IsoMessage getRespMessage() {
		return m_UnionRspMessage;
	}

	public IsoMessage UnionCommunicate(IsoMessage UnionReqMessage) throws ParseException {
		MessageFactory msgFactory = this.config.getMessFact8583();
		msgFactory.setUseBinaryMessages(true);
		ByteBuffer bb = UnionReqMessage.writeToBuffer(2);
		byte[] bUnionPayReqMsg = bb.array();
		this.log.info(BytesTool.logBytes("上送银联报文：\n", bUnionPayReqMsg, 0, bUnionPayReqMsg.length));

		byte[] bUnionPayRspMsg = new byte[2048];

		TcpServer tcpserver = new TcpServer(this.config.getUnionPayServerIp(), this.config.getUnionPayServerPort());
		int iUnionPayRspMsgLen = tcpserver.sendAndRecive(bUnionPayReqMsg, bUnionPayRspMsg);
		this.log.info(BytesTool.logBytes("接受银联报文：\n", bUnionPayRspMsg, 0, iUnionPayRspMsgLen));
		msgFactory.setUseBinaryMessages(true);
		return msgFactory.parseMessage(bUnionPayRspMsg, 13);
		
//		return null;

	}

	public IsoMessage UnionCommunicate(IsoMessage UnionReqMessage, int NetCode) throws ParseException {
		MessageFactory msgFactory = this.config.getMessFact8583();
		msgFactory.setUseBinaryMessages(true);
		ByteBuffer bb = UnionReqMessage.writeToBuffer(2);
		byte[] bUnionPayReqMsg = bb.array();
		this.log.info(BytesTool.logBytes("上送银联报文：\n", bUnionPayReqMsg, 0, bUnionPayReqMsg.length));

		byte[] bUnionPayRspMsg = new byte[2048];

		TcpServer tcpserver = new TcpServer(this.config.getUnionPayServerIp(), this.config.getUnionPayServerPort());
		int iUnionPayRspMsgLen = tcpserver.sendAndRecive(bUnionPayReqMsg, bUnionPayRspMsg);
		this.log.info(BytesTool.logBytes("接受银联报文：\n", bUnionPayRspMsg, 0, iUnionPayRspMsgLen));
		msgFactory.setUseBinaryMessages(true);
		return msgFactory.parseMessage(bUnionPayRspMsg, 13);

	}

	public String getDownType(String upType) {
		if (upType.equalsIgnoreCase("0200")) {
			return "0210";
		}

		if (upType.equalsIgnoreCase("0400")) {
			return "0410";
		}

		if (upType.equalsIgnoreCase("0220")) {
			return "0230";
		}

		if (upType.equalsIgnoreCase("0800")) {
			return "0810";
		}

		if (upType.equalsIgnoreCase("0820")) {
			return "0830";
		}

		if (upType.equalsIgnoreCase("0500")) {
			return "0510";
		}

		if (upType.equalsIgnoreCase("0320")) {
			return "0330";
		}

		if (upType.equalsIgnoreCase("0620")) {
			return "0630";
		}

		return null;
	}
}
