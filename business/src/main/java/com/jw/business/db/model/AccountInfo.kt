package com.jw.business.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import org.jetbrains.annotations.NotNull

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：账户信息类
 */

@Entity(tableName = "account")
class AccountInfo() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "_id")
    var _id: Long = 0// 账号
    @ColumnInfo(name = "account")
    var account: String? = null// 账号
    @ColumnInfo(name = "name")
    var name: String? = null// 用户名
    @ColumnInfo(name = "icon")
    var icon: String? = null// 用户图像
    @ColumnInfo(name = "sex")
    var sex: Int = 0// 性别 0:未设置 1:女 2:男 3:其他
    @ColumnInfo(name = "sign")
    var sign: String? = null// 用户个性签名
    @ColumnInfo(name = "area")
    var area: String? = null// 用户所在区域
    @ColumnInfo(name = "token")
    var token: String? = null// 用户与服务器交互的唯一标
    @ColumnInfo(name = "current")
    var current = 0// 是否是当前用户

    constructor(parcel: Parcel) : this() {

        account = parcel.readString()
        name = parcel.readString()
        icon = parcel.readString()
        sex = parcel.readInt()
        sign = parcel.readString()
        area = parcel.readString()
        token = parcel.readString()
        current = parcel.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(account)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeInt(sex)
        parcel.writeString(sign)
        parcel.writeString(area)
        parcel.writeString(token)
        parcel.writeInt(current)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<AccountInfo> {

            override fun newArray(size: Int) = arrayOfNulls<AccountInfo>(size)

            override fun createFromParcel(source: Parcel) = AccountInfo(source)

        }
    }

}