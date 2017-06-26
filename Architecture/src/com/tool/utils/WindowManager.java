package com.tool.utils;

import java.lang.reflect.Field;

import com.architecture.application.App;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


public class WindowManager {
	
	protected static final byte[] mLock = new byte[0];
	protected static WindowManager mInstance = null;
	public final static WindowManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new WindowManager(App.getInstance());
            }
            return mInstance;
        }
    }
	
	protected Context mContext = null;
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected double mDensity;
	protected int mDensityDpi;
	protected float mStatusBarHeight;
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public int getScreenWidth() {
		return mScreenWidth;
	}

	/**
	 * 获取屏幕高度
	 * @return
	 */
	public int getScreenHeight() {
		return mScreenHeight;
	}

	/**
	 * 获取屏幕密度
	 * @return
	 */
	public double getDensity() {
		return mDensity;
	}

	/**
	 * 获取屏幕密度dpi
	 * @return
	 */
	public int getDensityDpi() {
		return mDensityDpi;
	}
	
	/**
	 * 获取屏幕状态栏高度
	 * @return
	 */
	public float getStatusBarHeight() {
		return mStatusBarHeight;
	}
	
	protected WindowManager(Context context) {
		mContext = context;
		initialize();
	}
	
	protected void initialize() {
		DisplayMetrics metric = new DisplayMetrics();
		android.view.WindowManager wm = (android.view.WindowManager) mContext.
				getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
        mDensityDpi = metric.densityDpi;
        initStatusBarHeight();
	}
	
	protected void initStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
	        Object obj = c.newInstance();
	        Field field = c.getField("status_bar_height");
	        int resId = Integer.parseInt(field.get(obj).toString());
	        mStatusBarHeight = App.getInstance().getResources().getDimensionPixelSize(resId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取系统屏幕亮度
	 * @param activity
	 * @return
	 */
	public int getScreenBrightness() {
		int nowBrightnessValue = 0;
		ContentResolver resolver = mContext.getContentResolver();
		try {
			nowBrightnessValue = Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}
	
	/**
	 * 获取系统屏幕亮度是否自动调节
	 * @return
	 */
	public boolean isAutoBrightness() {
		ContentResolver resolver = mContext.getContentResolver();
        boolean automicBrightness = false;
        try {
            int mode = Settings.System.getInt(resolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            automicBrightness = mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }
	
	/**
	 * 设置系统屏幕亮度自动调节
	 */
    public void startAutoBrightness() {
		ContentResolver resolver = mContext.getContentResolver();
		try {
	        Settings.System.putInt(resolver,
	                Settings.System.SCREEN_BRIGHTNESS_MODE,
	                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    /**
	 * 停止系统屏幕亮度自动调节
	 */
    public void stopAutoBrightness() {
		ContentResolver resolver = mContext.getContentResolver();
		try {
	        Settings.System.putInt(resolver,
	                Settings.System.SCREEN_BRIGHTNESS_MODE,
	                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 针对横屏键盘弹起不调整窗体大小的bug
     * @param activity
     */
    public void assistActivity (Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    protected class AndroidBug5497Workaround {
    	
	    protected View mChildOfContent;
	    protected int usableHeightPrevious;
	    protected FrameLayout.LayoutParams frameLayoutParams;
	
	    protected AndroidBug5497Workaround(Activity activity) {
	        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
	        mChildOfContent = content.getChildAt(0);
	        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                possiblyResizeChildOfContent();
	            }
	        });
	        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
	    }
	
	    protected void possiblyResizeChildOfContent() {
	        int usableHeightNow = computeUsableHeight();
	        if (usableHeightNow != usableHeightPrevious) {
	            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
	            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
	            if (heightDifference > (usableHeightSansKeyboard/4)) {
	                // keyboard probably just became visible
	                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
	            } else {
	                // keyboard probably just became hidden
	                frameLayoutParams.height = usableHeightSansKeyboard;
	            }
	            mChildOfContent.requestLayout();
	            usableHeightPrevious = usableHeightNow;
	        }
	    }
	
	    protected int computeUsableHeight() {
	        Rect r = new Rect();
	        mChildOfContent.getWindowVisibleDisplayFrame(r);
	        return (r.bottom - r.top);
	    }
    }

}
