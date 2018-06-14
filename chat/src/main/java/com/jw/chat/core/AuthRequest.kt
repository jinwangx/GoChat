package com.jw.chat.core

import com.google.gson.Gson
import com.jw.chat.model.SequenceCreater
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:52
 * 版本：
 * 作者：Mr.jin
 * 描述：认证请求封装(account,token,uuid)
 */

class AuthRequest(private val account: String, private val token: String) : ChatRequest(null, null) {

    /**
     * @return 返回一个该请求的uuid
     */
    override var sequence: String? = null

    /**
     * 自动封装Auth请求
     * @return 返回一个json化的Auth请求
     */
    override val transport: String
        get() {
            val map = HashMap<String, Any>()
            map["action"] = "auth"
            map["type"] = "request"
            map["sequence"] = this!!.sequence!!
            map["sender"] = account
            map["token"] = token
            map["action"] = "auth"
            return Gson().toJson(map)
        }

    init {
        this.sequence = SequenceCreater.createSequence()
    }
}