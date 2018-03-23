package com.rhjf.appserver.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * @author hadoop
 * @version 1.0 2018年3月19日 - 下午3:14:08 2018
 */
public class AESCBCUtil {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	/**
	 * 数据加密
	 * 
	 * @param srcData
	 * @param key
	 * @param iv
	 * @return
	 */
	public static String encrypt(String srcData, byte[] key, byte[] iv) {
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
			byte[] encData = cipher.doFinal(srcData.getBytes());
			return Base64.encodeBase64String(encData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据解密
	 * 
	 * @param encDataStr
	 * @param key
	 * @param iv
	 * @return
	 */
	public static String decrypt(String encDataStr, byte[] key, byte[] iv) {
		byte[] encData = Base64.decodeBase64(encDataStr);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		byte[] decbbdt = null;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
			decbbdt = cipher.doFinal(encData);
			return new String(decbbdt);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
