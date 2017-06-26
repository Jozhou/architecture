package com.tool.utils;

import com.architecture.application.App;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class WindowUtils {
	private static final String LOG_TAG = "DCarWindowUtils";
    private static View mView = null;

    /**
     * 显示弹出框
     *
     * @param context
     * @param view
     */
    public static void showPopupWindow() {
    	try {
	        if (mView != null) {
	            LogcatUtils.i(LOG_TAG, "return cause already shown");
	            return;
	        }
	
	        LogcatUtils.i(LOG_TAG, "showPopupWindow");
	
	        // 获取应用的Context
	        Context context = App.getInstance();
	        // 获取WindowManager
	        WindowManager manager = (WindowManager) context
	                .getSystemService(Context.WINDOW_SERVICE);
	
	        mView = setUpView(context);
	
	        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	
	        // 类型
	        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
	
	        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
	
	        // 设置flag
	        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
	        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
	        // 不设置这个flag的话，home页的划屏会有问题
	        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
	        params.flags = flags;
	        // 不设置这个弹出框的透明遮罩显示为黑色
	        params.format = PixelFormat.TRANSLUCENT;
	
	        params.width = LayoutParams.WRAP_CONTENT;
	        params.height = LayoutParams.WRAP_CONTENT;
	
	        params.gravity = Gravity.TOP|Gravity.LEFT;
	
	        manager.addView(mView, params);
	
	        LogcatUtils.i(LOG_TAG, "add view");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
    	try {
	        LogcatUtils.i(LOG_TAG, "hidePopupWindow");
	        if (null != mView) {
	            // 获取WindowManager
	            WindowManager manager = (WindowManager) App.getInstance()
	                    .getSystemService(Context.WINDOW_SERVICE);
	            manager.removeView(mView);
	            mView = null;
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    }

    private static View setUpView(final Context context) {
		LogcatUtils.i(LOG_TAG, "setUp view");
		final LinearLayout linearLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
		lp.gravity = Gravity.CENTER;
		linearLayout.setBackgroundColor(0x01FF0000);
		linearLayout.setLayoutParams(lp);
		Button btn = new Button(context);
		btn.setLayoutParams(lp);
		btn.setBackgroundColor(Color.TRANSPARENT);
		linearLayout.addView(btn);
		return linearLayout;
    }
}
