package com.rhjf.appserver.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rhjf.appserver.db.DBBase;

@Service
public class MerchantPicService extends DBBase{

	public List<Map<String,Object>> merchantpiclist(String loginID){
		String sql = "select HandheldIDPhoto , IDCardFrontPhoto , IDCardReversePhoto from tab_loginuser where LoginID=?";
		return queryForList(sql, new Object[]{loginID});
	}
}
