package com.architecture.context;

public class Config {
	
	private static final String KEY_IS_DEBUG = "IS_DEBUG";
	private static final String KEY_IS_STORELOG = "IS_STORELOG";
	
	/**   
	 * 开启debug
	 */
	public static boolean IS_DEBUG = true;
	/**   
	 * 是否存储日志
	 */
	public static boolean IS_STORELOG = true;
	
//	private static String API_URL = "http://59.110.47.38:8080/teaching/a/mobile";
//
//	private static String IMG_URL = "http://59.110.47.38:8080/teaching/static/upload/images/";
//
//	private static String VIDEO_URL = "http://59.110.47.38:8080/video/";

	private static String API_URL = "http://223.202.60.138:8030/teaching/a/mobile";

	private static String IMG_URL = "http://223.202.60.138:8030/teaching/static/upload/images/";

	private static String VIDEO_URL = "http://223.202.60.138:8030/video/";
	
	/**
	 * 获取url请求的base地址
	 * @return
	 */
	public static String getBaseUrl() {
		return API_URL;
	}

	/**
	 * 获取图片地址
	 * @return
	 */
	public static String getImageUrl() {
		return IMG_URL;
	}

	/**
	 * 获取影像地址
	 * @return
	 */
	public static String getVideoUrl() {
		return VIDEO_URL;
	}

	/**
	 * 设置开启debug模式
	 * @param v
	 */
	public static void setIsDebug(boolean v) {
		IS_DEBUG = v;
		IS_STORELOG = v;
	}
	
}
