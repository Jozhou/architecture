package com.architecture.widget.adapterview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.architecture.models.api.IArrayOperater;
import com.architecture.models.api.BaseOperater.RspListener;
import com.architecture.models.api.IArrayOperater.PageType;
import com.architecture.models.entry.ArrayEntry;

public abstract class MListView<T> extends ListView<T> {

	protected IArrayOperater<T> mModel;
	protected ArrayEntry<T> mModelData; 
	
	/**
	 * 获取BaseListModel返回的数据实体
	 * @return
	 */
	public ArrayEntry<T> getModelData() {
		return mModelData;
	}
	
	protected RspListener mCallback = new RspListener() {
		@Override
		public void onRsp(boolean success, Object obj) {
			if(success) {
				mModelData = mModel.getDataEntry();
				if(mModelData != null && mModelData.getArray() != null) {
					if(mModelData.getArray().size() < 1) {
						onLoadBlank(getPageType());
					} else {
						onLoadSucc(getPageType());
					}
				} else {
					onLoadError(getPageType());
				}
			} else {
				onLoadError(getPageType());
			}
		}
	};
	
	public MListView(Context context) {
		this(context, null);
	}
	
	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 *数据请求完成
	 * @param pagetype
	 */
	protected void onLoadSucc(PageType pagetype) {
		ArrayList<T> olddatas = getDataSource();
		if(olddatas == null) {
			olddatas = new ArrayList<T>();
		}
		ArrayList<T> data = mModelData.getArray(); 
		boolean isLastPage  = mModelData.isLastPage();
		if(pagetype != PageType.CachePage) {
			onEndLoad(isLastPage);
		}
		if(pagetype == PageType.CachePage) {
			setDataSource(data);
			if(scrollToEdgeOnGetCacheData()) {
				scrollToTop();
			}
		} else if(pagetype == PageType.FirstPage) {
			if(clearListOnRefreshData()) {
				setDataSource(data);
			} else {
				olddatas.addAll(0, data);
				setDataSource(olddatas);
			}
			if(scrollToEdgeOnGetFirstPage()) {
				scrollToTop();
			}
		} else {
			olddatas.addAll(data);
			setDataSource(olddatas);
		}
	}
	
	/**
	 * 数据请求完成（空数据）
	 * @param pagetype
	 */
	protected void onLoadBlank(PageType pagetype) {
		if(pagetype != PageType.CachePage) {
			onEndLoad(true);
		}
		if(pagetype == PageType.FirstPage) {
			if(clearListOnRefreshData()) {
				setDataSource(new ArrayList<T>());
			}
		}
	}
	
	/**
	 * 数据请求出错
	 * @param entity
	 * @param pagetype
	 */
	protected void onLoadError(PageType pagetype) {
		if(pagetype != PageType.CachePage){
			boolean isLastPage  = false;
			try {
				isLastPage = mModelData.isLastPage();
			} catch (Exception e) {
				isLastPage = false;
			}
			onEndLoad(isLastPage);
		}
	}
	
	/**
	 * 取得缓存数据
	 */
	public void getCacheData() {
		if(mModel == null) {
			mModel = createMode();
		}
		if(mModel == null) {
			throw new RuntimeException("mModel is null");
		}
		mModel.getCacheData(mCallback);
	}
	
	/**
	 * 请求第一页数据
	 */
	@Override
	public void getFirstPage() {
		getFirstPage(true);
	}
	
	/**
	 * 请求第一页数据
	 * @param loadcache 是否加载缓存
	 * @param isManual 是否手动刷新
	 */
	public void getFirstPage(boolean loadcache) {
		getFirstPage(loadcache, false);
	}
	
	/**
	 * 请求第一页数据
	 * @param loadcache 是否加载缓存
	 * @param isManual 是否手动刷新
	 */
	public void getFirstPage(boolean loadcache, boolean isManual) {
		if(mIsBusy) {
			return;
		}
		if(mModel == null) {
			mModel = createMode();
		}
		if(mModel == null) {
			throw new RuntimeException("mModel is null");
		}
		if(mModel.getFirstPage(loadcache, mCallback)) {
			onBeginLoad(true, isManual);
		}
	}
	
	/**
	 * 请求下一页数据
	 */
	@Override
	public void getNextPage() {
		if(mIsBusy) {
			return;
		}
		super.getNextPage();
		if(mModel == null) {
			mModel = createMode();
		}
		if(mModel == null) {
			throw new RuntimeException("mModel is null");
		}
		mModel.getNextPage(mCallback);
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
		return true;
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
		return mModel;
	}
	
	/**
	 * 生成Model
	 * @return
	 */
	protected abstract IArrayOperater<T> createMode();
}
