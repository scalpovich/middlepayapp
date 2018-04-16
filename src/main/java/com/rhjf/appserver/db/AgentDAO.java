package com.rhjf.appserver.db;

import java.text.DecimalFormat;  
import java.util.Map;

import com.rhjf.appserver.util.AmountUtil;

/**
 * @author hadoop
 */
public class AgentDAO extends DBBase{

	
	/**
	 * 
	 *   查询代理商信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> agentInfo(Object[] obj){
		String sql = "select * from tab_agent where ID=?";
		return queryForMap(sql, obj);
	}
	
	
	/**
	 *   代理商配置信息
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> agentConfig(Object[] obj){
		String sql = "select * from tab_agent_config where AgentID=? and PayChannel=?";
		return queryForMap(sql, obj);
	}
	
	
	/**
	 *   全部舍掉
	 * @param amount
	 * @param fee
	 * @param top
	 * @return
	 */
	public static int makeFeeAbandon(String amount, Double fee, int top) {
		DecimalFormat format = new DecimalFormat("0.00");
		fee = Double.parseDouble(format.format(fee));
		Double feeTemp = (Long.parseLong(amount) * fee) / 1000;
		int poundage = (new Double(feeTemp)).intValue();
		if (poundage > top && top != 0) {
			return top;
		} else {
			return poundage;
		}
	}

	/**
	 *   舍弃小数部分并进一位
	 * @param amount
	 * @param fee
	 * @param top
	 * @return
	 */
	public static int makeFeeFurther(String amount, Double fee, int top) {
		Double feeTemp = Math.ceil((Long.parseLong(amount) * fee) / 1000);
		int poundage = (new Double(feeTemp)).intValue();
		if (poundage > top && top != 0) {
			return top;
		} else {
			return poundage;
		}
	}
	
	
	/**
	 *   四舍五入 保留整数
	 * @param amount
	 * @param fee
	 * @param top
	 * @return
	 */
	public static int makeFeeRounding(String amount, Double fee, int top) {
		DecimalFormat format = new DecimalFormat("0.00");
		fee = Double.parseDouble(format.format(fee));
		Double feeTemp = (Long.parseLong(amount) * fee) / 1000;
		int poundage = (int) Math.round(feeTemp);
		if (poundage > top && top != 0) {
			return top;
		} else {
			return poundage;
		}
	}
}
