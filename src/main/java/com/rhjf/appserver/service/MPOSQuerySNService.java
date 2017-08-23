package com.rhjf.appserver.service;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.MposDB;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;

public class MPOSQuerySNService {

	public void MPOSQuerySN(TabLoginuser user , RequestData request, ResponseData response){

		Map<String, Object> merchantInfo = MposDB.getMPOSMerchant(user.getID());

		response.setSn("");

		if (merchantInfo != null && !merchantInfo.isEmpty()) {
			if (merchantInfo.get("SN") != null) {
				response.setSn(merchantInfo.get("SN").toString());
			}
		}

		response.setRemarks("JHL");
		response.setRespCode(RespCode.SUCCESS[0]);
		response.setRespDesc(RespCode.SUCCESS[1]);
	}
}
