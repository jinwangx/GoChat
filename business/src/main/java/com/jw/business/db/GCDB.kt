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
        }
    }

    interface BackTask {
        companion object {
            val TABLE_NAME = "back_task"

            val COLUMN_ID = "_id"
            val COLUMN_OWNER = "owner"
            val COLUMN_PATH = "path"
            val COLUMN_STATE = "state"// 0:未执行 1:正在执行 2:执行完成
        }
    }
}