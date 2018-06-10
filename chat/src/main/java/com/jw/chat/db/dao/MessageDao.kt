package com.jw.chat.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.jw.chat.db.GCDB
import com.jw.chat.db.GCDBOpenHelper
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message


class MessageDao(context: Context) {
    private val helper: GCDBOpenHelper = GCDBOpenHelper.getInstance(context)

    fun addMessage(message: Message) {
        val db = helper.writableDatabase
        var values = ContentValues()
        values.put(GCDB.Message.COLUMN_ACCOUNT, message.account)
        values.put(GCDB.Message.COLUMN_CONTENT, message.content)
        values.put(GCDB.Message.COLUMN_CREATE_TIME, message.createTime)
        values.put(GCDB.Message.COLUMN_DIRECTION, message.direction)
        values.put(GCDB.Message.COLUMN_OWNER, message.owner)
        values.put(GCDB.Message.COLUMN_STATE, message.state)
        values.put(GCDB.Message.COLUMN_TYPE, message.type)
        values.put(GCDB.Message.COLUMN_URL, message.url)
        values.put(GCDB.Message.COLUMN_READ, if (message.isRead) 1 else 0)
        message.id = db.insert(GCDB.Message.TABLE_NAME, null, values)
        var sql = ("select * from " + GCDB.Conversation.TABLE_NAME
                + " where " + GCDB.Conversation.COLUMN_ACCOUNT + "=? and "
                + GCDB.Conversation.COLUMN_OWNER + "=?")
        var cursor: Cursor? = db.rawQuery(sql, arrayOf<String>(message.account!!, message.owner!!))
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
            cursor.close()
            cursor = null
            var unread = 0
            sql = ("select count(_id) from " + GCDB.Message.TABLE_NAME
                    + " where " + GCDB.Message.COLUMN_READ + "=0 and "
                    + GCDB.Message.COLUMN_ACCOUNT + "=? and "
                    + GCDB.Message.COLUMN_OWNER + "=?")
            cursor = db.rawQuery(sql, arrayOf<String>(message.account!!, message.owner!!))
            if (cursor != null && cursor.moveToNext()) {
                unread = cursor.getInt(0)
            }
            values = ContentValues()
            values.put(GCDB.Conversation.COLUMN_ACCOUNT, message.account)
            val type = message.type
            if (type == 0) {
                values.put(GCDB.Conversation.COLUMN_CONTENT,
                        message.content)
            } else if (type == 1) {
                values.put(GCDB.Conversation.COLUMN_CONTENT, "图片")
            }
            // values.put(GCDB.Conversation.COLUMN_ICON,
            // conversation.getIcon());
            // values.put(GCDB.Conversation.COLUMN_NAME,
            // conversation.getName());
            values.put(GCDB.Conversation.COLUMN_OWNER, message.owner)
            values.put(GCDB.Conversation.COLUMN_UNREAD, unread)
            values.put(GCDB.Conversation.COLUMN_UPDATE_TIME,
                    System.currentTimeMillis())
            val whereClause = (GCDB.Conversation.COLUMN_OWNER + "=? and "
                    + GCDB.Conversation.COLUMN_ACCOUNT + "=?")
            val whereArgs = arrayOf<String>(message.owner!!, message.account!!)
            db.update(GCDB.Conversation.TABLE_NAME, values, whereClause,
                    whereArgs)
        } else {
            val conversation = Conversation()
            conversation.account = message.account
            val type = message.type
            if (type == 0) {
                conversation.content = message.content
            } else if (type == 1) {
                conversation.content = "图片"
            }
            // conversation.setIcon(message.get);
            // conversation.setName(message.get);
            conversation.owner = message.owner
            conversation.unread = if (message.isRead) 0 else 1
            conversation.updateTime = System.currentTimeMillis()
            values = ContentValues()
            values.put(GCDB.Conversation.COLUMN_ACCOUNT,
                    conversation.account)
            values.put(GCDB.Conversation.COLUMN_CONTENT,
                    conversation.content)
            values.put(GCDB.Conversation.COLUMN_ICON, conversation.icon)
            values.put(GCDB.Conversation.COLUMN_NAME, conversation.name)
            values.put(GCDB.Conversation.COLUMN_OWNER, conversation.owner)
            values.put(GCDB.Conversation.COLUMN_UNREAD,
                    conversation.unread)
            values.put(GCDB.Conversation.COLUMN_UPDATE_TIME,
                    conversation.updateTime)
            db.insert(GCDB.Conversation.TABLE_NAME, null, values)
        }
    }

    fun updateMessage(message: Message) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Message.COLUMN_CONTENT, message.content)
        values.put(GCDB.Message.COLUMN_CREATE_TIME, message.createTime)
        values.put(GCDB.Message.COLUMN_DIRECTION, message.direction)
        values.put(GCDB.Message.COLUMN_STATE, message.state)
        values.put(GCDB.Message.COLUMN_TYPE, message.type)
        values.put(GCDB.Message.COLUMN_URL, message.url)
        values.put(GCDB.Message.COLUMN_READ, if (message.isRead) 1 else 0)
        val whereClause = GCDB.Message.COLUMN_ID + "=?"
        val whereArgs = arrayOf(message.id.toString() + "")
        db.update(GCDB.Message.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun queryMessage(owner: String, account: String): Cursor {
        val sql = ("select * from " + GCDB.Message.TABLE_NAME + " where "
                + GCDB.Message.COLUMN_OWNER + "=? and "
                + GCDB.Message.COLUMN_ACCOUNT + "=? order by "
                + GCDB.Message.COLUMN_CREATE_TIME + " asc")
        val db = helper.readableDatabase
        return db.rawQuery(sql, arrayOf(owner, account))
    }

    fun queryConversation(owner: String): Cursor {
        val sql = ("select * from " + GCDB.Conversation.TABLE_NAME
                + " where " + GCDB.Conversation.COLUMN_OWNER + "=? order by "
                + GCDB.Conversation.COLUMN_UPDATE_TIME + " desc")
        val db = helper.readableDatabase
        return db.rawQuery(sql, arrayOf(owner))
    }

    fun clearUnread(owner: String, account: String) {
        val db = helper.writableDatabase
        var values = ContentValues()
        values.put(GCDB.Message.COLUMN_READ, 1)
        var whereClause = (GCDB.Message.COLUMN_OWNER + "=? and "
                + GCDB.Message.COLUMN_ACCOUNT + "=?")
        val whereArgs = arrayOf(owner, account)
        db.update(GCDB.Message.TABLE_NAME, values, whereClause, whereArgs)
        values = ContentValues()
        values.put(GCDB.Conversation.COLUMN_UNREAD, 0)
        whereClause = (GCDB.Conversation.COLUMN_OWNER + "=? and "
                + GCDB.Conversation.COLUMN_ACCOUNT + "=?")
        db.update(GCDB.Conversation.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun getAllUnread(owner: String): Int {
        val sql = ("select sum(" + GCDB.Conversation.COLUMN_UNREAD
                + ") from " + GCDB.Conversation.TABLE_NAME + " where "
                + GCDB.Conversation.COLUMN_OWNER + "=?")

        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf(owner))
        var sum = 0
        if (cursor != null) {
            if (cursor.moveToNext()) {
                sum = cursor.getInt(0)
            }
            cursor.close()
        }
        return sum
    }
}