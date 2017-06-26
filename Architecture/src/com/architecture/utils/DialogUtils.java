package com.architecture.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.architecture.activity.base.BaseActivity;
import com.architecture.application.App;
import com.architecture.context.FragmentTag;
import com.architecture.context.IntentCode;
import com.architecture.fragment.ConfirmFragment;
import com.architecture.fragment.base.IDialogInterface;
import com.architecture.manager.ActivityManager;
import com.architecture.R;

public class DialogUtils {

	public static final int TOAST_MAXLEN = 50;

	public static void showToastMessage(int resId) {
		showToastMessage(resId, Toast.LENGTH_SHORT);
	}

	public static void showToastMessage(int resId, int duration) {
		showToastMessage(App.getInstance().getResources().getString(resId), duration);
	}

	public static void showToastMessage(String text) {
		showToastMessage(text, Toast.LENGTH_SHORT);
	}

	public static void showToastMessage(final String text, final int duration) {
		if(Looper.getMainLooper() == Looper.myLooper()) {
			_showToastMessage(text, duration);
		} else {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					_showToastMessage(text, duration);
				}
			});
		}
	}

	private static void _showToastMessage(String text, int duration) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		if (text.length() > TOAST_MAXLEN) {
			text = text.substring(0, TOAST_MAXLEN);
		}
		
		Toast mToast = Toast.makeText(App.getInstance(), text, duration);
		mToast.setText(text);
		mToast.setDuration(duration);
		
		mToast.show();
	}

	public static void showAlertDialog(final String msg) {
		if(Looper.getMainLooper() == Looper.myLooper()) {
			_showAlertDialog(msg);
		} else {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					_showAlertDialog(msg);
				}
			});
		}
	}

	private static void _showAlertDialog(final String msg) {
		Activity activity = ActivityManager.get().currentActivity();
		if(activity != null) {
			Builder b = new Builder(activity);
			b.setMessage(msg);
			b.setPositiveButton(R.string.action_sure,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			b.show();
		} else {
			_showToastMessage(msg, Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 显示只有确定的对话框
	 * @param title
	 */
	public static void showAlertFragment(final CharSequence title) {
		Activity activity = ActivityManager.get().currentActivity();
		if(activity != null && activity instanceof BaseActivity) {
			showAlertFragment((BaseActivity) activity, title, null);
		} else {
			Builder b = new Builder(activity);
			b.setMessage(title);
			b.setPositiveButton(R.string.action_sure,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			b.show();
		}
	}

	/**
	 * 显示只有确定的对话框
	 * @param title
	 */
	public static void showAlertFragment(final CharSequence title, final String ok) {
		Activity activity = ActivityManager.get().currentActivity();
		if(activity != null && activity instanceof BaseActivity) {
			showAlertFragment((BaseActivity) activity, title, ok, null);
		} else {
			Builder b = new Builder(activity);
			b.setMessage(title);
			b.setPositiveButton(ok,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			b.show();
		}
	}
	
	/**
	 * 显示只有确定按钮的对话框
	 * @param context
	 * @param title
	 * @param listener
	 */
	public static void showAlertFragment(final BaseActivity context, final CharSequence title, 
			IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, "", Gravity.CENTER, 
				context.getString(R.string.action_ok), "", 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示只有确定按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param content 内容
	 * @param listener
	 */
	public static void showAlertFragment(final BaseActivity context, final CharSequence title, 
			final String ok, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, "", Gravity.CENTER, ok, "", 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示只有确定按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param content 内容
	 * @param ok 确定按钮文本
	 * @param listener
	 */
	public static void showAlertFragment(final BaseActivity context, final CharSequence title, 
			final CharSequence content, final String ok, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, content, Gravity.CENTER, ok, "", 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示只有确定按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param titleGravity 标题文字的方向
	 * @param titleIcon 图标资源id
	 * @param content 内容
	 * @param contentGravity 内容文字的方向
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param autoclose 自动关闭的毫秒数 0 不自动关闭
	 * @param listener
	 */
	public static void showAlertFragment(final BaseActivity context, final CharSequence title, int titleGravity, int titleIcon,
			final CharSequence content, int contentGravity, final String ok, final int autoclose, IDialogInterface listener) {
		showConfirmFragment(context, title, titleGravity, titleIcon, content, contentGravity, ok, "", 
				autoclose, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	

	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title
	 * @param listener
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, "", Gravity.CENTER, 
				context.getString(R.string.action_ok), context.getString(R.string.action_cancel), 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param content 内容
	 * @param listener
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, 
			final CharSequence content, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, content, Gravity.CENTER, 
				context.getString(R.string.action_ok), context.getString(R.string.action_cancel), 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param content 内容
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param listener
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, 
			final String ok, final String cancel, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, "", Gravity.CENTER, ok, cancel, 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param content 内容
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param listener
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, 
			final CharSequence content, final String ok, final String cancel, IDialogInterface listener) {
		showConfirmFragment(context, title, Gravity.CENTER, 0, content, Gravity.CENTER, ok, cancel, 
				0, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param titleGravity 标题文字的方向
	 * @param titleIcon 图标资源id
	 * @param content 内容
	 * @param contentGravity 内容文字的方向
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param autoclose 自动关闭的毫秒数 0 不自动关闭
	 * @param listener
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, int titleGravity, int titleIcon,
			final CharSequence content, int contentGravity, final String ok, final String cancel, final int autoclose, IDialogInterface listener) {
		showConfirmFragment(context, title, titleGravity, titleIcon, content, contentGravity, ok, cancel, 
				autoclose, listener, FragmentTag.TAG_CONFIRM, R.id.frame_container);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param titleGravity 标题文字的方向
	 * @param titleIcon 图标资源id
	 * @param content 内容
	 * @param contentGravity 内容文字的方向
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param autoclose 自动关闭的毫秒数 0 不自动关闭
	 * @param listener
	 * @param tag
	 * @param containerID 装载fragment的控件id
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, int titleGravity, int titleIcon,
			final CharSequence content, int contentGravity, final String ok, final String cancel, final int autoclose, 
			IDialogInterface listener, String tag, int containerID) {
		showConfirmFragment(context, title, titleGravity, titleIcon, content, contentGravity, ok, cancel, 
				autoclose, listener, tag, containerID, true);
	}
	
	/**
	 * 显示有确定、取消按钮的对话框
	 * @param context
	 * @param title 标题
	 * @param titleGravity 标题文字的方向
	 * @param titleIcon 图标资源id
	 * @param content 内容
	 * @param contentGravity 内容文字的方向
	 * @param ok 确定按钮文本
	 * @param cancel 取消按钮文本
	 * @param autoclose 自动关闭的毫秒数 0 不自动关闭
	 * @param listener
	 * @param tag
	 * @param containerID 装载fragment的控件id
	 */
	public static void showConfirmFragment(final BaseActivity context, final CharSequence title, int titleGravity, int titleIcon,
			final CharSequence content, int contentGravity, final String ok, final String cancel, final int autoclose, 
			IDialogInterface listener, String tag, int containerID, boolean addToBackStack) {
		Bundle b = new Bundle();
		b.putCharSequence(IntentCode.INTENT_TITLE, title);
		b.putCharSequence(IntentCode.INTENT_MESSAGE, content);
		b.putString(IntentCode.INTENT_CANCELTEXT, cancel);
		b.putString(IntentCode.INTENT_OKTEXT, ok);
		b.putInt(IntentCode.INTENT_AUTOCLOSE, autoclose);
		b.putInt(IntentCode.INTENT_TITLE_GRAVITY, titleGravity);
		b.putInt(IntentCode.INTENT_CONTENT_GRAVITY, contentGravity);
		b.putInt(IntentCode.INTENT_TITLE_ICON, titleIcon);
		b.putBoolean(IntentCode.INTENT_ADDTOBACKSTACK, addToBackStack);
		ConfirmFragment fragment = context.addFragment(ConfirmFragment.class, tag, containerID, addToBackStack);
		if(fragment != null) {
			try {
				fragment.setArguments(b);
				fragment.setDialogListener(listener);
			} catch (IllegalStateException e) {
				
			}
		}
	}

}