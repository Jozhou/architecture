package com.architecture.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.architecture.activity.base.BaseActivity;
import com.architecture.context.IntentCode;
import com.architecture.widget.zoom.ZoomImageLayout;
import com.architecture.R;

/**
 * 查看大图页面
 * @author Administrator
 *
 */
public class ZoomImageActivity extends BaseActivity {

	private ZoomImageLayout mZoomImageLayout;
	
	private int mIndex;
	private String[] mImageUrls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoomimage);
	}
	
	@Override
	protected void onFindView() {
		super.onFindView();
		mZoomImageLayout = (ZoomImageLayout) findViewById(R.id.zoomimagelayout);
	}
	
	@Override
	protected void onQueryArguments(Intent intent) {
		super.onQueryArguments(intent);
		mImageUrls = intent.getStringArrayExtra(IntentCode.INTENT_ZOOMIMAGE_URL);
		mIndex = intent.getIntExtra(IntentCode.INTENT_ZOOMIMAGE_INDEX, 0);
	}
	
	@Override
	protected void onBindListener() {
		super.onBindListener();
		mZoomImageLayout.setOnSingleTapListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onApplyData() {
		super.onApplyData();
		mZoomImageLayout.setDataSource(mImageUrls);
		mZoomImageLayout.setSelectedIndex(mIndex);
	}

}
