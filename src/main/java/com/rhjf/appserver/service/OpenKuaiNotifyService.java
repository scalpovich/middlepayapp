package com.rhjf.appserver.service;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.db.OpenKuaiDB;

@Service
public class OpenKuaiNotifyService {

	
	
	public int updateOpenKuaiStatus(Object[] obj){
		
		return OpenKuaiDB.updateEncrypt(obj);
	}
}
