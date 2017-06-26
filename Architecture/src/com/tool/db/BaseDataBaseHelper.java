package com.tool.db;

import java.util.ArrayList;

import com.tool.utils.LogcatUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDataBaseHelper {
	
	private static final String TAG = "DataBaseHelper";

	protected Context mContext;
	protected static final byte[] mlocker = new byte[0];
	protected MySQLiteOpenHelper mOpenHelper;
	protected SQLiteDatabase mDataBase;
	
	public BaseDataBaseHelper(Context context) {
		mContext = context;
        if (mOpenHelper == null) {
        	mOpenHelper = new MySQLiteOpenHelper();
        }
        openDataBase();
    }
	
	/**
	 * 获取数据库名称
	 * @return
	 */
	protected abstract String getDataBaseName();
	
	/**
	 * 获取数据库版本
	 * @return
	 */
	protected abstract int getDataBaseVersion();

	/**
	 * 初始化数据库脚本
	 * @param db
	 */
	protected abstract void createDataBase(SQLiteDatabase db);
	
	/**
	 * 升级数据库脚本
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	protected abstract void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion);

	/**
	 * 清除数据库脚本
	 * @param db
	 */
	protected abstract void clearDataBase(SQLiteDatabase db);
	
	/**
	 * 打开数据库连接（如果已打开，先关闭再打开）
	 */
	public void openDataBase() {
        synchronized (mlocker) {
            try {
            	opendatabase();
            } catch (Exception e) {
                try {
                    Thread.sleep(1500);
                	opendatabase();
                } catch (Exception e2) {
                    LogcatUtils.e(TAG, e2);
                }
                LogcatUtils.e(TAG, e);
            }
        }
    }
	private void opendatabase() {
		if (mDataBase != null && mDataBase.isOpen()) {
        	mDataBase.close();
        }
        mDataBase = mOpenHelper.getWritableDatabase();
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeDataBase() {
		try {
            if (mDataBase != null) {
            	mDataBase.close();
            }
        } catch (Exception e) {
            LogcatUtils.e(TAG, e);
        }
    }
	
	/**
	 * 数据库是否打开
	 * @return
	 */
	protected boolean isOpen() {
		return mDataBase != null && mDataBase.isOpen();
	}
	
	/**
	 * 尝试打开数据库（如果已打开，不关闭直接返回）
	 */
	protected synchronized void tryOpenDataBase() {
		try {
			if (mDataBase == null || !mDataBase.isOpen()) {
		        mDataBase = mOpenHelper.getWritableDatabase();
	        }
		} catch (Exception e) {
			LogcatUtils.e(TAG, e);
		}
	}
	
	/**
	 * 删除表并重置索引为0
	 * @param db
	 * @param tableName
	 */
	protected synchronized void truncate(SQLiteDatabase db, String tableName) {
		String dropSql = "DROP TABLE IF EXISTS %s ;";
//        String resetSql = "UPDATE sqlite_sequence SET seq=0 WHERE name='%s';";

        db.execSQL(String.format(dropSql, tableName));
//        db.execSQL(String.format(resetSql, tableName));
	}

	/**
	 * 插入数据
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
    public synchronized long insert(String table, String nullColumnHack, ContentValues values) {
    	LogcatUtils.sqllog(table, "insert", "", values, "", null);
        tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.insert(table, nullColumnHack, values);
        } else {
        	return 0;
        }
    }
    
    /**
     * 插入数据（多行）
     * @param table
     * @param values
     * @return
     */
    public synchronized boolean insert(String table, ArrayList<ContentValues> values) {
		try {
			for (ContentValues value : values) {
				insert(table, null, value);
				value.clear();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
    /**
     * 更新数据
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public synchronized int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
    	LogcatUtils.sqllog(table, "update", "", values, whereClause, whereArgs);
		tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.update(table, values, whereClause, whereArgs);
        } else {
        	return 0;
        }
    }

	/**
	 * 删除数据
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
    public synchronized int delete(String table, String whereClause, String[] whereArgs) {
    	LogcatUtils.sqllog(table, "delete", "", null, whereClause, whereArgs);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.delete(table, whereClause, whereArgs);
        } else {
        	return 0;
        }
    }
    
    /**
     * 执行sql语句
     * @param sql
     * @throws SQLException
     */
    public synchronized void execSQL(String sql) throws SQLException {
    	LogcatUtils.sqllog("", "exec", sql, null, "", null);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	mDataBase.execSQL(sql);
        }
    }
    
    /**
     * 执行sql语句
     * @param sql
     * @param bindArgs
     * @throws SQLException
     */
    public synchronized void execSQL(String sql, Object[] bindArgs) throws SQLException {
    	LogcatUtils.sqllog("", "exec", sql, null, "", null);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	mDataBase.execSQL(sql, bindArgs);
        }
    }
	
    /**
     * 执行查询
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy) {
    	String sqllog = "groupby:" + groupBy + ";having:" + having + ";orderBy:" + orderBy;
    	LogcatUtils.sqllog(table, "query", sqllog, null, selection, selectionArgs);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } else {
        	return null;
        }
    }

    /**
     * 执行查询
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit) {
    	String sqllog = "groupby:" + groupBy + ";having:" + having + ";orderBy:" + orderBy + ";limit" + limit;
    	LogcatUtils.sqllog(table, "query", sqllog, null, selection, selectionArgs);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } else {
        	return null;
        }
    }

    /**
     * 执行查询
     * @param distinct
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public synchronized Cursor query(boolean distinct, String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
    	String sqllog = "distinct:" + (distinct ? "true" : "false") + ";groupby:" + 
            groupBy + ";having:" + having + ";orderBy:" + orderBy + ";limit" + limit;
    	LogcatUtils.sqllog(table, "query", sqllog, null, selection, selectionArgs);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } else {
        	return null;
        }
    }
	
    /**
     * 执行查询
     * @param sql
     * @param selectionArgs
     * @return
     */
    public synchronized Cursor rawQuery(String sql, String[] selectionArgs) {
    	String sqllog = "sql:" + sql + ";selectionArgs:" + selectionArgs;
    	LogcatUtils.sqllog("", "rawQuery", sqllog, null, "", null);
    	tryOpenDataBase();
        if(mDataBase != null) { 
        	return mDataBase.rawQuery(sql, selectionArgs);
        } else {
        	return null;
        }
    }
    
    /**
     * 表数据是否为空
     * @param table
     * @return
     */
    public synchronized boolean isEmpty(String table) {
    	tryOpenDataBase();
		Cursor cursor = null;
		try {
			cursor = mDataBase.query(table, null, null, null, null, null, null);
			if (null == cursor) {
				return true;
			}
			if (cursor.getCount() <= 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return false;
    }
    
    /**
     * 表数据的条数
     * @param table
     * @return
     */
    public synchronized int getRecordNumber(String table) {
    	tryOpenDataBase();
		Cursor cursor = null;
		try {
			cursor = mDataBase.query(table, null, null, null, null, null, null);
			if (null == cursor) {
				return 0;
			}
			return cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
    }
    
    
    /**
     * 数据库帮助类
     * @author lijunma
     *
     */
    private class MySQLiteOpenHelper extends SQLiteOpenHelper {

		public MySQLiteOpenHelper() {
			super(mContext, getDataBaseName(), null, getDataBaseVersion());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				createDataBase(db);
			} catch (Exception e) {
				LogcatUtils.e(TAG, e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				updateDataBase(db, oldVersion, newVersion);
			} catch (Exception e) {
				LogcatUtils.e(TAG, e);
				try {
					clearDataBase(db);
					createDataBase(db);
				} catch (Exception ex) {
					LogcatUtils.e(TAG, ex);
				}
			}
		}
		
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				clearDataBase(db);
				createDataBase(db);
			} catch (Exception e) {
				LogcatUtils.e(TAG, e);
			}
		}
	}
	
}
