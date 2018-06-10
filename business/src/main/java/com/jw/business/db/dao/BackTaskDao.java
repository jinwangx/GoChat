package com.jw.business.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jw.business.bean.BackTask;
import com.jw.business.db.GCDB;
import com.jw.business.db.GCDBOpenHelper;

public class BackTaskDao {
	private GCDBOpenHelper helper;

	public BackTaskDao(Context context) {
		helper = GCDBOpenHelper.getInstance(context);
	}

	public void addTask(BackTask task) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GCDB.BackTask.COLUMN_OWNER, task.getOwner());
		values.put(GCDB.BackTask.COLUMN_PATH, task.getPath());
		values.put(GCDB.BackTask.COLUMN_STATE, task.getState());
		task.setId(db.insert(GCDB.BackTask.TABLE_NAME, null, values));
	}

	public void updateTask(BackTask task) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GCDB.BackTask.COLUMN_OWNER, task.getOwner());
		values.put(GCDB.BackTask.COLUMN_PATH, task.getPath());
		values.put(GCDB.BackTask.COLUMN_STATE, task.getState());

		String whereClause = GCDB.BackTask.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { task.getId() + "" };
		db.update(GCDB.BackTask.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void updateState(long id, int state) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.BackTask.COLUMN_STATE, state);
		String whereClause = GCDB.BackTask.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { id + "" };
		db.update(GCDB.BackTask.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Cursor query(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + GCDB.BackTask.TABLE_NAME + " where "
				+ GCDB.BackTask.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public Cursor query(String owner, int state) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + GCDB.BackTask.TABLE_NAME + " where "
				+ GCDB.BackTask.COLUMN_OWNER + "=? and "
				+ GCDB.BackTask.COLUMN_STATE + "=?";
		return db.rawQuery(sql, new String[] { owner, "0" });
	}
}
