package com.jw.business.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GCDBOpenHelper extends SQLiteOpenHelper {

	private GCDBOpenHelper(Context context) {
		super(context, GCDB.NAME, null, GCDB.VERSION);
	}

	private static GCDBOpenHelper instance;

	public static GCDBOpenHelper getInstance(Context context) {
		if (instance == null) {
/*			当一个线程访问HMDBOpenHelper的一个synchronized(this)同步代码块时，它就获得了这个HMDBOpenHelper的对象锁。
			结果，其它线程对该HMDBOpenHelper对象所有同步代码部分的访问都被暂时阻塞*/
			synchronized (GCDBOpenHelper.class) {
				if (instance == null) {
					instance = new GCDBOpenHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GCDB.Account.SQL_CREATE_TABLE);
		db.execSQL(GCDB.Friend.SQL_CREATE_TABLE);
		db.execSQL(GCDB.Invitation.SQL_CREATE_TABLE);
		db.execSQL(GCDB.Message.SQL_CREATE_TABLE);
		db.execSQL(GCDB.Conversation.SQL_CREATE_TABLE);
		db.execSQL(GCDB.BackTask.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
