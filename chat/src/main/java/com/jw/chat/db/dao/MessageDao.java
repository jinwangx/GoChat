package com.jw.chat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jw.chat.db.GCDB;
import com.jw.chat.db.GCDBOpenHelper;
import com.jw.chat.db.bean.Conversation;
import com.jw.chat.db.bean.Message;


public class MessageDao {
	private GCDBOpenHelper helper;

	public MessageDao(Context context) {
		helper = GCDBOpenHelper.getInstance(context);
	}

	public void addMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GCDB.Message.COLUMN_ACCOUNT, message.getAccount());
		values.put(GCDB.Message.COLUMN_CONTENT, message.getContent());
		values.put(GCDB.Message.COLUMN_CREATE_TIME, message.getCreateTime());
		values.put(GCDB.Message.COLUMN_DIRECTION, message.getDirection());
		values.put(GCDB.Message.COLUMN_OWNER, message.getOwner());
		values.put(GCDB.Message.COLUMN_STATE, message.getState());
		values.put(GCDB.Message.COLUMN_TYPE, message.getType());
		values.put(GCDB.Message.COLUMN_URL, message.getUrl());
		values.put(GCDB.Message.COLUMN_READ, message.isRead() ? 1 : 0);

		message.setId(db.insert(GCDB.Message.TABLE_NAME, null, values));

		String sql = "select * from " + GCDB.Conversation.TABLE_NAME
				+ " where " + GCDB.Conversation.COLUMN_ACCOUNT + "=? and "
				+ GCDB.Conversation.COLUMN_OWNER + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { message.getAccount(),
				message.getOwner() });
		if (cursor != null && cursor.moveToNext()) {
			// String account = cursor.getString(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_ACCOUNT));
			// String content = cursor.getString(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_CONTENT));
			// String icon = cursor.getString(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_ICON));
			// String name = cursor.getString(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_NAME));
			// String owner = cursor.getString(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_OWNER));
			// int unread = cursor.getInt(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_UNREAD));
			// long updateTime = cursor.getLong(cursor
			// .getColumnIndex(GCDB.Conversation.COLUMN_UPDATE_TIME));
			//

			// 关闭cursor
			cursor.close();
			cursor = null;

			int unread = 0;

			sql = "select count(_id) from " + GCDB.Message.TABLE_NAME
					+ " where " + GCDB.Message.COLUMN_READ + "=0 and "
					+ GCDB.Message.COLUMN_ACCOUNT + "=? and "
					+ GCDB.Message.COLUMN_OWNER + "=?";
			cursor = db.rawQuery(sql, new String[] { message.getAccount(),
					message.getOwner() });
			if (cursor != null && cursor.moveToNext()) {
				unread = cursor.getInt(0);
			}

			values = new ContentValues();
			values.put(GCDB.Conversation.COLUMN_ACCOUNT, message.getAccount());

			int type = message.getType();
			if (type == 0) {
				values.put(GCDB.Conversation.COLUMN_CONTENT,
						message.getContent());
			} else if (type == 1) {
				values.put(GCDB.Conversation.COLUMN_CONTENT, "图片");
			}
			// values.put(GCDB.Conversation.COLUMN_ICON,
			// conversation.getIcon());
			// values.put(GCDB.Conversation.COLUMN_NAME,
			// conversation.getName());
			values.put(GCDB.Conversation.COLUMN_OWNER, message.getOwner());
			values.put(GCDB.Conversation.COLUMN_UNREAD, unread);
			values.put(GCDB.Conversation.COLUMN_UPDATE_TIME,
					System.currentTimeMillis());

			String whereClause = GCDB.Conversation.COLUMN_OWNER + "=? and "
					+ GCDB.Conversation.COLUMN_ACCOUNT + "=?";
			String[] whereArgs = new String[] { message.getOwner(),
					message.getAccount() };

			db.update(GCDB.Conversation.TABLE_NAME, values, whereClause,
					whereArgs);

		} else {
			Conversation conversation = new Conversation();
			conversation.setAccount(message.getAccount());
			int type = message.getType();
			if (type == 0) {
				conversation.setContent(message.getContent());
			} else if (type == 1) {
				conversation.setContent("图片");
			}
			// conversation.setIcon(message.get);
			// conversation.setName(message.get);
			conversation.setOwner(message.getOwner());
			conversation.setUnread(message.isRead() ? 0 : 1);
			conversation.setUpdateTime(System.currentTimeMillis());

			values = new ContentValues();
			values.put(GCDB.Conversation.COLUMN_ACCOUNT,
					conversation.getAccount());
			values.put(GCDB.Conversation.COLUMN_CONTENT,
					conversation.getContent());
			values.put(GCDB.Conversation.COLUMN_ICON, conversation.getIcon());
			values.put(GCDB.Conversation.COLUMN_NAME, conversation.getName());
			values.put(GCDB.Conversation.COLUMN_OWNER, conversation.getOwner());
			values.put(GCDB.Conversation.COLUMN_UNREAD,
					conversation.getUnread());
			values.put(GCDB.Conversation.COLUMN_UPDATE_TIME,
					conversation.getUpdateTime());

			db.insert(GCDB.Conversation.TABLE_NAME, null, values);
		}
	}

	public void updateMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GCDB.Message.COLUMN_CONTENT, message.getContent());
		values.put(GCDB.Message.COLUMN_CREATE_TIME, message.getCreateTime());
		values.put(GCDB.Message.COLUMN_DIRECTION, message.getDirection());
		values.put(GCDB.Message.COLUMN_STATE, message.getState());
		values.put(GCDB.Message.COLUMN_TYPE, message.getType());
		values.put(GCDB.Message.COLUMN_URL, message.getUrl());
		values.put(GCDB.Message.COLUMN_READ, message.isRead() ? 1 : 0);

		String whereClause = GCDB.Message.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { message.getId() + "" };
		db.update(GCDB.Message.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Cursor queryMessage(String owner, String account) {
		String sql = "select * from " + GCDB.Message.TABLE_NAME + " where "
				+ GCDB.Message.COLUMN_OWNER + "=? and "
				+ GCDB.Message.COLUMN_ACCOUNT + "=? order by "
				+ GCDB.Message.COLUMN_CREATE_TIME + " asc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner, account });
	}

	public Cursor queryConversation(String owner) {
		String sql = "select * from " + GCDB.Conversation.TABLE_NAME
				+ " where " + GCDB.Conversation.COLUMN_OWNER + "=? order by "
				+ GCDB.Conversation.COLUMN_UPDATE_TIME + " desc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner });
	}

	public void clearUnread(String owner, String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GCDB.Message.COLUMN_READ, 1);
		String whereClause = GCDB.Message.COLUMN_OWNER + "=? and "
				+ GCDB.Message.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { owner, account };
		db.update(GCDB.Message.TABLE_NAME, values, whereClause, whereArgs);

		values = new ContentValues();
		values.put(GCDB.Conversation.COLUMN_UNREAD, 0);
		whereClause = GCDB.Conversation.COLUMN_OWNER + "=? and "
				+ GCDB.Conversation.COLUMN_ACCOUNT + "=?";
		db.update(GCDB.Conversation.TABLE_NAME, values, whereClause, whereArgs);
	}

	public int getAllUnread(String owner) {
		String sql = "select sum(" + GCDB.Conversation.COLUMN_UNREAD
				+ ") from " + GCDB.Conversation.TABLE_NAME + " where "
				+ GCDB.Conversation.COLUMN_OWNER + "=?";

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { owner });
		int sum = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				sum = cursor.getInt(0);
			}
			cursor.close();
		}
		return sum;
	}
}
