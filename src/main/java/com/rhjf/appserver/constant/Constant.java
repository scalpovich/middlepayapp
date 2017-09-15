package com.rhjf.appserver.constant;


public class Constant {

	/** T0交易类型 **/
	public static final String T0 = "T0";
	
	/** T1 交易类型 **/
	public static final String T1 = "T1";
	
	/** 支付结果状态码  **/
	public static final String payRetCode = "0000";
	
	public static final String T0RetCode = "1000";
	
	/**  通知订单状态 **/
	public static final String orderStatus = "SUCCESS";
	
	/** 微信扫码支付 **/
	public static final String WX_ScanCode = "WX_SCANCODE";
	
	/** 支付宝扫码支付 **/
	public static final String Ali_ScanCode = "Alipay_SCANCODE";
	
	/** QQ扫码支付 **/
	public static final String QQ_ScanCode = "QQPAY_SCANCODE";
	

	/** 微信固定码交易 **/
	public static final String WX_SCANCODE_JSAPI = "WX_SCANCODE_JSAPI";
	
	/** 支付宝固定码交易 **/
	public static final String Alipay_SCANCODE_JSAPI = "Alipay_SCANCODE_JSAPI";
	
	/** 无卡快捷支付 **/
	public static final String KUAI_ScanCode = "OnlineKuaiPay";
	
	/** 银联二维码 **/
	public static final String UNION_QRCode = "UNIONPAY_QRCODE";
	
	
	public static final String PayChannelWXScancode = "1";
	
	public static final String payChannelAliScancode = "2";
	
	public static final String payChannelQQScancode = "3";
	
	public static final String payChannelKUAIScancode = "4";
	
	public static final String payChannelunionQRCode = "5";
	
	public static final String payChannelMpos = "6";
	

	public static final String cacheName = "sampleCache2";
	
	public static final String[] TradeType = {"扫码支付" , "固定码"};
	
	
	/**   费率 **/
	public static final String FeeRate = "5";
	
	/**  结算费率 **/
	public static final String SettlementRate = "4.6";
	
	
	/** 交易类型-动态秘钥 */
	public static final String TRANS_TP_DY_KEY = "dykey";
	/** 交易类型-实名验证֤ */
	public static final String TRANS_TP_ID_TRANS = "idtrans";
	/** 交易类型 */
	public static String TRADE_TYPE = "";
	/** 动态3DES秘钥 */
	public static String DYN_3DES_KEY = "";
	/** 动态秘钥随机数 */
	public static String RANDOM = "";

	public static final String URL_DYN = Constant.URL + "/auth-web/trans/getDynKey";
	
	public static final String URL_TRADE = Constant.URL + "/auth-web/trans/apiTrans";

		
	/** 版本信息 */
	public static final String VERSION_ID = "20161115";
	
	/** 请根据银联分配的信息填写，如有问题可咨询银联技术支持人员 **/
	/** 用户 */
	public static final String USER_ID = "";
	/** 渠道 */
	public static final String CHANNEL_ID = "";
	/** 3DES秘钥 */
	public static final String DES3_KEY = "";
	/** MD5秘钥 */
	
	public static final String MD5_KEY = "";
	
	public static final String URL = "https://";
	
	
	
	
	/**********************************   信鸽配置参数  *************************************/
	/**  ACCESS ID  **/
	public static final long XingeApp_IOS_ACCESS_ID = ;
	/**  ACCESS_KEY **/
	public static final String XingApp_IOS_ACCESS_KEY = "";
	
	
	public static final long XingApp_Android_ACCESS_ID = ;
	
	public static final String XingApp_Android_ACCESS_KEY = "";
	
	
	
	/*************************************   平台pos 参数   ****************************************/
	/** 代理商编号 **/
	public static final String POS_AGENTNO = "";
	
	/** 商户入网地址 **/
	public static final String POS_ADDMERCHANT_URL = "http://111.207.6.230:18088/back/customerApplay/addCustomerByInterface";
	
	/** 商户机具绑定 **/
	public static final String POS_MERCHANT_BIND_URL = "http://111.207.6.230:18088/back/customerApplay/mposbind";
	
	public static final String POS_MERCHANT_UNBIND_URL = "http://111.207.6.230:18088/back/customerApplay/mposunbind";
	
	/***************************  新平台   参数 ***************************************/
	
	/** 爱码付秘钥 **/
	public static final String REPORT_CHANNELNO = "";
	
	public static final String REPORT_CHANNELNAME = "";
	
	public static final String REPORT_DES3_KEY = "";
	
	public static final String REPORT_SIGN_KEY = "";
	
	public static final String REPORT_QUERY_KEY = "";
	

	
	
	
	
	
	public static final String REPORT_URL = "http://10.10.20.101:11024/middlepayportal/merchant/in";

	
	
	/*****************************************  新平台参数结束  *******************************************/
	
	
	
	/** 异步通知参数列表  按数组顺序排列 **/
	public static final String[] notifyParams = {"trxType","retCode","retMsg","r1_merchantNo",
			"r2_orderNumber","r3_amount","r4_bankId","r5_business","r6_timestamp","r7_completeDate",
			"r8_orderStatus","r9_serialNumber","r10_t0PayResult","sign"};

	
	public static final Integer[]  wxMCCType = {203,2,3,204,205,206,207,208,6,9,19,266,267,268,269,271,272,284,310,315,13,270,90,273,289,311,312,
			42,93,94,95,274,275,281,283,313,23,24,276,277,278,279,104,52,53,280,54,56,316,282,314,66,67,285,325,31,317,40,70,259,75,77,57,287,288,
			58,60,62,103,80,81,92,112,318,96,97,111,292,153,209,210,116,129,293,294,295,296,297,298,305,319,323,123,299,306,320,321,143,157,300,148,
			149,301,307,308,302,303,304,147,230,322,324,155,309,242,158,176,177,164,165,167,290,291,170,172};
	
	public static final String[] alipayMCCType = {"2015050700000010","2015050700000011","2015050700000012","2015050700000013","2015050700000014","2015050700000015",
			"2015050700000016","2015050700000017","2015050700000018","2015050700000019","2015050700000020","2015050700000021","2015050700000022",
			"2015052200000062","2016010600120962","2016031800152626","2016031800154367","2016031800154368","2016031800155597","2016031800156722",
			"2016031800158042","2016031800159500","2015050700000023","2015050700000024","2015050700000025","2015050700000026","2015050700000027",
			"2015050700000028","2015050700000029","2015050700000030","2015050700000031","2015050700000032","2015050700000033","2015050700000048",
			"2015050700000049","2015050700000050","2015050700000051","2015050700000052","2015061690000026","2015050700000056","2015050700000057",
			"2015050700000058","2015050700000059","2015050700000060","2015050700000061","2015061690000027","2016031800159501","2016031800160959",
			"2016031800162476","2016070500193665","2015050700000053","2015050700000054","2015050700000055","2015061690000025","2015050700000038",
			"2015050700000039","2015061690000030","2015050700000040","2015050700000041","2015061690000029","2016062900190066","2015050700000034",
			"2015050700000035","2015050700000036","2015050700000037","2015050700000042","2015050700000043","2015050700000045","2015050700000047",
			"2015062600011157","2015091100061275","2015050700000044","2015050700000046","2015061690000028","2015091000058486","2015091000056956",
			"2015091000060134","2016062800188784","2015090700042466","2016042200000058","2016062900190067","2015090700041394","2015063000012448",
			"2015090700039570","2016012000122673","2016012000124191","2016012000126089","2016012000127534","2016012000128641","2016012000129781",
			"2016012000130831","2015063000017354","2015063000019130","2015101000066113","2015101000064159","2015101000067631","2015063000015529",
			"2016062900190068","2016062900190069","2016062900190070","2016062900190071","2016062900190072","2016081100207105","2016062900190302",
			"2015110500074890","2015110500083341","2015110500077463","2015110500077464","2015110500078657","2015110500073009","2015110500078658",
			"2015110500078659","2015110500075901","2015110500085004","2016012900151604","2016051600179926","2016051600183429","2016051600181795",
			"2016051600179925","2016051600178152","2015110500080520","2016012900148581","2016012900149738","2016012900134880","2016012900138987",
			"2016012900140916","2016062900190077","2016062900190078","2016070700195434","2016070700195435","2016012900145271","2016051000164227",
			"2015063000024698","2016051600176301","2016051000165495","2016063000191709","2016012900143707","2016051000164228","2016051000167013",
			"2016051000168501","2016051000170050","2016051000165496","2016051000171940","2016062900190080","2016051000173119","2016051000171941",
			"2016051000165497","2016062900190081","2016051000175109","2015062600009243","2015062600006253","2015090700035947","2016062900190084",
			"2016062900190085","2016062900190086","2016062900190087","2016062900190088","2016062900190089","2016062900190090","2016062900190082",
			"2016062900190083","2016062900190097","2016062900190098","2016062900190099","2016062900190100","2016062900190101","2016062900190102",
			"2016062900190103","2016062900190104","2016062900190105","2016062900190107","2016062900190108","2016062900190109","2016062900190110",
			"2016062900190111","2016062900190178","2016062900190179","2016062900190180","2016062900190182","2016062900190183","2016062900190184",
			"2016062900190185","2016062900190186","2016062900190187","2016062900190188","2016062900190189","2016062900190190","2016062900190191",
			"2016062900190192","2016062900190193","2016062900190114","2016062900190194","2016062900190195","2016062900190196","2016062900190197",
			"2016062900190198","2016062900190116","2016062900190199","2016062900190200","2016062900190201","2016062900190202","2016062900190203",
			"2016062900190204","2016062900190205","2016062900190206","2016062900190207","2016062900190208","2016062900190209","2016062900190210",
			"2016062900190211","2016062900190119","2016062900190120","2016070500193666","2016062900190212","2016062900190174","2016062900190175",
			"2016062900190176","2016062900190177","2016062900190122","2016062900190123","2016062900190125","2016062900190213","2016062900190214",
			"2016062900190127","2016062900190128","2016062900190215","2016062900190216","2016062900190217","2016062900190130","2016062900190131",
			"2016062900190132","2016062900190133","2015112300119923","2016062900190282","2016062900190283","2016062900190284","2016062900190285",
			"2016062900190286","2016062900190287","2016062900190288","2016062900190289","2016062900190291","2016062900190292","2016062900190293",
			"2016062900190294","2016062900190295","2016062900190306","2016062900190307","2016062900190308","2016062900190309","2016062900190310",
			"2016062900190311","2016062900190312","2016062900190298","2016062900190299","2016062900190300","2016062900190301","2016062900190303",
			"2016062900190304","2015112300087778","2015112300087779","2015112300100346","2015112300103487","2015112300105211","2015112300107119",
			"2015112300108166","2015112300110076","2015112300111726","2015112300113179","2015112300118105","2016062900190313","2016062900190330",
			"2016062900190331","2016062900190332","2016062900190333","2016062900190335","2016062900190336","2016062900190327","2016062900190317",
			"2016062900190318","2016062900190319","2016062900190320","2016062900190328","2016062900190321","2016062900190329","2016062900190322",
			"2016062900190342","2016062900190343","2016062900190344","2016062900190345","2016062900190346","2016062900190347","2016062900190348",
			"2016062900190349","2016062900190350","2016062900190351","2016062900190352","2016062900190353","2016062900190354","2016062900190355",
			"2016062900190356","2016062900190357","2016062900190359","2016062900190360","2016062900190361","2016062900190362","2016062900190363",
			"2016062900190364","2016062900190365","2016062900190366","2016062900190367","2016062900190368","2016062900190369","2016062900190370",
			"2016062900190374","2016062900190375","2016062900190376","2016062900190377","2016062900190378","2016062900190379","2016062900190380",
			"2016062900190381","2016062900190382","2016062900190383","2016062900190384","2016062900190385","2016062900190386","2016062900190388",
			"2016062900190389"};

}
