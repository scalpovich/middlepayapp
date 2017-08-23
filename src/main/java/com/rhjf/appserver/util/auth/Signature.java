package com.rhjf.appserver.util.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class Signature {

	public static String getSign(Map<String, String> map, String key) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!StringUtils.isEmpty(entry.getValue())) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
	    result += "key=" + key;
		System.out.println(result);
		result = MD5.MD5Encode(result).toUpperCase();
		return result;
	}

	/**
	 * 从map数据里面重新计算�?次签�?
	 * @param map数据
	 * @return 
	 */
	public static String getSignFromResponseString(Map<String, String> map, String key) {
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签�?
		map.put("sign", "");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比�?
		return Signature.getSign(map, key);
	}

	/**
	 * @Title checkSing
	 * @Description 校验签名
	 * @param o
	 * @param sign
	 * @return boolean
	 */
	public static boolean checkSing(Map<String, String> map, String sign, String key) {
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		return sign.equals(getSign(map, key));
	}
	
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("serialNo", "1");
		map.put("funcCode", "2");
		String key = "akqr8sm6c4xkcf6u99orjtwwkbcu1111";
		
		try {
			String sign = getSign(map, key);
			System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
