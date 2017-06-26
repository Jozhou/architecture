package com.architecture.widget.zoom;

import java.util.ArrayList;

import com.architecture.R;
import com.architecture.widget.imageview.zoom.IZoomImageView;
import com.architecture.widget.layoutview.MFrameLayout;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class ZoomImageLayout extends MFrameLayout<String[]> {

	protected final static int MAXVIEWPAGERITEM = 3;
	
	protected int mPosition;
	protected TextView tvPager;
	protected ViewPager mViewPager;
	protected ArrayList<IZoomImageView> mViews;

	protected OnClickListener mSingleTapListener;
	protected GestureDetector mGestureDetector;
	
	public void setOnSingleTapListener(OnClickListener l) {
		mSingleTapListener = l;
	}

	private OnPageChangeListener mPagerListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			mPosition = position;
			setPagerIndex(position);
			IZoomImageView zoomimageview = mViews.get(position % mViews.size());
			zoomimageview.setLoadFromLocal(false);
			zoomimageview.setDataSource(mDataItem[position]);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }
		
		@Override
		public void onPageScrollStateChanged(int arg0) { }
	};
	
	private PagerAdapter mPagerAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public int getCount() {
			return mDataItem.length;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ViewPager viewpager = ((ViewPager) container);
			IZoomImageView zoomimageview = mViews.get(position % mViews.size());
			try {
				if(((View)zoomimageview).getParent() == null)
					viewpager.addView((View)zoomimageview, 0);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(position == viewpager.getCurrentItem()) {
				zoomimageview.setLoadFromLocal(false);
				zoomimageview.setDataSource(mDataItem[position]);
			} else {
				zoomimageview.setLoadFromLocal(true);
				zoomimageview.setDataSource(mDataItem[position]);
			}
			return zoomimageview;
		}
		
		@Override
		public void destroyItem(View container, int position,
				Object object) {
			/*
			ZoomImageView zoomimageview = mViews.get(position % mViews.size());
			try {
				((ViewPager) container).removeView(zoomimageview);
			} catch(Exception e) {
				e.printStackTrace();
			}
			*/
		}
	};
	
	public ZoomImageLayout(Context context) {
		super(context);
	}
	
	public ZoomImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ZoomImageLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.widget_zoomimagelayout;
	}

	@Override
	protected void onFindView() {
		tvPager = (TextView) findViewById(R.id.zoomimagelayout_tvpager);
		mViewPager = (ViewPager) findViewById(R.id.zoomimagelayout_viewpager);
	}

	@Override
	protected void onBindListener() {
		if(mViewPager != null) {
			mViewPager.setOnPageChangeListener(mPagerListener);
			mViewPager.setAdapter(mPagerAdapter);
			initViewPagerContainer();
		}
	}
	
	@Override
	protected void onApplyData() {
		mPagerAdapter.notifyDataSetChanged();
	}
	
	@SuppressWarnings("deprecation")
	protected void initViewPagerContainer() {
		if(mGestureDetector == null) {
			mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {
				@Override
				public boolean onDoubleTap(MotionEvent e) {
					IZoomImageView zoomimageview = mViews.get(mPosition % mViews.size());
					if(zoomimageview != null) {
			    		float defaultScale=zoomimageview.getDefaultScale();
						if (zoomimageview.getScale() != defaultScale) {
							zoomimageview.animateScale(defaultScale);
						} else {
							zoomimageview.animateScale(2 * defaultScale);
						}
					}
					return super.onDoubleTap(e);
				}
				
				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					if(mSingleTapListener != null) {
						IZoomImageView zoomimageview = mViews.get(mPosition % mViews.size());
						mSingleTapListener.onClick((View) zoomimageview);
						return true;
					}
					return super.onSingleTapConfirmed(e);
				}
			});
		}
		if(mViews == null) {
			mViews = new ArrayList<IZoomImageView>();
			for(int i = 0; i < MAXVIEWPAGERITEM; i++) {
				IZoomImageView zoomimageview = createZoomImageView();
				setZoomImageView(zoomimageview);
	 			zoomimageview.setGestureDetector(mGestureDetector);
	 			mViews.add(zoomimageview);
			}
		}
	}
	
	public void setSelectedIndex(int index) {
		setPagerIndex(index);
		mViewPager.setCurrentItem(index);
	}
	
	protected IZoomImageView createZoomImageView() {
		return new ZoomImageView(getContext());
	}
	
	protected void setZoomImageView(IZoomImageView zoomimageview) {
		zoomimageview.setMinScale(0.1f);
		zoomimageview.setMaxScale(5f);
		zoomimageview.setScale(1f);
	}
	
	protected void setPagerIndex(int num) {
		String string = (num + 1) + " / " + mDataItem.length;
		SpannableString sbs = new SpannableString(string);
		sbs.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.dd_dimen_48px), false) , 
				0, string.indexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvPager.setText(sbs);
	}
	
}
