package com.jw.chat.db.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话信息类
 */

@Entity(tableName = "conversation")
class Conversation {

    @PrimaryKey
    var owner: String? = null
    @PrimaryKey
    var account: String? = null
    @ColumnInfo
    var icon: String? = null
    @ColumnInfo
    var name: String? = null
    @ColumnInfo
    var content: String? = null
    @ColumnInfo
    var unread: Int = 0
    @ColumnInfo
    var updateTime: Long = 0
}