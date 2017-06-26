package com.architecture.models.entry;

import java.util.ArrayList;

public class ArrayEntry<T> extends BaseEntry {

	private static final long serialVersionUID = 3776599349241014151L;
	
	protected int totalCount = -1;
	protected ArrayList<T> array;
	
	public void setTotalCount(int v) {
		totalCount = v;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public ArrayEntry() {
		array = new ArrayList<T>();
	}
	
	public ArrayList<T> getArray() {
		return array;
	}
	
	/**
	 * 是否为最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return array.size() <= 0;
	}
	
}
