package com.architecture.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.architecture.R;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.ViewInject;
import com.architecture.widget.layoutview.MLinearLayout;

/**
 * Created by 20141022 on 2017/1/18.
 */
public class DoubleListView extends MLinearLayout{

    @ViewInject("ll_left")
    private LinearLayout llLeft;
    @ViewInject("ll_right")
    private LinearLayout llRight;
    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public DoubleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DoubleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleListView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_double_list;
    }

    @Override
    protected void onApplyData() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addItem(View v, int pos) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        measureChild(llLeft, widthMeasureSpec, heightMeasureSpec);
        measureChild(llRight, widthMeasureSpec, heightMeasureSpec);
        int lheight = llLeft.getMeasuredHeight();
        int rheight = llRight.getMeasuredHeight();
        LogcatUtils.e("DoubleListView","lheight:" + lheight + "," + "rheight:" + rheight);
        if (pos % 2 ==0) {
            llLeft.addView(v, params);
        } else {
            llRight.addView(v,params);
        }
    }
}
