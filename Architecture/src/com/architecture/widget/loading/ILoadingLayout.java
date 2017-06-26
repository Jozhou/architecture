package com.architecture.widget.loading;

public interface ILoadingLayout {

	void gotoLoading();
	void gotoLoading(boolean delay);
	void gotoSuccessful();
	void gotoBlank();
	void gotoError();
	void onApplyLoadingData();
	
}
