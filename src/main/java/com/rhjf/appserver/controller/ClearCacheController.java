package com.rhjf.appserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.util.EhcacheUtil;
import com.rhjf.appserver.util.LoggerTool;

@RestController
@RequestMapping("/clearcache")
public class ClearCacheController{

	LoggerTool logger = new LoggerTool(this.getClass());
	
	@RequestMapping("")
	public Object ClearCache(){
		logger.info("清除缓存中所有内容");
		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		ehcache.clear(Constant.cacheName);
		return "SUCCESS";
	}
}
