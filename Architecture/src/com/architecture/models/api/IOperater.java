package com.architecture.models.api;

public interface IOperater {

	/**
	 * 是否自动关闭链接（跟随页面）
	 * @return
	 */
	public boolean isAutoClose();
	/**
	 * 取消http请求
	 */
	public void cancel();
	
}
