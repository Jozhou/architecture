package com.architecture.models.api;

import com.architecture.models.api.BaseOperater.RspListener;
import com.architecture.models.entry.ArrayEntry;

public interface IArrayOperater<T> {

	ArrayEntry<T> getDataEntry();
	boolean getFirstPage(boolean loadcache, RspListener callback);
	void getNextPage(RspListener callback);
	boolean getCacheData(RspListener callback);
	
	public static enum PageType {
		CachePage,
		FirstPage,
		NextPage
	}
	
}
