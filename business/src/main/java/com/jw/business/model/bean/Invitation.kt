package com.jw.business.model.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：邀请类，如该邀请是谁发送，是否同意等等信息
 */

@Entity(tableName = "invitation")
class Invitation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Long = 0
    @ColumnInfo(name = "owner")
    var owner: String? = null
    @ColumnInfo(name = "invitator_account")
    var invitator_account: String? = null
    @ColumnInfo(name = "invitator_name")
    var invitator_name: String? = null
    @ColumnInfo(name = "invitator_icon")
    var invitator_icon: String? = null
    @ColumnInfo(name = "content")
    var content: String? = null
    @ColumnInfo(name = "agree")
    var agree: Boolean = false

    override fun toString(): String {
        return ("Invitation [_id=" + _id + ", owner=" + owner + ", account="
                + invitator_account + ", invitator_name=" + invitator_name + ", invitator_icon=" + invitator_icon + ", content="
                + content + ", agree=" + agree + "]")
    }
}