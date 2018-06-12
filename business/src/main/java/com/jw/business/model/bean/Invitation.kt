package com.jw.business.model.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：邀请类，如该邀请是谁发送，是否同意等等信息
 */

@Entity(tableName = "invitation")
class Invitation {
    @ColumnInfo(name = "id")
    var id: Long = 0
    @PrimaryKey
    @NotNull
    var owner: String? = null
    @ColumnInfo(name = "account")
    var account: String? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "icon")
    var icon: String? = null
    @ColumnInfo(name = "content")
    var content: String? = null
    @ColumnInfo(name = "isAgree")
    var isAgree: Boolean = false

    override fun toString(): String {
        return ("Invitation [id=" + id + ", owner=" + owner + ", account="
                + account + ", name=" + name + ", icon=" + icon + ", content="
                + content + ", agree=" + isAgree + "]")
    }
}