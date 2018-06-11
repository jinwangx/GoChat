package com.jw.business.model.bean

import java.io.Serializable

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：好友类
 */

class Contact : Serializable {
    var id: Long = 0
    var owner: String? = null
    var account: String? = null
    var name: String? = null
    var sign: String? = null
    var area: String? = null
    var icon: String? = null
    var sex: Int = 0
    var nickName: String? = null
    var alpha: String? = null
    var sort: Int = 0

    companion object {
        private const val serialVersionUID = -8705224160256150097L
    }
}