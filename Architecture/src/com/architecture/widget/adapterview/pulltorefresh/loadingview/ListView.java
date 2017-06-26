package com.architecture.widget.adapterview.pulltorefresh.loadingview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.architecture.widget.layoutview.ILayoutView;
import com.architecture.widget.loading.LoadingLayout;

public class ListView<T> extends LoadingLayout {

	protected PullToRefreshListView mListView;
	
	public ListView(Context context) {
		super(context);
	}
	
	public ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View getLayoutView() {
		mListView = new PullToRefreshListView(getContext());
		return mListView;
	}

	@Override
	protected int getLayoutResId() {
		return 0;
	}
	
	@Override
	public void onApplyLoadingData() {
		mListView.getRefreshableView().getFirstPage();
	}

	
	protected void initializeListView(com.architecture.widget.adapterview.ListView<T> listview) {
		
	}
	
	@Override
	protected boolean hideContentViewOnLoading() {
		return false;
	}
	
	/**
	 * 是否允许加载下一页
	 * @return
	 */
	protected boolean enabledLoadNextPage() {
		return false;
	}
	
	protected int getViewTypeCount() {
		return 1;
	}

	protected int getLayoutResId(int position, T t, int itemViewType) {
		return 0;
	}
	
	protected ILayoutView<T> getLayoutView(int position, T t, int itemViewType) {
		return null;
	}
	
	/**
	 * 加载第一页数据
	 * @param isManual
	 */
	protected void onGetFirstPage(boolean isManual) {
		
	}
	
	/**
	 * 加载第一页数据
	 * @param loadcache
	 * @param isManual
	 */
	protected void onGetNextPage() {
		
	}

	/**
	 * 获取数据源
	 * @return
	 */
	public ArrayList<T> getDataSource() {
		if(mListView != null) {
			return mListView.getRefreshableView().getDataSource();
		}
		return null;
	}
	
	/**
	 * 设置数据源
	 * @param data
	 */
	public void setDataSource(ArrayList<T> data) {
		if(mListView != null) {
			mListView.getRefreshableView().setDataSource(data);
		}
	}
	
	/**
	 * 刷新
	 */
	public void refresh() {
		mListView.getRefreshableView().getFirstPage();
	}
	
	/**
	 * 通知刷新
	 */
	public void notifyDataSetChanged() {
		mListView.getRefreshableView().notifyDataSetChanged();
	}
	
	/**
	 * 增加数据
	 * @param t
	 * @return
	 */
	public boolean addData(T t) {
		return mListView.addData(t);
	}
	
	/**
	 * 增加数据
	 * @param index 索引
	 * @param t 数据
	 * @return
	 */
	public boolean addData(int index, T t) {
		return mListView.addData(index, t);
	}
	
	/**
	 * 移除数据
	 * @param t
	 * @return
	 */
	public boolean removeData(T t) {
		return mListView.removeData(t);
	}
	
	/**
	 * 移除数据
	 * @param index 索引
	 * @return
	 */
	public T removeData(int index) {
		return mListView.removeData(index);
	}
	
	/**
	 * 清空数据
	 * @return
	 */
	public boolean clearData() {
		return mListView.clearData();
	}
	
	public class PullToRefreshListView extends com.architecture.widget.adapterview.pulltorefresh.ListView<T> {

		
		public PullToRefreshListView(Context context) {
			super(context);
		}
		
		@Override
		protected com.architecture.widget.adapterview.ListView<T> createListView(
				Context context, AttributeSet attrs) {
			return new InternalListView(context, attrs);
		}
		
		@Override
		protected void init(Context context, AttributeSet attrs) {
			super.init(context, attrs);
			ListView.this.initializeListView(getRefreshableView());
		}
		
		public class InternalListView extends com.architecture.widget.adapterview.pulltorefresh.ListView<T>.InternalListView {
			
			public InternalListView(Context context, AttributeSet attrs) {
				super(context, attrs);
			}

			@Override
			protected void initializeListView() {
				super.initializeListView();
				setDividerHeight(0);
				setHorizontalScrollBarEnabled(false);
				setVerticalScrollBarEnabled(true);
				hideFadingEdge();
			}
			
			@Override
			protected boolean getAlwaysShowHeaderOnRefresh() {
				return false;
			}
			
			@Override
			protected void onBeginLoad(boolean isFirstPage, boolean isManual) {
				super.onBeginLoad(isFirstPage, isManual);
				if(isFirstPage) {
					if(isManual) {
						if(getAdapterCount() < 1) {
							gotoLoading();
						}
					} else {
						gotoLoading();
					}
				}
			}
			
		}
		
		@Override
		protected void onGetFirstPage(boolean isManual) {
			super.onGetFirstPage(isManual);
			ListView.this.onGetFirstPage(isManual);
		}
		
		@Override
		protected void onGetNextPage() {
			super.onGetNextPage();
			ListView.this.onGetNextPage();
		}
		
		@Override
		protected boolean enabledLoadNextPage() {
			return ListView.this.enabledLoadNextPage();
		}

		@Override
		protected int getViewTypeCount() {
			return ListView.this.getViewTypeCount();
		}

		@Override
		protected int getLayoutResId(int position, T t, int itemViewType) {
			return ListView.this.getLayoutResId(position, t, itemViewType);
		}

		@Override
		protected ILayoutView<T> getLayoutView(int position, T t, int itemViewType) {
			return ListView.this.getLayoutView(position, t, itemViewType);
		}
	}
}