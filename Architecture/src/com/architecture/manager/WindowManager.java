package com.architecture.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import com.architecture.application.App;

public class WindowManager extends com.tool.utils.WindowManager {

	private static final byte[] mLock = new byte[0];
	private static WindowManager mInstance = null;
	public final static WindowManager get() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new WindowManager(App.getInstance());
            }
            return mInstance;
        }
    }
	
	/**
	 * 获取屏幕状态栏高度
	 * @return
	 */
	public float getStatusBarHeight() {
		if(mStatusBarHeight <= 0) {
			Activity activity = ActivityManager.get().currentActivity();
			if(activity != null) {
		        Rect rect = new Rect();
		        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		        mStatusBarHeight = rect.top;
			}
		}
		return mStatusBarHeight;
	}
	
	protected WindowManager(Context context) {
		super(context);
	}
	
}
