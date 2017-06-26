package com.architecture.context;

/**
 * 错误码（包括http请求的、其他）
 * @author Administrator
 *
 */
public class ErrorCode {

	/**
	 * uid过期
	 */
	public static final int CODE_ERROR_UID = -930;
	/**
	 *  登录过期
	 */
	public static final int CODE_ERROR_LOGIN = 5;
	/**
	 *  请求过快
	 */
	public static final int CODE_ERROR_REQUEST_QUICKY = -920;
	/**
	 *  业务逻辑错误
	 */
	public static final int CODE_ERROR_SERVICELOGIC = 7;
}
