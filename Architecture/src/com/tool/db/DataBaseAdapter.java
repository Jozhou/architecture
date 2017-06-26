package com.tool.db;

import java.util.Random;

import android.content.ContentValues;



public class DataBaseAdapter {
	
	private static final byte[] mLock = new byte[0];
	private static DataBaseAdapter mInstance = null;
	public final static DataBaseAdapter get() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new DataBaseAdapter();
            }
            return mInstance;
        }
    }
	
    private DataBaseAdapter() {	}

	
	 /**
     * 新增监控日志记录
     * 防止重复time，采用currentTimeMillis + 3位数字的方式标示一条记录
     * @param content
     * @return
     */
    public boolean addTableMonitor(String content) {
    	Random random = new Random();
    	DataBaseHelper helper = DataBaseHelper.get();
		ContentValues value = new ContentValues();
		value.put(DataBaseTable.COL_MONITOR_CONTENT, content);
		value.put(DataBaseTable.COL_MONITOR_TIME, System.currentTimeMillis() * 1000 + random.nextInt(1000));
		return helper.insert(DataBaseTable.TABLE_MONITOR, null, value) > 0;
    }

    /**
     * 移除监控日志记录
     * @param orderId 订单号
     * @return
     */
    public boolean removeTableMonitor(long time) {
    	String where = DataBaseTable.COL_MONITOR_TIME + "<=" + time + "";
		DataBaseHelper.get().delete(DataBaseTable.TABLE_MONITOR, where, null);
		return true;
    }
}
