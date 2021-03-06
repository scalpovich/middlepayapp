package com.rhjf.appserver.constant;

public class RespCode {

	public static final String notifySuccess = "SUCCESS";
	
	public static final String notifyfail = "fail";
	
	public static final String[] SUCCESS = {"00", "请求成功"};
	
	public static final String[] userDoesNotExist = {"E000" , "用户信息不存在"};
	
	public static final String[] ParamsError = {"E001" , "参数错误"};
	
	public static final String[] SystemConfigError = {"E002" , "系统配置异常"};
	
	public static final String[] TxndirError = {"E003" , "交易类型错误"};
	
	public static final String[] MerchantNoConfig = {"E004","交易商户配置异常"};
	
	public static final String[] TradeTypeConfigError = {"E005" , "交易类型未开通"};
	
	public static final String[] ServerDBError = {"E006" , "服务器数据异常"};
	
	public static final String[] HttpClientError = {"E007" , "目标服务器请求超时"};
	
	public static final String[] RegisterError = {"E008" , "注册失败,该账号已经存在"};
	
	public static final String[] PasswordError = {"E009"  ,  "密码错误"};
	
	public static final String[] IMGSAVEError = {"E010" , "照片保存失败"};
	
	public static final String[] BindedErrir = {"E011" , "用户只能绑定自己代理商申请的固定码"};
	
	public static final String[] DATANOTEXISTError = {"E012" , "数据不存在"};
	
	public static final String[] SIGNMACError = {"E013" , "mac校验失败"};
	
	public static final String[] INFOError = {"E014" , "信息不正确，请核对后重新输入"};
	
	public static final String[] SMSCodeError = {"E015" , "短息验证码错误，请核对"};
	
	public static final String[] LOGINError = {"E016" , "该账号已被其他设备登录。若非本人操作，请修改密码。"};
	
	public static final String[] MerchantInfoError = {"E017" , "信息处于审核或通过审核状态，目前无法修改信息"};
	
	public static final String[] MerchantInfoStatusError = {"E017" , "信息没有通过审核,无法执行此操作"};
	
	public static final String[] AgentTradeConfigError = {"E018" , "代理商交易配置信息不完整，请联系客服人员"};
	
	public static final String[] AccountNoError = {"E019","结算卡账号只支持储蓄卡"};
	
	public static final String[] SYSTEMError = {"E018" , "网络异常"};
	
	public static final String[] BankCardInfoErroe = {"E020" , "银行卡信息异常"};
	
	public static final String[] MerchantNameError = {"E021" , "商户名不和法，长度大于5位，并且小于12位；名称不能使用数字和英文"};
	
	public static final String[] TurnWalletError = {"E022" , "今天已经转入过"};
	
	public static final String[] TradeTimeError = {"E023" , "当前时间段无法交易"};

	public static final String[] TXAMOUNTError = {"T001" , "提现金额无效"};
	
	public static final String[] TXAMOUNTNOTENOUGH = {"T002" , "账户余额不足"};
	
	public static final String[] TJJQError = {"A001" , "上游通道错误"};
	
	public static final String[] SNBindError =  {"E021" , "设备已经被绑定"};



}
