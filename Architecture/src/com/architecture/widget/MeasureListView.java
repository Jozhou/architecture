package com.architecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MeasureListView extends ListView{
	public MeasureListView(Context context){
		super(context);
	}
	
	public MeasureListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeasureListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int hMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, hMeasureSpec);
	}

}
