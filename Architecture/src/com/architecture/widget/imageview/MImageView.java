package com.architecture.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.architecture.R;

public class MImageView extends ImageView {

	protected float mAspectRatio;
	protected float mMinAspectRatio;
	protected float mMaxAspectRatio;
	
	protected IDrawableCallback mDrawableCallback;
	
	public void setDrawableCallback(IDrawableCallback c) {
		mDrawableCallback = c;
	}
	
	public MImageView(Context context) {
		super(context);
	}
	
	public MImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeAttr(attrs, 0);
	}
	
	public MImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeAttr(attrs, defStyle);
	}
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	if(mAspectRatio > 0) {
    		int width = getMeasuredWidth();
    		if(width > 0) {
    			int height = (int) (width * mAspectRatio);
    			int minheight = (int) (width * mMinAspectRatio);
    			int maxheight = (int) (width * mMaxAspectRatio);
    			if(maxheight > 0 && height > maxheight) {
    				height = maxheight;
    			} else if(minheight > 0 && height < minheight) {
    				height = minheight;
    			}
    			setMeasuredDimension(width, height);
    			requestLayout();
    			if(mDrawableCallback != null) {
    				mDrawableCallback.onDrwableSizeChanged(width, height);
    			}
    		}
    	} else if(mAspectRatio == -1) {
    		int width = getMeasuredWidth();
    		if(width > 0) {
    			int height = 0;
    			Drawable d = getDrawable();
    			if(d != null && d.getIntrinsicHeight() > 0 && d.getIntrinsicWidth() > 0) {
    				height = width * d.getIntrinsicHeight() / d.getIntrinsicWidth();
    			} else {
    				height = getMeasuredHeight();
    			}
				int minheight = (int) (width * mMinAspectRatio);
    			int maxheight = (int) (width * mMaxAspectRatio);
    			if(maxheight > 0 && height > maxheight) {
    				height = maxheight;
    			} else if(minheight > 0 && height < minheight) {
    				height = minheight;
    			}
				setMeasuredDimension(width, height);
				requestLayout();
    			if(mDrawableCallback != null) {
    				mDrawableCallback.onDrwableSizeChanged(width, height);
    			}
    		}
    	}
    }
    
    public void setAspectRatio(float ratio) {
    	mAspectRatio = ratio;
    	requestLayout();
    }
	
	protected void initializeAttr(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.MImageView, defStyle, 0);

		mAspectRatio = typedArray.getFloat(R.styleable.MImageView_MImageView_aspectratio, 0);
		mMinAspectRatio = typedArray.getFloat(R.styleable.MImageView_MImageView_minaspectratio, 0);
		mMaxAspectRatio = typedArray.getFloat(R.styleable.MImageView_MImageView_maxaspectratio, 0);
		
		typedArray.recycle();
	}
	
    public static interface IProcessCallback {
    	Bitmap processBitmap(String url, Bitmap bitmap);
    }
    
    public static interface IDrawableCallback {
    	void onDrwableSizeChanged(int width, int height);
    }
}
