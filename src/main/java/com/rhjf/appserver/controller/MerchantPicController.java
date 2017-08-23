package com.rhjf.appserver.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhjf.appserver.service.MerchantPicService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/merchantpic")
public class MerchantPicController {

	@Autowired
	private MerchantPicService merchantPicService;
	
	@RequestMapping("")
	public Object merchantpic(HttpServletRequest request , HttpServletResponse response){
		
		String loginID = request.getParameter("loginID");
		
		List<Map<String,Object>> list = merchantPicService.merchantpiclist(loginID);
		
		
		System.out.println(list.size()); 
		
		if(list.size() == 0){
			return new JSONObject();
		}else{
			return JSONObject.fromObject(list.get(0));
		}
		
	}
}
