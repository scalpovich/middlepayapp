// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   DESede.java

package com.rhjf.appserver.util;

import java.io.IOException; 
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import com.rhjf.appserver.util.DESUtil;

public class EncrptMerchine {
	LoggerTool logger = new LoggerTool(this.getClass());
	Properties prop = null;

	public EncrptMerchine() {
		prop = new Properties();
		try {
			prop.load(EncrptMerchine.class.getResourceAsStream("/jmj.properties"));
		} catch (IOException e) {
			logger.error("加载加密机配置失败");
		}
	}

	public String ChangeKeyFromToProkey(String keyIndex, String tMKKeyIndex, String key) {
		String outKey = "";
		String termProkey = prop.getProperty(tMKKeyIndex);
		String prokey = prop.getProperty(keyIndex);
		// 解析密钥明文
		try {
			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, termProkey));
			outKey = DESUtil.bcd2Str(DESUtil.encrypt3(keyde, prokey));
		} catch (Exception e) {
			logger.error(e.toString());

		}
		return outKey;
	}

	public String ChangeKeyFromToProkey(String keyIndex, String tMKKeyIndex, String key, String tmkKey) {
		String outKey = "";
		String keyde = "";

		String termProkey = prop.getProperty(tMKKeyIndex);
		System.out.println("termProkey:" + termProkey);
		String prokey = prop.getProperty(keyIndex);
		System.out.println("prokey:" + prokey);
		// 解析密钥明文
		try {
			// 将终端主密钥解密
			String tmk = DESUtil.bcd2Str(DESUtil.decrypt3(tmkKey, termProkey));
			if (tmk.length() == 32) {
				keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, tmk));
			} else {
				keyde = DESUtil.bcd2Str(DESUtil.decrypt(key, tmk));
			}
			System.out.println("keyde:" + keyde);
			if (prokey.length() == 32) {
				outKey = DESUtil.bcd2Str(DESUtil.encrypt3(keyde, prokey));
			} else {
				outKey = DESUtil.bcd2Str(DESUtil.encrypt(keyde, prokey));
			}

			System.out.println("outKey:" + outKey);
		} catch (Exception e) {
			logger.error(e.toString());

		}
		return outKey;
	}

	public static String macXor(String macStr, String key, String keyIndex) {
		String macByte = DESUtil.GenXorData(DESUtil.string2Bytes(macStr), 0);
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密钥明文
			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));
			byte[] macbyte = new byte[8];
			if (keyde.length() == 16) {
				macbyte = DESUtil.encrypt(macByte, keyde);
			} else {
				macbyte = DESUtil.encrypt3(macByte, keyde);
			}

			return DESUtil.bcd2Str(macbyte);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String bankMac(String macStr, String key, String keyIndex) {
		String macByte = DESUtil.GenXorData(DESUtil.string2Bytes(macStr), 0);
		String macAsc = DESUtil.bcd2Str(macByte.getBytes());
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}

			String initKey = prop.getProperty(keyIndex);
			System.out.println("initKey:" + initKey);
			// 解析密钥明文
			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));
			System.out.println("keyde:" + keyde);
			byte[] leftByte = new byte[8];
			if (keyde.length() == 16) {
				leftByte = DESUtil.encrypt(macAsc.substring(0, 16), keyde);
			} else {
				leftByte = DESUtil.encrypt3(macAsc.substring(0, 16), keyde);
			}
			byte[] macByteAll = new byte[16];
			System.arraycopy(leftByte, 0, macByteAll, 0, 8);
			System.arraycopy(DESUtil.string2Bytes(macAsc.substring(16, 32)), 0, macByteAll, 8, 8);

			String temp = DESUtil.GenXorData(macByteAll, 0);
			System.out.println("temp:" + temp);
			byte[] mac = new byte[8];
			if (keyde.length() == 16) {
				mac = DESUtil.encrypt(temp, keyde);
			} else {
				mac = DESUtil.encrypt3(temp, keyde);
			}
			System.out.println("Utils.bcd2Str(mac):" + DESUtil.bcd2Str(mac));
			return DESUtil.bcd2Str(mac);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String xorBankMac(String macStr, String key, String keyIndex) {
		String macByte = DESUtil.GenXorData(DESUtil.string2Bytes(macStr), 0);
		byte[] mac = new byte[16];
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密钥明文
			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));
			if (keyde.length() == 16) {
				mac = DESUtil.encrypt(macByte, keyde);
			} else {
				mac = DESUtil.encrypt3(macByte, keyde);
			}

			return DESUtil.bcd2Str(mac);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String bankMac2(String macStr, String key, String keyIndex) {
		System.out.println(macStr);
		String macByte = DESUtil.GenXorData(DESUtil.string2Bytes(macStr), 0);
		String macAsc = DESUtil.bcd2Str(macByte.getBytes());
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密钥明文
			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));

			byte[] leftByte = new byte[8];
			if (keyde.length() == 16) {
				leftByte = DESUtil.encrypt(macAsc.substring(0, 16), keyde);
			} else {
				leftByte = DESUtil.encrypt3(macAsc.substring(0, 16) , keyde);
			}
			byte[] macByteAll = new byte[16];
			System.arraycopy(leftByte, 0, macByteAll, 0, 8);
			System.arraycopy(DESUtil.string2Bytes(macAsc.substring(16, 32)), 0, macByteAll, 8, 8);

			String temp = DESUtil.GenXorData(macByteAll, 0);
			byte[] mac = new byte[8];
			if (keyde.length() == 16) {
				mac = DESUtil.encrypt(temp, keyde);
			} else {
				mac = DESUtil.encrypt3(temp, keyde);
			}
			return DESUtil.bcd2Str(DESUtil.bcd2Str(mac).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	

	public static String DesTrack(String key, String keyIndex, String encTrack) {
		
		//加密
				try {
					
					Properties prop =new Properties();
					try {
						prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
					} catch (IOException e) {
						return null;
					}
					String initKey= prop.getProperty(keyIndex);
					//解析密钥明文
					String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));
					
					String tdMiWen = encTrack.substring(encTrack.length()-18 , encTrack.length() - 2);
					
					String tdMingWen = DESUtil.bcd2Str(DESUtil.decrypt3(tdMiWen, keyde));
					
					tdMingWen = encTrack.replace(tdMiWen, tdMingWen);
					
					if(tdMingWen.length() > 37){
						tdMingWen = tdMingWen.substring(0,36);
					}
					
					System.out.println("tdMingWen :" + tdMingWen); 
					
					return tdMingWen;

				} catch (Exception e) {
							
					e.printStackTrace();
					return "";
				}
		
//		// 加密
//		try {
//			Properties prop = new Properties();
//			try {
//				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
//			} catch (IOException e) {
//				return null;
//			}
//			String initKey = prop.getProperty(keyIndex);
//			// 解析密钥明文
//			String keyde = DESUtil.bcd2Str(DESUtil.decrypt3(key, initKey));
//			byte[] trackByte = new byte[encTrack.length()];
//			if (keyde.length() == 16) {
//				trackByte = DESUtil.decrypt(encTrack, keyde);
//			} else {
//				trackByte = DESUtil.decrypt3(encTrack, keyde);
//			}
//			String temp = DESUtil.bcd2Str(trackByte);
////			return temp.substring(0, temp.indexOf("F"));
//			return temp;
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			return "";
//		}
	}

	public static String GenKey(String keyIndex, String key) {
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密钥明文
			return DESUtil.bcd2Str(DESUtil.encrypt3(key, initKey));

		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String ChangePwd(String keyIndex, String pwd, String keyIndexOut) {
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密钥明文
			String pwdTemp = DESUtil.bcd2Str(DESUtil.decrypt3(pwd, initKey));
			String initKeyoutStr = prop.getProperty(keyIndexOut);
			return DESUtil.bcd2Str(DESUtil.encrypt3(pwdTemp, initKeyoutStr));
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String calLoginPwd(String usrID, String pwd, String keyIndex, String sendTime) {
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String initKey = prop.getProperty(keyIndex);
			// 解析密码明文
			String keyde = BytesTool.DecBytes2String(DESUtil.decrypt3(pwd, initKey));

			return getMD5Str(usrID + sendTime + keyde.replace(" ", ""));

		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String getMD5Str(String str) {

		MessageDigest messageDigest = null;

		try {
			System.out.println(str);
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			System.out.println("NoSuchAlgorithmException caught!");

			System.exit(-1);

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {

			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));

		}
		return md5StrBuff.toString();
	}

	public static String GenMac2(String keyIndex, String macStr) {
		String macByte = "";
		try {
			macByte = DESUtil.GenXorData(macStr.getBytes("GBK"), 0);
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		String macAsc = DESUtil.bcd2Str(macByte.getBytes());
		// 加密
		try {

			Properties prop = new Properties();
			try {
				prop.load(DESUtil.class.getResourceAsStream("/jmj.properties"));
			} catch (IOException e) {
				return null;
			}
			String keyde = prop.getProperty(keyIndex);
			byte[] leftByte = DESUtil.encrypt3(macAsc.substring(0, 16), keyde);
			byte[] macByteAll = new byte[16];
			System.arraycopy(leftByte, 0, macByteAll, 0, 8);
			System.arraycopy(DESUtil.string2Bytes(macAsc.substring(16, 32)), 0, macByteAll, 8, 8);
			String temp = DESUtil.GenXorData(macByteAll, 0);
			byte[] mac = DESUtil.encrypt3(temp, keyde);
			return DESUtil.bcd2Str(DESUtil.bcd2Str(mac).getBytes()).substring(0, 8);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}
}
