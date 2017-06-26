package com.architecture.widget.imageview.zoom;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Scroller;

import com.architecture.manager.WindowManager;
import com.architecture.widget.imageview.MThumbImageView;

/**
 * 带缩放功能的imageview
 * @author Administrator
 *
 */
public class MZoomImageView extends MThumbImageView {
	
	private static Method getPointerCount = null;
	private static Method getX = null;
	private static Method getY = null;
	private float defaultScale = 1;
	private Runnable LONG_CLICK = new Runnable() {
		public void run() {
			if (!m_drag && m_pressed)
				m_long = performLongClick();
		}
	};
	private int LONG_CLICK_DELAY = 500;
	private float SLOP = 16.0F;
	private float m_cx;
	private float m_cy = 0.0F;
	private boolean m_drag = false;
	private float m_dstScale = 1.0F;
	private boolean m_long = false;
	private Matrix m_matrix;
	private float m_maxScale = 3.4028235E+38F;
	private float m_minScale = 0.0F;
	private boolean m_pressed = false;
	private float m_scale = 1.0F;
	private OnScaleChangeListener m_scaleListener;
	private OnScrollChangeListener m_scrollListener;
	private Scroller m_scroller = null;
	private float m_span = 0.0F;
	private float m_srcScale = 1.0F;
	private long m_start = -1L;
	private Bundle m_state = null;
	private VelocityTracker m_tracker = null;
	private float m_x;
	private float m_x0;
	private float m_y = 0.0F;
	private float m_y0 = 0.0F;
	protected int mWidth;
	protected int mHeight;
	private GestureDetector gestureDetector;
	private boolean m_initialized;

	static {
		try {
			getPointerCount = MotionEvent.class.getMethod("getPointerCount", new Class[0]);
			Class<?>[] arrayOfClass1 = new Class<?>[1];
			arrayOfClass1[0] = Integer.TYPE;
			getX = MotionEvent.class.getMethod("getX", arrayOfClass1);
			Class<?>[] arrayOfClass2 = new Class<?>[1];
			arrayOfClass2[0] = Integer.TYPE;
			getY = MotionEvent.class.getMethod("getY", arrayOfClass2);
		} catch (Throwable localThrowable) {
		}
	}

	public MZoomImageView(Context paramContext) {
		this(paramContext, null);
	}

	public MZoomImageView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 2130771984);
	}

	public MZoomImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);

		setFocusableInTouchMode(true);
		setScaleType(ImageView.ScaleType.MATRIX);
		this.m_matrix = new Matrix();
		this.m_matrix.setScale(this.m_scale, this.m_scale);
		setImageMatrix(this.m_matrix);
		ViewConfiguration localViewConfiguration = ViewConfiguration
				.get(paramContext.getApplicationContext());
		this.SLOP = localViewConfiguration.getScaledTouchSlop();
		this.LONG_CLICK_DELAY = ViewConfiguration.getLongPressTimeout();
		this.m_scroller = new Scroller(paramContext);
	}

	private int computeMaxScrollX() {
		return Math.max(computeHorizontalScrollRange() - getWidth(), 0);
	}

	private int computeMaxScrollY() {
		return Math.max(computeVerticalScrollRange() - getHeight(), 0);
	}

	private static float dist(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4) {
		return (float) Math.hypot(paramFloat3 - paramFloat1, paramFloat4
				- paramFloat2);
	}

	private int getPointerCount(MotionEvent paramMotionEvent) {
		int i;
		if (getPointerCount != null) {
			try {
				int j = ((Integer) getPointerCount.invoke(paramMotionEvent,
						new Object[0])).intValue();
				i = j;
				return i;
			} catch (Throwable localThrowable) {
				i = 1;
			}
		} else {
			i = 1;
		}

		return i;
	}

	private float getX(MotionEvent paramMotionEvent, int paramInt) {
		float f1;
		if (getX != null) {
			try {
				Method localMethod = getX;
				Object[] arrayOfObject = new Object[1];
				arrayOfObject[0] = Integer.valueOf(paramInt);
				float f2 = ((Float) localMethod.invoke(paramMotionEvent,
						arrayOfObject)).floatValue();
				f1 = f2;
				return f1;
			} catch (Throwable localThrowable) {
				f1 = paramMotionEvent.getX();
			}
		} else {
			f1 = paramMotionEvent.getX();
		}
		return f1;
	}

	private float getY(MotionEvent paramMotionEvent, int paramInt) {
		float f1;
		if (getY != null) {
			try {
				Method localMethod = getY;
				Object[] arrayOfObject = new Object[1];
				arrayOfObject[0] = Integer.valueOf(paramInt);
				float f2 = ((Float) localMethod.invoke(paramMotionEvent,
						arrayOfObject)).floatValue();
				f1 = f2;
				return f1;
			} catch (Throwable localThrowable) {
				f1 = paramMotionEvent.getY();
			}
		} else {
			f1 = paramMotionEvent.getY();
		}
		return f1;
	}

	private boolean updateScale(float paramFloat1, float paramFloat2,
			float paramFloat3) {
		boolean flag;
		if (paramFloat1 != this.m_scale) {
			float f = this.m_scale;
			this.m_scale = paramFloat1;
			this.m_matrix.setScale(paramFloat1, paramFloat1);
			setImageMatrix(this.m_matrix);
			updateScroll(paramFloat1 * (paramFloat2 + getScrollX()) / f
					- paramFloat2, paramFloat1 * (paramFloat3 + getScrollY())
					/ f - paramFloat3);
			onScaleChanged(f, paramFloat1);
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private boolean updateScroll(float paramFloat1, float paramFloat2) {
		boolean flag = false;
		int i = computeHorizontalScrollRange() - getWidth();
		int j = computeVerticalScrollRange() - getHeight();
		int k;
		int l;
		if (i > 0)
			k = Math.min(Math.max(0, (int) paramFloat1), i);
		else
			k = i / 2;
		if (j > 0)
			l = Math.min(Math.max(0, (int) paramFloat2), j);
		else
			l = j / 2;
		if (getScrollX() != k || getScrollY() != l) {
			scrollTo(k, l);
			flag = true;
		}
		return flag;
	}

	protected int computeHorizontalScrollRange() {
		Drawable localDrawable = getDrawable();
		int i;
		if (localDrawable != null) {
			i = (int) ((float) localDrawable.getIntrinsicWidth() * m_scale);
		} else
			i = 0;
		return i;
	}

	public void computeScroll() {
		if (!this.m_scroller.isFinished()) {
			this.m_scroller.computeScrollOffset();
			updateScroll(this.m_scroller.getCurrX(), this.m_scroller.getCurrY());
			postInvalidate();
		}
		if (m_start != -1L) {
			float f = (float) (System.currentTimeMillis() - m_start) / 200F;
			if (f > 1.0F)
				f = 1.0F;
			updateScale(m_srcScale + f * (m_dstScale - m_srcScale),
					getWidth() / 2, getHeight() / 2);
			if (f == 1.0F)
				m_start = -1L;
			else
				postInvalidate();
		}
	}

	protected int computeVerticalScrollRange() {
		Drawable localDrawable = getDrawable();
		int i;
		if (localDrawable != null) {
			i = (int) ((float) localDrawable.getIntrinsicHeight() * m_scale);
		} else
			i = 0;
		return i;
	}

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
	if ((this.m_state != null) && (getWidth() > 0)) {
		onRestoreInstanceState(this.m_state);
	} else {
			if(!m_initialized) {
				updateScroll(0.0F, 0.0F);
				m_initialized = true;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onMultiTouchEvent(MotionEvent paramMotionEvent) {
		float f1 = getX(paramMotionEvent, 0);
		float f2 = getY(paramMotionEvent, 0);
		float f3 = getX(paramMotionEvent, 1);
		float f4 = getY(paramMotionEvent, 1);
		int action = paramMotionEvent.getAction();
		switch (action) {
		case MotionEvent.ACTION_POINTER_2_DOWN:
		case MotionEvent.ACTION_POINTER_1_DOWN:
			this.m_cx = ((f1 + f3) / 2.0F);
			this.m_cy = ((f2 + f4) / 2.0F);
			this.m_span = dist(f1, f2, f3, f4);
			break;
		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
			animateScale(this.m_scale);
			break;
		case MotionEvent.ACTION_MOVE:
			float f5 = dist(f1, f2, f3, f4);
			updateScale(f5 * this.m_scale / this.m_span, this.m_cx, this.m_cy);
			invalidate();
			this.m_span = f5;

			break;
		}
		return true;
	}

	public void onRestoreInstanceState(Bundle paramBundle) {
		if (getWidth() == 0) {
			m_state = paramBundle;
		} else {
			Drawable drawable = getDrawable();
			float f;
			float f1;
			float f2;
			float f3;
			if (drawable != null)
				f = drawable.getIntrinsicWidth();
			else
				f = 1.0F;
			f1 = paramBundle.getFloat("scale");
			if (f1 != 0.0F && f1 != 3.402823E+038F)
				f1 /= f;
			setScale(f1);
			f2 = paramBundle.getFloat("scrollX");
			f3 = paramBundle.getFloat("scrollY");
			updateScroll(f2 * (f * m_scale) - (float) (getWidth() / 2), f3
					* (f * m_scale) - (float) (getHeight() / 2));
		}

	}

	public void onSaveInstanceState(Bundle paramBundle) {
		Drawable drawable = getDrawable();
		float f;
		float f1;
		float f2;
		float f3;
		if (drawable != null)
			f = drawable.getIntrinsicWidth();
		else
			f = 1.0F;
		if (m_scale < m_maxScale) {
			if (m_scale <= m_minScale)
				f1 = 0.0F;
			else
				f1 = f * m_scale;
		} else {
			f1 = 3.402823E+038F;
		}
		paramBundle.putFloat("scale", f1);
		f2 = getScrollX() + getWidth() / 2;
		f3 = getScrollY() + getHeight() / 2;
		paramBundle.putFloat("scrollX", f2 / (f * m_scale));
		paramBundle.putFloat("scrollY", f3 / (f * m_scale));

	}

	protected void onScaleChanged(float paramFloat1, float paramFloat2) {
		if (this.m_scaleListener == null)
			return;
		this.m_scaleListener.onScaleChanged(paramFloat1, paramFloat2);
	}

	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		if (this.m_scrollListener == null)
			return;
		this.m_scrollListener.onScrollChanged(paramInt1, paramInt2, paramInt3,
				paramInt4);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		boolean bool;
		if (gestureDetector != null
				&& gestureDetector.onTouchEvent(paramMotionEvent)) {
			return true;
		} else {
			if (getPointerCount(paramMotionEvent) > 1) {
				this.m_pressed = false;
				bool = onMultiTouchEvent(paramMotionEvent);
			} else {
				bool = true;
				float f1 = paramMotionEvent.getX();
				float f2 = paramMotionEvent.getY();
				if (this.m_tracker == null)
					this.m_tracker = VelocityTracker.obtain();
				this.m_tracker.addMovement(paramMotionEvent);
				int action = paramMotionEvent.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					getParent().requestDisallowInterceptTouchEvent(true);
					if (!this.m_scroller.isFinished()) {
						this.m_scroller.abortAnimation();
					}
					this.m_x = f1;
					this.m_x0 = f1;
					this.m_y = f2;
					this.m_y0 = f2;
					this.m_drag = false;
					this.m_pressed = true;
					this.m_long = false;
					invalidate();
					postDelayed(this.LONG_CLICK, this.LONG_CLICK_DELAY);
					break;
				case MotionEvent.ACTION_MOVE:
					if (m_pressed) {
						if (!m_drag) {
							float f3 = f1 - this.m_x0;
							float f4 = f2 - this.m_y0;
							if (Math.sqrt(f3 * f3 + f4 * f4) > this.SLOP) {
								this.m_x = f1;
								this.m_x0 = f1;
								this.m_y = f2;
								this.m_y0 = f2;
								this.m_drag = true;
								invalidate();
							}
						} else {
							bool = updateScroll(getScrollX() + (this.m_x - f1),
									getScrollY() + (this.m_y - f2));
							if (!bool) {
								getParent().requestDisallowInterceptTouchEvent(
										false);
							}
							this.m_x = f1;
							this.m_y = f2;
							invalidate();
						}
					}
					break;
				case MotionEvent.ACTION_UP:

					if (this.m_pressed) {
						if (this.m_drag) {
							this.m_tracker.computeCurrentVelocity(1000);
							int i = (int) (0.75F * this.m_tracker
									.getXVelocity());
							int j = (int) (0.75F * this.m_tracker
									.getYVelocity());
							if ((i != 0) || (j != 0)) {

								int k = computeMaxScrollX();
								int l = computeMaxScrollY();
								this.m_scroller.fling(getScrollX(),
										getScrollY(), -i, -j, 0, k, 0, l);
							}
						} else {
							if (this.m_long) {
								removeCallbacks(this.LONG_CLICK);
							} else {
								performClick();
							}
						}
					}
					this.m_drag = false;
					this.m_pressed = false;
					invalidate();
					this.m_tracker.recycle();
					this.m_tracker = null;
					break;
				case MotionEvent.ACTION_CANCEL:
					this.m_drag = false;
					this.m_pressed = false;
					invalidate();
					this.m_tracker.recycle();
					this.m_tracker = null;
					if (this.m_long) {
						removeCallbacks(this.LONG_CLICK);
					}
					break;
				default:
					break;
				}
			}
			if (bool) {

			}
			return bool;
		}

	}

	public float getDefaultScale() {
		return defaultScale;
	}

	public float getMaxScale() {
		return this.m_maxScale;
	}

	public float getMinScale() {
		return this.m_minScale;
	}

	public float getScale() {
		return this.m_scale;
	}

	public void setDefaultScale() {
		setScale(defaultScale);
	}

	public void setMaxScale(float paramFloat) {
		this.m_maxScale = paramFloat;
		setScale(this.m_scale);
	}

	public void setMinScale(float paramFloat) {
		this.m_minScale = paramFloat;
		setScale(this.m_scale);
	}

	public void setScale(float paramFloat) {
		updateScale(Math.min(Math.max(this.m_minScale, paramFloat),
				this.m_maxScale), getWidth() / 2, getHeight() / 2);
		invalidate();
	}

	public void animateScale(float paramFloat) {
		this.m_srcScale = this.m_scale;
		this.m_dstScale = Math.min(Math.max(this.m_minScale, paramFloat),
				this.m_maxScale);
		if (this.m_srcScale == this.m_dstScale)
			return;
		this.m_start = System.currentTimeMillis();
		invalidate();
	}

	public void zoomIn() {
		animateScale(1.25F * this.m_scale);
	}

	public void zoomOut() {
		animateScale(this.m_scale / 1.25F);
	}

	public void setOnScaleChangeListener(
			OnScaleChangeListener paramOnScaleChangeListener) {
		this.m_scaleListener = paramOnScaleChangeListener;
	}

	public void setOnScrollChangeListener(
			OnScrollChangeListener paramOnScrollChangeListener) {
		this.m_scrollListener = paramOnScrollChangeListener;
	}

	public static abstract interface OnScrollChangeListener {
		public abstract void onScrollChanged(int paramInt1, int paramInt2,
				int paramInt3, int paramInt4);
	}

	public static abstract interface OnScaleChangeListener {
		public abstract void onScaleChanged(float paramFloat1, float paramFloat2);
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	
	public void setWidth(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
	}

	@Override
	public void setBackgroundResource(int resid) {
		super.setBackgroundResource(resid);
		ViewGroup.LayoutParams lpParam = getLayoutParams();
		if(lpParam != null) {
			if(resid == 0) {
				lpParam.width = LayoutParams.MATCH_PARENT;
				lpParam.height = LayoutParams.MATCH_PARENT;
				setLayoutParams(lpParam);
			} else {
				if(lpParam.width <= 0) {
					if(mWidth > 0)
						lpParam.width = mWidth;
					else
						lpParam.width = WindowManager.get().getScreenWidth();
				}
				if(lpParam.height <= 0) {
					if(mHeight > 0)
						lpParam.height = mHeight;
					else
						lpParam.height = (int) (lpParam.width * 0.75);
				}
				setLayoutParams(lpParam);
				defaultScale = 1;
				setScale(defaultScale);
			}
		}
	}
	
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if(resId > 0) {
			m_initialized = false;
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			m_initialized = false;
			float bmWidth = bm.getWidth();
			int vWidth = getWidthInner();
			defaultScale = vWidth / bmWidth;
			setScale(defaultScale);
			super.setImageBitmap(bm);
		}
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		if (drawable != null) {
			m_initialized = false;
			float bmWidth = drawable.getIntrinsicWidth();
			int vWidth = getWidthInner();
			defaultScale = vWidth / bmWidth;
			setScale(defaultScale);
			super.setImageDrawable(drawable);
		}
	}
	
	private int getWidthInner() {
		if(mWidth > 0)
			return mWidth;
		return Math.max(getWidth(), getMeasuredWidth());
	}
}
