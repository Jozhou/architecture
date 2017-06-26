package com.architecture.widget.imageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


/**
 * Created by 20141022 on 2016/11/22.
 */
public class MResizeThumbImageView extends MThumbImageView {
    public MResizeThumbImageView(Context context, AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
    }

    public MResizeThumbImageView(Context context) {
        super(context);
    }

    public MResizeThumbImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();

        if(d!=null){
            // ceil not round - avoid thin vertical gaps along the left/right edges  
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得  
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
