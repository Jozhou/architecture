package com.architecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.architecture.R;

public class RotateView extends ImageView {
	
	protected int mDuration = 5000;
	protected int mFrameCount = 360;
	protected Animation mAnimation;

    public RotateView(Context context) {
        super(context);
        initView(context);
    }

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context) {
        if(getVisibility() == View.VISIBLE) {
        	startAnimation();
        }
    }
    
    public void startAnimation(int frameCount, int duration) {
    	mDuration = duration;
    	mFrameCount = frameCount;
    	if(mAnimation == null) {
    		mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
    		mAnimation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    return (float)Math.floor(input * mFrameCount) / mFrameCount;
                }
            });
    	}
    	mAnimation.cancel();
    	mAnimation.reset();
    	mAnimation.setDuration(mDuration);
        startAnimation(mAnimation);
    }

    public void startAnimation() {
    	startAnimation(mFrameCount, mDuration);
    }
    
    public void stopAnimation() {
    	if(mAnimation != null) {
    		mAnimation.cancel();
    		mAnimation.reset();
    		mAnimation = null;
    	}
    }
    
    @Override
    public void setVisibility(int visibility) {
    	super.setVisibility(visibility);
    	if(visibility == View.VISIBLE) {
    		startAnimation();
    	} else {
    		stopAnimation();
    	}
    }
}