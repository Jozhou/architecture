package com.architecture.models.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.models.entry.CadImage;
import com.architecture.view.ImageDetailView;
import java.util.ArrayList;
import java.util.List;

/**
 * 警示牌适配类
 * 
 */
public class CadImageDetailAdapter extends PagerAdapter {

	private List<CadImage> data;
	// 缓存
	private List<ImageDetailView> convertViews;
	private Context mContext;

	public CadImageDetailAdapter(Context mContext) {
		this.mContext = mContext;
		convertViews = new ArrayList<ImageDetailView>();
	}

	/**
	 * 设置数据源
	 * 
	 * @param data
	 */
	public void setData(List<CadImage> data) {
		this.data = data;
	}
	
	@Override
	public int getCount() {
		if (data != null && !data.isEmpty()) {
			return data.size();
		}
		return 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		CadImage item = data.get(position);
		ImageDetailView imageDetailView = null;
		if (convertViews == null || convertViews.isEmpty()) {
			imageDetailView = new ImageDetailView(mContext);
		} else {
			imageDetailView = convertViews.remove(convertViews.size() - 1);
		}
		imageDetailView.setDataSource(item);
		imageDetailView.setParent((ViewPager) container);
		container.addView(imageDetailView);
		return imageDetailView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ImageDetailView view = (ImageDetailView) object;
		container.removeView(view);
		convertViews.add(view);
	}
	
}
