package com.jw.business.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.jw.business.model.bean.Contact
import com.jw.business.db.GCDB
import com.jw.business.db.GCDBOpenHelper

class FriendDao(context: Context) {
    private val helper: GCDBOpenHelper = GCDBOpenHelper.getInstance(context)

    fun queryFriends(owner: String): Cursor {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.Friend.TABLE_NAME + " where "
                + GCDB.Friend.COLUMN_OWNER + "=?")
        return db.rawQuery(sql, arrayOf(owner))
    }

    fun queryFriendByAccount(owner: String, account: String): Contact? {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.Friend.TABLE_NAME + " where "
                + GCDB.Friend.COLUMN_OWNER + "=? and "
                + GCDB.Friend.COLUMN_ACCOUNT + "=?")
        val cursor = db.rawQuery(sql, arrayOf(owner, account))
        if (cursor != null) {
            var friend: Contact? = null
            if (cursor.moveToNext()) {
                val alpha = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_ALPHA))
                val area = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_AREA))
                val icon = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_ICON))
                val name = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_NAME))
                val nickName = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_NICKNAME))
                val sex = cursor.getInt(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_SEX))
                val sign = cursor.getString(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_SIGN))
                val sort = cursor.getInt(cursor
                        .getColumnIndex(GCDB.Friend.COLUMN_SORT))
                friend = Contact()
                friend.account = account
                friend.alpha = alpha
                friend.area = area
                friend.icon = icon
                friend.name = name
                friend.nickName = nickName
                friend.owner = owner
                friend.sex = sex
                friend.sign = sign
                friend.sort = sort
            }
            cursor.close()
            return friend
        }
        return null
    }

    fun addFriend(friend: Contact) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Friend.COLUMN_ACCOUNT, friend.account)
        values.put(GCDB.Friend.COLUMN_ALPHA, friend.alpha)
        values.put(GCDB.Friend.COLUMN_AREA, friend.area)
        values.put(GCDB.Friend.COLUMN_ICON, friend.icon)
        values.put(GCDB.Friend.COLUMN_NAME, friend.name)
        values.put(GCDB.Friend.COLUMN_NICKNAME, friend.nickName)
        values.put(GCDB.Friend.COLUMN_OWNER, friend.owner)
        values.put(GCDB.Friend.COLUMN_SEX, friend.sex)
        values.put(GCDB.Friend.COLUMN_SIGN, friend.sign)
        values.put(GCDB.Friend.COLUMN_SORT, friend.sort)
        friend.id = db.insert(GCDB.Friend.TABLE_NAME, null, values)
    }

    fun updateFriend(friend: Contact) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Friend.COLUMN_ACCOUNT, friend.account)
        values.put(GCDB.Friend.COLUMN_ALPHA, friend.alpha)
        values.put(GCDB.Friend.COLUMN_AREA, friend.area)
        values.put(GCDB.Friend.COLUMN_ICON, friend.icon)
        values.put(GCDB.Friend.COLUMN_NAME, friend.name)
        values.put(GCDB.Friend.COLUMN_NICKNAME, friend.nickName)
        values.put(GCDB.Friend.COLUMN_OWNER, friend.owner)
        values.put(GCDB.Friend.COLUMN_SEX, friend.sex)
        values.put(GCDB.Friend.COLUMN_SIGN, friend.sign)
        values.put(GCDB.Friend.COLUMN_SORT, friend.sort)
        val whereClause = GCDB.Friend.COLUMN_ID + "=?"
        val whereArgs = arrayOf(friend.id.toString() + "")
        db.update(GCDB.Friend.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun updateFriend(account: String, iconPath: String) {
        val db = helper.readableDatabase
        val values = ContentValues()
        values.put(GCDB.Friend.COLUMN_ICON, iconPath)
        db.update(GCDB.Friend.TABLE_NAME, values, GCDB.Friend.COLUMN_ACCOUNT + "=?", arrayOf(account))
    }
}