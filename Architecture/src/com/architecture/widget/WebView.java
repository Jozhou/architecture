package com.architecture.widget;

import java.lang.reflect.Method;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;

import com.architecture.utils.DialogUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.OSUtils;

public class WebView extends android.webkit.WebView {

	private static final String TAG = WebView.class.getSimpleName();
	
	public static final int REQUESTCODE_FILECHOOSER = 1568901;
	public static final String SCHEME_WTAI_MC = "wtai://wp/mc;";
	
	protected String mWebviewUrl;
	protected ValueCallback<Uri> mUploadMessage;
	protected boolean mDownloadPicOnLoad;
    
	/**
	 * 是否允许页面加载过程中下载图片
	 * @param value
	 */
	public void setDownloadPicOnLoad(boolean value) {
		mDownloadPicOnLoad = value;
	}
	
	/**
	 * 取得当前正在加载的url地址
	 */
	public String getUrl() {
		return mWebviewUrl;
	}
	
	public WebView(Context context) {
		super(context);
		initializeWebView();
	}
	
	public WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeWebView();
	}

	public WebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeWebView();
	}
	
	protected boolean needScroll(){
		return true;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void initializeWebView() {
		if (needScroll()) {
			setScrollbarFadingEnabled(true);
			setVerticalScrollBarEnabled(true);
			setHorizontalScrollBarEnabled(false);
			setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		}
		getSettings().setUseWideViewPort(false);
		getSettings().setLightTouchEnabled(true);
		getSettings().setDefaultTextEncodingName("UTF-8");
		getSettings().setSupportZoom(true);
		getSettings().supportMultipleWindows();
		getSettings().setJavaScriptEnabled(true);
		getSettings().setAllowFileAccess(true);
		getSettings().setBuiltInZoomControls(false);
		getSettings().setSupportMultipleWindows(true);
		getSettings().setAppCacheEnabled(true);
		getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getContext().getCacheDir()
				.getAbsolutePath();
		getSettings().setAppCachePath(appCachePath);
		if(OSUtils.hasFroyo()) {
			getSettings().setPluginState(PluginState.ON);
		}
		getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		if(OSUtils.hasHoneycomb()) {
			getSettings().setAllowContentAccess(true);
		}
		if (OSUtils.hasHoneycomb()) {
			if(getContext() instanceof Activity) {
				((Activity)getContext()).getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 
						WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		}
		if (OSUtils.hasEclairMr1()) {
			getSettings().setDomStorageEnabled(true);
		}
		clearFocus();
		clearHistory();
		clearView();
//		setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

    @Override
    public void destroy() {
    	mUploadMessage = null;
		super.removeAllViews();
		super.destroy();
    }
	
	@SuppressWarnings("deprecation")
	protected void doEmulateShiftHeld() {
        try {
            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
            shiftPressEvent.dispatch(this);
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("deprecation")
    protected void cancelEmulateShiftHeld() {
        try {
            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.FLAG_CANCELED,
                    KeyEvent.KEYCODE_CLEAR, 0, 0);
            shiftPressEvent.dispatch(this);
        } catch (Exception e) {
        }
    }
	
    @Override
    public void loadUrl(String url) {
		if(!mDownloadPicOnLoad) {
			getSettings().setBlockNetworkImage(true);
		}
    	super.loadUrl(url);
    }
    
    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
		if(!mDownloadPicOnLoad) {
			getSettings().setBlockNetworkImage(true);
		}
    	super.loadUrl(url, additionalHttpHeaders);
    }
    
    @Override
    public void loadData(String data, String mimeType, String encoding) {
		if(!mDownloadPicOnLoad) {
			getSettings().setBlockNetworkImage(true);
		}
    	super.loadData(data, mimeType, encoding);
    }
    
    @Override
    public void loadDataWithBaseURL(String baseUrl, String data,
    		String mimeType, String encoding, String historyUrl) {
		if(!mDownloadPicOnLoad) {
			getSettings().setBlockNetworkImage(true);
		}
    	super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }
    
	/**
	 * 设置点击对象的背景框
	 */
	public void setLightTouchEnabled() {
    	loadUrl("javascript:eval(\"window.SetBodyStyleTapColor=function() {" +
	            "	var bodystyle = document.body.style.cssText;" +
	            "	if (bodystyle == undefined || bodystyle == null)" +
	            "		bodystyle = '';" +
	            "	var tapstylekey = '-webkit-tap-highlight-color';" +
	            "	if (bodystyle.indexOf(tapstylekey) < 0) {" +
	            "		bodystyle += (bodystyle == '' ? '' : ';') + tapstylekey + ':rgba(0,0,0,0);';" +
	            "		document.body.style.cssText = bodystyle;" +
    			"	}" +
	    		"}\");");
    	loadUrl("javascript:SetBodyStyleTapColor();");
    }

	/**
	 * 滑动至顶部
	 */
	public void scrollToTop() {
        loadUrl("javascript:window.scrollTo(0,0)");
    }
	
	/**
	 * 复制文本
	 */
	public void copyText() {  
		try {
			if (OSUtils.hasECLAIR01()) {  
				Method m = WebView.class.getMethod("emulateShiftHeld", Boolean.TYPE);   
				m.invoke(this, false);   
			} else {
				Method m = WebView.class.getMethod("emulateShiftHeld");   
				m.invoke(this);   
			}
		} catch (Exception e) {
			doEmulateShiftHeld();
		}
	}
	
	/**
	 * 取消复制文本
	 */
	public void cancelCopyText() {  
		try {
			cancelEmulateShiftHeld();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 移除cookie和session
	 */
	public void removeSessionCookie() {
		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		CookieSyncManager.getInstance().sync();
	}
	
	/**
	 * 处理选择文件的操作（在Activity的onActivityResult中调用）
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 */
	public void handlerActivityResultForFileChooser(int requestCode, 
			int resultCode, Intent intent) {
		if (requestCode == REQUESTCODE_FILECHOOSER) {
			if (mUploadMessage == null)
				return;
			Uri result = intent == null || resultCode != Activity.RESULT_OK ? 
					null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	public static class MWebChromeClient extends WebChromeClient {
		
		private WebView webview;
		private Context activity;
		
		public MWebChromeClient(WebView w, Context a) {
			webview = w;
			activity = a;
		}
		
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			LogcatUtils.i(TAG, consoleMessage.message());
			return super.onConsoleMessage(consoleMessage);
		}
		
		// For Android 3.0+
		public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ) {  
			webview.mUploadMessage = uploadMsg;  
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
			intent.addCategory(Intent.CATEGORY_OPENABLE);  
			intent.setType("image/*");  
			if (activity instanceof Activity) {
				((Activity) activity).startActivityForResult(Intent.createChooser(intent, 
						"File Chooser"), REQUESTCODE_FILECHOOSER); 
			}
		}

		// For Android < 3.0
		public void openFileChooser( ValueCallback<Uri> uploadMsg ) {
			openFileChooser(uploadMsg, "");
		}


		// For Android > 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
			openFileChooser(uploadMsg, "");
		}
		
	}
	
	public static class MWebViewClient extends WebViewClient {
		
		protected WebView webview;
		protected Context activity;
		
		public MWebViewClient(WebView w, Context a) {
			webview = w;
			activity = a;
		}
		
		@Override
		public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
			webview.mWebviewUrl = url;
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(android.webkit.WebView view, String url) {
			webview.setLightTouchEnabled();
			if(!webview.mDownloadPicOnLoad) {
				webview.getSettings().setBlockNetworkImage(false);
			}
			super.onPageFinished(view, url);
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
			try {
				if(activity != null) {
					if (url.startsWith(WebView.SCHEME_TEL)) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						activity.startActivity(intent);
						return true;
					} else if (url.startsWith(SCHEME_WTAI_MC)) {
						Intent intent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(WebView.SCHEME_TEL + url.substring(SCHEME_WTAI_MC.length())));
						activity.startActivity(intent);
						return true;
					} else if (url.startsWith(WebView.SCHEME_MAILTO)) {
						Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
						activity.startActivity(intent);
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
		
	}
	
	public static class MDownloadListener implements DownloadListener {
		
		@SuppressWarnings("unused")
		private WebView webview;
		private Activity activity;
		
		public MDownloadListener(WebView w, Activity a) {
			webview = w;
			activity = a;
		}
		
		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			if(activity.getPackageManager().resolveActivity(intent, 0) == null) {
				DialogUtils.showToastMessage("您的手机未安装任何浏览器应用，无法完成下载");
				return;
			}
			activity.startActivity(intent);
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		invalidate();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}