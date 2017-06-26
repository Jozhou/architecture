package com.architecture.widget.loading;

import android.content.Context;
import android.util.AttributeSet;

import com.architecture.widget.layoutview.MRelativeLayout;
import com.architecture.R;

public class LoadingView extends MRelativeLayout<Void> {
	
	public LoadingView(Context context) {
		super(context);
	}
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.view_loading_loading;
	}

	@Override
	protected void onApplyData() {
		
	}

}
