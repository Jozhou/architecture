package com.architecture.utils;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.architecture.application.App;

public class PreferenceUtils {
	
	public static void putString(String key, String value) {
		putString(key, value, false);
	}
	
	public static void putString(String key, String value, boolean async) {
		try {
			Editor editor = _getSharedPreferences().edit().putString(key, value);
			commitEditor(editor, async);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putString(String name, String key, String value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putString(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getString(String key) {
		return getString(key, "");
	}
	
	public static String getString(String key, String defValue) {
		try {
			return _getSharedPreferences().getString(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static String getString(String name, String key, String defValue) {
		try {
			return _getSharedPreferences(name).getString(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static void putStringSet(String key, Set<String> value) {
		try {
			Editor editor = _getSharedPreferences().edit().putStringSet(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putStringSet(String name, String key, Set<String> value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putStringSet(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> getStringSet(String key, Set<String> defValue) {
		try {
			return _getSharedPreferences().getStringSet(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static Set<String> getStringSet(String name, String key, Set<String> defValue) {
		try {
			return _getSharedPreferences(name).getStringSet(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static void putBoolean(String key, boolean value) {
		try {
			Editor editor = _getSharedPreferences().edit().putBoolean(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putBoolean(String name, String key, boolean value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putBoolean(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		try {
			return _getSharedPreferences().getBoolean(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static boolean getBoolean(String name, String key) {
		return getBoolean(name, key, false);
	}
	
	public static boolean getBoolean(String name, String key, boolean defValue) {
		try {
			return _getSharedPreferences(name).getBoolean(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static void putFloat(String key, float value) {
		try {
			Editor editor = _getSharedPreferences().edit().putFloat(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putFloat(String name, String key, float value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putFloat(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static float getFloat(String key) {
		return getFloat(key, 0);
	}
	
	public static float getFloat(String key, float defValue) {
		try {
			return _getSharedPreferences().getFloat(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static float getFloat(String name, String key) {
		return getFloat(name, key, 0);
	}
	
	public static float getFloat(String name, String key, float defValue) {
		try {
			return _getSharedPreferences(name).getFloat(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static void putInt(String key, int value) {
		try {
			Editor editor = _getSharedPreferences().edit().putInt(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putInt(String name, String key, int value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putInt(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	
	public static int getInt(String key, int defValue) {
		try {
			return _getSharedPreferences().getInt(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static int getInt(String name, String key) {
		return getInt(name, key, 0);
	}
	
	public static int getInt(String name, String key, int defValue) {
		try {
			return _getSharedPreferences(name).getInt(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static void putLong(String key, long value) {
		try {
			Editor editor = _getSharedPreferences().edit().putLong(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putLong(String name, String key, long value) {
		try {
			Editor editor = _getSharedPreferences(name).edit().putLong(key, value);
			commitEditor(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static long getLong(String key) {
		return getLong(key, 0);
	}
	
	public static long getLong(String key, long defValue) {
		try {
			return _getSharedPreferences().getLong(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static long getLong(String name, String key) {
		return getLong(name, key, 0);
	}
	
	public static long getLong(String name, String key, long defValue) {
		try {
			return _getSharedPreferences(name).getLong(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}
	
	public static boolean contains(String key) {
		if (key == null) {
			return false;
		}
		return _getSharedPreferences().contains(key);
	}
	
	public static boolean contains(String name, String key) {
		if (key == null) {
			return false;
		}
		return _getSharedPreferences(name).contains(key);
	}
	
	
	public static void remove(String key) {
		if (key == null) {
			return;
		}
		Editor editor = _getSharedPreferences().edit().remove(key);
		commitEditor(editor);
	}
	
	
	public static void remove(String name, String key) {
		if (key == null) {
			return;
		}
		Editor editor = _getSharedPreferences(name).edit().remove(key);
		commitEditor(editor);
	}
	
	private static SharedPreferences _getSharedPreferences() {
		return _getSharedPreferences(App.getInstance().getPackageName());
	}
	
	private static SharedPreferences _getSharedPreferences(String name) {
		return App.getInstance()
				.getSharedPreferences(name, Context.MODE_MULTI_PROCESS);
	}
	
	/**
	 * 提交Editor， 针对版本不同，提交方式不一样。 
	 * 从api9开始，建议使用apply，是异步的。提速
	 * @param editor
	 */
	public static void commitEditor(Editor editor) {
		commitEditor(editor, false);
	}
	
	/**
	 * 提交Editor， 针对版本不同，提交方式不一样。 
	 * 从api9开始，建议使用apply，是异步的。提速
	 * @param editor
	 */
	public static void commitEditor(Editor editor, boolean async) {
		if(editor == null) 
			return;
		if (async) {
			editor.commit();
		} else {
			if(OSUtils.hasGingerbread()) {
				editor.apply();
			} else {
				editor.commit();
			}
		}
	}
}
