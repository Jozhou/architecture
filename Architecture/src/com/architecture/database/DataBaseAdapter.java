package com.architecture.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.architecture.common.data.Account;

public class DataBaseAdapter {

	final String DB_AES_KEY = "QSXDKbdfsLDKAFdsal==aSDF";

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

	private DataBaseAdapter() {
	}

	/**
	 * 新增已读消息记录
	 */
	public boolean addMessage(String msgID) {
		Account account = Account.get();
		DataBaseHelper helper = DataBaseHelper.get();
		ContentValues value = new ContentValues();
		value.put(DataBaseTable.COL_MESSAGE_ID, msgID);
		value.put(DataBaseTable.COL_MESSAGE_DRIVERID, account.userName);
		return helper.insert(DataBaseTable.TABLE_MESSAGE, null, value) > 0;
	}

	/**
	 * 获得已读消息记录
	 */
	public List<String> getMessages() {
		Account account = Account.get();
		List<String> array = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = DataBaseHelper.get().query(DataBaseTable.TABLE_MESSAGE, null,
					DataBaseTable.COL_MESSAGE_DRIVERID + "='" + account.userName + "'", null, null, null, null);
			if (cursor != null) {
				String entity = null;
				while (cursor.moveToNext()) {
					entity = cursor.getString(cursor.getColumnIndex(DataBaseTable.COL_MESSAGE_ID));
					array.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return array;
	}

}
