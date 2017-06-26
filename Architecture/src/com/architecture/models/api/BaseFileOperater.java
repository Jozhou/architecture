package com.architecture.models.api;

import java.lang.ref.WeakReference;

import com.android.minivolley.DefaultRetryPolicy;
import com.android.minivolley.Request;
import com.android.minivolley.RequestQueue;
import com.android.minivolley.Response;
import com.android.minivolley.VolleyError;
import com.android.minivolley.toolbox.FileDownloadRequest;
import com.android.minivolley.toolbox.Volley;
import com.architecture.activity.base.BaseActivity;
import com.architecture.application.App;
import com.architecture.context.Config;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.NetworkUtils;
import com.architecture.R;

import android.content.Context;

/**
 * file http请求基类
 * 
 * @author malijun
 */
public abstract class BaseFileOperater implements IOperater {

	public static final String TAG = "DCarTask";
	public static final int CONNECTION_TIMEOUT_MS = 5000;
	public static final int SOCKET_TIMEOUT_MS = 60000;
	public static final int TIMEOUT_RETRY_TIMES = 0;
	public static float BACKOFMULTIPLIER = 2.0f;

	protected String mUrl;
	protected boolean showLoading;
	protected RspListener listener;
	protected WeakReference<Context> context;

	/**
	 * 显示隐藏等待对话框
	 * @param showLoading
	 */
	public void setShowLoading(boolean showLoading) {
		this.showLoading = showLoading;
	}
	
	@Override
	public boolean isAutoClose() {
		return false;
	}
	
	/**
	 * 显示服务器返回的错误信息的类型
	 * @return
	 */
	protected ErrorTip getErrorTipType() {
		return ErrorTip.toast;
	}
	
	/**
	 * 获取http连接超时时间（毫秒）
	 * @return
	 */
	protected int getConnectTimeOut() {
		return CONNECTION_TIMEOUT_MS;
	}
	
	/**
	 * 获取http读取超时时间（毫秒）
	 * @return
	 */
	protected int getSocketTimeOut() {
		return SOCKET_TIMEOUT_MS;
	}
	
	/**
	 * 获取http重连次数
	 * @return
	 */
	protected int getRetryTimes() {
		return TIMEOUT_RETRY_TIMES;
	}
	
	/**
	 * 获取是否缓存文件
	 * @return
	 */
	protected boolean getShouldCache() {
		return true;
	}
	
	/**
	 * 取得context
	 * @return
	 */
	protected Context getContext() {
		return context.get();
	}
	
	/**
	 * 设置下载url地址
	 * @param url
	 */
	public void setUrlAction(String url) {
		mUrl = url;
	}

	/**
	 * 初始化请求action
	 */
	protected String getUrlAction() {
		return mUrl;
	}
	
	/**
	 * 获取存放下载文件的位置
	 * @return
	 */
	protected abstract String getStoredFilePath();

	public BaseFileOperater(Context context) {
		this.context = new WeakReference<Context>(context);
		this.showLoading = false;
		if (context != null && context instanceof BaseActivity) {
			if (isAutoClose()) {
				((BaseActivity) context).addOperater(this);
			}
		}
	}
	
	/**
	 * 取消http请求
	 */
	@Override
	public void cancel() {
		context.clear();
		listener = null;
		// 暂不实现从队列移除逻辑
	}
	
	/**
	 * 发起http请求
	 * @param listener
	 * @return
	 */
	public boolean onReq(final RspListener l) {
		listener = l;
		if (!NetworkUtils.isNetWorkConnected()) {
			showLoading(false);
			showToastError(R.string.error_network_tip);
			if(listener != null) {
				listener.onRsp(false);
			}
			return false;
		}
		showLoading(true);
		Response.ProgressListener succ = new Response.ProgressListener() {
			@Override
			public void onCanceled() {
				showLoading(false);
				if (listener != null) {
					listener.onCanceled();
				}
			}

			@Override
			public void onProgress(long total, long current) {
				if (listener != null) {
					listener.onProgress(total, current);
				}
			}

			@Override
			public void onSuccess() {
				showLoading(false);
				if (listener != null) {
					listener.onRsp(true);
				}
			}
		};
		Response.ErrorListener error = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError exception) {
				showLoading(false);
				if(Config.IS_DEBUG) {
					showToastError(R.string.handleerror_response_error);
				} else {
					showToastError(R.string.network_response_error);
				}
				if (listener != null) {
					listener.onRsp(false);
				}
				LogcatUtils.e(TAG, exception.toString());
			}
		};
		FileDownloadRequest request = new FileDownloadRequest(getUrlAction(), getStoredFilePath(), succ, error);
		DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(getSocketTimeOut(),
				getRetryTimes(), BACKOFMULTIPLIER);
		retryPolicy.setConnectionTimeOut(getConnectTimeOut());
		request.setRetryPolicy(retryPolicy);
		request.setShouldCache(getShouldCache());
		OperateManager.getInstance().onReq(request);
		return true;
	}

	/**
	 * 显示错误信息
	 * @return
	 */
	protected void showToastError(int resId) {
		showToastError(App.getInstance().getString(resId));
	}

	/**
	 * 显示错误信息
	 * @return
	 */
	protected void showToastError(String msg) {
		switch (getErrorTipType()) {
		case toast:
		case dialog:
			DialogUtils.showToastMessage(msg);
			break;
		case none:
		default:
			break;
		};
	}

	/**
	 * 显示隐藏等待对话框
	 * @param show
	 */
	protected void showLoading(boolean show) {
		if(showLoading) {
			if (getContext() != null && getContext() instanceof BaseActivity) {
				((BaseActivity) getContext()).showLoading(show);
			}
		}
	}

	/**
	 * 请求处理类
	 * @author Administrator
	 *
	 */
	public abstract static class RspListener implements Cloneable {
		
		public abstract void onRsp(boolean success);
		public abstract void onProgress(long total, long current);
		public abstract void onCanceled();
		
	}

	/**
	 * http连接管理类
	 * @author Administrator
	 *
	 */
	public static class OperateManager {

		private static OperateManager manager = null;

		private RequestQueue queue;

		synchronized public static OperateManager getInstance() {
			if (manager == null) {
				manager = new OperateManager();
			}
			return manager;
		}

		private OperateManager() {
			if (queue != null) {
				queue.cancelAll(10);
			}
			queue = Volley.newRequestQueue(App.getInstance());
		}

		synchronized public void onReq(Request<? extends Object> req) {
			LogcatUtils.i(TAG, "request url : " + req.getUrl());
			queue.add(req);
		}

		public void onDestroy() {
			if (queue != null) {
				queue.cancelAll(10);
				queue.stop();
			}
			manager = null;
		}
	}
	
	/**
	 * 错误提示类型
	 * @author Administrator
	 *
	 */
	public enum ErrorTip {
		none,
		toast,
		dialog
	}
	
}
