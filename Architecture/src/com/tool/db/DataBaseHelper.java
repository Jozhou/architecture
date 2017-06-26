package com.tool.db;

import com.architecture.application.App;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseHelper extends BaseDataBaseHelper {

	private static final String DataBaseName = "dcarframework.db";
	/**
	 * 当前数据库版本号，每次数据库表结构有改动需增加
	 */
	private static final int DataBaseVersion = 1;
	
	private static final byte[] mLock = new byte[0];
	private static DataBaseHelper mInstance = null;
	public final static DataBaseHelper get() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new DataBaseHelper(App.getInstance());
            }
            return mInstance;
        }
    }
	
    protected DataBaseHelper(Context context) {
		super(context);
	}

	@Override
	protected String getDataBaseName() {
		return DataBaseName;
	}

	@Override
	protected int getDataBaseVersion() {
		return DataBaseVersion;
	}

	@Override
	protected void createDataBase(SQLiteDatabase db) {
		db.execSQL(DataBaseCreate.getTableMonitorCreateSQL());
	}

	@Override
	protected void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
//		int curVersion = oldVersion;
//    	if(curVersion <= 1) {
//    		DataBaseUpdate.update1to2(db);
//    		curVersion = 2;
//    	}
	}

	@Override
	protected void clearDataBase(SQLiteDatabase db) {
        String[] tables = new String[] { 
        	DataBaseTable.TABLE_MONITOR
        };
        for (String table : tables) {
        	truncate(db, table);
        }
	}

}
