package com.onlan.lazymemo.sqlData;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MemoInfoService {

	private DataBaseOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	private Cursor cursor;

	public MemoInfoService(Context context) {
		dbOpenHelper = new DataBaseOpenHelper(context);
	}

	public long save(MemoInfo memoInfo) {
		if (database != null) {
			database = null;
		}
		database = dbOpenHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("contents", memoInfo.getContent());
		contentValues.put("time", memoInfo.getTime());
		contentValues.put("date", memoInfo.getDate());
		contentValues.put("type", memoInfo.getType());
		contentValues.put("bgcorclor", memoInfo.getBgcorclor());
		contentValues.put("isalarmclock", memoInfo.getIsalarmclock());

		long ID = database.insert("memo_info", null, contentValues);
		return ID;

	}

	public void update(MemoInfo memoInfo) {
		if (database != null) {
			database = null;
		}
		database = dbOpenHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("contents", memoInfo.getContent());
		contentValues.put("time", memoInfo.getTime());
		contentValues.put("date", memoInfo.getDate());
		contentValues.put("type", memoInfo.getType());
		contentValues.put("bgcorclor", memoInfo.getBgcorclor());
		contentValues.put("isalarmclock", memoInfo.getIsalarmclock());
		database.update("memo_info", contentValues, "_id=?",
				new String[] { String.valueOf(memoInfo.getid()) });
	}

	public MemoInfo getMemoInfoInfo(String date, String time) {
		if (database != null) {

			database = null;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		database = dbOpenHelper.getReadableDatabase();
		cursor = database.query("memo_info", new String[] { "_id", "contents",
				"time", "date", "type", "isalarmclock", "bgcorclor" },
				"date=?", new String[] { date }, null, null, null);

		if (cursor.moveToNext()) {
			if (cursor.getString(2).equals(time)) {

				return new MemoInfo(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
			}

		}
		if (cursor != null) {
			cursor.close();
		}
		return null;
	}

	public void close() {
		if (database != null) {
			database.close();
		}
		if (cursor != null) {
			cursor.close();
		}
	}

	public long deleteSignleinfo(String id) {
		database = dbOpenHelper.getWritableDatabase();
		return database.delete("memo_info", "_id=?", new String[] { id });

	}

	public void deleteTable() {
		database = dbOpenHelper.getWritableDatabase();
		database.delete("memo_info", null, null);
		database.execSQL("update sqlite_sequence set seq=0 where name='memo_info'");
	}

	public List<MemoInfo> getAllData(int startResult, int maxResult) {
		List<MemoInfo> memoInfo = new ArrayList<MemoInfo>();
		if (database != null) {

			database = null;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		database = dbOpenHelper.getReadableDatabase();
		cursor = database.query(true, "memo_info",
				new String[] { "_id", "contents", "time", "date", "type",
						"isalarmclock", "bgcorclor" }, null, null, null, null,
				"time asc", startResult + "," + maxResult);
		while (cursor.moveToNext()) {
			memoInfo.add(new MemoInfo(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor.getInt(4),
					cursor.getInt(5), cursor.getInt(6)));
		}
		if (cursor != null) {
			cursor.close();
		}

		return memoInfo;
	}

	public List<MemoInfo> getGroupDate() {
		List<MemoInfo> memoInfo = new ArrayList<MemoInfo>();
		if (database != null) {

			database = null;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		database = dbOpenHelper.getReadableDatabase();
		cursor = database.query(false, "memo_info",
				new String[] { "_id", "contents", "time", "date", "type",
						"isalarmclock", "bgcorclor" }, null, null, null, null,
				"date asc", 0 + "," + getCount());
		while (cursor.moveToNext()) {
			memoInfo.add(new MemoInfo(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor.getInt(4),
					cursor.getInt(5), cursor.getInt(6)));
		}
		if (cursor != null) {
			cursor.close();
		}

		return memoInfo;
	}

	public int getCount() {
		if (database != null) {

			database = null;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		database = dbOpenHelper.getReadableDatabase();
		cursor = database.query("memo_info", new String[] { "count(*)" }, null,
				null, null, null, null);
		if (cursor.moveToNext()) {
			return cursor.getInt(0);
		}
		if (cursor != null) {
			cursor.close();
		}

		return 0;
	}
}