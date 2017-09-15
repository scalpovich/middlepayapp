package com.rhjf.appserver.util.solab.communicate;

import java.text.ParseException; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rhjf.appserver.util.BytesTool;
import com.rhjf.appserver.util.solab.iso8583.IsoMessage;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;

public class UnionRecvThread extends Thread {
	private int nRet = 0;
	private Config config = Config.getInstance();
	private UnionSession umsSession = UnionSession.getInstance();
	private RecvMessageList recvList = RecvMessageList.getInstance();
	private Log log = LogFactory.getLog(this.getClass());
	private BytesTool bytesTool = new BytesTool();
	MessageFactory msgFactory = config.getMessFact8583();
	int nDataLen = 0;

	public UnionRecvThread() {
	}

	public UnionRecvThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}

	public void run() {
		TcpServer tcpserver = umsSession.getTcpServer();
		while (!umsSession.getExit()) {

			// 读取银商返回结果，解析出商户号，终端号，交易流水，交易类别，存入消息队列
			byte[] szRecvMessage = null;
			szRecvMessage = tcpserver.receive();
			if (szRecvMessage != null) {
				IsoMessage message;
				try {
					message = msgFactory.parseMessage(szRecvMessage, 11);
					String transType = String.format("%04x", message.getType());
					String field3 = message.getObjectValue(3) != null ? message.getObjectValue(3).toString() : "";
					String terId = message.getObjectValue(41) != null ? message.getObjectValue(41).toString() : "";
					String merId = message.getObjectValue(42) != null ? message.getObjectValue(42).toString() : "";
					String seq = message.getObjectValue(11) != null ? message.getObjectValue(11).toString() : "";
					String batchNum = message.getObjectValue(60) != null
							? message.getObjectValue(60).toString().substring(2, 8) : "";
					// 解析szRecvMessage,获取到商户号、终端号、交易流水号
					recvList.putMessage(transType, field3, merId, terId, batchNum, seq, message);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				// 没有值，重新建连
				TcpServer tcpServer1 = new TcpServer(config.getUnionPayServerIp(), config.getUnionPayServerPort());
				umsSession.setTcpServer(tcpServer1);
				tcpserver = tcpServer1;
			}
		}
	}
}
