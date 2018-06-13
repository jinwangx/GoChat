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
 * 描述：消息类，如该消息属于谁，内容，发送成功或者失败
 */

@Entity(tableName = "message")
class Message {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name="_id")
    var _id: Long = 0
    @ColumnInfo(name="owner")
    var owner: String? = null
    @ColumnInfo(name="account")
    var account: String? = null // 接收者或发送者
    @ColumnInfo(name="direct")
    var direction: Int = 0  // 0:发送 1:接收
    @ColumnInfo(name="type")
    var type: Int = 0// 0:text 1:image
    @ColumnInfo(name="content")
    var content: String? = null
    @ColumnInfo(name="url")
    var url: String? = null
    @ColumnInfo(name="state")
    var state: Int = 0  // 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
    @ColumnInfo(name="read")
    var read: Boolean = false
    @ColumnInfo(name="create_time")
    var create_time: Long = 0
}