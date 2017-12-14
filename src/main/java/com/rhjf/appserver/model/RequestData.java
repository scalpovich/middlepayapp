package com.rhjf.appserver.model;

import java.io.Serializable;


/**
 *   请求实体类
 * @author a
 *
 */
public class RequestData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1010811141837310709L;

	/** 登录账号 **/
	private String loginID;
	
	/** 登录密码 **/
	private String loginPwd;
	
	/** 新密码 **/
	private String newLoginPwd;

	/**  请求类型 **/
	private String txndir;
	
	/** 发起交易时间 **/
	private String sendTime;
	
	/**  交易金额  **/
	private String amount;
	
	/**  终端流水号 **/
	private String sendSeqId;
	
	/** 支付类型 微信或支付宝 **/
	private String payChannel;
	
	/** 终端版本号 **/
	private String version;
	
	private String agencyNumber;
	
	/**  * 信用卡类别（区分银行） */
	private String bankId;

	/**  * 信用卡类别（区分银行） */
	private String bankType;
	
	/** 终端信息 **/
	private String terminalInfo;
	
	/**  注册推广人 **/
	private String tgr;
	
	/** * 手机号  */
	private String phoneNumber;
	
	/** 身份证号 **/
	private String idNumber;
	
	/** 真实姓名 **/
	private String realName;
	
	/**  完善资料  真实姓名 **/
	private String name;
	
	/** 商户名称 **/
	private String merchantName;

	/** 银行名称 **/
	private String bankName;
	
	/** 分行名称 **/
	private String bankSubbranch;
	
	/** 银行卡号 **/
	private String bankCardNo;
	
	/** 所在省份 **/
	private String state;
	
	/** 所在城市  **/
	private String city;
	
	/** 所在区 **/
	private String county;
	
	/** 联系地址 **/
	private String address;
	
	/** 门牌号 **/
	private String houseNumber;
	
	/** 邮箱 **/
	private String email;
	
	/** 信用卡账号 **/
	private String creditCardNo;
	
	/** 开户行省份 **/
	private String bankProv;
	
	/** 开户行城市 **/
	private String bankCity;
	
	/** 手持身份证照片 **/
	private String handheldIDPhoto;
	
	/** 身份证正面照片 **/
	private String iDCardFrontPhoto;
	
	/** 身份证反面照片 **/
	private String iDCardReversePhoto;
	
	/** 银行卡照片 **/
	private String bankCardPhoto;
	
	/** 营业执照照片 **/
	private String businessPhoto;
	
	/**  到账类型 T1 或者T0 **/
	private String tradeCode;
	
	/** 固定码访问地址  **/
	private String qrcodeurl;
	
	/**  数据校验mac值 **/
	private String mac;
	
	/** 提现类型  信用卡提现和 分润提现 **/
	private String txType;
	
	/** 短信验证码 **/
	private String smsCode;
	
	/** ios设备token **/
	private String deviceToken;
	
	/** 设备类型 **/
	private String deviceType;
	
	/** 银行预留手机号 **/
	private String payerPhone;
	/** 订单号 **/
	private String orderNumber;
	/** 商户号 **/
	private String merchantNo;
	
	/** 显示的页数 **/
	private Integer page;
	
	/** 查询交易月报和交易报表的时间条件  格式为 年月 如 201607 **/
	private String tradeDate;

	
	/**  mpos 交易密码  **/
	private String pin;
	
	/** 磁道2数据 **/
	private String track2Data;
	
	/** 磁道3数据 **/
	private String track3Data;
	
	/** IC卡数据域  **/
	private String icRelatedData;
	
	/** 服务条件点码 **/
	private String pointOfserviceEntryModel;
	
	/** 卡序列号 **/
	private String cardSerno;
	/** mpos 设备sn 号 **/
	private String sn;
	
	/** mpos 交易 签名照片 **/
	private String signImg;
	
	/**  mpos 交易参考号 **/
	private String refetno;
	
	/**  文本内容 (意见反馈) **/
	private String message;
	
	/**  商户ID **/
	private String merchantID;
	
	/** 信用卡cvn2号码 **/
	private String cvn2;
	
	/** 卡片有效期 信用卡有效期 示例： 09/15 填1509 **/
	private String expired;
	
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getPayerPhone() {
		return payerPhone;
	}

	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getTxndir() {
		return txndir;
	}

	public void setTxndir(String txndir) {
		this.txndir = txndir;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSendSeqId() {
		return sendSeqId;
	}

	public void setSendSeqId(String sendSeqId) {
		this.sendSeqId = sendSeqId;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}


	public String getTerminalInfo() {
		return terminalInfo;
	}
	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}

	public String getAgencyNumber() {
		return agencyNumber;
	}

	public void setAgencyNumber(String agencyNumber) {
		this.agencyNumber = agencyNumber;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}


	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankSubbranch() {
		return bankSubbranch;
	}

	public void setBankSubbranch(String bankSubbranch) {
		this.bankSubbranch = bankSubbranch;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHandheldIDPhoto() {
		return handheldIDPhoto;
	}

	public void setHandheldIDPhoto(String handheldIDPhoto) {
		this.handheldIDPhoto = handheldIDPhoto;
	}

	public String getIDCardFrontPhoto() {
		return iDCardFrontPhoto;
	}

	public void setIDCardFrontPhoto(String iDCardFrontPhoto) {
		this.iDCardFrontPhoto = iDCardFrontPhoto;
	}

	public String getIDCardReversePhoto() {
		return iDCardReversePhoto;
	}

	public void setIDCardReversePhoto(String iDCardReversePhoto) {
		this.iDCardReversePhoto = iDCardReversePhoto;
	}

	public String getBankCardPhoto() {
		return bankCardPhoto;
	}

	public void setBankCardPhoto(String bankCardPhoto) {
		this.bankCardPhoto = bankCardPhoto;
	}

	public String getBusinessPhoto() {
		return businessPhoto;
	}

	public void setBusinessPhoto(String businessPhoto) {
		this.businessPhoto = businessPhoto;
	}

	public String getTgr() {
		return tgr;
	}

	public void setTgr(String tgr) {
		this.tgr = tgr;
	}


	public String getNewLoginPwd() {
		return newLoginPwd;
	}

	public void setNewLoginPwd(String newLoginPwd) {
		this.newLoginPwd = newLoginPwd;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getQrcodeurl() {
		return qrcodeurl;
	}

	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getBankProv() {
		return bankProv;
	}

	public void setBankProv(String bankProv) {
		this.bankProv = bankProv;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getTrack2Data() {
		return track2Data;
	}

	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}

	public String getTrack3Data() {
		return track3Data;
	}

	public void setTrack3Data(String track3Data) {
		this.track3Data = track3Data;
	}

	public String getIcRelatedData() {
		return icRelatedData;
	}

	public void setIcRelatedData(String icRelatedData) {
		this.icRelatedData = icRelatedData;
	}

	public String getPointOfserviceEntryModel() {
		return pointOfserviceEntryModel;
	}
	public void setPointOfserviceEntryModel(String pointOfserviceEntryModel) {
		this.pointOfserviceEntryModel = pointOfserviceEntryModel;
	}

	public String getCardSerno() {
		return cardSerno;
	}

	public void setCardSerno(String cardSerno) {
		this.cardSerno = cardSerno;
	}
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSignImg() {
		return signImg;
	}

	public void setSignImg(String signImg) {
		this.signImg = signImg;
	}

	public String getRefetno() {
		return refetno;
	}
	public void setRefetno(String refetno) {
		this.refetno = refetno;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}
	
}
