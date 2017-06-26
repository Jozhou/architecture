package com.architecture.widget.adapterview;


public interface IFooterBar {
	
	void showLoading();
	
	void showLoadMore();
	
	void showLoadAll();
	
	void hide();
	
	public static enum Status {
		hide,
		loadmore,
		loading,
		loadall
	}
	
}
