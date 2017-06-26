package com.architecture.models.api;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.architecture.models.entry.ArrayEntry;
import com.architecture.utils.FileUtils;

public abstract class BaseArrayOperater<T> extends BaseOperater implements IArrayOperater<T> {

	protected static final int PAGESIZE = 15;
	protected static final String KEY_PAGENO = "pageNo";
	protected static final String KEY_PAGESIZE = "pageSize";
	protected static final String KEY_TOTALCOUNT = "totalCount";
	
	protected static final String CACHE_FOLDER = "json";
	
	protected ArrayEntry<T> mDataEntry;
	protected int mPageIndex = getFirstPageIndex();
	
	protected int getFirstPageIndex() {
		return 1;
	}
	
	@Override
	public ArrayEntry<T> getDataEntry() {
		return mDataEntry;
	}
	
	public BaseArrayOperater(Context context) {
		super(context);
		showLoading = false;
	}

	@Override
	protected ErrorTip getErrorTipType() {
		return ErrorTip.none;
	}

	@Override
	public void onParser(JSONObject response) {
		mDataEntry = parseJsonObject(response);
		parsePagerData(response);
	}

	@Override
	public void onParser(JSONArray response) {
		mDataEntry = parseJsonArray(response);
	}
	
	protected abstract ArrayEntry<T> parseJsonObject(JSONObject jo); 
	protected abstract ArrayEntry<T> parseJsonArray(JSONArray ja); 

	@Override
	public boolean getFirstPage(boolean loadcache, RspListener callback) {
		if(loadcache) {
			if(!getCacheData(callback != null ? callback.clone() : null)) {
				return false;
			}
		}
		if(callback != null) {
			callback.setPageType(PageType.FirstPage);
		}
		return getPageData(callback);
	}

	@Override
	public void getNextPage(RspListener callback) {
		if(callback != null) {
			callback.setPageType(PageType.NextPage);
		}
		getPageData(callback);
	}
	
	/**
	 * 缓存文件名称
	 * @return
	 */
	protected String getCacheFileName() {
		return null;
	}
	
	/**
	 * 换成文件路径
	 * @return
	 */
	protected String getCacheFilePath() {
		if(TextUtils.isEmpty(getCacheFileName())) {
			return null;
		}
		return CACHE_FOLDER + File.separator + getCacheFileName();
	}
	
	/**
	 * 从缓存获取数据
	 * @param callback
	 * @return true 继续从网络加载数据  false 不再加载网络数据
	 */
	@Override
	public boolean getCacheData(RspListener callback) {
		if(callback != null) {
			callback.setPageType(PageType.CachePage);
		}
		if(!TextUtils.isEmpty(getCacheFileName())) {
			String json = FileUtils.getStringFromCachePath(getCacheFilePath());
			if(!TextUtils.isEmpty(json)) {
				try {
					JSONObject jo = new JSONObject(json);
					boolean result = jo.optBoolean("result");
					if (result) {
						onParser(jo);
						callback.onRsp(true, BaseArrayOperater.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	protected boolean getPageData(RspListener c) {
		setPostParam(c);
		return onReq(new RspListenerProxy(c));
	}
	
	protected void setPostParam(RspListener c) {
		try {
			paramsObj.put(KEY_PAGESIZE, PAGESIZE);
			if(c.getPageType() == PageType.FirstPage) {
				paramsObj.put(KEY_PAGENO, getFirstPageIndex());
			} else {
				paramsObj.put(KEY_PAGENO, mPageIndex);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	protected void setPagerData(PageType pageType) {
		if(pageType == PageType.FirstPage) {
			mPageIndex = getFirstPageIndex() + 1;
		} else {
			mPageIndex++;
		}
	}
	
	/**
	 * 解析页码
	 * @param response
	 */
	protected void parsePagerData(JSONObject response) {
		int totalCount = -1;
		try {
			totalCount = response.optInt(KEY_TOTALCOUNT, -1);
		} catch (Exception e) {
			e.printStackTrace();
			totalCount = -1;
		}
		mDataEntry.setTotalCount(totalCount);
	}
	
	public class RspListenerProxy extends RspListener {
		
		private RspListener mListener;
		
		public RspListenerProxy(RspListener l) {
			mListener = l;
		}

		@Override
		public void onRsp(boolean success, Object obj) {
			if(mListener != null) {
				if(success) {
					setPagerData(mListener.getPageType());
					FileUtils.saveString2CachePath(BaseArrayOperater.super.getRspJson(), 
							getCacheFilePath());
				}
				mListener.onRsp(success, obj);
			}
		}
		
	}
	
}
