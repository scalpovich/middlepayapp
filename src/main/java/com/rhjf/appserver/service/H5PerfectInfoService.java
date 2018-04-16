package com.rhjf.appserver.service;

import java.util.ArrayList;  
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.rhjf.appserver.db.BankCodeDAO;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.TradeDAO;
import org.springframework.stereotype.Service;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.StringEncoding;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.MD5;
import com.rhjf.appserver.util.UtilsConstant;
import com.rhjf.appserver.util.auth.AuthUtil;
import com.rhjf.appserver.util.auth.Author;

import net.sf.json.JSONObject;

@Service
public class H5PerfectInfoService {
	
	LoggerTool log = new LoggerTool(this.getClass());
	/**
	 *   更新用户信息
	 * @param json
	 * @return
	 * @throws Exception 
	 */
	public JSONObject updateUserInfo(String userID , JSONObject json) throws Exception{
		
		JSONObject js = new JSONObject();
		
		// 用户登录的手机号
		String loginID = json.getString("loginID");
		//  商户名称
		String merchantName = json.getString("merchantName"); 
		
		
		boolean flag = UtilsConstant.checkMerchantName(merchantName);
		if(flag){
			log.info("用户："+ userID + "商户名不合法," + merchantName);
			js.put("respCode", "01");
//			js.put("respMsg", RespCode.MerchantInfoError[1]);
			js.put("respMsg" , "商户名无效，长度大于5位，并且小于12位；不能为纯数字或英文；");
			return js;
		}
		
		
		//  签购单显示名称
		String merchantBillName = json.getString("merchantName");
		//  商户联系人名称
		String merchantPersonName = json.getString("merchantPersonName");
		//  商户联系邮箱
		String merchantPersonEmail = "";
		// 营业执照号
		String businessLicense = json.getString("businessLicense");
		// 法人
		String legalPersonName = json.getString("merchantPersonName");
		// 法人身份证
		String legalPersonID = json.getString("legalPersonID");
		// 安装省份
		String installProvince = json.getString("installProvince");
		//  安装城市
		String installCity = json.getString("installCity");
		// 安装区
		String installCounty = json.getString("installCounty");
		// 经营地址
		String operateAddress = json.getString("operateAddress");
		// 商户类型
		String merchantType = "PERSON";

		LoginUserDAO.h5updateUserInfo(new Object[]{merchantType,legalPersonName,legalPersonID,merchantName,installProvince,
				installCity,installCounty,businessLicense,operateAddress,merchantPersonEmail,merchantBillName,merchantPersonName,loginID});
		
		
		/**********  更新结算信息   *************************/
		
		// 开户人名称
		String accountName = json.getString("merchantPersonName");
		// 开户上账号
		String accountNo = json.getString("accountNo");
		// 开户银行
//		String bankName = json.getString("bankName");
		// 支行名称
		String bankBranch = json.getString("bankBranch");
		// 开户行省份
		String bankProv = json.getString("bankProv");
		// 开户行城市
		String bankCity = json.getString("bankCity");
		
		
		Map<String,Object> bankmap = BankCodeDAO.bankBinMap(new Object[]{accountNo});
		if(bankmap == null){
			log.info("银行卡号：" + accountNo + "获取银行名称和银联号失败");
			js.put("respCode", "01");
			js.put("respMsg", "银行卡号：" + accountNo + "获取银行名称和银联号失败");
			return js;
		}
		String bankName = UtilsConstant.ObjToStr( bankmap.get("bankName")); 
		String bankCode = "";
		
		//  结算人信用卡
		String creditCardNo = json.getString("creditCardNo");
		// 结算账户性质  对公或对私
		String bankType = "TOPRIVATE";
		
		List<Map<String,Object>> payChannelList = TradeDAO.getPayChannel();
		
		List<Object[]> userConfig = new ArrayList<Object[]>();
		for (Map<String, Object> map : payChannelList) {
			Object[] obj = new Object[]{UtilsConstant.getUUID(),userID,map.get("ID"),0,0,Constant.FeeRate ,Constant.FeeRate ,Constant.SettlementRate, Constant.SettlementRate};
			userConfig.add(obj);
		}
		TradeDAO.saveUserConfig(userConfig);
		
		//  根据银行名称 开户行省份 城市 等信息 查询分行名称信息表 
		Map<String,Object> bankinfomap = BankCodeDAO.bankInfo(new Object[]{bankName,bankBranch,bankProv,bankCity});
		if(bankinfomap!=null){
			log.info("查询到的银行信息:" + bankinfomap.toString()); 
//			LoginUserDAO.saveOrUpBankInfotest(new Object[]{UtilsConstant.getUUID(),userID,accountName,accountNo,bankinfomap.get("BankBranch"),
//					bankProv,bankCity,bankinfomap.get("BankCode"),bankinfomap.get("BankName"),creditCardNo,bankType
//					,accountName,accountNo,bankinfomap.get("BankBranch"),bankProv,bankCity,bankinfomap.get("BankCode"),bankinfomap.get("BankName"),creditCardNo,bankType});
			bankCode = bankinfomap.get("BankCode").toString();
			bankName = bankinfomap.get("BankName").toString();
			bankBranch = bankinfomap.get("BankBranch").toString();
		}else{
			bankinfomap = BankCodeDAO.bankInfo(new Object[]{bankName,"分行营业部",bankProv,bankCity});
			if(bankinfomap!=null){
				bankCode = bankinfomap.get("BankCode").toString();
				bankName = bankinfomap.get("BankName").toString();
				bankBranch = bankinfomap.get("BankBranch").toString();
			}else{
				bankinfomap = BankCodeDAO.bankInfo(new Object[]{bankName, bankCity + "分行",bankProv,bankCity});
				if(bankinfomap!=null){
					log.info("查询到的银行信息:" + bankinfomap.toString());
					bankCode = bankinfomap.get("BankCode").toString();
					bankBranch = bankinfomap.get("BankBranch").toString();
					bankName = bankinfomap.get("BankName").toString();
				}else{
					log.info("没有查询到银行信息，或返回多条数据，将不进行匹配");
				}
			}
		}
		
		LoginUserDAO.saveOrUpBankInfo(new Object[]{UtilsConstant.getUUID(),userID,accountName,accountNo,bankBranch,bankProv,bankCity,bankCode,bankName,creditCardNo,bankType
				, "",accountName,accountNo,bankBranch,bankProv,bankCity,bankCode,bankName,creditCardNo,bankType , ""});
		
//		/**********  鉴权   *************************/
//		Map<String,String> authMap=new HashMap<String,String>();
//		AuthService authService = new AuthService();
//		authMap.put("accName", accountName);
//		authMap.put("cardNo", accountNo);
//		authMap.put("certificateNo", legalPersonID);
//		Map<String,String> reqMap=authService.authKuai(authMap);
		
		Map<String,String> reqMap = AuthUtil.authentication(accountName,accountNo,legalPersonID,"");
		
		if(reqMap.get("respCode").equals(Author.SUCESS_CODE)){
			//鉴权成功，向上游报商户
			EhcacheUtil ehcache = EhcacheUtil.getInstance();
			LoginUser user = null;
			Object obj = ehcache.get(Constant.cacheName,loginID + "UserInfo" );
			if(obj == null){
				log.info("查询数据库");
				user = this.loginuser(loginID);
				if(user == null){
					log.info("未查到用户 " + loginID +"信息");
				}
				ehcache.put(Constant.cacheName, loginID + "UserInfo" , user);
			}else{
				log.info("查询缓存");
				user = (LoginUser) obj;
			}
			int alipaylength = Constant.alipayMCCType.length;
			
			Random random = new Random(alipaylength-1);
			int index = random.nextInt(alipaylength-1);
			String alipaymcccNumber = Constant.alipayMCCType[index];
			Integer wxmcccNumber = Constant.wxMCCType[index];
			Map<String,Object> wxmap = this.getUserConfig(new Object[]{user.getID(),1});
			Map<String,Object> alipaymap = this.getUserConfig(new Object[]{user.getID(),2});
			Map<String,Object> map = new TreeMap<String, Object>();
			map.put("channelName", Constant.REPORT_CHANNELNAME);
			map.put("channelNo", Constant.REPORT_CHANNELNO);
			map.put("merchantName", merchantName);
			map.put("merchantBillName", merchantBillName);
			map.put("installProvince", installProvince);
			map.put("installCity",  installCity);
			map.put("installCounty", installCounty);
			map.put("operateAddress", operateAddress);
			map.put("merchantType", merchantType);
			map.put("businessLicense", businessLicense);
			map.put("legalPersonName", legalPersonName);
			map.put("legalPersonID", legalPersonID);
			map.put("merchantPersonName", merchantPersonName);
			map.put("merchantPersonPhone",  loginID);
			
			map.put("wxType", wxmcccNumber);
			map.put("wxT1Fee", 	Double.parseDouble(wxmap.get("T1SaleRate").toString())/10.0);
			map.put("wxT0Fee",  Double.parseDouble(wxmap.get("T0SaleRate").toString())/10.0);
			
			map.put("alipayType", alipaymcccNumber);
			map.put("alipayT1Fee", Double.parseDouble(alipaymap.get("T1SaleRate").toString())/10.0);
			map.put("alipayT0Fee", Double.parseDouble(alipaymap.get("T0SaleRate").toString())/10.0);
			
			map.put("bankType", bankType);
			map.put("accountName", accountName);
			map.put("accountNo", DESUtil.encode(Constant.REPORT_DES3_KEY,accountNo));
			map.put("bankName", bankName);
			map.put("bankProv", bankProv);
			map.put("bankCity", bankCity);
			map.put("bankBranch", bankBranch);
			map.put("bankCode", bankCode);

			
			log.info("需要签名的的数据：" + JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY); 
			
			String sign = MD5.sign( JSONObject.fromObject(map).toString() + Constant.REPORT_SIGN_KEY , StringEncoding.UTF_8);
			map.put("sign", sign.toUpperCase());
			
			log.info("用户" + loginID + "入网请求报文:" + map.toString());
			
			try {
				String content = HttpClient.post(Constant.REPORT_URL, map, null);
				
				log.info("入网响应报文:" + content);
				JSONObject respJS = JSONObject.fromObject(content);
				
				String respCode = respJS.getString("respCode");
				
				if(Constant.payRetCode.equals(respCode)){
					
					String merchantNo = respJS.getString("merchantNo");// 商户号
					String signKey = respJS.getString("signKey");		//  微信签名秘钥
					String desKey = respJS.getString("desKey");			//  微信des秘钥
					String queryKey = respJS.getString("queryKey");		//  查询秘钥
					
//					String AlipaySignKey = respJS.getString("AlipaySignKey");	// 支付宝签名秘钥
//					String AlipaydesKey = respJS.getString("AlipaydesKey");		// 支付des秘钥
					//MerchantID,MerchantName,SignKey,DESKey,QueryKey,UserID,PayType
					
					/*
					 *  保存商户秘钥等信息 
					 *
					 */
					List<Object[]> list = new ArrayList<Object[]>();
					Object[] objs = new Object[]{merchantNo,merchantName,signKey,desKey,queryKey,user.getID(),Constant.PayChannelWXScancode};
					list.add(objs);
					objs = new Object[]{merchantNo,merchantName,signKey,desKey,queryKey,user.getID(),Constant.payChannelAliScancode};
					list.add(objs);
					this.saveMerchantInfo(list);
					
					this.updateUserBankStatus(new Object[]{1, 0 , loginID});
					if (user.getThreeLevel()!=null && !user.getThreeLevel().toString().equals("")) {
						log.info("判断商户上级商户是否需要升级");
						String levelUpResult=this.levelUp(user.getThreeLevel().toString());
						if (levelUpResult.equals("fail")) {
							log.info("用户上级商户ID："+user.getThreeLevel()+"升级失败;当前等级--"+user.getMerchantLeve());
						}
					}
					js.put("respCode", "00");
					js.put("respMsg", "提交成功");
				}else{
					this.updateUserBankStatus(new Object[]{0 , 0 , loginID});
					
					js.put("respCode", "01");
					js.put("respMsg", respJS.get("respMsg"));
				}
			} catch (Exception e) {
				log.error(loginID + "入网异常：" + e.getMessage());
				js.put("respCode", "01");
				js.put("respMsg", e.getMessage());
			}
			
		}else{
			js.put("respCode", reqMap.get("respCode"));
			js.put("respMsg", reqMap.get("respMsg"));
		}
		return js; 
	}
	
	
	public int[] saveMerchantInfo(List<Object[]> list) {
		return LoginUserDAO.saveMerchantInfo(list);
	}


	public List<Map<String,Object>> merchantTypeList(){
		return LoginUserDAO.merchantTypeList();
	}
	
	
	public LoginUser getMerchantInfoByLoginID(String loginID){
		return LoginUserDAO.LoginuserInfo(loginID);
	}
	
	public Map<String,Object> getUserBankCard(String userID){
		return LoginUserDAO.getUserBankCard(userID);
	}
	
	public Map<String,Object> getUserConfig(Object[] obj){
		return TradeDAO.getUserConfig(obj);
	}
	
	public LoginUser loginuser(String loginID){
		return LoginUserDAO.LoginuserInfo(loginID);
	}
	
	public int updateUserBankStatus(Object[] obj){
		return LoginUserDAO.updateUserBankStatus(obj);
	}
	
	public Map<String,Object> bankBinMap(Object[] obj){
		return BankCodeDAO.bankBinMap(obj);
	}
	
	public String levelUp(String UserID) {
		// 查询出审核用户的上一级用户商户类型和用户ID (topUser)
		try {
			LoginUser user = LoginUserDAO.getLoginuserInfo(UserID);
			if (user != null) {
				int MerchantLevel = user.getMerchantLeve();

				log.info("上级商户 " + UserID + "目前等级为：" + MerchantLevel);

				if (MerchantLevel < 2) {
					int userCount = LoginUserDAO.getUserCount(UserID);
					log.info("上级商户：" + UserID + "直接扩展商户 ，  并且通过审核的数量：" + userCount);
					
					Map<String, Object> result = LoginUserDAO.userLevelUpCount(MerchantLevel + 1);
					
					int levelUpCount = Integer.parseInt(result.get("UserCount").toString());
					
					log.info("上级商户：" + UserID + "如果升级需要扩展的人数：" + levelUpCount);
					
					if (userCount >= levelUpCount) {
						
						log.info("上级商户：" + UserID + "符合升级条件：当前扩展人数：" + userCount + "，系统需要扩展人数：" + levelUpCount ); 
						
						// 升级
						int modifyLoginUserMerchantLevel = LoginUserDAO.updateUserLev(MerchantLevel + 1, UserID);
						if (modifyLoginUserMerchantLevel == 0) {
							log.info("上级商户：" + UserID + "更新商户等级失败");
							
							return "fail";
						}
						// 同时查出升级之后费率（使用升级后商户类型查询费率）
						int statusResult = LoginUserDAO.updateUserRate(new Object[]{UserID , MerchantLevel + 1});
						if (statusResult == 0) {
							
							log.info("上级商户：" + UserID + "更新商户费率失败");
							return "fail";
						}
					}
				}else{
					log.info("上级商户：" + UserID + "目前等级已经为2 是最高等级，无需升级");
				}
			}else{
				log.info("上级商户ID:" + UserID + "不存在");
				return "fail";
			}
		} catch (Exception e) {
			log.info("商户升级失败,错误信息为" + e.getMessage());
			return "fail";
		}
		
		log.info("上级商户：" + UserID + "升级成功");

		return "success";
	}
	
}
