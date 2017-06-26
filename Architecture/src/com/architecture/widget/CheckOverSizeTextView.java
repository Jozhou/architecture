package com.architecture.widget;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

/**
 * <p style="color:purple">
 * If you want to check if the text content is over max size, you must set a
 * ellipsize type and maxline in xml or see {@link #setMaxLines(int)} and
 * {@link #setEllipsize(android.text.TextUtils.TruncateAt)}<br>
 * Because of invalidate is delayed,so you must use
 * {@link OnOverSizeChangedListener} call the callback method
 * </p>
 *
 * @author Super.Yuan <span style="color:green"> Feel free to contact me:
 *         <a href="mailto:462086630@qq.com">462086630@qq.com</a></span>
 *
 */
public class CheckOverSizeTextView extends TextView {

    protected boolean isOverSize;
    private OnOverSizeChangedListener changedListener;
    private int maxLineLowV;

    public CheckOverSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CheckOverSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckOverSizeTextView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (changedListener != null) {
            changedListener.onChanged(checkOverLine());
        }
    }

    private void init() {
        // invalidate when layout end
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (changedListener != null) {
                    changedListener.onChanged(checkOverLine());
                }
            }
        });
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        this.maxLineLowV = maxlines;
    }
    
    public int getMaxLineLowV() {
        return this.maxLineLowV;
    }

    /**
     * <p>
     * <span style="color: purple;">
     * <em>check if the text content has ellipsis </em></span>
     * </p>
     *
     * @return if the text content over maxlines
     */
    public boolean checkOverLine() {
        int maxLine = getMaxLineLowV();
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mLayout");
            field.setAccessible(true);
            Layout mLayout = (Layout) field.get(this);
            if (mLayout == null)
                return false;
            if (getLineCount() < maxLine) {
                isOverSize = false;
            } else {
                isOverSize = mLayout.getEllipsisCount(maxLine - 1) > 0 ? true : false;
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return isOverSize;
    }

    public boolean isOverSize() {
        return isOverSize;
    }

    public void displayAll() {
        setMaxLines(Integer.MAX_VALUE);
        setEllipsize(null);
    }

    public void hide(int maxlines) {
        setEllipsize(TruncateAt.END);
        setMaxLines(maxlines);
    }

    // set a listener for callback
    public OnOverSizeChangedListener getChangedListener() {
        return changedListener;
    }

    public void setOnOverLineChangedListener(OnOverSizeChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public interface OnOverSizeChangedListener {
        /**
         * <span style="color:purple">when invalide,the method will be called
         * and tell you whether the content text is over size
         *
         * @param isOverLine
         *            whether content text is over size
         */
        public void onChanged(boolean isOverSize);
    };
}