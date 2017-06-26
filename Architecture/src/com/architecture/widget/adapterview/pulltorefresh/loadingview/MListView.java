package com.architecture.widget.adapterview.pulltorefresh.loadingview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.architecture.models.api.IArrayOperater;
import com.architecture.models.api.IArrayOperater.PageType;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.NetworkUtils;
import com.architecture.widget.FooterBar;
import com.architecture.widget.adapterview.IFooterBar;
import com.architecture.widget.layoutview.ILayoutView;
import com.architecture.widget.loading.LoadingLayout;
import com.architecture.R;

public abstract class MListView<T> extends LoadingLayout {

	protected PullToRefreshListView mListView;
	
	public MListView(Context context) {
		super(context);
	}
	
	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View getLayoutView() {
		mListView = new PullToRefreshListView(getContext());
		return mListView;
	}

	@Override
	public void onApplyLoadingData() {
		mListView.getRefreshableView().getFirstPage();
	}

	protected IArrayOperater<T> getMode() {
		return mListView.getRefreshableView().getModel();
	}

	protected abstract IArrayOperater<T> createMode();
	
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
		return true;
	}
	
	/**
	 * 加载失败的提示语
	 * @return
	 */
	protected int getLoadErrResId() {
		return R.string.get_data_err;
	}
	
	protected int getViewTypeCount() {
		return 1;
	}

	protected int getLayoutResId(int position, T t, int itemViewType) {
		return 0;
	}
	
	protected IFooterBar getFooterView() {
		return new FooterBar(mContext);
	}
	
	protected ILayoutView<T> getLayoutView(int position, T t, int itemViewType) {
		return null;
	}
	
	/**
	 * 请求第一页数据返回后是否滑动至顶部或底部
	 * @return
	 */
	protected boolean scrollToEdgeOnGetFirstPage() {
		return false;
	}
	
	/**
	 * 请求缓存数据返回后是否滑动至顶部或底部
	 * @return
	 */
	protected boolean scrollToEdgeOnGetCacheData() {
		return false;
	}
	
	/**
	 * 加载第一页数据
	 * @param loadcache
	 * @param isManual
	 */
	protected void onGetFirstPage(boolean loadcache, boolean isManual) {
		
	}
	
	/**
	 * 加载第一页数据
	 * @param loadcache
	 * @param isManual
	 */
	protected void onGetNextPage() {
		
	}
	
	/**
	 * 转换数据源
	 * @param array
	 * @return
	 */
	protected ArrayList<T> transferDataSource(ArrayList<T> array) {
		return array;
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
	
	public class PullToRefreshListView extends com.architecture.widget.adapterview.pulltorefresh.MListView<T> {

		
		public PullToRefreshListView(Context context) {
			super(context);
		}
		
		@Override
		protected com.architecture.widget.adapterview.MListView<T> createListView(Context context, AttributeSet attrs) {
			return new InternalListView(context, attrs);
		}
		
		@Override
		protected void init(Context context, AttributeSet attrs) {
			super.init(context, attrs);
			MListView.this.initializeListView(getRefreshableView());
		}
		
		public class InternalListView extends com.architecture.widget.adapterview.pulltorefresh.MListView<T>.InternalListView {
			
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
			protected IFooterBar getFooterView() {
				return MListView.this.getFooterView();
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
			
			@Override
			public void setDataSource(ArrayList<T> array) {
				super.setDataSource(transferDataSource(array));
			}
			
		}
		
		@Override
		protected boolean enabledLoadNextPage() {
			return MListView.this.enabledLoadNextPage();
		}

		@Override
		protected int getViewTypeCount() {
			return MListView.this.getViewTypeCount();
		}

		@Override
		protected int getLayoutResId(int position, T t, int itemViewType) {
			return MListView.this.getLayoutResId(position, t, itemViewType);
		}

		@Override
		protected ILayoutView<T> getLayoutView(int position, T t, int itemViewType) {
			return MListView.this.getLayoutView(position, t, itemViewType);
		}

		@Override
		protected IArrayOperater<T> createMode() {
			return MListView.this.createMode();
		}
		
		@Override
		protected boolean scrollToEdgeOnGetCacheData() {
			return MListView.this.scrollToEdgeOnGetCacheData();
		}
		
		@Override
		protected boolean scrollToEdgeOnGetFirstPage() {
			return MListView.this.scrollToEdgeOnGetFirstPage();
		}
		
		@Override
		protected void onLoadSucc(PageType pagetype) {
			super.onLoadSucc(pagetype);
			gotoSuccessful();
		}
		
		@Override
		protected void onLoadBlank(PageType pagetype) {
			super.onLoadBlank(pagetype);
			if(pagetype == PageType.FirstPage && getAdapterCount() < 1)
				gotoBlank();
		}
		
		@Override
		protected void onGetFirstPage(boolean loadcache, boolean isManual) {
			super.onGetFirstPage(loadcache, isManual);
			MListView.this.onGetFirstPage(loadcache, isManual);
		}
		
		@Override
		protected void onGetNextPage() {
			super.onGetNextPage();
			MListView.this.onGetNextPage();
		}
		
		@Override
		protected void onLoadError(PageType pagetype) {
			super.onLoadError(pagetype);
			if(pagetype == PageType.FirstPage && getAdapterCount() < 1) {
				gotoError();
			} else {
				if(NetworkUtils.isNetWorkConnected()) {
					int resid = MListView.this.getLoadErrResId();
					if(resid > 0) {
						DialogUtils.showToastMessage(resid);
					}
				}
			}
		}
		
	}
}
