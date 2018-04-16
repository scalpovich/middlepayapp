package com.rhjf.appserver.controller;

import com.rhjf.appserver.db.LoginUserDAO;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.util.EhcacheUtil;

@ResponseBody
@Controller
@RequestMapping("/upuserstatus")
public class UpdateUserInfoController {

	
	@RequestMapping("")
	public Object updateUser(HttpServletRequest request ){
	
		String loginid = request.getParameter("a");
		String status = request.getParameter("b");
		LoginUserDAO.updateUserBankStatus(new Object[]{ status ,status , loginid});
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
		return "00";
	}
}
