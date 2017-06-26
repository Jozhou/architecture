package com.architecture.widget;

import com.architecture.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;;

public class QueryStateCheckBox extends CheckBox implements OnTouchListener{

	public QueryStateCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnTouchListener(this);
	}
	
	public QueryStateCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}
	
	@Override
	public boolean performClick() {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int width = this.getWidth();
		int offset = getResources().getDimensionPixelOffset(R.dimen.dd_dimen_12px);
		float x = event.getX();
		if(x>0 && x<(width/2-offset)){
			if(!isChecked()){
				setChecked(true);
			}
		}else if(x>(width/2+offset) && x<width){
			if(isChecked()){
				setChecked(false);
			}
		}
		return true;
	}
	
	
	
}
