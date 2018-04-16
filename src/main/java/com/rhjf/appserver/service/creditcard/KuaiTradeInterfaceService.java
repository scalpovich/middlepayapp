package com.rhjf.appserver.service.creditcard;

import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;

public interface KuaiTradeInterfaceService {

	void send(LoginUser user ,RequestData reqData , ResponseData repData);
}
