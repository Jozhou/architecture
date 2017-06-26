package com.architecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.architecture.utils.ViewInject;
import com.architecture.widget.adapterview.IFooterBar;
import com.architecture.widget.layoutview.MLinearLayout;
import com.architecture.R;

public class FooterBar extends MLinearLayout<Void> implements IFooterBar {
	
	@ViewInject("tvcontent")
	protected TextView tvContent;

	public FooterBar(Context context) {
		super(context);
	}
	
	public FooterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.footview_layout;
	}
	
	@Override
	protected void onApplyData() {
		
	}

	@Override
	public void showLoading() {
		tvContent.setText(R.string.footer_loading);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void showLoadMore() {
//		tvContent.setText(R.string.footer_loadmore);
//		setVisibility(View.VISIBLE);
		setVisibility(View.GONE);
	}

	@Override
	public void showLoadAll() {
		tvContent.setText(R.string.footer_loadall);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		setVisibility(View.GONE);
	}

}
