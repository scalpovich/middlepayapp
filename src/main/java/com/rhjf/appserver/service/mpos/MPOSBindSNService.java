package com.rhjf.appserver.service.mpos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.MCCConstant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.MposDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.HttpClient;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONObject;

public class MPOSBindSNService {

	
	LoggerTool logger = new LoggerTool(this.getClass());
	
	public void mposBindSN(LoginUser user ,  RequestData  request, ResponseData response){
		
		String sn = request.getSn();
		
		boolean flag =  MposDAO.getBindSN(sn);
		if(flag){
			logger.info("设备:" + sn + "已经绑定过了");
			response.setRespCode(RespCode.SNBindError[0]);
			response.setRespDesc(RespCode.SNBindError[1]);
			return ;
		}
		
		Map<String , Object> merchantInfo = MposDAO.getMPOSMerchant(user.getID());
		
		if(merchantInfo == null || merchantInfo.isEmpty()){
			logger.info("查询pos商户失败");
			
			/** 如果查询mpos商户为空  将向上游入网  **/
		
			Map<String,Object> merchantConfig = TradeDAO.getUserConfig(new Object[]{user.getID() , Constant.payChannelMpos});
			
			if(merchantConfig == null || merchantConfig.isEmpty()){
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(new Object[]{UtilsConstant.getUUID() , user.getID() , Constant.payChannelMpos , 0 , 0 , Constant.FeeRate , Constant.FeeRate,
						Constant.FeeRate , Constant.FeeRate });
				int x = TradeDAO.saveUserConfig(list)[0];
				if(x < 0 ){
					logger.info("用户：" + user.getID() + "支付类型：" + Constant.payChannelMpos + " 支付配置信息获取失败,停止交易操作" );
					response.setRespCode(RespCode.TradeTypeConfigError[0]);
					response.setRespDesc(RespCode.TradeTypeConfigError[1]); 
					return ;
				}else{
					merchantConfig = TradeDAO.getUserConfig(new Object[]{ user.getID() , Constant.payChannelMpos});
				}
			}
			
			Map<String,Object> merchantBankInfO = LoginUserDAO.getUserBankCard(user.getID());
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			Object[] keys =  MCCConstant.mcc.keySet().toArray();
			
			Random random = new Random(keys.length-1);
			int index = random.nextInt(keys.length-1);
			String key = (String)keys[index];
			
			String value  = MCCConstant.mcc.get(key);
			
			String province = "" , code = "";
			for (int i = 0; i < MCCConstant.cityName.length; i++) {
				if(MCCConstant.cityName[i].contains(user.getCity())){
					province = MCCConstant.parantCode[i];
					code = MCCConstant.cityCode[i];
					break;
				}
			}
			
			params.put("agentNo", Constant.POS_AGENTNO); 							//代理商标号
			params.put("bizDomain", key);     							//经营范围
			params.put("mcc", value);										//mcc
			params.put("fullName", user.getMerchantName());						//商户名全称
			params.put("shortName", user.getMerchantName());								//商户简称
			params.put("address", user.getAddress());					//注册地址
			params.put("province", province);									//安装归属省
			params.put("city", code);										//安装归属市
			params.put("posShopAddress", user.getAddress());				//安装归属地址
			params.put("posPerson", user.getName());									//联系人
			params.put("posContact", user.getLoginID());						//电话
			params.put("legalName", user.getName());									//法人
			params.put("legalId", user.getIDCardNo());					//法人证件号码
			params.put("licenseNo", user.getBusinessLicense());					//营业执照号
			params.put("settleBank", "unknwoun");								//银行编码  bankInfo.get("BankSymbol").toString()
			params.put("settleAccountType", "对私");							//入账对公/对私  目前只支持对私
			params.put("settleSubbranch", merchantBankInfO.get("BankBranch").toString());		//开户行名称
			params.put("settleAccountNo", merchantBankInfO.get("AccountNo").toString());			//开户账号
			params.put("settleAccountName", merchantBankInfO.get("AccountName").toString());							//入账人名称
			params.put("settleUnionNo",  merchantBankInfO.get("BankCode").toString());					//支付系统行号
			params.put("settleMobile", user.getLoginID());						//结算卡关联手机号
			params.put("rate", String.valueOf(Double.parseDouble(merchantConfig.get("T1SaleRate").toString())/10.0));										//借记卡费率
			params.put("ceilingLimit", "9999");								//借记卡封顶金额-元/笔
			params.put("ccRate", String.valueOf(Double.parseDouble(merchantConfig.get("T1SaleRate").toString())/10.0));									//贷记卡费率-?%
			params.put("terminal", request.getSn());
			params.put("d0Flag", "N");										//D0 标志  Y/N
			params.put("mccCategory", "1");									//商户类型 标准类1  优惠类2
			
			
			logger.info("向pos平台入网请求报文：" + JSONObject.fromObject(params));
			
			String result = HttpClient.post(Constant.POS_ADDMERCHANT_URL , params, "2");
			logger.info("向pos平台入网响应 ：" + result);   //result:{"respCode":"0000","posCati":"22637563","PosShop":"683473880438580","respMsg":"商户新增成功","CustomerNo":"5834738859"}
			JSONObject json = JSONObject.fromObject(result);
			
			String respCode = json.getString("respCode");
			if("0000".equals(respCode)){
				String posCati = json.getString("posCati");
				String PosShop = json.getString("PosShop");
				String CustomerNo = json.getString("CustomerNo");
				MposDAO.saveMposMerchant(new Object[]{user.getID(),"1",PosShop,posCati, CustomerNo ,"1","1",request.getSn()});
			}
		} else {
			
			String posCati = merchantInfo.get("PlatTermNo").toString();
			String posSn =  request.getSn();
			String posShop = merchantInfo.get("PlatMerchantID").toString();
			String customerNo = merchantInfo.get("CustomerNo").toString();
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("posCati",posCati);
			params.put("posSn",posSn);
			params.put("posShop",posShop);
			params.put("customerNo",customerNo);
			
			try {
				
				logger.info("绑定设备请求报文：" + params.toString());
				
				String content = HttpClient.post(Constant.POS_MERCHANT_BIND_URL, params, "2");
				
				JSONObject json = JSONObject.fromObject(content);
				
				String respCode = json.getString("respCode");
				
				logger.info("绑定设备响应报文：" + content); 
				
				
				if("0000".equals(respCode)){
					int x = MposDAO.BindSN(user.getID(), sn);
					if(x > 0){
						response.setRespCode(RespCode.SUCCESS[0]);
						response.setRespDesc(RespCode.SUCCESS[1]);
						
					}else {
						response.setRespCode(RespCode.ServerDBError[0]);
						response.setRespDesc(RespCode.ServerDBError[1]);
					}
				}else{
					response.setRespDesc(RespCode.HttpClientError[1]);
					response.setRespDesc(json.getString("respMsg"));
				}
			} catch (Exception e) {
				
				logger.info(e.getMessage()); 
				response.setRespCode(RespCode.HttpClientError[0]);
				response.setRespDesc(RespCode.HttpClientError[1]);
			}
			
			
		}
	}
}
