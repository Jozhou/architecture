package com.architecture.widget.layoutview;


public interface ILayoutView<T> {
	
	void setPosition(int position, int total);
	
	void setDataSource(T t);
	
	T getDataSource();
	
	void notifyDataSetChanged();

}
