package com.rhjf.appserver.util.solab.communicate;

public class UnionSession {

	private boolean m_bExit = false;
	private boolean m_bSocketError = false;
	private TcpServer m_TcpServer;

	private static UnionSession instance = null;

	public static UnionSession getInstance() {
		if (instance == null) {
			instance = new UnionSession();
			return instance;
		} else {
			return instance;
		}
	}

	public UnionSession() {
	}

	public void setTcpServer(TcpServer pTcpServer) {
		m_TcpServer = pTcpServer;
	}

	public TcpServer getTcpServer() {
		return m_TcpServer;
	}

	public void setExit(boolean bExit) {
		m_bExit = bExit;
	}

	public boolean getExit() {
		return m_bExit;
	}

	public void setSocketError(boolean bSocketError) {
		m_bSocketError = bSocketError;
	}

	public boolean getSocketError() {
		return m_bSocketError;
	}

}
