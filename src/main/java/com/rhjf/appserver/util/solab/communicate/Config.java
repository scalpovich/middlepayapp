package com.rhjf.appserver.util.solab.communicate;

import java.io.InputStream; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;
import com.rhjf.appserver.util.solab.iso8583.parse.ConfigParser;

/**
 * 
 * @author kx
 * @date 2014-2-24
 *
 */
public class Config {

	LoggerTool log = new LoggerTool(this.getClass());
	
	private static Config instance = null;
	private String bankId = "00000626";
	private int serverPort = 0;
	private MessageFactory messFact8583 = null;
	private String unionPayServerIp = "";
	private int unionPayServerPort = 0;

	private String merchantId = "";
	private String terminalId = "";

	private String cryptServerIp = "";
	private int cryptServerPort = 0;

	private String sekIndexTmk = null;

	private String sekIndexMac = null;

	private int nTermIndexMaK = 0;
	private int nTermIndexPinK = 0;
	private int nTermIndexTMK = 0;

	private int nBankIndexMaK = 0;
	private int nBankIndexPinK = 0;
	private int nBankIndexTMK = 0;

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
			return instance;
		} else {
			return instance;
		}
	}

	public Config() {
		loadConfig();
	}

	private void loadConfig() {

		InputStream is = Config.class.getClassLoader().getResourceAsStream("config.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = builder.parse(is);
			if (doc == null) {
				throw new Exception("unable to load Document");
			}

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();

			XPathExpression expr = xpath.compile("/SetUp/ServerPort");
			Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			serverPort = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/UnionPayServer/Ip");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			unionPayServerIp = node.getTextContent();

			expr = xpath.compile("/SetUp/UnionPayServer/Port");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			unionPayServerPort = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/UnionPayServer/TermialId");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			terminalId = node.getTextContent();

			expr = xpath.compile("/SetUp/UnionPayServer/MerchantId");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			merchantId = node.getTextContent();

			// ���ܻ�����
			expr = xpath.compile("/SetUp/CryptServer/Ip");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			cryptServerIp = node.getTextContent();

			expr = xpath.compile("/SetUp/CryptServer/Port");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			cryptServerPort = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/CryptServer/SekIndexTMK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			sekIndexTmk = node.getTextContent();

			expr = xpath.compile("/SetUp/CryptServer/SekIndexMAK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			sekIndexMac = node.getTextContent();

			expr = xpath.compile("/SetUp/TermKeyInfo/IndexTMK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nTermIndexTMK = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/TermKeyInfo/IndexMAK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nTermIndexMaK = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/TermKeyInfo/IndexPinK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nTermIndexPinK = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/BankKeyInfo/IndexTMK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nBankIndexTMK = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/BankKeyInfo/IndexMAK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nBankIndexMaK = Integer.parseInt(node.getTextContent());

			expr = xpath.compile("/SetUp/BankKeyInfo/IndexPinK");
			node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			nBankIndexPinK = Integer.parseInt(node.getTextContent());

			messFact8583 = ConfigParser.createFromClasspathConfig("8583config.xml");

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public int getServerPort() {
		return serverPort;
	}

	public MessageFactory getMessFact8583() {
		return messFact8583;
	}

	public String getUnionPayServerIp() {
		return unionPayServerIp;
	}

	public int getUnionPayServerPort() {
		return unionPayServerPort;
	}

	public String getCryptServerIp() {
		return cryptServerIp;
	}

	public void setCryptServerIp(String cryptServerIp) {
		this.cryptServerIp = cryptServerIp;
	}

	public int getCryptServerPort() {
		return cryptServerPort;
	}

	public void setCryptServerPort(int cryptServerPort) {
		this.cryptServerPort = cryptServerPort;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getSekIndexTmk() {
		return sekIndexTmk;
	}

	public void setSekIndexTmk(String sekIndexTmk) {
		this.sekIndexTmk = sekIndexTmk;
	}

	public String getSekIndexMac() {
		return sekIndexMac;
	}

	public void setSekIndexMac(String sekIndexMac) {
		this.sekIndexMac = sekIndexMac;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public int getTermIndexMaK() {
		return nTermIndexMaK;
	}

	public int getTermIndexPinK() {
		return nTermIndexPinK;
	}

	public int getTermIndexTMK() {
		return nTermIndexTMK;
	}

	public int getBankIndexMaK() {
		return nBankIndexMaK;
	}

	public int getBankIndexPinK() {
		return nBankIndexPinK;
	}

	public int getBankIndexTMK() {
		return nBankIndexTMK;
	}

}
