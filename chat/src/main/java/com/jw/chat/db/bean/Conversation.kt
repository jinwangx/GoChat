package com.jw.chat.db.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话信息类
 */

@Entity(tableName = "conversation")
class Conversation {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name="_id")
    var _id:Long=0
    @ColumnInfo(name="owner")
    var owner: String? = null
    @ColumnInfo(name="account")
    var account: String? = null
    @ColumnInfo(name="icon")
    var icon: String? = null
    @ColumnInfo(name="name")
    var name: String? = null
    @ColumnInfo(name="content")
    var content: String? = null
    @ColumnInfo(name="unread_count")
    var unread_count: Int = 0
    @ColumnInfo(name="update_time")
    var update_time: Long = 0
}