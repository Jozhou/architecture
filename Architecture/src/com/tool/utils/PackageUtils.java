package com.tool.utils;

import android.app.ActivityManager;
import android.content.Context;

public class PackageUtils {
	
	/**
	 * 是否为主进程
	 * @param context
	 * @return
	 */
	public static boolean isMainProcess(Context context) {
		String process = getProcessName(context);
		if(process != null && process.equals(context.getPackageName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前进程名称
	 * @param context
	 * @return
	 */
	public static String getProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	
}
