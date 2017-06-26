package com.architecture.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.architecture.application.App;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;

public class PackageUtils extends com.tool.utils.PackageUtils {
	
	/**
	 * 获取包信息
	 * @param context
	 * @return
	 */
	public  static PackageInfo getPackageInfo(Context context){
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取版本名称
	 * @param context
	 * @return
	 */
	public static String getPackageVersionName(Context context){
		return getPackageInfo(context).versionName;
	}
	
	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static int getPakcageVersionCode(Context context){
		return getPackageInfo(context).versionCode;
	}
	
	/**
	 * 获取应用名称
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) { 
        PackageManager packageManager = null; 
        ApplicationInfo applicationInfo = null; 
        try {
            packageManager = context.getPackageManager(); 
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0); 
        } catch (PackageManager.NameNotFoundException e) { 
            applicationInfo = null; 
        } 
        String applicationName =  
        (String) packageManager.getApplicationLabel(applicationInfo); 
        return applicationName; 
    } 
	
	/**
	 * 获取application的metadata
	 * @param key
	 * @return
	 */
	public static String getApplicationMetadata(String key) {
		try {
			Context context = App.getInstance();
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString(key);
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取application的metadata
	 * @param key
	 * @return
	 */
	public static boolean getApplicationMetadataBoolean(String key) {
		try {
			Context context = App.getInstance();
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getBoolean(key);
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取application的metadata
	 * @param key
	 * @return
	 */
	public static int getApplicationMetadataInteger(String key) {
		try {
			Context context = App.getInstance();
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (appInfo.metaData.containsKey(key)) {
				return appInfo.metaData.getInt(key);
			}
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 获取包签名md5
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getPackageSignMd5(Context context) {
		String packageName = getPackageInfo(context).packageName;
		Signature[] signs = getRawSignature(context, packageName);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(signs[0].toByteArray());
			byte[] digest = md.digest();
			String res = toHexString(digest);
			return res;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取包签名sha1
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getPackageSignSha1(Context context) {
		String packageName = getPackageInfo(context).packageName;
		Signature[] signs = getRawSignature(context, packageName);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(signs[0].toByteArray());
			byte[] digest = md.digest();
			String res = toHexString(digest);
			return res;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Signature[] getRawSignature(Context context,
			String packageName) {
		if ((packageName == null) || (packageName.length() == 0)) {
			return null;
		}
		PackageManager pkgMgr = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pkgMgr.getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
		if (info == null) {
			return null;
		}
		return info.signatures;
	}

	private static void byte2hex(byte b, StringBuffer buf) {
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}

	/*
	 * 
	 * Converts a byte array to hex string
	 */
	private static String toHexString(byte[] block) {
		StringBuffer buf = new StringBuffer();
		int len = block.length;
		for (int i = 0; i < len; i++) {
			byte2hex(block[i], buf);
			if (i < len - 1) {
				buf.append(":");
			}
		}
		return buf.toString();
	}
	
	/**
	 * 重启app
	 * 
	 * @param context
	 */
	public static void restartApplication() {
//		Intent intent = new Intent(App.getInstance(), ReLoginActivity.class);
//        PendingIntent restartIntent = PendingIntent.getActivity(    
//        		App.getInstance(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager manager = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);    
//        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 200, restartIntent); 
//        com.architecture.manager.ActivityManager.get().popupAllActivity();  
//        System.exit(0);
	}
	
}
