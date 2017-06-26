package com.architecture.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.architecture.application.App;
import com.architecture.R;

public class IntentUtils {
	
	/**
	 * 拨打电话
	 * @param activity
	 * @param passengerPhone
	 */
	public static void call(String passengerPhone) {
		if (TextUtils.isEmpty(passengerPhone)) {
			DialogUtils.showToastMessage(R.string.cannot_use_empty_phone);
		} else {
			try {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + passengerPhone));
				phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				App.getInstance().startActivity(phoneIntent);
			} catch (Exception e) {
				OSUtils.copyToClipboard(passengerPhone, 
						App.getInstance().getString(R.string.call_phone_fail));
			}
		}
	}
	
	/**
	 * 打开网址
	 */
	public static boolean openUrl(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			OSUtils.copyToClipboard(url, context.getString(R.string.notinstall_actionview));
			return false;
		}
	}
	
}
