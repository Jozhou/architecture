package com.architecture.widget.loading;

import android.content.Context;
import android.util.AttributeSet;

import com.architecture.R;

/**
 * 浅色底色的错误页
 * @author Administrator
 *
 */
public class ErrorView2 extends ErrorView {

	public ErrorView2(Context context) {
		super(context);
	}
	
	public ErrorView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.view_loading_failed_light;
	}
	
}
