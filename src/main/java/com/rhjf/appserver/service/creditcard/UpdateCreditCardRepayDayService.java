package com.rhjf.appserver.service.creditcard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.TermKeyDAO;
import com.rhjf.appserver.db.UserCreditCardDAO;
import com.rhjf.appserver.model.RequestData;
import com.rhjf.appserver.model.ResponseData;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.util.DES3;
import com.rhjf.appserver.util.DESUtil;
import com.rhjf.appserver.util.LoadPro;
import com.rhjf.appserver.util.LoggerTool;
import com.rhjf.appserver.util.UtilsConstant;

/**
 * 更新信用卡还款日
 * 
 * @author hadoop
 *
 */
public class UpdateCreditCardRepayDayService {

	private LoggerTool log = new LoggerTool(this.getClass());

	public void updateCreditCardRepayDay(LoginUser user, RequestData request, ResponseData response) {

		String creditCardNo = request.getCreditCardNo();
		String repayDay = request.getTradeDate();

		Map<String, Object> termKey = TermKeyDAO.selectTermKey(user.getID());
		String initKey = LoadPro.loadProperties("config", "DBINDEX");

		String bankCardno = "";
		try {
			String desckey = DESUtil.deskey(UtilsConstant.ObjToStr(termKey.get("MacKey")), initKey);
			bankCardno = DES3.decode(creditCardNo, desckey);
			log.info("用户：" + user.getLoginID() + " 添加收款信用卡 ：  密文：" + creditCardNo + " 原文，" + bankCardno);
		} catch (Exception e) {
			log.error("卡号加密异常 ：", e);
			response.setRespCode(RespCode.SYSTEMError[0]);
			response.setRespDesc(RespCode.SYSTEMError[1]);
			return;
		}

		UserCreditCardDAO.updateRepayDay(new Object[] { repayDay, user.getID(), bankCardno });

		try {
			String methodName = "creditCardList";
			Class<?> clz = Class.forName("com.rhjf.appserver.service.creditcard.CreditCardListService");
			Method m = clz.getDeclaredMethod(methodName, new Class[] { LoginUser.class, RequestData.class, ResponseData.class });
			m.invoke(clz.newInstance(), user, request, response);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

}
