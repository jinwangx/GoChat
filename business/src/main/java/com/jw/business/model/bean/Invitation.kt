package com.jw.business.model.bean

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：邀请类，如该邀请是谁发送，是否同意等等信息
 */

class Invitation {
    var id: Long = 0
    var owner: String? = null
    var account: String? = null
    var name: String? = null
    var icon: String? = null
    var content: String? = null
    var isAgree: Boolean = false

    override fun toString(): String {
        return ("Invitation [id=" + id + ", owner=" + owner + ", account="
                + account + ", name=" + name + ", icon=" + icon + ", content="
                + content + ", agree=" + isAgree + "]")
    }
}