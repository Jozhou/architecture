package com.architecture.widget.loading;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.architecture.utils.ViewInjectUtils;
import com.architecture.R;

public abstract class LoadingLayout extends RelativeLayout implements ILoadingLayout {

	public static final int NOTSET = 0;
	public static final int INITIALIZED = 1;
	public static final int LOADING = 2;
	public static final int SUCCESSFUL = 4;
	public static final int ERROR = 5;
	
	protected static final int WHAT_SHOWLOADING = 0;
	protected static final int DELAY_SHOWLOADING = 500;

	protected View BlankView;
	protected View LoadingView;
	protected View ErrorView;
	protected View ContentView;
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case WHAT_SHOWLOADING:
				gotoLoading();
				break;
			}
		};
	};
	
	/**
	 * 获取当前状态
	 * @return
	 */
	public int getStatus() {
		return mStatus;
	}

	private boolean initializing;
	
	protected int mStatus = NOTSET;
	protected Context mContext;
	
	/**
	 * 加载数据失败后，在resume是否重新加载
	 * @return
	 */
	protected boolean getRetryOnError() {
		return true;
	}

	public LoadingLayout(Context context) {
		super(context);
		initializeLayout(context);
	}
	
	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeLayout(context);
	}
	
	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeLayout(context);
	}
	
	protected void initializeLayout(Context context) {
		mContext = context;
		setContentView();
		onInjectView();
		onFindView();
		onBindListener();
		onApplyData();
		mStatus = INITIALIZED;
	}
	
	protected void setContentView() {
		initializing = true;
		int contentResId = getLayoutResId();
		if(contentResId != 0) {
			ContentView = LayoutInflater.from(mContext).inflate(contentResId, null, false);
		}
		if(ContentView == null) {
			ContentView = getLayoutView();
		}
		if(ContentView == null) {
			ContentView = new RelativeLayout(mContext);
		}
		addView(ContentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		initializing = false;
	}
	
	/**
	 * 初始化控件、获取内部控件（注释类遍历）
	 */
	protected void onInjectView() {
		ViewInjectUtils.onInjectView(this);
	}
	
	/**
	 * 初始化控件、获取内部控件
	 */
	protected void onFindView() {
		
	}
	
	/**
	 * 绑定控件事件
	 */
	protected void onBindListener() {
		
	}
	
	/**
	 * 加载数据
	 */
	protected void onApplyData() {
		
	}
	
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		// 不加载其他控件
		if(initializing) {
			super.addView(child, index, params);
		} else {
			if(ContentView instanceof ViewGroup) {
				((ViewGroup) ContentView).addView(child, index, params);
			}
		}
	}
	
	@Override
	protected boolean addViewInLayout(View child, int index,
			android.view.ViewGroup.LayoutParams params,
			boolean preventRequestLayout) {
		// 不加载其他控件
		if(initializing) {
			return super.addViewInLayout(child, index, params, preventRequestLayout);
		}
		return false;
	}
	
	@Override
    protected void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	mHandler.removeCallbacksAndMessages(null);
    }
	
	/**
	 * 切换至加载中是否隐藏内容控件
	 * @return
	 */
	protected boolean hideContentViewOnLoading() {
		return true;
	}
	
	/**
	 * 切换至加载空白状态
	 */
	@Override
	public void gotoBlank() {
		if(BlankView == null) {
			int blankResId = getBlankResId();
			if(blankResId != 0) {
				BlankView = LayoutInflater.from(mContext).inflate(blankResId, null, false);
			}
			if(BlankView == null) {
				BlankView = getBlankView();
			}
			if(BlankView != null) {
				initializing = true;
				addView(BlankView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				initializing = false;
			}
		}
		mHandler.removeCallbacksAndMessages(null);
		if(LoadingView != null)
			LoadingView.setVisibility(View.GONE);
		if(ContentView != null)
			ContentView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(BlankView != null)
			BlankView.setVisibility(View.VISIBLE);
		mStatus = SUCCESSFUL;
	}

	/**
	 * 切换至加载中状态
	 */
	@Override
	public void gotoLoading() {
		gotoLoading(false);
	}
	
	/**
	 * 切换至加载中状态
	 */
	@Override
	public void gotoLoading(boolean delay) {
		if(delay) {
			mHandler.sendEmptyMessageDelayed(WHAT_SHOWLOADING, getLoadingDelay());
			return;
		}
		if(mStatus == LOADING)
			return;
		if(LoadingView == null) {
			int loadingResId = getLoadingResId();
			if(loadingResId != 0) {
				LoadingView = LayoutInflater.from(mContext).inflate(loadingResId, null, false);
			}
			if(LoadingView == null) {
				LoadingView = getLoadingView();
			}
			if(LoadingView != null) {
				initializing = true;
				addView(LoadingView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				initializing = false;
//			} else {
//				throw new IllegalStateException("you should override getLoadingResId or getLoadingView method");
			}
		}
		if(hideContentViewOnLoading()) {
			if(ContentView != null)
				ContentView.setVisibility(View.GONE);
		}
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(LoadingView != null)
			LoadingView.setVisibility(View.VISIBLE);
		mStatus = LOADING;
	}
	
	/**
	 * 获取延迟显示Loading的delay值
	 * @return
	 */
	protected int getLoadingDelay() {
		return DELAY_SHOWLOADING;
	}
	
	/**
	 * 切换至加载出错状态
	 */
	@Override
	public void gotoError() {
		if(ErrorView == null) {
			int errResId = getErrorResId();
			if(errResId != 0) {
				ErrorView = LayoutInflater.from(mContext).inflate(errResId, null, false);
			}
			if(ErrorView == null) {
				ErrorView = getErrorView();
			}
			if(ErrorView != null) {
				initializing = true;
				addView(ErrorView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				initializing = false;
			}
			if(!(ErrorView instanceof IErrorView)) {
				throw new IllegalStateException("ErrorView should implements IErrorView interface");
			}
			((IErrorView) ErrorView).setRefreshLayout(this);
		}
		mHandler.removeCallbacksAndMessages(null);
		if(mStatus == ERROR)
			return;
		if(LoadingView != null)
			LoadingView.setVisibility(View.GONE);
		if(ContentView != null)
			ContentView.setVisibility(View.GONE);
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.VISIBLE);
		mStatus = ERROR;
	}
	
	/**
	 * 切换至加载成功状态
	 */
	@Override
	public void gotoSuccessful() {
		mHandler.removeCallbacksAndMessages(null);
		if(LoadingView != null)
			LoadingView.setVisibility(View.GONE);
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(ContentView != null)
			ContentView.setVisibility(View.VISIBLE);
		mStatus = SUCCESSFUL;
	}
	
	/**
	 * 加载数据
	 */
	public void onApplyLoadingData() {
		gotoLoading();
	}

	/**
	 * 获取布局文件resid
	 * @return
	 */
	protected View getLayoutView() {
		return null;
	}

	/**
	 * 获取布局文件resid
	 * @return
	 */
	protected int getLayoutResId() {
		return 0;
	}
	
	/**
	 * 获取加载中布局文件resid
	 * @return
	 */
	protected int getLoadingResId() {
		return R.layout.view_loading_loading;
	}
	/**
	 * 获取加载中布局文件resid
	 * @return
	 */
	protected View getLoadingView() {
		return null;
//		return new LoadingView(mContext);
	}
	/**
	 * 获取加载出错布局文件resid
	 * @return
	 */
	protected int getErrorResId() {
		return 0;
	}
	/**
	 * 获取加载出错布局文件resid
	 * @return
	 */
	protected View getErrorView() {
		return new ErrorView(mContext);
	}
	/**
	 * 获取加载无数据布局文件resid
	 * @return
	 */
	protected int getBlankResId() {
		return R.layout.view_loading_blank;
	}
	/**
	 * 获取加载无数据布局文件resid
	 * @return
	 */
	protected View getBlankView() {
		return null;
	}
	
	/***=================== IViewCycle ==================***/
	
	public void onResume() {
		if(mStatus == INITIALIZED || 
				(mStatus == ERROR && getRetryOnError())) {
			onApplyLoadingData();
		}
	}

}