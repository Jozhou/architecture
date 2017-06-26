package com.architecture.models.api;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.minivolley.AuthFailureError;
import com.android.minivolley.DefaultRetryPolicy;
import com.android.minivolley.NetworkResponse;
import com.android.minivolley.Request;
import com.android.minivolley.RequestQueue;
import com.android.minivolley.Response;
import com.android.minivolley.VolleyError;
import com.android.minivolley.toolbox.ImageRequest;
import com.android.minivolley.toolbox.JsonObjectRequest;
import com.android.minivolley.toolbox.MultipartRequest;
import com.android.minivolley.toolbox.MultipartRequestParams;
import com.android.minivolley.toolbox.Volley;
import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.application.App;
import com.architecture.context.Config;
import com.architecture.models.api.IArrayOperater.PageType;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

abstract public class BaseOperater implements IOperater {

	public static final String TAG = "RequestTask";
	public static final String ERROR_NETWORK_BAD = "netword_bad";
	public static final int CONNECTION_TIMEOUT_MS = 2500;
	public static final int SOCKET_TIMEOUT_MS = 5000;
	public static final int TIMEOUT_RETRY_TIMES = 0;
	public static float BACKOFMULTIPLIER = 2.0f;

	protected WeakReference<Context> context;
	protected IRspListener listener;
	protected boolean code;
	protected String msg;
	protected String rspJson;
	protected boolean showLoading;
	protected HashMap<String, Object> params;
	protected JSONObject paramsObj;

	/**
	 * 返回状态值
	 * 
	 * @return
	 */
	public boolean getResultCode() {
		return code;
	}

	/**
	 * 返回状态描述
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 返回json字符串
	 * 
	 * @return
	 */
	public String getRspJson() {
		return rspJson;
	}

	/**
	 * 显示隐藏等待对话框
	 * 
	 * @param showLoading
	 */
	public void setShowLoading(boolean showLoading) {
		this.showLoading = showLoading;
	}

	/**
	 * 返回调试假数据
	 * 
	 * @return 需返回解密后的json字符串 eg: {"busiCode":"BASE000","code":1,"content":{
	 *         },"msg":"成功","status":"SUCCESS","uid":"xxxxxxx","version":"180"}
	 */
	protected String getDebugJson() {
		return "";
	}

	/**
	 * 显示服务器返回的错误信息的类型
	 * 
	 * @return
	 */
	protected ErrorTip getErrorTipType() {
		return ErrorTip.toast;
	}

	/**
	 * 获取http连接超时时间（毫秒）
	 * 
	 * @return
	 */
	protected int getConnectTimeOut() {
		return CONNECTION_TIMEOUT_MS;
	}

	/**
	 * 获取http读取超时时间（毫秒）
	 * 
	 * @return
	 */
	protected int getSocketTimeOut() {
		return SOCKET_TIMEOUT_MS;
	}

	/**
	 * 获取http重连次数
	 * 
	 * @return
	 */
	protected int getRetryTimes() {
		return TIMEOUT_RETRY_TIMES;
	}

	/**
	 * 获取token（用于放重复提交）
	 * 
	 * @return
	 */
	protected String getToken() {
		return "";
	}

	/**
	 * 取得context
	 * 
	 * @return
	 */
	protected Context getContext() {
		return context.get();
	}
	
	/**
	 * 是否自动关闭链接（跟随页面）
	 * @return
	 */
	@Override
	public boolean isAutoClose() {
		return true;
	}

	/**
	 * 初始化请求action
	 */
	abstract protected String getUrlAction();

	/**
	 * 解析返回json数据
	 * 
	 * @param response
	 *            JSONObject
	 */
	abstract protected void onParser(JSONObject response);

	/**
	 * 解析返回json数据
	 * 
	 * @param response
	 *            JSONArray
	 */
	abstract protected void onParser(JSONArray response);

	public BaseOperater(Context context) {
		this.context = new WeakReference<Context>(context);
		this.showLoading = true;
		this.params = new HashMap<String, Object>();
		this.paramsObj = new JSONObject();
		if (context != null && context instanceof BaseActivity) {
			if (isAutoClose()) {
				((BaseActivity) context).addOperater(this);
			}
		}
	}

	/**
	 * 生成url的参数
	 * 
	 * @return
	 */
	protected String getUrlParam() {
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		try {
			if (params != null) {
				for (String key : params.keySet()) {
					sb.append(key);
					sb.append("=");
					sb.append(params.get(key));
					sb.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 返回http的header的map
	 * 
	 * @return
	 */
	protected Map<String, String> getHttpHeader() {
		Map<String, String> headers = new HashMap<String, String>();
		String token = getToken();
		if (!TextUtils.isEmpty(token)) {
			headers.put("carAsyncToken", token);
		}
		return headers;
	}

	/**
	 * 获取http请求的类型(post get)
	 * 
	 * @return
	 */
	protected int getHttpMethod() {
		return Request.Method.GET;
	}
	
	/**
	 * 生成url
	 *
	 * @return
	 */
	protected String generateUrl() {
		StringBuffer url = new StringBuffer();
		url.append(Config.getBaseUrl()).append(getUrlAction()).append(getUrlParam());
		return url.toString();
	}

	/**
	 * 初始化multipart Post数据
	 *
	 * @param param
	 */
	protected void setPostParam(MultipartRequestParams param) {
		if (paramsObj != null) {
			Iterator iterator = paramsObj.keys();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				param.put(key, paramsObj.optString(key));
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
	}

	/**
	 * 发起http请求
	 * 
	 * @param l
	 * @return
	 */
	public boolean onReq(final RspListener l) {
		return onReq(l, getHttpMethod(), false);
	}

	/**
	 * 发起http请求
	 * 
	 * @param l
	 * @param multipart
	 * @return
	 */
	public boolean onReq(final RspListener l, boolean multipart) {
		return onReq(l, getHttpMethod(), multipart);
	}

	/**
	 * 发起http请求
	 * 
	 * @param l
	 * @param multipart
	 * @return
	 */
	public boolean onReq(final RspListener l, int method, boolean multipart) {
		listener = l;
		if (listener != null) {
			listener.setOperater(this);
		}
		if (!NetworkUtils.isNetWorkConnected()) {
			showLoading(false);
			showToastError(R.string.error_network_tip);
			if (listener != null) {
				listener._onRsp(false, null);
			}
			return false;
		}
		showLoading(true);
		String url = generateUrl();
		Response.Listener<String> succ = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (getContext() != null && getContext() instanceof Activity) {
					if (((Activity) getContext()).isFinishing())
						return;
				}
				rspJson = response;
				BaseOperater.this.onResponse(rspJson, listener);
			}
		};
		Response.ErrorListener error = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError exception) {
				if (getContext() != null && getContext() instanceof Activity) {
					if (((Activity) getContext()).isFinishing())
						return;
				}
				// debug 假数据
				if (Config.IS_DEBUG) {
					String debug_json = getDebugJson();
					if (!TextUtils.isEmpty(debug_json)) {
						rspJson = debug_json;
						BaseOperater.this.onResponse(debug_json, listener);
						return;
					}
				}
				showLoading(false);
				if (Config.IS_DEBUG) {
					showToastError(R.string.handleerror_response_error);
				} else {
					showToastError(R.string.network_response_error);
				}
				if (listener != null) {
					listener._onRsp(false, "request error");
				}
				LogcatUtils.e(TAG, exception.toString());
			}
		};
		Request<?> request = null;
		if (multipart) {
			MultipartRequestParams param = new MultipartRequestParams();
			setPostParam(param);
			request = new MultipartRequest(url, param, succ, error) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return getHttpHeader();
				}
			};
		} else {
			request = new JsonObjectRequest(method, url, paramsObj, succ, error) {
				@Override
				protected Response<String> parseNetworkResponse(NetworkResponse response) {
					Response<String> superResponse = super.parseNetworkResponse(response);
					/**
					 * Map<String, String> responseHeaders = response.headers;
					 * asyncStatus = responseHeaders.get("carAsyncStatus");
					 */
					return superResponse;
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return getHttpHeader();
				}
			};
		}
		DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(getSocketTimeOut(), getRetryTimes(), BACKOFMULTIPLIER);
		retryPolicy.setConnectionTimeOut(getConnectTimeOut());
		request.setRetryPolicy(retryPolicy);
		OperateManager.getInstance().onReq(request);
		return true;
	}

	/**
	 * 发起http image请求
	 * 
	 * @param l
	 * @return
	 */
	public boolean onReq(final String url, final BitmapRspListener l) {
		return onReq(url, 0, 0, l);
	}

	/**
	 * 发起http image请求
	 * 
	 * @param l
	 * @return
	 */
	public boolean onReq(final String url, final int maxWidth, final int maxHeight, final BitmapRspListener l) {
		listener = l;
		if (listener != null) {
			listener.setOperater(this);
		}
		if (!NetworkUtils.isNetWorkConnected()) {
			DialogUtils.showToastMessage(R.string.error_network_tip);
			if (listener != null) {
				listener._onRsp(false, null);
			}
			return false;
		}
		showLoading(true);
		Response.Listener<Bitmap> response = new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bitmap) {
				showLoading(false);
				if (listener != null) {
					listener._onRsp(true, bitmap);
				}
			}

		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				showLoading(false);
				if (listener != null) {
					listener._onRsp(false, null);
				}
			}
		};
		ImageRequest request = new ImageRequest(url, response, maxWidth, maxHeight, null, errListener);
		OperateManager.getInstance().onReq(request);
		return true;
	}

	/**
	 * 成功的处理请求
	 * 
	 * @param result
	 * @param l
	 */
	protected void onResponse(final String result, final IRspListener l) {
		JSONObject resultJ = null;
		try {
			showLoading(false);
			resultJ = new JSONObject(result);
			LogcatUtils.i(TAG, "response : " + resultJ.toString());
			if (resultJ != null) {
				code = resultJ.optBoolean("result");
				if (resultJ.has("message")) {
					msg = resultJ.optString("message");
				}
			}
			if (code) {
				onParser(resultJ);
				if (l != null) {
					l._onRsp(true, BaseOperater.this);
				}
			} else {
				showServerError(msg);
				if (l != null) {
					l._onRsp(false, resultJ);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (Config.IS_DEBUG) {
				showToastError(R.string.handlesucc_response_error);
			} else {
				showToastError(R.string.network_response_error);
			}
			if (l != null) {
				l._onRsp(false, "parse error");
			}
		}
	}


	/**
	 * 显示错误信息
	 * 
	 * @return
	 */
	protected void showToastError(int resId) {
		showToastError(App.getInstance().getString(resId));
	}

	/**
	 * 显示错误信息
	 * 
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
		}
		;
	}

	/**
	 * 显示隐藏等待对话框
	 * 
	 * @param show
	 */
	protected void showLoading(boolean show) {
		if (showLoading) {
			if (getContext() != null && getContext() instanceof BaseActivity) {
				((BaseActivity) getContext()).showLoading(show);
			}
		}
	}

	/**
	 * 请求处理接口
	 * 
	 * @author Administrator
	 *
	 */
	public abstract static interface IRspListener {
		
		void setOperater(BaseOperater operater);

		void _onRsp(boolean success, Object obj);

		void onRsp(boolean success, Object obj);

	}

	/**
	 * 请求处理类
	 * 
	 * @author Administrator
	 *
	 */
	public abstract static class RspListener implements Cloneable, IRspListener {

		protected BaseOperater mOperater;
		protected PageType mPageType = PageType.FirstPage;

		public void setOperater(BaseOperater operater) {
			mOperater = operater;
		}

		public void setPageType(PageType v) {
			mPageType = v;
		}

		public PageType getPageType() {
			return mPageType;
		}

		@Override
		public void _onRsp(boolean success, Object obj) {
			onRsp(success, obj);
		}

		@Override
		public abstract void onRsp(boolean success, Object obj);

		@Override
		public RspListener clone() {
			Object o = null;
			try {
				o = super.clone();
			} catch (CloneNotSupportedException e) {
				System.out.println(e.toString());
			}
			return (RspListener) o;
		}
	}

	/**
	 * 请求处理类
	 * 
	 * @author Administrator
	 *
	 */
	public abstract static class BitmapRspListener implements IRspListener {

		protected BaseOperater mOperater;

		public void setOperater(BaseOperater operater) {
			mOperater = operater;
		}

		@Override
		public void _onRsp(boolean success, Object obj) {
			onRsp(success, obj);
		}
		
		@Override
		public void onRsp(boolean success, Object obj) {
			if (obj != null && obj instanceof Bitmap) {
				onRsp(success, (Bitmap) obj);
			} else {
				onRsp(success, null);
			}
		}

		public abstract void onRsp(boolean success, Bitmap bitmap);

	}

	/**
	 * http连接管理类
	 * 
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
	 * 显示服务器返回的错误信息
	 *
	 * @return
	 */
	protected void showServerError(String msg) {
		if (TextUtils.isEmpty(msg)) {
			if (Config.IS_DEBUG) {
				msg = App.getInstance().getString(R.string.server_response_error, code);
			} else {
				msg = App.getInstance().getString(R.string.network_response_error);
			}
		}
		switch (getErrorTipType()) {
			case toast:
				DialogUtils.showToastMessage(msg);
				break;

			case dialog:
				DialogUtils.showAlertFragment(msg);
				break;

			default:
				break;
		}
		;
	}

	/**
	 * 错误提示类型
	 * 
	 * @author Administrator
	 *
	 */
	public enum ErrorTip {
		none, toast, dialog
	}
}
