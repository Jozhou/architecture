package com.architecture.database;



public class DataBaseCreate {
	
	/**
	 * 创建gps打点记录表
	 * @return
	 */
	static String getTableGpsCreateSQL() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
        	.append(DataBaseTable.TABLE_GPS)
        	.append(" ( ")
        	.append(DataBaseTable.COL_GPS_ORDERID).append(" TEXT NOT NULL,")
        	.append(DataBaseTable.COL_GPS_LAT).append(" TEXT NOT NULL,")
        	.append(DataBaseTable.COL_GPS_LON).append(" TEXT NOT NULL,")
        	.append(DataBaseTable.COL_GPS_TIME).append(" LONG,")
        	.append(DataBaseTable.COL_GPS_ACCURACY).append(" FLOAT,")
        	.append(DataBaseTable.COL_GPS_ALTITUDE).append(" DOUBLE,")
        	.append(DataBaseTable.COL_GPS_BEARING).append(" FLOAT,")
        	.append(DataBaseTable.COL_GPS_SPEED).append(" FLOAT,")
        	.append(DataBaseTable.COL_GPS_PROVIDER).append(" TEXT NOT NULL,")
        	.append(DataBaseTable.COL_GPS_PHONE_TIME).append(" LONG,")
        	.append(DataBaseTable.COL_GPS_ORDER_STATE).append(" INTEGER")
        	.append(")").toString();
    }
	/**
	 * 创建数据采集记录表
	 * @return
	 */
	static String getTableDataCollectCreateSQL() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
        	.append(DataBaseTable.TABLE_DATA_COLLECT)
        	.append(" ( ")
        	.append(DataBaseTable.COL_SYS_TIME).append(" LONG,")
        	.append(DataBaseTable.COL_DATA_INFO).append(" TEXT NOT NULL")
        	.append(")").toString();
    }
	
	/**
	 * 创建已读消息标记表
	 */
	static String getTableMessageCreateSql(){
		return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
				.append(DataBaseTable.TABLE_MESSAGE)
				.append(" ( ")
				.append(DataBaseTable.COL_MESSAGE_ID).append(" TEXT NOT NULL,")
				.append(DataBaseTable.COL_MESSAGE_DRIVERID).append(" LONG NOT NULL")
	        	.append(")").toString();
	}
	
	/**
	 * 警示牌推送消息
	 * @deprecated
	 */
	static String getTableWarnBoardCreateSql(){
		return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
				.append(DataBaseTable.TABLE_WARN_PUSH)
				.append(" ( ")
				.append(DataBaseTable.COL_PUSH_CONTENT).append(" TEXT NOT NULL")
	        	.append(")").toString();
	}
	
	/**
	 * 创建延迟展示提示信息记录表
	 */
	static String getTableDelayedPromptCreateSql(){
		return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
				.append(DataBaseTable.TABLE_DELAYED_PROMPT_INFO)
				.append(" ( ")
				.append(DataBaseTable.COL_PROMPT_CONTENT).append(" TEXT NOT NULL,")
				.append(DataBaseTable.COL_PROMPT_TYPE).append(" TEXT NOT NULL")
	        	.append(")").toString();
	}
	
	/**
	 * 空车调度打点信息
	 */
	static String getTableIdleCarDispatchCreateSql(){
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
            	.append(DataBaseTable.TABLE_GPS_IDLECAR)
            	.append(" ( ")
            	.append(DataBaseTable.COL_GPS_IDLEID).append(" TEXT NOT NULL,")
            	.append(DataBaseTable.COL_GPS_LAT).append(" TEXT NOT NULL,")
            	.append(DataBaseTable.COL_GPS_LON).append(" TEXT NOT NULL,")
            	.append(DataBaseTable.COL_GPS_TIME).append(" LONG,")
            	.append(DataBaseTable.COL_GPS_ACCURACY).append(" FLOAT,")
            	.append(DataBaseTable.COL_GPS_ALTITUDE).append(" DOUBLE,")
            	.append(DataBaseTable.COL_GPS_BEARING).append(" FLOAT,")
            	.append(DataBaseTable.COL_GPS_SPEED).append(" FLOAT,")
            	.append(DataBaseTable.COL_GPS_PROVIDER).append(" TEXT NOT NULL,")
            	.append(DataBaseTable.COL_GPS_PHONE_TIME).append(" LONG")
            	.append(")").toString();
        }
}
