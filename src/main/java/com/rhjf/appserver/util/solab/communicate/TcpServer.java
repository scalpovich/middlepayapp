package com.rhjf.appserver.util.solab.communicate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rhjf.appserver.util.BytesTool;

public class TcpServer {
	private Log log = LogFactory.getLog(this.getClass());
	private Socket sock;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	int iRecivedLen = 0;
	Thread thread1;
	byte[] baRecived2 = new byte[2048];

	public TcpServer(String serverIp, int serverPort) {
		try {
			sock = new Socket(serverIp, serverPort);
			sock.setSoTimeout(6000000);// 超时时间xx秒
			sock.setKeepAlive(true);
			sock.setReceiveBufferSize(2048);
			sock.setSendBufferSize(2048);

			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());

		} catch (IOException e) {
			log.error(e);
		}
	}

	public TcpServer(String serverIp, int serverPort, int timeOut) {
		try {

			sock = new Socket(serverIp, serverPort);
			sock.setSoTimeout(timeOut);
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());

		} catch (IOException e) {
			log.error(e);
		}
	}

	public int sendAndRecive(byte[] ba2Send, byte[] baRecived) {
		return sendAndRecive(ba2Send, baRecived, true);
	}

	public int sendAndRecive(byte[] ba2Send, byte[] baRecived, boolean isClose) {

		int iReciveLen = 0;
		try {
			// 向外发送生成信息

			out.write(ba2Send);

			// 缴费的回应居然有1132个，原来写1024个长度都不够了

			// 读取接受数据的长度
			byte[] baRecSumLen = new byte[2];
			int ireadLen = in.read(baRecSumLen);
			if (ireadLen < 2) {
				if (in.available() > 0) {
					ireadLen = in.read(baRecSumLen, 1, 1);
				} else {
					// 异常处理
				}
			}

			// 报文长度
			int iSumLen = BytesTool.byteArray2ToInt(baRecSumLen);
			// 写入定义的bytse数组

			byte[] baTmp = new byte[iSumLen];

			int iReadLength = in.read(baTmp);
			if (iReadLength == iSumLen) {
				System.arraycopy(baRecSumLen, 0, baRecived, 0, 2);
				System.arraycopy(baTmp, 0, baRecived, 2, iSumLen);
				iReciveLen = iSumLen + 2;
			}

			if (isClose) {
				sock.close();
			}
				

		} catch (SocketTimeoutException ex1) {
			try {
				log.info("sendAndRecive时出现超时，关闭sock");
				Thread.sleep(2000);
				iReciveLen = -1;
				if (!sock.isClosed())
					sock.close();
			} catch (Exception e1) {
				log.error(e1);
			}
		} catch (Exception e) {
			log.error(e);
			iReciveLen = -1;
		}
		return iReciveLen;
	}

	public int send(byte[] ba2Send) {
		try {
			// log.info(bytesTool.logBytes("发送银商的数据：\n", ba2Send, 0,
			// ba2Send.length));
			out.write(ba2Send);
		} catch (SocketTimeoutException ex1) {
			log.error("send(银商)时出现超时，关闭sock  :", ex1);
			return -1;
		} catch (Exception e) {
			log.error("send(银商)时出错  :" + e);
			return -2;
		}
		return 0;
	}

	public byte[] receive() {
		byte[] baRecived = null;
		try {
			// 缴费的回应居然有1132个，原来写1024个长度都不够了
			// 读取接受数据的长度
			byte[] baRecSumLen = new byte[2];
			log.info("开始读数据");
			int ireadLen = in.read(baRecSumLen);
			if (ireadLen < 2) {
				if (in.available() > 0) {
					ireadLen = in.read(baRecSumLen, 1, 1);
				} else if (ireadLen == -1) {
					// 异常处理
					log.info("Read socket 返回-1，貌似超时了");
					return null;
				}
			}
			// 报文长度
			int iSumLen = BytesTool.byteArray2ToInt(baRecSumLen);
			// 写入定义的byte数组
			byte[] baTmp = new byte[iSumLen];
			int iReadLength = in.read(baTmp);
			baRecived = baTmp;
			/*
			 * baRecived = new byte[iSumLen+2];
			 * 
			 * if(iReadLength == iSumLen){ System.arraycopy(baRecSumLen, 0,
			 * baRecived, 0, 2); System.arraycopy(baTmp, 0, baRecived, 2,
			 * iSumLen); //iReciveLen = iSumLen + 2; }
			 */
			log.info(BytesTool.logBytes("接收银商的数据：\n", baRecived, 0, baRecived.length));
		} catch (SocketTimeoutException ex1) {
			// try {
			log.error("Recive（银商）时出现超时:  " + ex1);
			return baRecived = null;
			// } catch (Exception e1) {

			// }
		} catch (Exception e) {
			log.error("Recive（银商）时出错：  " + e);
			return baRecived = null;
		}
		return baRecived;
	}

}
