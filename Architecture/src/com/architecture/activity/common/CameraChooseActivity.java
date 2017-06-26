package com.architecture.activity.common;

import com.architecture.R;

/**
 * 只能通过相机获取图片
 * @author 20141022
 *
 */
public class CameraChooseActivity extends PhotoChooserActivity {

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_camerachooser;
	}
}
