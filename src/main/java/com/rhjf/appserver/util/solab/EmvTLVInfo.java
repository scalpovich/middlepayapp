package com.rhjf.appserver.util.solab;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


public class EmvTLVInfo {

	private HashMap<String, String> hashmap = new HashMap<String, String>();
	static Logger logger = Logger.getLogger(EmvTLVInfo.class.getName());

//	public void EmvTLVInfo(String data) {
//		AppendData(data);
//	}

	public void AppendData(String data) {
		int tag_start = 0;
		int tag_length = 0;
		String t;

		int len_length = 0;
		String l;

		int value_start = 0;
		int value_length = 0;
		String v;

		int total_length = 0;

		for (tag_start = 0; tag_start < data.length();) {
			// 判断是否是两字节的Tag,去除 SW 9000
			if (data.substring(tag_start, tag_start + 4).equals("9000")) {
				total_length = 4;
				tag_start += total_length;
				return;
			}

			t = data.substring(tag_start, tag_start + 2);

			if ("9F".equals(t) || "5F".equals(t) || "BF".equals(t) || "DF".equals(t)) {
				tag_length = 4;
			} else {
				tag_length = 2;
			}
			t = data.substring(tag_start, tag_start + tag_length);

			// 处理L
			l = data.substring(tag_start + tag_length, tag_start + tag_length + 2);
			// s 与 0x80 进行与运算，判断第一个比特位是否为 1
			int first_bit = Integer.parseInt(l, 16) & 0x80;
			if (first_bit == 0) {
				// 说明第一个比特位是0，则1字节即为长度
				len_length = 2;
				l = data.substring(tag_start + tag_length, tag_start + tag_length + len_length);
			} else {
				// 否则s后7个比特位的值为长度值L所占几个字节
				// s 与 0x7f进行与运算，计算s后7个比特位的值
				len_length = Integer.parseInt(l, 16) & 0x7F;
				len_length += 1;
				len_length += len_length;

				// ＋2 跳过第一个字节
				l = data.substring(tag_start + tag_length + 2, tag_start + tag_length + len_length);

			}

			value_length = Integer.parseInt(l, 16) * 2;
			value_start = tag_start + tag_length + len_length;
			v = data.substring(value_start, value_start + value_length);

			total_length = tag_length + len_length + value_length;

			hashmap.put(t, v);

			if ("70".equals(t) || "6F".equals(t) || "61".equals(t) || "A5".equals(t)) {
				AppendData(v);
			}

			tag_start = tag_start + total_length;
		}

	}

	public String matchmod(String data) {
		int mod_start = 0;
		int mod_length = 0;
		String mod;

		int len_length = 0;
		String len;
		int mod_num = 0;

		String ret = "";

		for (mod_start = 0; mod_start < data.length();) {
			// 判断是否是两字节的Tag
			mod_length = 2;
			mod = data.substring(mod_start, mod_start + mod_length);

			if ("9F".equals(mod) || "5F".equals(mod) || "BF".equals(mod) || "DF".equals(mod)) {
				mod_length = 4;
			} else {
				mod_length = 2;
			}

			mod = data.substring(mod_start, mod_start + mod_length);

			// 处理L
			len_length = 2;
			len = data.substring(mod_start + mod_length, mod_start + mod_length + len_length);
			// s 与 0x80 进行与运算，判断第一个比特位是否为 1
			int first_bit = Integer.parseInt(len, 16) & 0x80;
			if (first_bit == 0) {
				// 说明第一个比特位是0，则1字节即为长度
				len_length = 2;
			} else {
				// 否则s后7个比特位的值为长度值L所占几个字节
				// s 与 0x7f进行与运算，计算s后7个比特位的值
				len_length = Integer.parseInt(len, 16) & 0x7F;
				len_length += len_length;// 字符串长度加倍

			}
			// 真实的L字符串
			len = data.substring(mod_start + mod_length, mod_start + mod_length + len_length);

			if (hashmap.get(mod).length() != (Integer.parseInt(len, 16)) * 2) {
				logger.fatal("EmvTLVInfo.java" + "matchmod  t = " + mod + "  v = " + hashmap.get(mod));
				return "";
			} else {
				ret = ret + hashmap.get(mod);
				logger.fatal("EmvTLVInfo.java" + "matchmod  t = " + mod + "  v = " + hashmap.get(mod));
				mod_start += mod_length + len_length;
			}

		}
		logger.fatal("EmvTLVInfo.java" + "matchmod  ret = " + ret);
		return ret;

	}

	public boolean AppendARQC(String arqc) {
		if (arqc.isEmpty()) {
			return false;
		}

		if (arqc.substring(0, 2).equals("80")) {
			hashmap.put("9F27", arqc.substring(4, 4 + 2)); // CID 密文信息数据 1字节
			hashmap.put("9F36", arqc.substring(4 + 2, 4 + 2 + 4)); // ATC
																	// 应用交易计数器
																	// 2字节
			hashmap.put("9F26", arqc.substring(4 + 2 + 4, 4 + 2 + 4 + 16)); // AC
																			// 应用密文
																			// 8字节
			if (arqc.length() > 26) {
				hashmap.put("9F10", arqc.substring(4 + 2 + 4 + 16, arqc.length())); // 发卡行应用数据（可选）
			}
		} else {

			return false;
		}

		return true;
	}

	public boolean AppendTC(String tc) {
		if (tc.isEmpty()) {
			return false;
		}

		if (tc.substring(0, 2).equals("80")) {
			// hashmap.put("9F27", tc.substring(4 ,4+2)); // CID 密文信息数据 9
			// hashmap.put("9F36", tc.substring(4+2 ,4+2+4)); // ATC 应用交易计数器
			// hashmap.put("9F26", tc.substring(4+2+4 ,4+2+4+32)); //AC 应用密文
			// hashmap.put("9F10", tc.substring(4+2+4+32 ,tc.length())); //AC
			// 应用密文
		} else {

			return false;
		}

		return true;
	}

	public String FindVWithT(String T) {

		if (hashmap.containsKey(T)) {

			return hashmap.get(T);
		} else {

			return "";
		}
	}

	public String FindTLVWithT(String T) {

		if (hashmap.containsKey(T)) {

			if ((hashmap.get(T).length() / 2) >= 128) {
				return T + "81" + String.format("%02X", hashmap.get(T).length() / 2) + hashmap.get(T);
			} else {

				return T + String.format("%02X", hashmap.get(T).length() / 2) + hashmap.get(T);
			}
		} else {

			return "";
		}
	}

	public boolean containsT(String T) {
		if (hashmap.containsKey(T)) {

			return true;
		} else {

			return false;
		}

	}

	public int num() {

		return hashmap.size();
	}

	public int sizeof(String T) {
		if (hashmap.containsKey(T)) {

			return hashmap.get(T).length();
		} else {

			return 0;
		}
	}

	public String[] getAll() {

		String[] all = new String[hashmap.size()];
		Iterator iter = hashmap.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String t = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ((v.length() / 2) > 128) {

				all[i] = t + "81" + String.format("%02X", v.length() / 2) + v;

			} else {

				all[i] = t + String.format("%02X", v.length() / 2) + v;

			}
			i++;
		}

		return all;
	}

	public String get55YuStrHX() {

		logger.fatal("EmvTLVInfo.java" + "get55YuStr start");
		String all = new String();
		all = "";
		Iterator iter = hashmap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String t = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("4F08".equals(t) || "50".equals(t) || "57".equals(t) || "5A".equals(t) || "82".equals(t)
					|| "84".equals(t) || "95".equals(t) || "9A".equals(t) || "9B".equals(t) || "9C".equals(t)
					|| "5F2A".equals(t) || "5F34".equals(t) || "9F02".equals(t) || "9F03".equals(t) || "9F06".equals(t)
					|| "9F09".equals(t) || "9F10".equals(t) || "9F1A".equals(t) || "9F1E".equals(t) || "9F21".equals(t)
					|| "9F26".equals(t) || "9F27".equals(t) || "9F33".equals(t) || "9F34".equals(t) || "9F35".equals(t)
					|| "9F36".equals(t) || "9F37".equals(t) || "9F39".equals(t) || "9F40".equals(t) || "9F41".equals(t)

			) {

				if ((v.length() / 2) >= 128) {

					all = all + t + "81" + String.format("%02X", v.length() / 2) + v;

				} else {

					logger.fatal("EmvTLVInfo.java" + "len > 128, t = " + t + " l = "
							+ String.format("%02X", v.length() / 2) + " v = " + v);
					all = all + t + String.format("%02X", v.length() / 2) + v;

				}

			} else if ("5F24".equals(t)) {

				if ((v.length() / 2) > 2) {

					all = all + t + String.format("%02X", 2) + v.substring(0, 4);

				} else {

					all = all + t + String.format("%02X", v.length() / 2) + v;

				}

			}

		}

		logger.fatal("EmvTLVInfo.java" + "getallstr, all = " + all);
		return all;
	}

	public String get55YuStr() {

		logger.fatal("EmvTLVInfo.java" + "get55YuStr start");
		String all = new String();
		all = "";
		Iterator iter = hashmap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String t = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("82".equals(t) || "95".equals(t) || "9A".equals(t) || "9C".equals(t) || "5F2A".equals(t)
					|| "9F02".equals(t) || "9F03".equals(t) || "9F10".equals(t) || "9F1A".equals(t) || "9F26".equals(t)
					|| "9F27".equals(t) || "9F33".equals(t) || "9F36".equals(t) || "9F37".equals(t)

			) {

				if ((v.length() / 2) >= 128) {

					all = all + t + "81" + String.format("%02X", v.length() / 2) + v;

				} else {

					logger.fatal("EmvTLVInfo.java" + "len > 128, t = " + t + " l = "
							+ String.format("%02X", v.length() / 2) + " v = " + v);
					all = all + t + String.format("%02X", v.length() / 2) + v;

				}

			}

		}

		logger.fatal("EmvTLVInfo.java" + "getallstr, all = " + all);
		return all;
	}

	public String getCardSerno() {

		logger.fatal("EmvTLVInfo.java" + "get55YuStr start");
		String all = new String();
		all = "";
		Iterator iter = hashmap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String t = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("5F34".equals(t)) {
				if ((v.length() / 2) > 2) {
					return v.substring(0, 4);
				} else {
					return v;
				}
			}
		}
		return "";
	}
}
