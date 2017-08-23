package com.rhjf.appserver.util;

public class Test {

	public static void main(String[] args) {
		
		String termTmkKey = MD5.md5(UtilsConstant.getUUID(), "UTF-8").toUpperCase();
		String tmk = MD5.md5(UtilsConstant.getUUID(), "UTF-8").toUpperCase();
		
		System.out.println(UtilsConstant.getUUID()); 
		
		System.out.println(termTmkKey);
		System.out.println(tmk);
	}
}
 