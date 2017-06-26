package com.architecture.utils;

import java.security.MessageDigest;

public class SHA256Utils {

	/**
	 * 加密
	 * @param src
	 * @return
	 */
	public static String encode(String src) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(src.getBytes());
			return bytes2Hex(digest.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
