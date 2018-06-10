package com.jw.business.bean

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：账户信息类
 */

class Account private constructor(parcel: Parcel) : Parcelable {

    var account: String? = null// 账号
    var name: String? = null// 用户名
    var icon: String? = null// 用户图像
    var sex: Int = 0// 性别 0:未设置 1:女 2:男 3:其他
    var sign: String? = null// 用户个性签名
    var area: String? = null// 用户所在区域
    var token: String? = null// 用户与服务器交互的唯一标
    var isCurrent: Boolean = false// 是否是当前用户

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        val `val` = Bundle()
        `val`.putString("account", account)
        `val`.putString("name", name)
        `val`.putString("icon", icon)
        `val`.putInt("sex", sex)
        `val`.putString("sign", sign)
        `val`.putString("area", area)
        `val`.putString("token", token)
        `val`.putBoolean("current", isCurrent)
        dest.writeBundle(`val`)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Account> = object : Parcelable.Creator<Account> {
            override fun newArray(size: Int): Array<Account?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): Account {
                return Account(source)
            }
        }
    }

    init {
        val `val` = parcel.readBundle()
        account = `val`.getString("account")
        name = `val`.getString("name")
        icon = `val`.getString("icon")
        sex = `val`.getInt("sex")
        sign = `val`.getString("sign")
        area = `val`.getString("area")
        token = `val`.getString("token")
        isCurrent = `val`.getBoolean("current")
    }
}