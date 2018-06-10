package com.jw.business.db

interface GCDB {

    interface Account {
        companion object {
            //用户
            val TABLE_NAME = "account"
            //id,账号，姓名，性别，头像，   ，地区，   ，
            val COLUMN_ID = "_id"
            val COLUMN_ACCOUNT = "account"    //账号
            val COLUMN_NAME = "name"          //姓名
            val COLUMN_SEX = "sex"              //性别
            val COLUMN_ICON = "icon"          //头像
            val COLUMN_SIGN = "sign"          //
            val COLUMN_AREA = "area"          //地区
            val COLUMN_TOKEN = "token"
            val COLUMN_CURRENT = "current"

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_ACCOUNT + " text," + COLUMN_NAME + " text,"
                    + COLUMN_SEX + " integer," + COLUMN_ICON + " text,"
                    + COLUMN_SIGN + " text," + COLUMN_AREA + " text,"
                    + COLUMN_TOKEN + " text," + COLUMN_CURRENT + " integer" + ")")
        }
    }

    interface Friend {
        companion object {
            //朋友
            val TABLE_NAME = "friend"
            //id,拥有者，账号，姓名，   ，地区，头像，性别，备注名，    ，
            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"            //本人
            val COLUMN_ACCOUNT = "account"        //该朋友账号
            val COLUMN_NAME = "name"              //该朋友姓名
            val COLUMN_SIGN = "sign"
            val COLUMN_AREA = "area"              //该朋友地区
            val COLUMN_ICON = "icon"              //该朋友头像
            val COLUMN_SEX = "sex"                  //该朋友性别
            val COLUMN_NICKNAME = "nick_name"
            val COLUMN_ALPHA = "alpha"
            val COLUMN_SORT = "sort"

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
                    + COLUMN_NAME + " text," + COLUMN_SIGN + " text," + COLUMN_AREA
                    + " text," + COLUMN_ICON + " text," + COLUMN_SEX + " integer,"
                    + COLUMN_NICKNAME + " text," + COLUMN_ALPHA + " text,"
                    + COLUMN_SORT + " integer" + ")")
        }
    }

    interface Invitation {
        companion object {
            val TABLE_NAME = "invitation"
            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"                  //本人
            val COLUMN_INVITATOR_ACCOUNT = "invitator_account"// 邀请者的黑信号
            val COLUMN_INVITATOR_NAME = "invitator_name"// 邀请者的名字
            val COLUMN_INVITATOR_ICON = "invitator_icon"// 邀请者的图片
            val COLUMN_CONTENT = "content"// 邀请者的图片
            val COLUMN_AGREE = "agree"// 是否已经同意

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_OWNER + " text," + COLUMN_INVITATOR_ACCOUNT + " text,"
                    + COLUMN_INVITATOR_ICON + " text," + COLUMN_CONTENT + " text,"
                    + COLUMN_INVITATOR_NAME + " text," + COLUMN_AGREE + " integer"
                    + ")")
        }
    }

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

    interface BackTask {
        companion object {
            val TABLE_NAME = "back_task"

            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"
            val COLUMN_PATH = "path"
            val COLUMN_STATE = "state"// 0:未执行 1:正在执行 2:执行完成

            val SQL_CREATE_TABLE = ("create table " + TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_OWNER + " text," + COLUMN_PATH + " text,"
                    + COLUMN_STATE + " integer" + ")")
        }
    }

    companion object {
        val NAME = "gochat.db"
        val VERSION = 1
    }
}