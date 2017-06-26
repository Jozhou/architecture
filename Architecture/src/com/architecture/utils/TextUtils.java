package com.architecture.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class TextUtils {

	/**
	 * 
	 * @param s
	 *            起始位
	 * @param e
	 *            结束位
	 * @param newSize
	 *            新字体大小
	 * @param content
	 *            内容
	 * @return
	 */
	public static SpannableStringBuilder changeSize(int s, int e, int newSize,
			String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(newSize);
		builder.setSpan(span, s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 
	 * @param s
	 *            起始位
	 * @param e
	 *            结束位
	 * @param newColor
	 *            新字体颜色
	 * @param content
	 *            内容
	 * @return
	 */
	public static SpannableStringBuilder changeColor(int s, int e,
			int newColor, String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan span = new ForegroundColorSpan(newColor);
		builder.setSpan(span, s, e, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 
	 * @param cs 颜色起始位
	 * @param ce 颜色结束位
	 * @param ss 字体大小起始位
	 * @param se 字体大小结束位
	 * @param newColor 新字体颜色
	 * @param newSize 新字体大小
	 * @param content 文字内容
	 * @return
	 */
	public static SpannableStringBuilder changeColorSize(int cs, int ce,
			int ss, int se, int newColor, int newSize, String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan span = new ForegroundColorSpan(newColor);
		builder.setSpan(span, cs, ce, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		AbsoluteSizeSpan span_ = new AbsoluteSizeSpan(newSize);
		builder.setSpan(span_, ss, se, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
	
	/**
	 * 隐藏电话号码
	 * @param phone
	 * @return
	 */
	public static String getHidePhoneNumber(String phone) {
		if (android.text.TextUtils.isEmpty(phone)) {
			return phone;
		}
		int phoneLen = phone.length();
		if (phoneLen >= 8) {
			String lastFour = phone.substring(
					phoneLen - 4, phoneLen);
			phone = phone.substring(0, phoneLen - 8)
					+ "****" + lastFour;
		} else if (phoneLen >= 4) {
			String lastFour = phone.substring(
					phoneLen - 4, phoneLen);
			phone = replaceStar(lastFour, phoneLen - 4);
		}
		return phone;
	}

	private static String replaceStar(String lastFour, int len) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			buffer.append("*");
		}
		return buffer.append(lastFour).toString();
	}
	
}
