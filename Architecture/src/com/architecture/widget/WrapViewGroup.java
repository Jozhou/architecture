package com.architecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WrapViewGroup extends ViewGroup {

    private final static int VIEW_MARGIN = 10;

    public WrapViewGroup(Context context) {
        super(context);
    }

    public WrapViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int cCount = getChildCount();

        // 遍历每个子元素
        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + VIEW_MARGIN;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight();
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth)
            {
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth + this.getChildAt(0).getMeasuredWidth()+ VIEW_MARGIN; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;
                // 开启记录下一行的高度
                lineHeight = childHeight;
            } else
            // 否则累加值lineWidth,lineHeight取最大高度
            {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1)
            {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

        final int count = getChildCount();
        int row = 0;
        int lengthX = arg1;
        int lengthY = arg2;
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth()+ VIEW_MARGIN;
            int height = child.getMeasuredHeight();
            lengthX += width ;
            lengthY = row * height + arg2 + height;
            //换行
            if (lengthX > arg3) {
                lengthX = width + arg1 + this.getChildAt(0).getMeasuredWidth()+ VIEW_MARGIN;
                row++;
                lengthY = row * height + arg2 + height;
                if(i > 0){
                    String str = ((TextView) this.getChildAt(i-1)).getText().toString();
                    ((TextView) this.getChildAt(i-1)).setText(str.replace("|", " "));
                }
            }

            if (i == count - 1)
            {
                String str = ((TextView)child).getText().toString();
                ((TextView)child).setText(str.replace("|", " "));
            }

            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }
    }
}