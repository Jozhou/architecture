package com.architecture.common.data;

import java.util.UUID;

import android.content.Context;
import android.text.TextUtils;

import com.architecture.activity.base.BaseActivity;
import com.architecture.cache.data.CacheManager;
import com.architecture.models.entry.BaseEntry;
import com.architecture.utils.DataUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.PackageUtils;

public class Account extends BaseEntry {

	private static final long serialVersionUID = 3290338719537789052L;
	private static final String TAG = Account.class.getSimpleName();

	private static final byte[] mLock = new byte[0];
	private static Account mInstance = null;

	public final static Account get() {
		synchronized (mLock) {
			if (mInstance == null) {
				mInstance = new Account();
			}
			return mInstance;
		}
	}

	public String token = "";
	public String userName = "";
	public String rsclass = "";
	public String major = "";
	public String password = "";
	public String name = "";
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("username:").append(userName).append(",rsclass:").append(rsclass).
			append(",major:").append(major);
		LogcatUtils.d(TAG, sb.toString());
		return sb.toString();
	}

	private Account() {
		DataUtils.getAccount(this);
	}

	/**
	 * 重新加载数据
	 */
	public void reload() {
		DataUtils.getAccount(this);
	}

	/**
	 * 是否已登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return isLogin(false);
	}

	/**
	 * 是否已登录
	 * 
	 * @param loadFromPref
	 *            是否从文件pref加载最新状态
	 * @return
	 */
	public boolean isLogin(boolean loadFromPref) {
		if (loadFromPref) {
			DataUtils.getAccount(this);
		}
		return !TextUtils.isEmpty(userName)
				&& !TextUtils.isEmpty(password);
	}

	/**
	 * 登录
	 * 
	 * @param driverId
	 * @param username
	 * @param password
	 * @param name
	 * @param tel
	 * @param serviceCityId
	 */
	public void login(String token, String userName,
			String rsclass, String major, String password, String name) {
		this.token = token;
		this.userName = userName;
		this.rsclass = rsclass;
		this.major = major;
		this.password = password;
		this.name = name;

		save();
	}

	public void logout(final Context context) {
		logout(context, true);
	}

	/**
	 * 退出登录
	 * 
	 * @param context
	 * @param requestOperater
	 *            是否需要请求operater
	 */
	public void logout(final Context context, boolean requestOperater) {
		if (requestOperater) {
			if (context != null && context instanceof BaseActivity) {
				((BaseActivity) context).showLoading(true);
			}
		} else {
			_logout();
		}
	}

	/**
	 * 退出登录
	 */
	private void _logout() {
		clear();
		// 跳转至登录页
		PackageUtils.restartApplication();
	}

	/**
	 * 清除登录信息
	 */
	public void clear() {
		userName = "";
		password = "";
		rsclass = "";
		major = "";
		token = "";
		name = "";
		// 清除账号信息
		DataUtils.removeAccount();
		// 清除缓存
		CacheManager.get().clearCache();
	}

	public void save() {
		DataUtils.putAccount(this);
	}

}
