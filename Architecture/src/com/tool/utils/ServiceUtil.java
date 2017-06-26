package com.tool.utils;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

import com.architecture.application.App;

public class ServiceUtil {

	public static boolean isServiceRunning(String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(200);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className)) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
