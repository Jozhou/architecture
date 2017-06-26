package com.architecture.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.utils.ViewInjectUtils;
import com.architecture.R;

public class RelativeLayout extends android.widget.RelativeLayout implements ILoadingLayout {

	protected OnClickListener mLoadingClick = null;
	
	/**
	 * 设置加载数据的事件
	 * @param listener
	 */
	public void setOnLoadingClickListener(OnClickListener listener) {
		mLoadingClick = listener;
	}
	
	public RelativeLayout(Context context) {
		super(context);
		initializeLayout(context, null);
	}

	public RelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeLayout(context, attrs);
	}

	public RelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeLayout(context, attrs);
	}
	
	protected View BlankView;
	protected View LoadingView;
	protected View ErrorView;
	protected android.widget.RelativeLayout ContentView;

	private boolean initializing;
	protected Context mContext;
	
	protected void initializeLayout(Context context, AttributeSet attrs) {
		mContext = context;
		setContentView(attrs);
		onInjectView();
		onFindView();
		onBindListener();
		onApplyData();
	}
	
	protected void setContentView(AttributeSet attrs) {
		initializing = true;
		
		int contentResId = getLayoutResId();
		if(contentResId != 0) {
			ContentView = (android.widget.RelativeLayout) LayoutInflater.from(mContext).inflate(contentResId, null, false);
		}
		if(ContentView == null) {
			ContentView = getLayoutView();
		}
		if(ContentView == null) {
			ContentView = new android.widget.RelativeLayout(mContext);
		}
		ContentView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
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
	
	/**
	 * 切换至加载中是否隐藏内容控件
	 * @return
	 */
	protected boolean hideContentViewOnLoading() {
		return false;
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
			if(!(BlankView instanceof IBlankView)) {
				throw new IllegalStateException("BlankView should implements IBlankView interface");
			} else {
				((IBlankView) BlankView).setRefreshLayout(this);
			}
		}
		if(LoadingView != null)
			LoadingView.setVisibility(View.INVISIBLE);
		if(ContentView != null)
			ContentView.setVisibility(View.INVISIBLE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(BlankView != null)
			BlankView.setVisibility(View.VISIBLE);
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
			}
		}
		if(hideContentViewOnLoading()) {
			if(ContentView != null)
				ContentView.setVisibility(View.INVISIBLE);
		}
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(LoadingView != null)
			LoadingView.setVisibility(View.VISIBLE);
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
			} else {
				((IErrorView) ErrorView).setRefreshLayout(this);
			}
		}
		if(LoadingView != null)
			LoadingView.setVisibility(View.GONE);
		if(ContentView != null)
			ContentView.setVisibility(View.INVISIBLE);
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 切换至加载成功状态
	 */
	@Override
	public void gotoSuccessful() {
		if(LoadingView != null)
			LoadingView.setVisibility(View.GONE);
		if(BlankView != null)
			BlankView.setVisibility(View.GONE);
		if(ErrorView != null)
			ErrorView.setVisibility(View.GONE);
		if(ContentView != null)
			ContentView.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取布局文件resid
	 * @return
	 */
	protected android.widget.RelativeLayout getLayoutView() {
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
		return 0;
	}
	/**
	 * 获取加载无数据布局文件resid
	 * @return
	 */
	protected View getBlankView() {
		return new BlankView(mContext);
	}

	@Override
	public void onApplyLoadingData() {
		if (mLoadingClick != null) {
			mLoadingClick.onClick(this);
		}
	}
	
}