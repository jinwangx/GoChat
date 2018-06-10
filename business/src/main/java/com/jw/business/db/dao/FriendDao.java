package com.jw.business.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jw.business.bean.Contact;
import com.jw.business.db.GCDB;
import com.jw.business.db.GCDBOpenHelper;

public class FriendDao {
	private GCDBOpenHelper helper;

	public FriendDao(Context context) {
		helper = GCDBOpenHelper.getInstance(context);
	}

	public Cursor queryFriends(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + GCDB.Friend.TABLE_NAME + " where "
				+ GCDB.Friend.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public Contact queryFriendByAccount(String owner, String account) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + GCDB.Friend.TABLE_NAME + " where "
				+ GCDB.Friend.COLUMN_OWNER + "=? and "
				+ GCDB.Friend.COLUMN_ACCOUNT + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { owner, account });
		if (cursor != null) {
			Contact friend = null;
			if (cursor.moveToNext()) {
				String alpha = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_ALPHA));
				String area = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_AREA));
				String icon = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_ICON));
				String name = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_NAME));
				String nickName = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_NICKNAME));
				int sex = cursor.getInt(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_SEX));
				String sign = cursor.getString(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_SIGN));
				int sort = cursor.getInt(cursor
						.getColumnIndex(GCDB.Friend.COLUMN_SORT));

				friend = new Contact();
				friend.setAccount(account);
				friend.setAlpha(alpha);
				friend.setArea(area);
				friend.setIcon(icon);
				friend.setName(name);
				friend.setNickName(nickName);
				friend.setOwner(owner);
				friend.setSex(sex);
				friend.setSign(sign);
				friend.setSort(sort);
			}
			cursor.close();
			return friend;
		}
		return null;
	}

	public void addFriend(Contact friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Friend.COLUMN_ACCOUNT, friend.getAccount());
		values.put(GCDB.Friend.COLUMN_ALPHA, friend.getAlpha());
		values.put(GCDB.Friend.COLUMN_AREA, friend.getArea());
		values.put(GCDB.Friend.COLUMN_ICON, friend.getIcon());
		values.put(GCDB.Friend.COLUMN_NAME, friend.getName());
		values.put(GCDB.Friend.COLUMN_NICKNAME, friend.getNickName());
		values.put(GCDB.Friend.COLUMN_OWNER, friend.getOwner());
		values.put(GCDB.Friend.COLUMN_SEX, friend.getSex());
		values.put(GCDB.Friend.COLUMN_SIGN, friend.getSign());
		values.put(GCDB.Friend.COLUMN_SORT, friend.getSort());

		friend.setId(db.insert(GCDB.Friend.TABLE_NAME, null, values));
	}

	public void updateFriend(Contact friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Friend.COLUMN_ACCOUNT, friend.getAccount());
		values.put(GCDB.Friend.COLUMN_ALPHA, friend.getAlpha());
		values.put(GCDB.Friend.COLUMN_AREA, friend.getArea());
		values.put(GCDB.Friend.COLUMN_ICON, friend.getIcon());
		values.put(GCDB.Friend.COLUMN_NAME, friend.getName());
		values.put(GCDB.Friend.COLUMN_NICKNAME, friend.getNickName());
		values.put(GCDB.Friend.COLUMN_OWNER, friend.getOwner());
		values.put(GCDB.Friend.COLUMN_SEX, friend.getSex());
		values.put(GCDB.Friend.COLUMN_SIGN, friend.getSign());
		values.put(GCDB.Friend.COLUMN_SORT, friend.getSort());

		String whereClause = GCDB.Friend.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { friend.getId() + ""};
		db.update(GCDB.Friend.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void updateFriend(String account,String iconPath){
		SQLiteDatabase db=helper.getReadableDatabase();
		ContentValues values=new ContentValues();
		values.put(GCDB.Friend.COLUMN_ICON, iconPath);
		db.update(GCDB.Friend.TABLE_NAME,values, GCDB.Friend.COLUMN_ACCOUNT + "=?",new String[]{account});
	}
}
