package com.architecture.widget;

import com.architecture.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DashLine extends View {

	private int dashColor;
	private float dashWidth;
	private float dashGap;
	private Paint mPaint;
	private Path mPath;
	private DashPathEffect dashPathEffect;
	public DashLine(Context context) {
		this(context, null);
	}
	
	public DashLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DashLine);
		dashColor = ta.getColor(R.styleable.DashLine_dashColor, 0x000000);
		dashWidth = ta.getDimension(R.styleable.DashLine_dashWidth, 2);
		dashGap = ta.getDimension(R.styleable.DashLine_dashGap, 2);
		ta.recycle();
		dashPathEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 1);
		mPaint = new Paint();
		mPaint.setColor(dashColor);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setPathEffect(dashPathEffect);
		mPath = new Path();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		mPath.moveTo(0, 0);
		mPath.lineTo(getMeasuredWidth(), 0);
		mPaint.setStrokeWidth(getMeasuredHeight());
		canvas.drawPath(mPath, mPaint);
		super.onDraw(canvas);
	}
}
