package com.architecture.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class SystemUtils {
	private static String TAG = "SystemUtils";
	public static final int UNKNOWN = 0;
	public static final int V5 = 1;
	public static final int V6 = 2;
	public static final int V7 = 3;

	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

	/**
	 * 获取小米手机的系统版本
	 * @return
	 */
	public static int getMIUIVersion() {
		int versionName = UNKNOWN;
		try {
			Class<?> classType = Class.forName("android.os.SystemProperties");
			Method getStringMethod = classType.getDeclaredMethod("get",
					String.class, String.class);
			String version = (String) getStringMethod.invoke(classType,
					KEY_MIUI_VERSION_NAME, "");
			if ("v5".equalsIgnoreCase(version)) {
				versionName = V5;
			} else if ("v6".equalsIgnoreCase(version)) {
				versionName = V6;
			} else if ("v7".equalsIgnoreCase(version)) {
				versionName = V7;
			}
		} catch (Exception e) {
		}
		return versionName;
	}

	private static String KEY_ISMI = "isMIUI";

	// 检测是不是MIUI
	public static boolean isMIUI() {
		if (PreferenceUtils.getBoolean(KEY_ISMI, false)) {
			return PreferenceUtils.getBoolean(KEY_ISMI, false);
		}
		Properties prop = new Properties();
		boolean isMIUI = false;
		try {
			FileInputStream fs = new FileInputStream(new File(
					Environment.getRootDirectory(), "build.prop"));
			prop.load(fs);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
				|| prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
				|| prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
		PreferenceUtils.putBoolean(KEY_ISMI, isMIUI);// 保存是否MIUI
		return isMIUI;
	}

	/**
	 * 检查手机是否是miui
	 * 
	 * @ref http://dev.xiaomi.com/doc/p=254/index.html
	 * @return
	 */
	public static boolean isMIUI2() {
		String device = Build.MANUFACTURER;
		System.out.println("Build.MANUFACTURER = " + device);
		if (device.equals("Xiaomi")) {
			System.out.println("this is a xiaomi device");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否已经添加了自启管理
	 * @param context
	 * @return
	 */
	public static boolean isMiAutoStartOpen(Context context) {
		try {
			Class<?> classType = Class.forName("android.miui.AppOpsUtils");
			Object simpelObject = classType.newInstance();
			Method[] methods = classType.getMethods();
			Class<?>[] types = null;
			if (methods != null && methods.length > 0) {
				for (Method method : methods) {
					Log.d(TAG, "method-->" + method.getName());
					Log.d(TAG, "getReturnType-->" + method.getReturnType());
					if (method.getName().equals("getApplicationAutoStart")) {
						types = method.getParameterTypes();
						// if(types!=null && types.length>0){
						// for (Class<?> class1 : types) {
						// Log.d(TAG, "type-->"+class1.getName());
						// }
						// }
						break;
					}
					Log.d(TAG, "-------------------------");
				}
			}

			Method method = classType.getDeclaredMethod(
					"getApplicationAutoStart", types);
			Integer r = (Integer) method.invoke(simpelObject, new Object[] {
					context, context.getPackageName() });
			Log.d(TAG, "r-->" + r);
			return r == 0;
		} catch (Exception e) {
			Log.e(TAG, "isMiAutoStartOpen", e);
		}
		return true;
	}

	/**
	 * 跳转到小米手机的自启管理界面
	 * @param context
	 * @return
	 */
	public static boolean startMi(Context context) {
		try {
			context.startActivity(getMiIntent(context));
		} catch (Exception e) {
			startMiBack(context);
		}
		return true;
	}

	/**
	 * 跳转到小米的授权管理界面
	 * @param context
	 */
	public static void startMiBack(final Context context) {
		int version = SystemUtils.getMIUIVersion();
		if (version == SystemUtils.V5) {
			try {
				Intent intent = new Intent();
				intent.setAction("miui.intent.action.PERM_CENTER");
				context.startActivity(intent);
			} catch (Exception e) {
			}
		} else if (version == SystemUtils.V6 || version == SystemUtils.V7) {
			try {
				Intent intent = new Intent();
				intent.setAction("miui.intent.action.LICENSE_MANAGER");
				context.startActivity(intent);
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 跳转到小米手机的自启管理界面所需要的intent
	 * @param context
	 * @return
	 */
	private static Intent getMiIntent(Context context) {
		Intent intent = new Intent();
		intent.setClassName("com.miui.securitycenter",
				"com.miui.permcenter.autostart.AutoStartManagementActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return intent;
	}
}
