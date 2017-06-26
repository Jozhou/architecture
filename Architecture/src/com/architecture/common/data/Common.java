package com.architecture.common.data;

import com.architecture.models.entry.BaseEntry;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;

import java.util.List;

public class Common extends BaseEntry {

	private static final String TAG = Common.class.getSimpleName();

	public static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	public static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	public static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	public static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

	private static final byte[] mLock = new byte[0];
	private static Common mInstance = null;
	
	private List<CadImage> cadImages;

	private List<ProjectInfo> projectInfos;

	public final static Common get() {
		synchronized (mLock) {
			if (mInstance == null) {
				mInstance = new Common();
			}
			return mInstance;
		}
	}

	private Common() {
	}
	
	public void setCadImages(List<CadImage> cadImages) {
		this.cadImages = cadImages;
	}
	
	public List<CadImage> getCadImages() {
		return this.cadImages;
	}

	public void setProjectInfos(List<ProjectInfo> projectInfos) {
		this.projectInfos = projectInfos;
	}

	public List<ProjectInfo> getProjectInfos() {
		return this.projectInfos;
	}
}
