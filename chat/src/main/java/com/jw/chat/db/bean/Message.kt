package com.jw.chat.db.bean

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：消息类，如该消息属于谁，内容，发送成功或者失败
 */

class Message {
    // String COLUMN_ID = "_id";
    // String COLUMN_OWNER = "owner";
    // String COLUMN_ACCOUNT = "account";// 接收者或发送者
    // String COLUMN_DIRECTION = "direct";// 0:发送 1:接收
    // String COLUMN_TYPE = "type";
    // String COLUMN_CONTENT = "content";
    // String COLUMN_URL = "url";
    // String COLUMN_STATE = "state";// 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
    // String COLUMN_CREATE_TIME = "create_time";
    var id: Long = 0
    var owner: String? = null
    var account: String? = null
    var direction: Int = 0// 0:发送 1:接收
    var type: Int = 0// 0:text 1:image
    var content: String? = null
    var url: String? = null
    var state: Int = 0
    var isRead: Boolean = false
    var createTime: Long = 0
}