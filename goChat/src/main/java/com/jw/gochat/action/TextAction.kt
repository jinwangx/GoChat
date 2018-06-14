package com.jw.gochat.action

import android.content.Context
import com.jw.chat.business.MessageBusiness
import com.jw.chat.db.bean.Message
import com.jw.gochat.event.TextEvent
import org.greenrobot.eventbus.EventBus

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到聊天消息或发送聊天信息时执行对应数据库更新，并通知界面更新
 */

class TextAction : Action() {

    override val action: String
        get() = "text"

    override fun doAction(context: Context, data: Map<String, Any>) {
        if (data == null) {
            return
        }
        val receiver = data["receiver"].toString()
        val sender = data["sender"].toString()
        val content = data["content"].toString()
        // 数据存储
        val message = Message()
        message.account = sender
        message.content = content
        message.create_time = System.currentTimeMillis()
        message.direction = 1  //1代表接受，0代表发送
        message.owner = receiver
        message.read = false
        message.type = 0  //0代表文字，1代表图片
        MessageBusiness.insert(message)
        EventBus.getDefault().post(TextEvent(message))
    }
}