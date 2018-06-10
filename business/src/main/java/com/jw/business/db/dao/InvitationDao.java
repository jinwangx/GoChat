package com.jw.business.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jw.business.bean.Invitation;
import com.jw.business.db.GCDB;
import com.jw.business.db.GCDBOpenHelper;

public class InvitationDao {
	private GCDBOpenHelper helper;

	public InvitationDao(Context context) {
		helper = GCDBOpenHelper.getInstance(context);
	}

	public Cursor queryCursor(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + GCDB.Invitation.TABLE_NAME + " where "
				+ GCDB.Invitation.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public void addInvitation(Invitation invitation) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Invitation.COLUMN_OWNER, invitation.getOwner());
		values.put(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT,
				invitation.getAccount());
		values.put(GCDB.Invitation.COLUMN_INVITATOR_NAME, invitation.getName());
		values.put(GCDB.Invitation.COLUMN_INVITATOR_ICON, invitation.getIcon());
		values.put(GCDB.Invitation.COLUMN_CONTENT, invitation.getContent());
		values.put(GCDB.Invitation.COLUMN_AGREE, invitation.isAgree() ? 1 : 0);
		db.insert(GCDB.Invitation.TABLE_NAME, null, values);
	}

	public void updateInvitation(Invitation invitation) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Invitation.COLUMN_INVITATOR_NAME, invitation.getName());
		values.put(GCDB.Invitation.COLUMN_INVITATOR_ICON, invitation.getIcon());
		values.put(GCDB.Invitation.COLUMN_CONTENT, invitation.getContent());
		values.put(GCDB.Invitation.COLUMN_AGREE, invitation.isAgree() ? 1 : 0);

		String whereClause = GCDB.Invitation.COLUMN_OWNER + "=? and "
				+ GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?";
		String[] whereArgs = new String[] { invitation.getOwner(),
				invitation.getAccount() };

		db.update(GCDB.Invitation.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Invitation queryInvitation(String owner, String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select * from " + GCDB.Invitation.TABLE_NAME + " where "
				+ GCDB.Invitation.COLUMN_OWNER + "=? and "
				+ GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?";

		Cursor cursor = db.rawQuery(sql, new String[] { owner, account });
		Invitation invitation = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				// String account = cursor
				// .getString(cursor
				// .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT));
				String name = cursor.getString(cursor
						.getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_NAME));
				String icon = cursor.getString(cursor
						.getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ICON));
				boolean agree = cursor.getInt(cursor
						.getColumnIndex(GCDB.Invitation.COLUMN_AGREE)) == 1;
				String content = cursor.getString(cursor
						.getColumnIndex(GCDB.Invitation.COLUMN_CONTENT));
				// String owner = cursor.getString(cursor
				// .getColumnIndex(GCDB.Invitation.COLUMN_OWNER));
				long id = cursor.getLong(cursor
						.getColumnIndex(GCDB.Invitation.COLUMN_ID));

				invitation = new Invitation();
				invitation.setAccount(account);
				invitation.setAgree(agree);
				invitation.setContent(content);
				invitation.setIcon(icon);
				invitation.setName(name);
				invitation.setOwner(owner);
				invitation.setId(id);
			}
			cursor.close();
		}
		return invitation;
	}

	public boolean hasUnagree(String owner) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select count(" + GCDB.Invitation.COLUMN_ID + ") from "
				+ GCDB.Invitation.TABLE_NAME + " where "
				+ GCDB.Invitation.COLUMN_AGREE + "=0";
		Cursor cursor = db.rawQuery(sql, null);

		int count = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
			cursor.close();
		}
		return count != 0;
	}
}
