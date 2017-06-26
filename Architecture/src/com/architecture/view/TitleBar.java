package com.architecture.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.utils.ViewInject;
import com.architecture.widget.layoutview.MRelativeLayout;
import com.architecture.R;

public class TitleBar extends MRelativeLayout<String> {

	@ViewInject("titlebar_lefticon")
	protected ImageView imgLeft;
	@ViewInject("titlebar_lefttext")
	protected TextView tvLeft;
	@ViewInject("titlebar_title")
	protected TextView tvTitle;
	@ViewInject("titlebar_title_icon")
	protected ImageView imgTitle;
	@ViewInject("titlebar_righticon")
	protected ImageView imgRight;
	@ViewInject("titlebar_righttext")
	protected TextView tvRight;
	
	public TitleBar(Context context) {
		super(context);
	}
	
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeAttr(attrs);
	}

	
	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeAttr(attrs);
	}
	
	protected void initializeAttr(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.TitleBar);
		
		int lefticon = typedArray.getResourceId(R.styleable.TitleBar_lefticon, 0);
		setLeftIcon(lefticon);
		
		String lefttext = typedArray.getString(R.styleable.TitleBar_lefttext);
		setLeftText(lefttext);
		
		int titleicon = typedArray.getResourceId(R.styleable.TitleBar_titleicon, 0);
		setTilteIcon(titleicon);
		
		String title = typedArray.getString(R.styleable.TitleBar_title);
		setTitle(title);
		
		int righticon = typedArray.getResourceId(R.styleable.TitleBar_righticon, 0);
		setRightIcon(righticon);
		
		String righttext = typedArray.getString(R.styleable.TitleBar_righttext);
		setRightText(righttext);
		
		typedArray.recycle();
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.view_titlebar;
	}
	
	@Override
	protected void onApplyData() {
		setTitle(mDataItem);
	}
	
	public void setLeftIcon(int resid) {
		if(imgLeft != null) {
			imgLeft.setImageResource(resid);
			imgLeft.setVisibility(resid != 0 ? View.VISIBLE : View.GONE);
		}
	}

	public void setLeftText(String text) {
		if(tvLeft != null) {
			tvLeft.setText(text);
			tvLeft.setVisibility(!TextUtils.isEmpty(text) ? 
					View.VISIBLE : View.GONE);
		}
	}
	
	public void setTitle(int resId) {
		setTitle(getResources().getString(resId));
	}
	
	public void setTitle(String title) {
		if(tvTitle != null) {
			tvTitle.setText(title);
			tvTitle.setVisibility(!TextUtils.isEmpty(title) ? 
					View.VISIBLE : View.GONE);
		}
	}

	public void setTilteIcon(int resid){
		if(imgTitle != null) {
			imgTitle.setImageResource(resid);
			imgTitle.setVisibility(resid != 0 ?	View.VISIBLE : View.GONE);
		}
	}
	
	public void setRightIcon(int resid) {
		if(imgRight != null) {
			imgRight.setImageResource(resid);
			imgRight.setVisibility(resid != 0 ? View.VISIBLE : View.GONE);
		}
	}
	
	public void setRightText(String text) {
		if(tvRight != null) {
			tvRight.setText(text);
			tvRight.setVisibility(!TextUtils.isEmpty(text) ? 
					View.VISIBLE : View.GONE);
		}
	}
	
	public void setRightText(String text, float textSize) {
		if(tvRight != null) {
			tvRight.setText(text);
			tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
			tvRight.setVisibility(!TextUtils.isEmpty(text) ? 
					View.VISIBLE : View.GONE);
		}
	}
	
	public void setLeftOnClickListener(OnClickListener listener) {
		if(imgLeft != null) {
			imgLeft.setOnClickListener(listener);
		}
		if(tvLeft != null) {
			tvLeft.setOnClickListener(listener);
		}
	}
	
	public void setRightOnClickListener(OnClickListener listener) {
		if(imgRight != null) {
			imgRight.setOnClickListener(listener);
		}
		if(tvRight != null) {
			tvRight.setOnClickListener(listener);
		}
	}
	
	public void setTitleOnClickListener(OnClickListener listener) {
		if(tvTitle != null) {
			tvTitle.setOnClickListener(listener);
		}
		if(imgTitle != null) {
			imgTitle.setOnClickListener(listener);
		}
	}
	
}
