package com.jw.chat.db

interface GCDB {

    interface Message {
        companion object {
            val TYPE_TEXT = 0
            val TYPE_IMAGE = 1

            val TABLE_NAME = "message"
            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"
            val COLUMN_ACCOUNT = "account"// 接收者或发送者
            val COLUMN_DIRECTION = "direct"// 0:发送 1:接收
            val COLUMN_TYPE = "type"
            val COLUMN_CONTENT = "content"
            val COLUMN_URL = "url"
            val COLUMN_STATE = "state"// 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
            val COLUMN_READ = "read"// 0:未读 1:已读
            val COLUMN_CREATE_TIME = "create_time"

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
                    + COLUMN_DIRECTION + " integer," + COLUMN_TYPE + " integer,"
                    + COLUMN_CONTENT + " text," + COLUMN_URL + " text,"
                    + COLUMN_STATE + " integer," + COLUMN_READ + " integer,"
                    + COLUMN_CREATE_TIME + " integer" + ")")
        }
    }

    interface Conversation {
        companion object {
            val TABLE_NAME = "conversation"

            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"         //本人
            val COLUMN_ACCOUNT = "account"     //对话者账号
            val COLUMN_ICON = "icon"           //对话者头像
            val COLUMN_NAME = "name"           //对话者姓名
            val COLUMN_CONTENT = "content"     //对话内容
            val COLUMN_UNREAD = "unread_count" //未读消息数量
            val COLUMN_UPDATE_TIME = "update_time"

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
                    + COLUMN_ICON + " text," + COLUMN_NAME + " text,"
                    + COLUMN_CONTENT + " text," + COLUMN_UNREAD + " integer,"
                    + COLUMN_UPDATE_TIME + " integer" + ")")
        }
    }
}