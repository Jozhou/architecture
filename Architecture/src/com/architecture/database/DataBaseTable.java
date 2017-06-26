package com.architecture.database;

public class DataBaseTable {

	/**
	 * 打点记录表
	 */
	public static final String TABLE_GPS = "t_gps";
	
	public static final String COL_GPS_ORDERID = "uid";
	public static final String COL_GPS_LAT = "lat";
	public static final String COL_GPS_LON = "lon";
	public static final String COL_GPS_TIME = "time";
	
	public static final String COL_GPS_ACCURACY = "accuracy";
	public static final String COL_GPS_ALTITUDE = "altitude";
	public static final String COL_GPS_BEARING = "bearing";
	public static final String COL_GPS_SPEED = "speed";
	public static final String COL_GPS_PROVIDER = "provider";
	
	public static final String COL_GPS_PHONE_TIME = "p_time";
	public static final String COL_GPS_ORDER_STATE = "state";
	/**
	 * 已读消息标识表
	 */
	public static final String TABLE_MESSAGE = "t_msg";
	public static final String COL_MESSAGE_ID = "msg_id";
	public static final String COL_MESSAGE_DRIVERID = "msg_driver_id";
	

	/**
	 * 数据采集表
	 */
	public static final String TABLE_DATA_COLLECT = "t_data_collect";
	public static final String COL_SYS_TIME = "systime";
	public static final String COL_DATA_INFO = "dataInfo";

	/**
	 * 警示牌
	 * @deprecated
	 */
	public static final String TABLE_WARN_PUSH = "t_warn_push";
	/**
	 * @deprecated
	 */
	public static final String COL_PUSH_CONTENT = "pushcontent";
	
	/**
	 * 延迟展示提示信息
	 */
	public static final String TABLE_DELAYED_PROMPT_INFO = "t_delayed_prompt_info";
	public static final String COL_PROMPT_CONTENT = "content";
	public static final String COL_PROMPT_TYPE = "type";
	
	/**
	 * 空车调度
	 */
	public static final String TABLE_GPS_IDLECAR = "t_gps_idlecar";
	public static final String COL_GPS_IDLEID = "idle_id";
}
