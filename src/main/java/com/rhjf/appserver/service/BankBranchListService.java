package com.rhjf.appserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.BankCodeDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

import net.sf.json.JSONArray;


/**
 *   根据银行卡号 和 开户行地址 获取支行名称
 * @author hadoop
 *
 */
public class BankBranchListService {

	private LoggerTool log = new LoggerTool(this.getClass());

	public void bankBranchList(RequestData request, ResponseData response) {
		String bankProv = request.getBankProv();
		String bankCity = request.getBankCity();
		String accountNo = request.getBankCardNo();

		log.info("获取支行名称列表： 所在省份:" + bankProv + " , 城市:" + bankCity + ", 卡号：" + accountNo);

		Map<String, Object> bankBinMap = BankCodeDAO.bankBinMap(new Object[] { accountNo });

		if (bankBinMap == null || bankBinMap.isEmpty()) {

			log.info("获取支行列表失败: 所在省份:" + bankProv + " , 城市:" + bankCity + ", 卡号：" + accountNo);

			response.setRespCode(RespCode.BankCardInfoErroe[0]);
			response.setRespDesc(RespCode.BankCardInfoErroe[1]);

		} else {
			Map<String, String> map = new HashMap<>();
			map.put("bankName", UtilsConstant.ObjToStr(bankBinMap.get("bankName")));
			map.put("bankProv", bankProv);
			map.put("bankCity", bankCity);

			List<Map<String,Object>> list = BankCodeDAO.bankBranchList(map);

			List<String> banbranchlist = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				banbranchlist.add(list.get(i).get("BankBranch").toString());
			}
			
			response.setList(JSONArray.fromObject(banbranchlist).toString());

			response.setRespCode(RespCode.SUCCESS[0]);
			response.setRespDesc(RespCode.SUCCESS[1]);
		}
	}
}
