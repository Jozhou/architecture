package com.architecture.widget.adapterview.pulltorefresh;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.architecture.models.api.IArrayOperater;
import com.architecture.models.api.IArrayOperater.PageType;
import com.architecture.widget.layoutview.ILayoutView;
import com.architecture.widget.pulltorefresh.EmptyViewMethodAccessor;
import com.architecture.widget.pulltorefresh.LoadingLayout;
import com.architecture.widget.pulltorefresh.LoadingLayoutProxy;
import com.architecture.widget.pulltorefresh.OverscrollHelper;
import com.architecture.widget.pulltorefresh.PullToRefreshAdapterViewBase;
import com.architecture.widget.pulltorefresh.PullToRefreshBase;
import com.architecture.R;

public abstract class MListView<T> extends PullToRefreshAdapterViewBase<com.architecture.widget.adapterview.MListView<T>> {

	protected static final int FLAG_SETREFRESH = 1;
	
	private LoadingLayout mHeaderLoadingView;
	private LoadingLayout mFooterLoadingView;

	private FrameLayout mLvFooterLoadingFrame;

	private boolean mListViewExtrasEnabled;
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case FLAG_SETREFRESH:
				setRefreshing();
				break;
			}
		};
	};
	
	public MListView(Context context) {
		super(context);
	}

	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MListView(Context context, Mode mode) {
		super(context, mode);
	}

	public MListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected void onRefreshing(final boolean doScroll) {
		/**
		 * If we're not showing the Refreshing view, or the list is empty, the
		 * the header/footer views won't show so we use the normal method.
		 */
		ListAdapter adapter = getRefreshableView().getAdapter();
		if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
			super.onRefreshing(doScroll);
			return;
		}

		super.onRefreshing(false);

		final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
		final int selection, scrollToY;

		switch (getCurrentMode()) {
			case MANUAL_REFRESH_ONLY:
			case PULL_FROM_END:
				origLoadingView = getFooterLayout();
				listViewLoadingView = mFooterLoadingView;
				oppositeListViewLoadingView = mHeaderLoadingView;
				selection = getRefreshableView().getCount() - 1;
				scrollToY = getScrollY() - getFooterSize();
				break;
			case PULL_FROM_START:
			default:
				origLoadingView = getHeaderLayout();
				listViewLoadingView = mHeaderLoadingView;
				oppositeListViewLoadingView = mFooterLoadingView;
				selection = 0;
				scrollToY = getScrollY() + getHeaderSize();
				break;
		}

		// Hide our original Loading View
		origLoadingView.reset();
		origLoadingView.hideAllViews();

		// Make sure the opposite end is hidden too
		oppositeListViewLoadingView.setVisibility(View.GONE);

		// Show the ListView Loading View and set it to refresh.
		listViewLoadingView.setVisibility(View.VISIBLE);
		listViewLoadingView.refreshing();

		if (doScroll) {
			// We need to disable the automatic visibility changes for now
			disableLoadingLayoutVisibilityChanges();

			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);

			// Make sure the ListView is scrolled to show the loading
			// header/footer
			getRefreshableView().setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	@Override
	protected void onReset() {
		/**
		 * If the extras are not enabled, just call up to super and return.
		 */
		if (!mListViewExtrasEnabled) {
			super.onReset();
			return;
		}

		final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
		final int scrollToHeight, selection;
		final boolean scrollLvToEdge;

		switch (getCurrentMode()) {
			case MANUAL_REFRESH_ONLY:
			case PULL_FROM_END:
				originalLoadingLayout = getFooterLayout();
				listViewLoadingLayout = mFooterLoadingView;
				selection = getRefreshableView().getCount() - 1;
				scrollToHeight = getFooterSize();
				scrollLvToEdge = Math.abs(getRefreshableView().getLastVisiblePosition() - selection) <= 1;
				break;
			case PULL_FROM_START:
			default:
				originalLoadingLayout = getHeaderLayout();
				listViewLoadingLayout = mHeaderLoadingView;
				scrollToHeight = -getHeaderSize();
				selection = 0;
				scrollLvToEdge = Math.abs(getRefreshableView().getFirstVisiblePosition() - selection) <= 1;
				break;
		}

		// If the ListView header loading layout is showing, then we need to
		// flip so that the original one is showing instead
		if (listViewLoadingLayout.getVisibility() == View.VISIBLE) {

			// Set our Original View to Visible
			originalLoadingLayout.showInvisibleViews();

			// Hide the ListView Header/Footer
			listViewLoadingLayout.setVisibility(View.GONE);

			/**
			 * Scroll so the View is at the same Y as the ListView
			 * header/footer, but only scroll if: we've pulled to refresh, it's
			 * positioned correctly
			 */
			if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
				getRefreshableView().setSelection(selection);
				setHeaderScroll(scrollToHeight);
			}
		}

		// Finally, call up to super
		super.onReset();
	}

	@Override
	protected LoadingLayoutProxy createLoadingLayoutProxy(final boolean includeStart, final boolean includeEnd) {
		LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart, includeEnd);

		if (mListViewExtrasEnabled) {
			final Mode mode = getMode();

			if (includeStart && mode.showHeaderLoadingLayout()) {
				proxy.addLayout(mHeaderLoadingView);
			}
			if (includeEnd && mode.showFooterLoadingLayout()) {
				proxy.addLayout(mFooterLoadingView);
			}
		}

		return proxy;
	}

	protected com.architecture.widget.adapterview.MListView<T> createListView(Context context, AttributeSet attrs) {
		final com.architecture.widget.adapterview.MListView<T> lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalListViewSDK9(context, attrs);
		} else {
			lv = new InternalListView(context, attrs);
		}
		return lv;
	}

	@Override
	protected com.architecture.widget.adapterview.MListView<T> createRefreshableView(Context context, AttributeSet attrs) {
		setOnRefreshListener(new OnRefreshListener<com.architecture.widget.adapterview.MListView<T>>() {
			@Override
			public boolean canRefresh() {
				return !getRefreshableView().getIsBusy();
			}

			@Override
			public void onRefresh(PullToRefreshBase<com.architecture.widget.adapterview.MListView<T>> refreshView) {
				getRefreshableView().getFirstPage(true, true);
			}
			
		});
		com.architecture.widget.adapterview.MListView<T> lv = createListView(context, attrs);
		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);

		setShowIndicator(false);
		mListViewExtrasEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);

		if (mListViewExtrasEnabled) {
			final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

			// Create Loading Views ready for use later
			FrameLayout frame = new FrameLayout(getContext());
			mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
			mHeaderLoadingView.setVisibility(View.GONE);
			frame.addView(mHeaderLoadingView, lp);
			getRefreshableView().addHeaderView(frame, null, false);

			mLvFooterLoadingFrame = new FrameLayout(getContext());
			mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
			mFooterLoadingView.setVisibility(View.GONE);
			mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);

			/**
			 * If the value for Scrolling While Refreshing hasn't been
			 * explicitly set via XML, enable Scrolling While Refreshing.
			 */
			if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
				setScrollingWhileRefreshingEnabled(true);
			}
		}
	}

	@TargetApi(9)
	final class InternalListViewSDK9 extends InternalListView {

		public InternalListViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(MListView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

	protected class InternalListView extends com.architecture.widget.adapterview.MListView<T> implements EmptyViewMethodAccessor {

		private boolean mAddedLvFooter = false;

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				super.dispatchDraw(canvas);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				return super.dispatchTouchEvent(ev);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			// Add the Footer View at the last possible moment
			if (null != mLvFooterLoadingFrame && !mAddedLvFooter) {
				addFooterView(mLvFooterLoadingFrame, null, false);
				mAddedLvFooter = true;
			}

			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView) {
			MListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
		
		/**
		 * 是否在刷新的时候总是显示刷新头部
		 * @return true 总是显示头部，  false 在listview已经有数据渲染了才显示头部，否则不显示
		 */
		protected boolean getAlwaysShowHeaderOnRefresh() {
			return false;
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
		protected void onBeginLoad(boolean isFirstPage, boolean isManual) {
			super.onBeginLoad(isFirstPage, isManual);
			if(isManual) {
				if(isFirstPage && (getAlwaysShowHeaderOnRefresh() || getAdapterCount() > 0)) {
					mHandler.sendEmptyMessageDelayed(FLAG_SETREFRESH, 1000);
				} else {
					mHandler.removeCallbacksAndMessages(null);
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							forceRefreshComplete();
						}
					}, 1000);
				}
			}
		}
		
		@Override
		protected void onEndLoad(boolean isLastPage) {
			super.onEndLoad(isLastPage);
			mHandler.removeCallbacksAndMessages(null);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					forceRefreshComplete();
				}
			}, 1000);
		}
		
		@Override
		public void getFirstPage(boolean loadcache, boolean isManual) {
			super.getFirstPage(loadcache, isManual);
			MListView.this.onGetFirstPage(loadcache, isManual);
		}
		
		@Override
		public void getNextPage() {
			super.getNextPage();
			MListView.this.onGetNextPage();
		}

		@Override
		protected boolean clearListOnRefreshData() {
			return MListView.this.clearListOnRefreshData();
		}

		@Override
		protected boolean scrollToEdgeOnGetFirstPage() {
			return MListView.this.scrollToEdgeOnGetFirstPage();
		}

		@Override
		protected boolean scrollToEdgeOnGetCacheData() {
			return MListView.this.scrollToEdgeOnGetCacheData();
		}
		
		@Override
		protected void onLoadSucc(PageType pagetype) {
			super.onLoadSucc(pagetype);
			MListView.this.onLoadSucc(pagetype);
		}
		
		@Override
		protected void onLoadBlank(PageType pagetype) {
			super.onLoadBlank(pagetype);
			MListView.this.onLoadBlank(pagetype);
		}
		
		@Override
		protected void onLoadError(PageType pagetype) {
			super.onLoadError(pagetype);
			MListView.this.onLoadError(pagetype);
		}

		@Override
		protected IArrayOperater<T> createMode() {
			return MListView.this.createMode();
		}

	}

	/**
	 * 设置数据源
	 * @param data
	 */
	public void setDataSource(ArrayList<T> data) {
		getRefreshableView().setDataSource(data);
	}

	/**
	 * 取得数据源
	 * @return
	 */
	public ArrayList<T> getDataSource() {
		return getRefreshableView().getDataSource();
	}

	/**
	 * 通知adapter刷新UI
	 */
	public void notifyDataSetChanged() {
		getRefreshableView().notifyDataSetChanged();
	}
	
	/**
	 * 获取数据源的条数
	 */
	public int getAdapterCount() {
		return getRefreshableView().getAdapterCount();
	}
	
	/**
	 * 本地数据源增加数据
	 * @param t
	 * @return
	 */
	public boolean addData(T t) {
		return getRefreshableView().addData(t);
	}
	
	/**
	 * 本地数据源增加数据
	 * @param index
	 * @param t
	 * @return
	 */
	public boolean addData(int index, T t) {
		return getRefreshableView().addData(index, t);
	}
	
	/**
	 * 本地数据源移除数据
	 * @param t
	 * @return
	 */
	public boolean removeData(T t) {
		return getRefreshableView().removeData(t);
	}
	
	/**
	 * 本地数据源移除数据
	 * @param index
	 * @return
	 */
	public T removeData(int index) {
		return getRefreshableView().removeData(index);
	}
	
	/**
	 * 本地数据源移除全部数据
	 * @return
	 */
	public boolean clearData() {
		return getRefreshableView().clearData();
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
	 *数据请求完成
	 * @param pagetype
	 */
	protected void onLoadSucc(PageType pagetype) {
		
	}
	
	/**
	 * 数据请求完成（空数据）
	 * @param pagetype
	 */
	protected void onLoadBlank(PageType pagetype) {
		
	}
	
	/**
	 * 数据请求出错
	 * @param entity
	 * @param pagetype
	 */
	protected void onLoadError(PageType pagetype) {
		
	}
	
	/**
	 * 取得缓存数据
	 */
	public void getCacheData() {
		getRefreshableView().getCacheData();
	}
	
	/**
	 * 请求第一页数据
	 */
	public void getFirstPage() {
		getRefreshableView().getFirstPage();
	}
	
	/**
	 * 请求第一页数据
	 * @param loadcache 是否加载缓存
	 */
	public void getFirstPage(boolean loadcache) {
		getRefreshableView().getFirstPage(loadcache);
	}
	
	/**
	 * 请求下一页数据
	 */
	public void getNextPage() {
		getRefreshableView().getNextPage();
	}
	
	/**
	 * 请求第一页数据返回后是否清空原列表数据
	 * @return
	 */
	protected boolean clearListOnRefreshData() {
		return true;
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
	 * 取得Model
	 * @return
	 */
	public IArrayOperater<T> getModel() {
		return getRefreshableView().getModel();
	}

	/**
	 * 是否允许触底加载下一页
	 * @return
	 */
	protected boolean enabledLoadNextPage() {
		return true;
	}

	/**
	 * ListView的模板数
	 * @return
	 */
	protected int getViewTypeCount() {
		return 1;
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

	protected abstract IArrayOperater<T> createMode();

}

