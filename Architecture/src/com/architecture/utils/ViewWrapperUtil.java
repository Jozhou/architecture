package com.architecture.utils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public abstract class ViewWrapperUtil {
	
	
	/**
	 * 递归注册所有button点击时间，建议在页面空间不是非常多的情况下使用这个方式，如果有性能要求，慎重此接口
	 * @param v
	 * @param listener
	 */
	public static void registorAllButtonClick(View v, OnClickListener listener) {
		if (v instanceof Button) {
			v.setOnClickListener(listener);
		} else if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) v;
			for (int i = 0; i < group.getChildCount(); i++) {
				View subv = group.getChildAt(i);
				if (subv instanceof Button) {
					subv.setOnClickListener(listener);
				} else if (subv instanceof ViewGroup) {
					registorAllButtonClick(subv, listener);
				}
			}
		}
	}
	
	
	public static void registerViewOnClickListener(int id, OnClickListener clickListener, Activity context){
		View view = context.findViewById(id);
		if(view != null){
			view.setOnClickListener(clickListener);
		}
	}

}
