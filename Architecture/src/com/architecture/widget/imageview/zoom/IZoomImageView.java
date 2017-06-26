package com.architecture.widget.imageview.zoom;

import android.view.GestureDetector;

public interface IZoomImageView {
	
	void setLoadFromLocal(boolean value);
	
	void setDataSource(String url);
	
	float getDefaultScale();

	float getScale();

	void setMaxScale(float paramFloat);

	void setMinScale(float paramFloat);

	void setScale(float paramFloat);

	void animateScale(float paramFloat);

	void setGestureDetector(GestureDetector gestureDetector);
	
}
