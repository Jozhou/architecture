package com.architecture.application;

import com.architecture.activity.common.PhotoChooserActivity;
import com.architecture.context.Config;
import com.architecture.database.DataBaseHelper;
import com.architecture.utils.FileUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.OSUtils;
import com.architecture.utils.PackageUtils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ViewConfiguration;

public class App extends Application {

	private static String TAG = "App";
	protected static App instance;
	
	public static App getInstance() {
		return instance;
	}
	
    @Override
    public void onCreate() {
        super.onCreate();
		instance  =  this;
		LogcatUtils.i(TAG,
				"app oncreate in " + PackageUtils.getProcessName(this));
		if (!PackageUtils.isMainProcess(this)) {
			// 非主进程不初始化相关信息
			return;
		}
		initializeStrictMode();
		initializeDataBase();
		ZCCrashHandler.get();
		clearCache();
    }
    
    /**
     * 防止ViewConfiguration内存泄露
     */
    protected void hackViewConfiguration() {
		ViewConfiguration.get(this);
    }
    
	@Override
	public void onTerminate() {
		super.onTerminate();
		if (!PackageUtils.isMainProcess(this)) {
			// 非主进程不进行操作
			return;
		}
		exit();
	}

	public void exit() {
		LogcatUtils.i(TAG, "app exit");
		DataBaseHelper.get().closeDataBase();
		OSUtils.keepWifiOff();
		OSUtils.keepCpuOff();
	}

	/**
	 * 加入严格模式的检测，尽早暴露问题
	 */
	private void initializeStrictMode() {
		if (Config.IS_DEBUG && OSUtils.hasGingerbread()) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyLog().build());
		}
	}

	/**
	 * 初始化数据库
	 */
	private void initializeDataBase() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataBaseHelper.get();
			}
		}).start();
	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileUtils.removeFileOverPeriod(PhotoChooserActivity.CachePicFolder(), 86400000);
			}
		}).start();
	}

	/**
	 * 注册本地receiver
	 * 
	 * @param receiver
	 * @param fileter
	 */
	public void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter fileter) {
		try {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, fileter);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消注册本地receiver
	 * 
	 * @param receiver
	 */
	public void unregisterLocalReceiver(BroadcastReceiver receiver) {
		try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送本地广播
	 * 
	 * @param intent
	 */
	public void sendLocalBroadcast(Intent intent) {
		try {
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
}
