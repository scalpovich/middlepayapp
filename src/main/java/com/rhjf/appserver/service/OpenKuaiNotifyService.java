package com.rhjf.appserver.service;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.db.OpenKuaiDAO;

@Service
public class OpenKuaiNotifyService {

	
	
	public int updateOpenKuaiStatus(Object[] obj){
		
		return OpenKuaiDAO.updateEncrypt(obj);
	}
}
