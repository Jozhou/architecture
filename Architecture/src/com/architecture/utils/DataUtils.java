package com.architecture.utils;

import com.architecture.common.data.Account;

/**
 * 用SharedPreferences存储
 * 
 */

public class DataUtils {
	
	private static final String TAG = DataUtils.class.getSimpleName();
	

	/****************  Acccount 相关  ************************/
	private static final String KEY_ACCOUNT_NAME = "account_name";
	private static final String KEY_ACCOUNT_USERNAME = "account_username";
	private static final String KEY_ACCOUNT_PASSWORD = "account_password";
    private static final String KEY_ACCOUNT_RSCLASS = "account_rsclass";
    private static final String KEY_ACCOUNT_MAJOR = "account_major";
    private static final String KEY_ACCOUNT_TOKEN = "account_token";
    
	private static final String KEY_ACCOUNT_LASTLOGIN = "account_lastlogin";
	/**
	 * 首页的新手引导浮层
	 */
	private static final String KEY_MAIN_GUID_0 = "key_main_guid_0_";
    
    /**
     * 上次发生crash的时间戳
     */
    public static final String KEY_LASTCRASH = "lastCrashTime";
    
    /**
     * apk环境
     */
    public static final String KEY_APKENVIRONMENT = "apk_environment";

    /**
     * 是否第一次安装
     */
    public static final String KEY_ISFIRST_INSTALL = "isFirstInstall";
    
    /**
     * 合作司机引导页是否已展示
     */
    public static final String KEY_PTDRIVER_GUIDE = "ptdriver_guide_100";
    
	/**
	 * 获取首页引导是否查看
	 */
	public static boolean getWorkMainGuid0() {
		return PreferenceUtils.getBoolean(KEY_MAIN_GUID_0 + Account.get().userName);
	}
	
	/**
	 * 保存用户信息
	 * @param account
	 */
	public static void putAccount(Account account) {
		if (account == null) {
			return;
		}
		try {
			PreferenceUtils.putString(KEY_ACCOUNT_USERNAME, account.userName);
			PreferenceUtils.putString(KEY_ACCOUNT_PASSWORD, account.password);
			PreferenceUtils.putString(KEY_ACCOUNT_RSCLASS, account.rsclass);
            PreferenceUtils.putString(KEY_ACCOUNT_MAJOR, account.major);
            PreferenceUtils.putString(KEY_ACCOUNT_TOKEN, account.token);
			PreferenceUtils.putString(KEY_ACCOUNT_NAME, account.name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存用户上次登录时间
	 * @param account
	 */
	public static void putAccountLastLogin() {
		try {
			PreferenceUtils.putLong(KEY_ACCOUNT_LASTLOGIN, System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public static void getAccount(Account account) {
		try  {
			account.userName = PreferenceUtils.getString(KEY_ACCOUNT_USERNAME);
			account.password = PreferenceUtils.getString(KEY_ACCOUNT_PASSWORD);
			account.rsclass = PreferenceUtils.getString(KEY_ACCOUNT_RSCLASS);
			account.major = PreferenceUtils.getString(KEY_ACCOUNT_MAJOR);
			account.token = PreferenceUtils.getString(KEY_ACCOUNT_TOKEN);
			account.name = PreferenceUtils.getString(KEY_ACCOUNT_NAME);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 获取用户上次登录时间
	 * @return
	 */
	public static long getAccountLastLogin() {
		return PreferenceUtils.getLong(KEY_ACCOUNT_LASTLOGIN);
	}
	
	/**
	 * 移除用户信息
	 */
	public static void removeAccount() {
		PreferenceUtils.remove(KEY_ACCOUNT_USERNAME);
		PreferenceUtils.remove(KEY_ACCOUNT_NAME);
		PreferenceUtils.remove(KEY_ACCOUNT_PASSWORD);
		PreferenceUtils.remove(KEY_ACCOUNT_RSCLASS);
		PreferenceUtils.remove(KEY_ACCOUNT_MAJOR);
		PreferenceUtils.remove(KEY_ACCOUNT_TOKEN);
	}
	
}
