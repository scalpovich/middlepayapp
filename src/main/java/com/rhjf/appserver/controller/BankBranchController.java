package com.rhjf.appserver.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.db.BankCodeDB;
import com.rhjf.appserver.db.LoginUserDB;
import com.rhjf.appserver.db.UserBankCardDB;
import com.rhjf.appserver.model.TabLoginuser;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.auth.AuthUtil;
import com.rhjf.appserver.util.auth.Author;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bankbranch")
public class BankBranchController {

	
	private LoggerTool log  = new LoggerTool(this.getClass());
	
	@RequestMapping("")
	@ResponseBody
	public Object searchBankBranch(HttpServletRequest request){
		
		String bankNum = request.getParameter("bankNum");
		String bankAdd = request.getParameter("bankAdd");
		
		String prov = bankAdd.split("-")[0];
		String city = bankAdd.split("-")[1];
		
		Map<String,Object> map = BankCodeDB.bankBinMap(new Object[]{bankNum});
		
		JSONObject json = new JSONObject();
		
		if(map == null || map.isEmpty()){
			json.put("code", "01");
			json.put("msg", "获取银行名称失败");
			return json;
		}
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("bankName", map.get("bankName").toString());
		param.put("bankProv", prov);
		param.put("bankCity", city);
		List<Map<String,Object >> list =  BankCodeDB.bankBranchList(param);
			
		json.put("code", "00");
		json.put("list", JSONArray.fromObject(list).toString());
		
		return json;
	}
	
	
	
	@RequestMapping("addCreditCard")
	@ResponseBody
	public Object addCreditCard(HttpServletRequest request) {
		String loginID = request.getParameter("loginID");
		String creditCard = request.getParameter("creditCard");

		log.info("用户：" + loginID + "添加信用卡账号 , " + creditCard);

		TabLoginuser user = LoginUserDB.LoginuserInfo(loginID);

		Map<String, String> reqMap = AuthUtil.authentication(user.getName(), creditCard, user.getIDCardNo() , "");

		JSONObject json = new JSONObject();

		log.info("鉴权三要素:" + user.getName() + " -- " + creditCard + " -- " + user.getIDCardNo());
		if (reqMap.get("respCode").equals(Author.SUCESS_CODE)) {

			UserBankCardDB.addCreditCardNo(new Object[] { creditCard, user.getID() });

			log.info("用户：" + loginID + " 信用卡账号 , " + creditCard + "鉴权卡号，并且保存成功");

			json.put("code", "00");
			json.put("msg", "保存成功");
		} else {
			log.info("卡号：" + creditCard + "鉴权没有通过");
			json.put("code", "01");
			json.put("msg", "信用卡卡号鉴权未通过.");
		}
		return json;
	}
	
}
