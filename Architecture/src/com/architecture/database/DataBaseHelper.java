package com.architecture.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.architecture.application.App;

public class DataBaseHelper extends com.tool.db.BaseDataBaseHelper {

	private static final String DataBaseName = "dcar.db";
	/**
	 * 当前数据库版本号，每次数据库表结构有改动需增加
	 */
	private static final int DataBaseVersion = 6;
	
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
		db.execSQL(DataBaseCreate.getTableGpsCreateSQL());
		db.execSQL(DataBaseCreate.getTableDataCollectCreateSQL());
		db.execSQL(DataBaseCreate.getTableMessageCreateSql());
		db.execSQL(DataBaseCreate.getTableDelayedPromptCreateSql());
		db.execSQL(DataBaseCreate.getTableIdleCarDispatchCreateSql());
	}

	@Override
	protected void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
		int curVersion = oldVersion;
    	if (curVersion <= 1) {
    		DataBaseUpdate.update1to2(db);
    		curVersion = 2;
    	}
    	if (curVersion <= 2) {
    		DataBaseUpdate.update2to3(db);
    		curVersion = 3;
    	}
    	if (curVersion <= 3) {
    		DataBaseUpdate.update3to4(db);
    		curVersion = 4;
    	}
    	if (curVersion <= 4) {
    		DataBaseUpdate.update4to5(db);
    		curVersion = 5;
    	}
    	if (curVersion <= 5){
    		DataBaseUpdate.update5to6(db);
    		curVersion = 6;
    	}
	}

	@Override
	protected void clearDataBase(SQLiteDatabase db) {
        String[] tables = new String[] { 
        	DataBaseTable.TABLE_GPS,
        	DataBaseTable.TABLE_DATA_COLLECT,
        	DataBaseTable.TABLE_MESSAGE,
        	DataBaseTable.TABLE_DELAYED_PROMPT_INFO,
        	DataBaseTable.TABLE_GPS_IDLECAR
        };
        for (String table : tables) {
        	truncate(db, table);
        }
	}
}
