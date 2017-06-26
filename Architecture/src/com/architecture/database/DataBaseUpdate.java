package com.architecture.database;

import android.database.sqlite.SQLiteDatabase;

public class DataBaseUpdate {

	/**
	 * 数据库版本从1升至2需执行的操作
	 * @param db
	 */
	static void update1to2(SQLiteDatabase db) {
		db.execSQL(DataBaseCreate.getTableDataCollectCreateSQL());
		db.execSQL(DataBaseCreate.getTableMessageCreateSql());
	}
	
	/**
	 * 2update to 3
	 */
	@SuppressWarnings("deprecation")
	static void update2to3(SQLiteDatabase db){
		db.execSQL(DataBaseCreate.getTableWarnBoardCreateSql());
	}
	
	/**
	 * 3update to 4
	 */
	static void update3to4(SQLiteDatabase db){
		db.execSQL("ALTER TABLE " + DataBaseTable.TABLE_GPS + " ADD " + DataBaseTable.COL_GPS_PHONE_TIME + " LONG;");
	}
	
	/**
	 * 4update to 5
	 */
	static void update4to5(SQLiteDatabase db){
		db.execSQL("ALTER TABLE " + DataBaseTable.TABLE_GPS + " ADD " + DataBaseTable.COL_GPS_ORDER_STATE + " INTEGER;");
	}
	
	/**
	 * 5update to 6
	 */
	@SuppressWarnings("deprecation")
	static void update5to6(SQLiteDatabase db){
		db.execSQL(DataBaseCreate.getTableIdleCarDispatchCreateSql());
		
		db.beginTransaction();
		db.execSQL(DataBaseCreate.getTableDelayedPromptCreateSql());
		String migrateSql = "INSERT INTO " + DataBaseTable.TABLE_DELAYED_PROMPT_INFO + "(" + DataBaseTable.COL_PROMPT_CONTENT + ", " + DataBaseTable.COL_PROMPT_TYPE + ")"
				+" SELECT " + DataBaseTable.COL_PUSH_CONTENT + ", 'push_warnboard' FROM "+ DataBaseTable.TABLE_WARN_PUSH;
		db.execSQL(migrateSql);
		String dropSql="DROP TABLE IF EXISTS " + DataBaseTable.TABLE_WARN_PUSH;
		db.execSQL(dropSql);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
