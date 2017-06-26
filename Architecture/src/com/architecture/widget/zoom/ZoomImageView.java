package com.architecture.widget.zoom;

import com.architecture.R;
import com.architecture.manager.WindowManager;
import com.architecture.widget.imageview.MRecycleImageView.IStatusChanged;
import com.architecture.widget.imageview.zoom.IZoomImageView;
import com.architecture.widget.imageview.zoom.MZoomImageView;
import com.architecture.widget.layoutview.MFrameLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;

public class ZoomImageView extends MFrameLayout<String> implements IZoomImageView {

	protected boolean mLoadFromLocal;
	
	protected View mFloatLayer;
	protected ILoadingBar mLoadingBar;
	protected MZoomImageView mImageView;
	
	public ZoomImageView(Context paramContext) {
		super(paramContext);
	}
	
	public ZoomImageView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}
	
	public ZoomImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.widget_zoomimageview;
	}

	@Override
	protected void onFindView() {
		mImageView = (MZoomImageView) findViewById(R.id.zoomimageview_imageview);
		mFloatLayer = findViewById(R.id.zoomimageview_floatlayer);
		mLoadingBar = getLoadingBar();
		mLoadingBar.setVisibility(View.GONE);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		addView((View)mLoadingBar, 1, lp);
	}
	
	protected ILoadingBar getLoadingBar() {
		LoadingBar bar = new LoadingBar(getContext());
		return bar;
	}

	@Override
	protected void onBindListener() {
		mImageView.setWidth(WindowManager.get().getScreenWidth(),
				(int)(WindowManager.get().getScreenWidth() * 0.75));
		mImageView.setLoadingResID(getLoadingResID());
    	mImageView.setLoadErrResID(getLoadErrResID());
		mImageView.setStatusListener(new IStatusChanged() {
			@Override
			public void onProgress(int percent) {
				if(mLoadingBar != null) {
					mLoadingBar.setProcessValue(percent);
				}
			}
			
			@Override
			public void onEnd() {
				if(mLoadingBar != null) {
					mLoadingBar.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onBegin() {
				if(mLoadingBar != null) {
					mLoadingBar.setProcessValue(0);
					mLoadingBar.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onApplyData() {
		mImageView.setImageUrl(mDataItem, mLoadFromLocal);
	}
	
	/**
	 * 是否只是从本地加载
	 */
	public void setLoadFromLocal(boolean value) {
		mLoadFromLocal = value;
	}
	
	/**================= MZoomImageView ===================**/

	public float getDefaultScale() {
		if(mImageView != null)
			return mImageView.getDefaultScale();
		return -1;
	}

	public float getScale() {
		if(mImageView != null)
			return mImageView.getScale();
		return -1;
	}

	public void setMaxScale(float paramFloat) {
		if(mImageView != null)
			mImageView.setMaxScale(paramFloat);
	}

	public void setMinScale(float paramFloat) {
		if(mImageView != null)
			mImageView.setMinScale(paramFloat);
	}

	public void setScale(float paramFloat) {
		if(mImageView != null)
			mImageView.setScale(paramFloat);
	}

	public void animateScale(float paramFloat) {
		if(mImageView != null)
			mImageView.animateScale(paramFloat);
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		if(mImageView != null)
			mImageView.setGestureDetector(gestureDetector);
	}
	
	protected int getLoadingResID() {
		return R.drawable.placeholder;
	}
	
	protected int getLoadErrResID() {
		return R.drawable.placeholder;
	}
}
