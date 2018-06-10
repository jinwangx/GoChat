package com.jw.chat.db.bean

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话信息类
 */

class Conversation {
    // String COLUMN_ID = "_id";
    // String COLUMN_OWNER = "owner";
    // String COLUMN_ACCOUNT = "account";
    // String COLUMN_ICON = "icon";
    // String COLUMN_NAME = "name";
    // String COLUMN_CONTENT = "content";
    // String COLUMN_UNREAD = "unread_count";
    // String COLUMN_UPDATE_TIME = "update_time";

    var owner: String? = null
    var account: String? = null
    var icon: String? = null
    var name: String? = null
    var content: String? = null
    var unread: Int = 0
    var updateTime: Long = 0
}