package com.rhjf.appserver.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hadoop
 */
public class BytesTool {

	public static int bcdBytes2Int(byte[] baBcd) {
		int iRtn = 0;

		StringBuffer sb = new StringBuffer(baBcd.length * 2);
		for (int i = 0; i < baBcd.length; i++) {
			sb.append((byte) ((baBcd[i] & 0xf0) >>> 4));
			sb.append((byte) (baBcd[i] & 0x0f));
		}

		iRtn = Integer.parseInt(sb.toString());
		return iRtn;
	}

	public static byte[] int2Bytes(int iSrc, int iLen) {
		if (iLen > 4)
			return null;

		byte[] baRtn = new byte[iLen];

		int pos = 0;
		if (iLen == 4) {
			baRtn[0] = (byte) ((iSrc & 0xff000000) >> 24);
			pos++;
		}
		if (iLen > 2) {
			baRtn[pos] = (byte) ((iSrc & 0xff0000) >> 16);
			pos++;
		}
		if (iLen > 1) {
			baRtn[pos] = (byte) ((iSrc & 0xff00) >> 8);
			pos++;
		}
		baRtn[pos] = (byte) (iSrc & 0x00ff);

		return baRtn;
	}

	public static String bytes2Str(byte[] baSrc, int iStart, int length) {
		if ((iStart + length) > baSrc.length)
			return null;

		String sRtn = "";
		for (int i = 0; i < length; i++)
			sRtn += baSrc[iStart + i];

		return sRtn;
	}

	public static byte[] bytesSub(byte[] baSrc, int iStart, int length) {
		if ((iStart + length) > baSrc.length)
			return null;

		byte[] baRtn = new byte[length];
		for (int i = 0; i < length; i++)
			baRtn[i] = baSrc[iStart + i];

		return baRtn;
	}

	public static byte[] bytes2Hex(byte[] baSrc) {
		byte[] baRtn = new byte[baSrc.length * 2];
		for (int i = 0; i < baSrc.length; i++) {
			byte b = (byte) baSrc[i];
			byte bh = (byte) (b >> 4 & 0x0f);
			byte bl = (byte) (b & 0x0f);
			baRtn[i * 2] = hex(bh);
			baRtn[i * 2 + 1] = hex(bl);
		}
		return baRtn;
	}

	public static byte[] hex2Bytes(byte[] baSrc) {
		byte[] baRtn = new byte[baSrc.length / 2];
		int pos = 0; // char where we start
		int bufpos = 0;

		while (pos < baSrc.length) {
			char c1 = (char) baSrc[pos];
			char c2 = (char) baSrc[pos + 1];

			baRtn[bufpos] = (byte) ((hex2Int(c1) << 4) | hex2Int(c2));

			pos += 2;
			bufpos++;
		}

		return baRtn;
	}

	private static int hex2Int(char ccSrc) {
		char cc = ccSrc;
		if (Character.isLowerCase(ccSrc))
			cc = Character.toUpperCase(ccSrc);

		int iRtn = 0;
		switch (cc) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			iRtn = (int) (cc - 48);
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
			iRtn = (int) (cc - 55);
			break;
		}
		return iRtn;
	}

	private static byte hex(byte bSrc) {
		byte bRtn = 0x00;

		switch (bSrc) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			bRtn = (byte) (0x30 + bSrc);
			break;
		case 10: // A
		case 11: // B
		case 12: // C
		case 13: // D
		case 14: // E
		case 15: // F
			bRtn = (byte) (bSrc - 9 + 0x40);
			break;
		}

		return bRtn;
	}

	private static char hex(int i) {
		switch (i) {
		case 10:
			return 'A';
		case 11:
			return 'B';
		case 12:
			return 'C';
		case 13:
			return 'D';
		case 14:
			return 'E';
		case 15:
			return 'F';
		default:
			return (char) (i + 48);
		}
	}

	public static String bytes2HexStr(byte[] ba, boolean isInsSpace) {
		StringBuffer md = new StringBuffer(32);
		for (int i = 0; i < ba.length; i++) {
			int j = ba[i];
			if (j < 0)
				j += 256;
			md.append(hex(j / 16));
			md.append(hex(j % 16));
			if (isInsSpace)
				md.append(" ");
		}
		return md.toString();
	}

	public static byte[] HexStr2Bytes(String sSrc) {
		byte[] baRtn = new byte[sSrc.length() / 2];
		int charpos = 0; // char where we start
		int bufpos = 0;

		while (charpos < sSrc.length()) {
			char c1 = (char) sSrc.charAt(charpos);
			char c2 = (char) sSrc.charAt(charpos + 1);

			baRtn[bufpos] = (byte) ((hex2Int(c1) << 4) | hex2Int(c2));

			charpos += 2;
			bufpos++;
		}

		return baRtn;
	}

	public static String logBytes(String prefix, byte[] ba) {
		String strRtn = prefix + bytes2HexStr(ba, true);
		return strRtn;
	}

	public static String logBytes(String prefix, byte[] ba, int iStart, int iLen) {
		byte[] baData = bytesSub(ba, iStart, iLen);
		String strRtn = prefix + bytes2HexStr(baData, true);
		return strRtn;
	}

	public static byte[] getBaNow() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date now = new Date();
		String ss = sf.format(now);
		byte[] baSs = HexStr2Bytes(ss);
		return baSs;
	}

	public static String getStrNow() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date now = new Date();
		String ss = sf.format(now);
		return ss;
	}


	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			// System.out.println(\"translation\"+e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}

	public static int toInt(byte b) {
		if (b >= 0)
			return (int) b;
		else
			return (int) (b + 256);
	}

	// 4 byte Array to int
	public static int byteArray4ToInt(byte[] byteValue) {
		if (byteValue.length != 4)
			return 0;

		int intValue = 0;
		try {
			intValue = toInt(byteValue[0]);
			intValue = (intValue << 8) + toInt(byteValue[1]);
			intValue = (intValue << 8) + toInt(byteValue[2]);
			intValue = (intValue << 8) + toInt(byteValue[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intValue;
	}

	public static int byteArray2ToInt(byte[] bytes) {
		return (toInt(bytes[0]) << 8) + toInt(bytes[1]);
	}

	public static int DecBytes2Int(byte[] bytes) {
		String iRn = "";
		String sReal = "";
		for (int i = 0; i < bytes.length; i++) {
			switch (bytes[i]) {
			case 48:
				iRn = "0";
				break;
			case 49:
				iRn = "1";
				break;
			case 50:
				iRn = "2";
				break;
			case 51:
				iRn = "3";
				break;
			case 52:
				iRn = "4";
				break;
			case 53:
				iRn = "5";
				break;
			case 54:
				iRn = "6";
				break;
			case 55:
				iRn = "7";
				break;
			case 56:
				iRn = "8";
				break;
			case 57:
				iRn = "9";
				break;

			}
			sReal = sReal + iRn;
		}
		int iReal = Integer.parseInt(sReal);
		return iReal;

	}

	public static String DecBytes2String(byte[] bytes) {
		String sReal = "";
		String s = new String(bytes);
		sReal = s;

		return sReal;

	}

	public static String DecBytes2String(byte[] bytes, String encode) {

		String sReal = "";
		try {
			sReal = new String(bytes, encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sReal;

	}

	public static String HexBytes2String(byte[] bytes) {
		String iRn = "";
		String sReal = "";
		for (int i = 0; i < bytes.length; i++) {
			switch (bytes[i]) {
			case 31:
				iRn = "1";
				break;
			case 32:
				iRn = "2";
				break;
			case 33:
				iRn = "3";
				break;
			case 34:
				iRn = "4";
				break;
			case 35:
				iRn = "5";
				break;
			case 36:
				iRn = "6";
				break;
			case 37:
				iRn = "7";
				break;
			case 38:
				iRn = "8";
				break;
			case 39:
				iRn = "9";
				break;
			case 30:
				iRn = "0";
				break;
			case 41:
				iRn = "A";
				break;
			case 42:
				iRn = "B";
				break;
			case 43:
				iRn = "C";
				break;
			case 44:
				iRn = "D";
				break;
			case 45:
				iRn = "E";
				break;
			case 46:
				iRn = "F";
				break;

			}
			sReal = sReal + iRn;
		}

		return sReal;

	}

	public static String long2Str6(Long serial) {
		String sSerial = Long.toString(serial);
		for (int i = sSerial.length(); i < 6; i++) {
			sSerial = "0" + sSerial;
		}
		return sSerial;
	}

	public static String padLef(String str, String padStr, int len) {
		for (int i = str.length(); i < len; i++) {
			str = padStr + str;
		}
		return str;
	}
}
