package com.rhjf.appserver.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.util.EhcacheUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping("")
	@ResponseBody
	public Object test(){
	
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		List<?> list = ehcache.getAllCacheObjects(Constant.cacheName);
		JSONObject json = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			json.put(list.get(i), ehcache.get(Constant.cacheName, list.get(i).toString()));
		}
		return json.toString();
	}
}
