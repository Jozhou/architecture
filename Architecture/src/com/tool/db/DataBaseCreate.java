package com.tool.db;



public class DataBaseCreate {
	
	/**
	 * 创建监控日志表
	 * @return
	 */
	static String getTableMonitorCreateSQL() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
        	.append(DataBaseTable.TABLE_MONITOR)
        	.append(" ( ")
        	.append(DataBaseTable.COL_MONITOR_CONTENT).append(" TEXT NOT NULL,")
        	.append(DataBaseTable.COL_MONITOR_TIME).append(" LONG ")
        	.append(")").toString();
    }
	
}
