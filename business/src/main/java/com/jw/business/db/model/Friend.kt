package com.jw.business.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：好友类
 */

@Entity(tableName = "friend")
class Friend : Serializable {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name="_id")
    var _id: Long = 0
    @ColumnInfo(name="owner")
    var owner: String? = null
    @ColumnInfo(name="account")
    var account: String? = null
    @ColumnInfo(name="name")
    var name: String? = null
    @ColumnInfo(name="sign")
    var sign: String? = null
    @ColumnInfo(name="area")
    var area: String? = null
    @ColumnInfo(name="icon")
    var icon: String? = null
    @ColumnInfo(name="sex")
    var sex: Int = 0
    @ColumnInfo(name="nick_name")
    var nick_name: String? = null
    @ColumnInfo(name="alpha")
    var alpha: String? = null
    @ColumnInfo(name="sort")
    var sort: Int = 0

    companion object {
        private const val serialVersionUID = -8705224160256150097L
    }
}