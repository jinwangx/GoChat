package com.jw.chat.core

import com.google.gson.Gson
import com.jw.chat.callback.GoChatCallBack
import com.jw.chat.model.ChatMessage
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:52
 * 版本：
 * 作者：Mr.jin
 * 描述：消息请求操作类，可以拿到请求的uuid以及json化的字符串
 */

open class ChatRequest(val callBack: GoChatCallBack?, private val message: ChatMessage?) {
    /**
     * @return 返回原生的请求序列
     */
    open var sequence: String? = null
    private val map: MutableMap<String, Any>?
    /**
     * @return 返回Json化后的请求序列
     */
    open val transport: String
        get() = Gson().toJson(map)

    init {
        map = HashMap()
        if (message != null) {
            map.putAll(this.message.getMap())
            sequence = map["sequence"] as String
        }
    }

    /**
     * 给请求序列设置一个key
     * @param account
     */
    fun setAccount(account: String) {
        if (map != null) {
            map["account"] = account
        }
    }
}