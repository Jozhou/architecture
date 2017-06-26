package com.architecture.widget.zoom;

import android.widget.ProgressBar;
import android.widget.TextView;

public interface ILoadingBar {
	
	ProgressBar getProgressBar();
	
	TextView getProgressText();
	
	void setProcessValue(int percent);
	
	void setVisibility(int visibility);
}
