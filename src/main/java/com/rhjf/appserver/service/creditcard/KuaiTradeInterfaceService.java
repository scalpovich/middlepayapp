package com.rhjf.appserver.service.creditcard;

import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.TabLoginuser;

public interface KuaiTradeInterfaceService {

	void send(TabLoginuser user ,RequestData reqData , ResponseData repData);
}
