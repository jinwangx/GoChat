package com.jw.business.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jw.business.bean.Account;
import com.jw.business.db.GCDB;
import com.jw.business.db.GCDBOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class AccountDao {
	private GCDBOpenHelper helper;

	public AccountDao(Context context) {
		helper = GCDBOpenHelper.getInstance(context);
	}

	public List<Account> getAllAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + GCDB.Account.TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);

		List<Account> list = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (list == null) {
					list = new ArrayList<Account>();
				}
				Account account = new Account();

				account.setAccount(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ACCOUNT)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_TOKEN)));
				list.add(account);
			}
		}
		return list;
	}

	public Account getCurrentAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + GCDB.Account.TABLE_NAME + " where "
				+ GCDB.Account.COLUMN_CURRENT + "=1";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account account = new Account();

				account.setAccount(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ACCOUNT)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_TOKEN)));
				return account;
			}
		}
		return null;
	}

	public Account getByAccount(String account) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + GCDB.Account.TABLE_NAME + " where "
				+ GCDB.Account.COLUMN_ACCOUNT + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { account });

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account a = new Account();

				a.setAccount(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ACCOUNT)));
				a.setArea(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_AREA)));
				a.setCurrent(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1);
				a.setIcon(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_ICON)));
				a.setName(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_NAME)));
				a.setSex(cursor.getInt(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SEX)));
				a.setSign(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_SIGN)));
				a.setToken(cursor.getString(cursor
						.getColumnIndex(GCDB.Account.COLUMN_TOKEN)));
				return a;
			}
		}
		return null;
	}

	public void addAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Account.COLUMN_ACCOUNT, account.getAccount());
		values.put(GCDB.Account.COLUMN_AREA, account.getArea());
		values.put(GCDB.Account.COLUMN_ICON, account.getIcon());
		values.put(GCDB.Account.COLUMN_NAME, account.getName());
		values.put(GCDB.Account.COLUMN_SEX, account.getSex());
		values.put(GCDB.Account.COLUMN_SIGN, account.getSign());
		values.put(GCDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(GCDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		db.insert(GCDB.Account.TABLE_NAME, null, values);
	}

	public void updateAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GCDB.Account.COLUMN_AREA, account.getArea());
		values.put(GCDB.Account.COLUMN_ICON, account.getIcon());
		values.put(GCDB.Account.COLUMN_NAME, account.getName());
		values.put(GCDB.Account.COLUMN_SEX, account.getSex());
		values.put(GCDB.Account.COLUMN_SIGN, account.getSign());
		values.put(GCDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(GCDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		String whereClause = GCDB.Account.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { account.getAccount() };
		db.update(GCDB.Account.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void updateAccount(Account account,String iconPath){
		SQLiteDatabase db=helper.getReadableDatabase();
		ContentValues values=new ContentValues();
		values.put("icon",iconPath);
		db.update(GCDB.Account.TABLE_NAME, values, GCDB.Account.COLUMN_ACCOUNT + "=?", new String[]{account.getAccount()});
	}

	public void deleteAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = GCDB.Account.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { account.getAccount() };
		db.delete(GCDB.Account.TABLE_NAME, whereClause, whereArgs);
	}
}
