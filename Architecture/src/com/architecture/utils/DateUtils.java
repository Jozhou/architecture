package com.architecture.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.architecture.R;

public abstract class DateUtils {
	/**
	 * 时间戳转换为 小时，分，秒数组
	 * 
	 * @param time
	 * @return
	 */
	public static long[] timestamp2IntegreArray(long time) {
		long[] i = new long[3];
		i[0] = 0;
		i[1] = 0;
		i[2] = 0;
		if (time >= 360000000) {// 大于100小时
			return i;
		}
		long hourc = time / 3600000;

		long minuec = (time - hourc * 3600000) / (60000);

		long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;

		i[0] = hourc;
		i[1] = minuec;
		i[2] = secc;

		return i;
	}

	/**
	 * 时间戳转换为天, 小时，分数组
	 * 
	 * @param time
	 * @return
	 */
	public static int[] timestamp2IntegreArrayWithDay(int sec) {
		int[] i = new int[3];
		i[0] = 0;
		i[1] = 0;
		i[2] = 0;
		int day = sec / 86400;
		int hourc = (sec - day * 24 * 3600) / 3600;
		int minuec = (sec - day * 24 * 3600 - hourc * 3600) / 60;
		i[0] = day;
		i[1] = hourc;
		i[2] = minuec;

		return i;
	}
	
	/**
	 * 格式化时间（yyyy-MM-dd hh:mm:ss格式）
	 * @param time
	 * @return
	 */
	public static String formatServiceTime2yyMMddHHmmss(long time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
	}

	/**
	 * 格式化时间（yyyy-MM-dd hh:mm格式）
	 * @param time
	 * @return
	 */
	public static String formatServiceTime2yyMMddHHmm(long time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
	}

	/**
	 * 格式化时间（hh:mm:ss格式）
	 * @param time
	 * @return
	 */
	public static String formatServiceTime2yyMMHHmm(long time) {
		return new SimpleDateFormat("MM-dd HH:mm").format(new Date(time));
	}

	/**
	 * 格式化时间（hh:mm:ss格式）
	 * @param time
	 * @return
	 */
	public static String formatServiceTime2HHmmss(long time) {
		if (time >= 360000000) {
			return "00:00:00";
		}
		String timeCount = "";
		long hourc = time / 3600000;
		String hour = "0" + hourc;
		hour = hour.substring(hour.length() - 2, hour.length());

		long minuec = (time - hourc * 3600000) / (60000);
		String minue = "0" + minuec;
		minue = minue.substring(minue.length() - 2, minue.length());

		long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
		String sec = "0" + secc;
		sec = sec.substring(sec.length() - 2, sec.length());
		timeCount = hour + ":" + minue + ":" + sec;
		return timeCount;
	}
	
	/**
	 * 根据时间戳获取年月日时分
	 * @param timestamp
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String[] getFormatDateArray(long timestamp){
		Date date = new Date(timestamp);
		SimpleDateFormat format = new SimpleDateFormat("dd-HH-mm");
		String formatDate = format.format(date);
		return formatDate.split("-");
	}
	
	public static String getIntervalMonth(int interval){
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.MONTH, interval);
		int intervalYear = calendar.get(Calendar.YEAR);
		if(currentYear == intervalYear){
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			return currentMonth + "月";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");  
	        String dateStr = sdf.format(calendar.getTime()); 
	        return dateStr;
		}
		
	}
	
	public static String numToUpper(int num) {
        String u[] = {"○", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }
	
	public static String monthToUppder(int month) {
        if (month < 10) {
            return numToUpper(month)+"月";
        } else if (month == 10) {
            return "十月";
        } else {
            return "十" + numToUpper(month - 10) + "月";
        }
    }
}
