package com.architecture.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.architecture.application.App;
import com.architecture.context.Config;

public class MemoryUtils {

	
	private static File file = null;
	private static String createLogFileTime = null;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat SDF2 = new SimpleDateFormat("MM-dd HH:mm:ss,SSS");
	private static ExecutorService executorAppendLog = Executors.newSingleThreadExecutor();
	
	static {
		
	}

	private static void init() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				createLogFileTime = SDF.format(new Date());
				file = new File(FileUtils.getCachePath() + FileUtils.CACHEMEMPATH
						+ "/dcar-" + createLogFileTime + ".log");
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists())
					file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Log.w("LogUtils", "can't find sdcard for log store.");
		}
	}

	private static boolean checkStoreLog() {
		return file != null && file.exists() && !TextUtils.isEmpty(createLogFileTime) 
				&& createLogFileTime.equals(SDF.format(new Date()));
	}
	
	private static void appendLog(final File file, final String allocate, final String total, final String max) {
		if(file==null || !file.exists()){
			return;
		} 
		executorAppendLog.execute(new Runnable() {
					@Override
					public void run() {
						synchronized (LogcatUtils.class) {
							BufferedWriter out = null;
							try {
								out = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream(file, true), "UTF-8"), 8192);
								StringBuffer sb = new StringBuffer();
								sb.append(SDF2.format(new Date()));
								sb.append("\t ");
								sb.append(allocate);
								sb.append("\t ");
								sb.append(total);
								sb.append("\t ");
								sb.append(max);
								sb.append("\r\n");
								out.write(sb.toString());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									if (out != null) {
										out.close();
										out = null;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			);
	}
	
	/**
	 * debug当前内存
	 * @param memory
	 */
	public static void save(String allocate, String total, String max) {
		if(Config.IS_STORELOG){
			if (!checkStoreLog() || !createLogFileTime.equals(SDF.format(new Date()))) {
				init();
			}
			appendLog(file, allocate, total, max);
		}
	}
	
	/**
	 * 获取当前分配内存
	 * @param context
	 * @return
	 */
	public static double getAllocated() {
		return Runtime.getRuntime().totalMemory() - 
				Runtime.getRuntime().freeMemory();
	}
	
	/**
	 * 获取当前总内存
	 * @param context
	 * @return
	 */
	public static double getTotal() {
		return Runtime.getRuntime().totalMemory();
	}
	
	/**
	 * 最大可分配内存
	 * @param context
	 * @return
	 */
	public static double getMax() {
		ActivityManager activityManager = (ActivityManager) App.getInstance().
				getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass();
	}
	
	/**
	 * 是否低内存
	 * @param context
	 * @return
	 */
	public static boolean isLowMemory() {
		final ActivityManager activityManager = (ActivityManager) App.getInstance().
				getSystemService(Context.ACTIVITY_SERVICE);    
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();   
        activityManager.getMemoryInfo(info);   
        return info.lowMemory;
	}
	
}
