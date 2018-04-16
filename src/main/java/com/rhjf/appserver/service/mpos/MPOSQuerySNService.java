package com.rhjf.appserver.service.mpos;

import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.MposDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;

public class MPOSQuerySNService {

	public void MPOSQuerySN(LoginUser user , RequestData request, ResponseData response){

		Map<String, Object> merchantInfo = MposDAO.getMPOSMerchant(user.getID());

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
