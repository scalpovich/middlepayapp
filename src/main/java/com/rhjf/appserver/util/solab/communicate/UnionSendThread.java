package com.rhjf.appserver.util.solab.communicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rhjf.appserver.util.BytesTool;

public class UnionSendThread extends Thread {
	private int nRet = 0;
	private Config config = Config.getInstance();
	private UnionSession umsSession = UnionSession.getInstance();
	private SendMessageList sendList = SendMessageList.getInstance();
	private Log log = LogFactory.getLog(this.getClass());
	private BytesTool bytesTool = new BytesTool();

	public void run() {
		// 读取发送队列中的交易报文发送银商
		TcpServer tcpserver = umsSession.getTcpServer();
		while (!umsSession.getExit()) {
			byte[] szSendMessage = null;
			szSendMessage = sendList.getMessage();
			;
			if (szSendMessage != null)// 如果有值
			{
				// 发送等待返回结果，无论是否失败全部删除发送队列中的数据
				nRet = tcpserver.send(szSendMessage);
				if (nRet == 0) {
					log.info(bytesTool.logBytes("发送(银商)成功,执行删除数据：\n", szSendMessage, 0, szSendMessage.length));
				} else {
					log.info(bytesTool.logBytes("发送（银商）数据失败，失败数据为：\n", szSendMessage, 0, szSendMessage.length));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.error("发送（银商）失败，sleep异常：" + e);
						e.printStackTrace();
					}
					tcpserver = umsSession.getTcpServer();
				}
				sendList.removeMessage();
			} else// 没有值
			{
				try {
					Thread.sleep(500);
					tcpserver = umsSession.getTcpServer();
				} catch (InterruptedException e) {
					log.error("读取发送队列，sleep异常：" + e);
					e.printStackTrace();
				}
			}
		}
	}
}
