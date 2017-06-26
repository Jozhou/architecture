package com.architecture.widget.adapterview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;

import com.architecture.cache.image.ImageFetcher;
import com.architecture.utils.ButtonUtils;
import com.architecture.utils.OSUtils;
import com.architecture.widget.FooterBar;
import com.architecture.widget.adapterview.IFooterBar.Status;
import com.architecture.widget.layoutview.ILayoutView;

public abstract class ListView<T> extends android.widget.ListView {
	
	protected Context mContext;
	protected boolean mIsBusy;
	protected boolean mIsLastPage;
	protected IFooterBar mFooterView;
	
	protected LayoutViewAdapter<T> mAdapter;
	protected OnScrollListener mScrollListener;
	
	protected OnScrollListener mScrollListenerProxy = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Pause fetcher to ensure smoother scrolling when flinging
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            	ImageFetcher.get().setPauseWork(true);
            } else {
            	ImageFetcher.get().setPauseWork(false);
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            	if (enabledLoadNextPage() && scrollToEdgeLoadNextPage() && !mIsBusy && !mIsLastPage) {
            		boolean toedge = false;
        			if(view.getLastVisiblePosition() == (view.getCount() - 1)) {
        				toedge = true;
        			}
            		if(toedge) {
            			getNextPage();
            		}
            	}
            }
            if(mScrollListener != null) {
            	mScrollListener.onScrollStateChanged(view, scrollState);
            }
        }
        
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, 
        		int visibleItemCount, int totalItemCount) {
            if(mScrollListener != null) {
            	mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    };
	
	public ListView(Context context) {
		this(context, null);
	}
	
	public ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initializeListView();
		initializeAdapter();
	}
	
	public ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initializeListView();
		initializeAdapter();
	}

	protected void initializeListView() {
		setCacheColorHint(Color.TRANSPARENT);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(enabledLoadNextPage()) {
			mFooterView = getFooterView();
			if(!scrollToEdgeLoadNextPage()) {
				if(mFooterView instanceof View) {
					((View) mFooterView).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
				            if (ButtonUtils.isFastClick()) {
				                     return;
				            }
							if (enabledLoadNextPage() && !mIsBusy && !mIsLastPage) {
								getNextPage();
							}
						}
					});
				}
			}
    		setFooterDividersEnabled(false);
		    addFooterView((View)mFooterView);
		}
		super.setOnScrollListener(mScrollListenerProxy);
		mAdapter = new LayoutViewAdapter<T>(mContext) {
			@Override
			public int getViewTypeCount() {
				return ListView.this.getViewTypeCount();
			}

			@Override
			protected int getLayoutResId(int position, T t, int itemViewType) {
				return ListView.this.getLayoutResId(position, t, itemViewType);
			}

			@Override
			protected ILayoutView<T> getLayoutView(int position, T t,
					int itemViewType) {
				return ListView.this.getLayoutView(position, t, itemViewType);
			}
		};
	}
	
	/**
	 * 初始化adapter
	 */
	protected void initializeAdapter() {
		setAdapter(mAdapter);
	}

	/*=============== protected method ====================*/
	/**
	 * 设置是否http请求中
	 * @param value
	 */
	protected void setIsBusy(boolean value) {
		mIsBusy = value;
	}
	
	/**
	 * 是否http请求中
	 * @param value
	 */
	public boolean getIsBusy() {
		return mIsBusy;
	}
	
	/**
	 * 参数错误
	 * @param msg
	 */
	protected void onInvalidParam(String msg) {
		throw new RuntimeException(msg);
	}
	
	/**
	 * 初始化时是否滑动至顶部或底部
	 * @return
	 */
	protected boolean scrollToEdgeOnInit() {
		return true;
	}
	
	/**
	 * 滑动至顶部或底部自动加载下一页
	 * @return
	 */
	protected boolean scrollToEdgeLoadNextPage() {
		return true;
	}
	
	/**
	 * 设置Footer状态
	 */
	protected void setFooterView(Status status) {
    	if(mFooterView != null) {
    		switch(status) {
    		case loadmore:
    			mFooterView.showLoadMore();
    			break;
    		case loading:
    			mFooterView.showLoading();
    			break;
    		case loadall:
    			mFooterView.showLoadAll();
    			break;
    		case hide:
    		default:
    			mFooterView.hide();
    		}
    	}
    }
	
	/**
	 * 开始加载数据
	 * @param isFirstPage 是否请求的第一页数据
	 * @param isManual 是否手动刷新
	 */
	protected void onBeginLoad(boolean isFirstPage, boolean isManual) {
		mIsBusy = true;
		if(isFirstPage) {
			mIsLastPage = false;
			setFooterView(Status.hide);
		} else {
	        setFooterDividersEnabled(true);
			setFooterView(Status.loading);
		}
	}

	/**
	 * 结束加载数据
	 * @param isLastPage 是否是最后一页数据
	 */
	protected void onEndLoad(boolean isLastPage) {
		mIsBusy = false;
		if(isLastPage) {
			mIsLastPage = true;
			setFooterView(Status.loadall);
			setFooterDividersEnabled(true);
		} else {
			setFooterView(Status.loadmore);
			setFooterDividersEnabled(false);
		}
	}
	
	/**
	 * ListView的模板数
	 * @return
	 */
	protected int getViewTypeCount() {
		return 1;
	}
    
	/**
	 * 是否允许触底加载下一页
	 * @return
	 */
	protected boolean enabledLoadNextPage() {
		return true;
	}
    
	/**
	 * 取得Footer
	 * @return
	 */
	protected IFooterBar getFooterView() {
		return new FooterBar(mContext);
	}
	
	/*=============== public method ====================*/
	/**
	 * 请求第一页数据（需自己实现获取数据）
	 */
	public void getFirstPage() {
		getFirstPage(false);
	}
	
	/**
	 * 请求第一页数据（需自己实现获取数据）
	 * @param isManual 是否手动刷新
	 */
	public void getFirstPage(boolean isManual) {
		if(mIsBusy) {
			return;
		}
		onBeginLoad(true, isManual);
	}
	
	/**
	 * 请求下一页数据（需自己实现获取数据）
	 */
	public void getNextPage() {
		if(mIsBusy) {
			return;
		}
		onBeginLoad(false, false);
	}

	/**
	 * 设置数据源
	 * @param data
	 */
	public void setDataSource(ArrayList<T> data) {
		mAdapter.setDataSource(data);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 取得数据源
	 * @return
	 */
	public ArrayList<T> getDataSource() {
		if(mAdapter != null)
			return mAdapter.getDataSource();
		else
			return null;
	}

	/**
	 * 通知adapter刷新UI
	 */
	public void notifyDataSetChanged() {
		if(mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 获取数据源的条数
	 */
	public int getAdapterCount() {
		ListAdapter adapter = getAdapter();
		if(adapter != null) {
			if(adapter instanceof HeaderViewListAdapter) {
				adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter(); 
			}
			return adapter.getCount();
		}
		return 0;
	}
	
	/**
	 * 本地数据源增加数据
	 * @param t
	 * @return
	 */
	public boolean addData(T t) {
		ArrayList<T> datas = getDataSource();
		if(datas != null) {
			datas.add(t);
			setDataSource(datas);
			return true;
		}
		return false;
	}
	
	/**
	 * 本地数据源增加数据
	 * @param index
	 * @param t
	 * @return
	 */
	public boolean addData(int index, T t) {
		ArrayList<T> datas = getDataSource();
		if(datas != null) {
			datas.add(index, t);
			setDataSource(datas);
			return true;
		}
		return false;
	}
	
	/**
	 * 本地数据源移除数据
	 * @param t
	 * @return
	 */
	public boolean removeData(T t) {
		ArrayList<T> datas = getDataSource();
		if(datas != null) {
			boolean result = datas.remove(t);
			setDataSource(datas);
			return result;
		}
		return false;
	}
	
	/**
	 * 本地数据源移除数据
	 * @param index
	 * @return
	 */
	public T removeData(int index) {
		ArrayList<T> datas = getDataSource();
		if(datas != null) {
			T t = datas.remove(index);
			setDataSource(datas);
			return t;
		}
		return null;
	}
	
	/**
	 * 本地数据源移除全部数据
	 * @return
	 */
	public boolean clearData() {
		ArrayList<T> datas = getDataSource();
		if(datas != null) {
			datas.clear();
			setDataSource(datas);
			return true;
		}
		return false;
	}
	
	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		if(getAdapter() != null) {
			setAdapter(null);
			super.addHeaderView(v, data, isSelectable);
			setAdapter(mAdapter);
		} else {
			super.addHeaderView(v, data, isSelectable);
		}
	}
	
	/**
	 * 滑动到顶部
	 */
	public void scrollToTop() {
		if (!isStackFromBottom()) {
			setStackFromBottom(true);
		}
		setStackFromBottom(false);
	}
	
	/**
	 * 滑动到底部
	 */
	public void scrollToBottom() {
		if(getAdapter() != null) {
			setSelection(getAdapter().getCount() - 1);
		}
	}
	
	/**
	 * 是否滑动到顶部
	 * @return
	 */
	public boolean isScrollAtTop() {
		if (getCount() == 0) {
			return true;
		} else if (getFirstVisiblePosition() == 0) {
			final View firstVisibleChild = getChildAt(0);
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= getTop();
			}
		}
		return false;
	}

	/**
	 * 是否滑动到底部
	 * @return
	 */
	public boolean isScrollAtBottom() {
		final int count = getCount();
		final int lastVisiblePosition = getLastVisiblePosition();
		if (count == 0) {
			return true;
		} else if (lastVisiblePosition == count - 1) {
			final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
			final View lastVisibleChild = getChildAt(childIndex);

			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= getBottom();
			}
		}
		return false;
	}

	/**
	 * 设置垂直滚动条(track)
	 * @param resId
	 */
	public void setVerticalTrackDrawable(int resId) {
		setVerticalTrackDrawable(getResources().getDrawable(resId));
	}

	/**
	 * 设置垂直滚动条(track)
	 * @param resId
	 */
	public void setVerticalTrackDrawable(Drawable d) {
		try {
			Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
		    mScrollCacheField.setAccessible(true);
		    Object mScrollCache = mScrollCacheField.get(this);

		    Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
		    scrollBarField.setAccessible(true);
		    Object scrollBar = scrollBarField.get(mScrollCache);

		    Method method = scrollBar.getClass().getDeclaredMethod("setVerticalTrackDrawable", Drawable.class);
		    method.setAccessible(true);
		    method.invoke(scrollBar, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置垂直滚动条(thumb)
	 * @param resId
	 */
	public void setVerticalThumbDrawable(int resId) {
		setVerticalThumbDrawable(getResources().getDrawable(resId));
	}
	
	/**
	 * 设置垂直滚动条(thumb)
	 * @param d
	 */
	public void setVerticalThumbDrawable(Drawable d) {
		try {
			Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
		    mScrollCacheField.setAccessible(true);
		    Object mScrollCache = mScrollCacheField.get(this);

		    Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
		    scrollBarField.setAccessible(true);
		    Object scrollBar = scrollBarField.get(mScrollCache);

		    Method method = scrollBar.getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
		    method.setAccessible(true);
		    method.invoke(scrollBar, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 隐藏上下阴影
	 */
	public void hideFadingEdge() {
		setFadingEdgeLength(0);
		if(OSUtils.hasGingerbread()) {
			setOverScrollMode(OVER_SCROLL_NEVER);
		}
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	/**
	 * 取得listitem的布局resid
	 * @param position
	 * @param t
	 * @param itemViewType
	 * @return
	 */
	protected int getLayoutResId(int position, T t, int itemViewType) {
		return 0;
	}


	/**
	 * 取得listitem的布局view
	 * @param position
	 * @param t
	 * @param itemViewType
	 * @return
	 */
	protected ILayoutView<T> getLayoutView(int position, T t, int itemViewType) {
		return null;
	}
	
}
