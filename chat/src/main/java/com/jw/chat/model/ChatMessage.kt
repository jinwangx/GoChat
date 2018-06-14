package com.jw.chat.model

import java.util.HashMap

/**
 * 创建时间：
 * 更新时间 2017/11/1 23:05
 * 版本：
 * 作者：Mr.jin
 * 描述：聊天信息
 */

class ChatMessage private constructor() {
    private var map: HashMap<String, Any>? = null

    var body: MessageBody? = null
    var receiver: String? = null
    private var account: String? = null
    private var token: String? = null

    private var type: Type? = null

    enum class Type {
        TEXT, IMAGE, INVITATION
    }

    fun setAccount(account: String) {
        this.account = account
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun getMap(): Map<String, Any> {
        map = HashMap()
        map!!["type"] = "request"
        map!!["sequence"] = SequenceCreater.createSequence()
        map!!["sender"] = this.account!!
        map!!["token"] = this.token!!
        map!!["action"] = this.getTypeString(type!!)!!
        map!!["receiver"] = this.receiver!!
        map!!["content"] = this.getMessageBody(body)!!
        return map as HashMap<String, Any>
    }

    private fun getTypeString(type: Type): String? {
        when (type) {
            Type.TEXT -> return "text"
            Type.IMAGE -> return "image"
            Type.INVITATION -> return "invitation"
            else -> {
            }
        }
        return null
    }

    private fun getMessageBody(body: MessageBody?): String? {
        if (body is TextBody) {
            return body.content
        } else if (body is InvitationBody) {
            return body.content
        }
        return null
    }

    companion object {
        fun createMessage(type: Type): ChatMessage {
            val msg = ChatMessage()
            msg.type = type
            return msg
        }
    }
}