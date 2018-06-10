package com.jw.business.db.dao

import android.content.ContentValues
import android.content.Context
import com.jw.business.model.bean.Account
import com.jw.business.db.GCDB
import com.jw.business.db.GCDBOpenHelper
import java.util.*


class AccountDao(context: Context) {
    private val helper: GCDBOpenHelper = GCDBOpenHelper.getInstance(context)

    val allAccount: List<Account>?
        get() {
            val db = helper.readableDatabase
            val sql = "select * from " + GCDB.Account.TABLE_NAME
            val cursor = db.rawQuery(sql, null)
            var list: MutableList<Account>? = null
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (list == null) {
                        list = ArrayList()
                    }
                    val account = Account()
                    account.account = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_ACCOUNT))
                    account.area = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_AREA))
                    account.isCurrent = cursor.getInt(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1
                    account.icon = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_ICON))
                    account.name = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_NAME))
                    account.sex = cursor.getInt(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_SEX))
                    account.sign = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_SIGN))
                    account.token = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_TOKEN))
                    list.add(account)
                }
            }
            return list
        }

    val currentAccount: Account?
        get() {
            val db = helper.readableDatabase
            val sql = ("select * from " + GCDB.Account.TABLE_NAME + " where "
                    + GCDB.Account.COLUMN_CURRENT + "=1")
            val cursor = db.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val account = Account()
                    account.account = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_ACCOUNT))
                    account.area = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_AREA))
                    account.isCurrent = cursor.getInt(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1
                    account.icon = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_ICON))
                    account.name = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_NAME))
                    account.sex = cursor.getInt(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_SEX))
                    account.sign = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_SIGN))
                    account.token = cursor.getString(cursor
                            .getColumnIndex(GCDB.Account.COLUMN_TOKEN))
                    return account
                }
            }
            return null
        }

    fun getByAccount(account: String): Account? {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.Account.TABLE_NAME + " where "
                + GCDB.Account.COLUMN_ACCOUNT + "=?")
        val cursor = db.rawQuery(sql, arrayOf(account))
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val a = Account()
                a.account = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_ACCOUNT))
                a.area = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_AREA))
                a.isCurrent = cursor.getInt(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_CURRENT)) == 1
                a.icon = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_ICON))
                a.name = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_NAME))
                a.sex = cursor.getInt(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_SEX))
                a.sign = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_SIGN))
                a.token = cursor.getString(cursor
                        .getColumnIndex(GCDB.Account.COLUMN_TOKEN))
                return a
            }
        }
        return null
    }

    fun addAccount(account: Account) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Account.COLUMN_ACCOUNT, account.account)
        values.put(GCDB.Account.COLUMN_AREA, account.area)
        values.put(GCDB.Account.COLUMN_ICON, account.icon)
        values.put(GCDB.Account.COLUMN_NAME, account.name)
        values.put(GCDB.Account.COLUMN_SEX, account.sex)
        values.put(GCDB.Account.COLUMN_SIGN, account.sign)
        values.put(GCDB.Account.COLUMN_TOKEN, account.token)
        values.put(GCDB.Account.COLUMN_CURRENT, if (account.isCurrent) 1 else 0)
        db.insert(GCDB.Account.TABLE_NAME, null, values)
    }

    fun updateAccount(account: Account) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Account.COLUMN_AREA, account.area)
        values.put(GCDB.Account.COLUMN_ICON, account.icon)
        values.put(GCDB.Account.COLUMN_NAME, account.name)
        values.put(GCDB.Account.COLUMN_SEX, account.sex)
        values.put(GCDB.Account.COLUMN_SIGN, account.sign)
        values.put(GCDB.Account.COLUMN_TOKEN, account.token)
        values.put(GCDB.Account.COLUMN_CURRENT, if (account.isCurrent) 1 else 0)
        val whereClause = GCDB.Account.COLUMN_ACCOUNT + "=?"
        val whereArgs = arrayOf<String>(account.account!!)
        db.update(GCDB.Account.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun updateAccount(account: Account, iconPath: String) {
        val db = helper.readableDatabase
        val values = ContentValues()
        values.put("icon", iconPath)
        db.update(GCDB.Account.TABLE_NAME, values, GCDB.Account.COLUMN_ACCOUNT + "=?", arrayOf<String>(account.account!!))
    }

    fun deleteAccount(account: Account) {
        val db = helper.writableDatabase
        val whereClause = GCDB.Account.COLUMN_ACCOUNT + "=?"
        val whereArgs = arrayOf<String>(account.account!!)
        db.delete(GCDB.Account.TABLE_NAME, whereClause, whereArgs)
    }
}