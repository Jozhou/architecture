package com.architecture.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.architecture.application.App;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class OSUtils {

	protected static int mWifiStatus = -1;
	protected static ArrayList<PowerManager.WakeLock> mArrayCpuLock = null;
	
	/**
	 * 转到操作系统设置
	 */
	public static void gotoSetting(Context context) {
		context.startActivity(new Intent(Settings.ACTION_SETTINGS));
	}
	
	/**
	 * 转到操作系统网络设置
	 */
	public static void gotoNetSetting(Context context) {
		String action = null;
		if (hasIcecreamSandwich()) {
			action = android.provider.Settings.ACTION_SETTINGS;
		} else {
			action = android.provider.Settings.ACTION_WIRELESS_SETTINGS;
		}
		context.startActivity(new Intent(action));
	}

	/**
	 * 获取app最大可用内存
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
	
	/**
	 * 设置wifi的状态
	 * @param context
	 */
	public static void enabledWifi(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED ||
				wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
				wifiManager.setWifiEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查找是否存在location的provider
	 * @param provider
	 * @return
	 */
	public static boolean hasLocationProvider(String provider) {
		if(TextUtils.isEmpty(provider)) {
			return false;
		}
		LocationManager manager = (LocationManager) App.getInstance().
				getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = manager.getAllProviders();
		if(providers != null) {
			for(int i = 0; i < providers.size(); i++) {
				if(provider.equals(providers.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 安装apk文件
	 * @param apkFilePath
	 */
	public static void installApk(String apkFilePath) throws FileNotFoundException {
		File file = new File(apkFilePath);
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setDataAndType(Uri.parse("file://" + apkFilePath), "application/vnd.android.package-archive");  
		App.getInstance().startActivity(intent); 
	}
	
    /**
	 * 弹出软键盘
	 * @param context
	 * @param view
	 */
	public static void ShowSoftInput(Context context, View view){
		try {
			view.requestFocus();
			InputMethodManager imm = (InputMethodManager)context.getApplicationContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) { }
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 */
	public static void hideSoftInput(Context context){
		try {
			if(context instanceof Activity) {
				hideSoftInput(context, (Activity)context);
			}
		} catch (Exception e) { }
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 * @param activity
	 */
	public static void hideSoftInput(Context context, Activity activity){
		try {
			InputMethodManager imm = (InputMethodManager)context.getApplicationContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 
				InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) { }
	}
	
	/**
	 * 复制至黏贴板
	 * @param text
	 * @param toast
	 */
	@SuppressWarnings("deprecation")
	public static void copyToClipboard(String text, String toast) {
		if(OSUtils.hasHoneycomb()) {
			ClipboardManager cm = (ClipboardManager) App.getInstance().
					getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setPrimaryClip(ClipData.newPlainText(null, text));
		} else {
			android.text.ClipboardManager cm = (android.text.ClipboardManager) App.getInstance().
					getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(text);
		}
		if(!TextUtils.isEmpty(text)) {
			Toast.makeText(App.getInstance(), toast, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 点亮屏幕解锁
	 * @param context
	 */
	public static void wakeUpAndUnlock() {
		try {
			// 获取电源管理器对象
			PowerManager pm = (PowerManager) App.getInstance()
					.getSystemService(Context.POWER_SERVICE);
			// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
			@SuppressWarnings("deprecation")
			PowerManager.WakeLock wl = pm.newWakeLock(
					PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
			// 点亮屏幕
			wl.acquire();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保持cpu一直唤醒
	 * @param context
	 */
	public static PowerManager.WakeLock keepCpuOn() {
		try {
			// 获取电源管理器对象
			PowerManager pm = (PowerManager) App.getInstance()
					.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "cpuon");
			wl.acquire();
			if (mArrayCpuLock == null) {
				mArrayCpuLock = new ArrayList<PowerManager.WakeLock>();
			}
			mArrayCpuLock.add(wl);
			return wl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取消cpu一直唤醒
	 * @param context
	 */
	public static void keepCpuOff(PowerManager.WakeLock wl) {
		if (wl != null) {
			wl.release();
			wl = null;
		}
	}

	/**
	 * 取消cpu一直唤醒
	 * @param context
	 */
	public static void keepCpuOff() {
		if (mArrayCpuLock != null) {
			for (int i = mArrayCpuLock.size() - 1; i >= 0; i--) {
				keepCpuOff(mArrayCpuLock.remove(i));
			}
		}
	}

	/**
	 * 保持wifi一直唤醒
	 */
	@SuppressWarnings("deprecation")
	public static int keepWifiOn() {
		try {
			Context context = App.getInstance();
			ContentResolver resolver = context.getContentResolver();
			int value = Settings.System.getInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
					Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
			if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
				Settings.System.putInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
						Settings.System.WIFI_SLEEP_POLICY_NEVER);
			}
			if (mWifiStatus == -1) {
				mWifiStatus = value;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 取消wifi一直唤醒
	 */
	@SuppressWarnings("deprecation")
	public static void keepWifiOff(int newvalue) {
		Context context = App.getInstance();
		ContentResolver resolver = context.getContentResolver();
		int value = Settings.System.getInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
				Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
		if (newvalue != value) {
			Settings.System.putInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
					newvalue);
		}
	}

	/**
	 * 取消wifi一直唤醒
	 */
	public static void keepWifiOff() {
		if (mWifiStatus != -1) {
			keepWifiOff(mWifiStatus);
			mWifiStatus = -1;
		}
	}

	/**
	 * activity是否在前台运行
	 * @param context
	 * @param className
	 * @return
	 */
    public static boolean isActivityForeground(Context context,String className) {
        if (context == null || TextUtils.isEmpty(className)) {  
            return false;  
        }  
   
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo> list = am.getRunningTasks(1);  
        if (list != null && list.size() > 0) {  
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {  
                return true;  
            }  
        }  
        return false; 
     }

	/**
	 * Android 2.0
	 * @return
	 */
	public static boolean hasECLAIR() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}
	
	/**
	 * Android 2.0.1
	 * @return
	 */
	public static boolean hasECLAIR01() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1;
	}
	
	/**
	 * Android 2.1.x
	 * @return
	 */
	public static boolean hasEclairMr1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1;
	}
	
	/**
	 * Android 2.2.x
	 * @return
	 */
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Android 2.3
	 * Android 2.3.1
	 * Android 2.3.2
	 * @return
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * Android 2.3.3
	 * Android 2.3.4
	 * @return
	 */
	public static boolean hasGingerbreadMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
	}

	/**
	 * Android 3.0.x
	 * @return
	 */
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}


	/**
	 * Android 3.1.x
	 * @return
	 */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}


	/**
	 * Android 3.2
	 * @return
	 */
	public static boolean hasHoneycombMR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
	}


	/**
	 * Android 4.0
	 * Android 4.0.1
	 * Android 4.0.2
	 * @return
	 */
	public static boolean hasIcecreamSandwich(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}


	/**
	 * Android 4.0.3
	 * Android 4.0.4
	 * @return
	 */
	public static boolean hasIcecreamSandwichMR1(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
	}


	/**
	 * Android 4.1
	 * Android 4.1.1
	 * @return
	 */
	public static boolean hasJellyBean(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}


	/**
	 * Android 4.2
	 * Android 4.2.2
	 * @return
	 */
	public static boolean hasJellyBeanMR1(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}


	/**
	 * Android 4.3.x
	 * @return
	 */
	public static boolean hasJellyBeanMR2(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}


	/**
	 * Android 4.4.x
	 * @return
	 */
	public static boolean hasKitkat(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}
	
	/**
	 * Android 5.x
	 * @return
	 */
	public static boolean hasLollipop() {
		return Build.VERSION.SDK_INT >= 21;
	}

}
