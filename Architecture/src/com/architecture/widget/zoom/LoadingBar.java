package com.architecture.widget.zoom;

import com.architecture.R;
import com.architecture.widget.layoutview.MLinearLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LoadingBar extends MLinearLayout<Void> implements ILoadingBar {

	protected TextView mProgressText;
	
	public LoadingBar(Context context) {
		super(context);
	}
	
	public LoadingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LoadingBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void initializeLayout(Context context) {
		super.initializeLayout(context);
		setFocusable(false);
		setClickable(false);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.widget_loadingbar;
	}

	@Override
	protected void onFindView() {
		mProgressText = (TextView) findViewById(R.id.loadingbar_txt);
	}

	@Override
	protected void onBindListener() {
		
	}

	@Override
	protected void onApplyData() {
		
	}
	
	public ProgressBar getProgressBar() {
		return null;
	}
	
	public TextView getProgressText() {
		return mProgressText;
	}
	
	/**
	 * 设置进度
	 * @param percent
	 */
	public void setProcessValue(int percent) {
		if(mProgressText != null) {
			if(percent < 0)
				percent = 0;
			if(percent > 100)
				percent = 100;
			mProgressText.setText(String.valueOf(percent) + "%");
		}
	}

}
