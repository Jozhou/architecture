package com.architecture.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import com.architecture.widget.imageview.graphics.MBitmapDrawable;

/**
 * 可自动回收内存的ImageView
 * @author lijunma
 *
 */
public class MRecycleImageView extends MImageView {

	public MRecycleImageView(Context context) {
		super(context);
	}
	
	public MRecycleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MRecycleImageView(Context context, AttributeSet attrs, int paramInt) {
		super(context, attrs, paramInt);
	}

    /**
     * @see android.widget.ImageView#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        // This has been detached from Window, so clear the drawable
        setImageDrawable(null);
        super.onDetachedFromWindow();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	boolean needreset = false;
    	Drawable drawable = getDrawable();
    	if(drawable != null) {
    		if(drawable instanceof MBitmapDrawable) {
	    		if(((MBitmapDrawable) drawable).getIsMultiDisplayed()) {
	    			needreset = true;
	    		}
    		} else if(drawable instanceof LayerDrawable) {
    			LayerDrawable layerDrawable = (LayerDrawable) drawable;
                for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
                    if(layerDrawable.getDrawable(i) instanceof MBitmapDrawable) {
        	    		if(((MBitmapDrawable) layerDrawable.getDrawable(i)).getIsMultiDisplayed()) {
        	    			needreset = true;
        	    		}
            		}
                }
    		}
    	}
    	if(needreset) {
    		synchronized (drawable) {
        		int dwidth = drawable.getIntrinsicWidth();
                int dheight = drawable.getIntrinsicHeight();

                int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
                int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
                if (dwidth <= 0 || dheight <= 0 || ScaleType.FIT_XY == getScaleType()) {
                	drawable.setBounds(0, 0, vwidth, vheight);
                } else {
                	drawable.setBounds(0, 0, dwidth, dheight);
                }
                super.onDraw(canvas);
			}
    	} else {
    		super.onDraw(canvas);
    	}
    }

    /**
     * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
     */
    @Override
    public void setImageDrawable(Drawable drawable) {

        // Keep hold of previous Drawable
        final Drawable previousDrawable = getDrawable();

        // Call super to set new Drawable
        super.setImageDrawable(drawable);

        // Notify new Drawable that it is being displayed
        notifyDrawable(drawable, true);

        // Notify old Drawable so it is no longer being displayed
        notifyDrawable(previousDrawable, false);
    }

    /**
     * Notifies the drawable that it's displayed state has changed.
     *
     * @param drawable
     * @param isDisplayed
     */
    private static void notifyDrawable(Drawable drawable, final boolean isDisplayed) {
        if (drawable instanceof MBitmapDrawable) {
            // The drawable is a CountingBitmapDrawable, so notify it
            ((MBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
        } else if (drawable instanceof LayerDrawable) {
            // The drawable is a LayerDrawable, so recurse on each layer
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
                notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
            }
        }
    }

    public static interface IStatusChanged {
    	void onBegin();
    	void onEnd();
    	void onProgress(int percent);
    }

}
