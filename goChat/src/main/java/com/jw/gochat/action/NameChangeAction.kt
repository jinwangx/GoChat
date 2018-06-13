package com.jw.gochat.action

import android.content.Context
import android.content.Intent
import com.jw.business.business.FriendBusiness
import com.jw.gochat.receiver.PushReceiver

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送名称变更时，执行对应数据库更新，并通知界面更新
 */

class NameChangeAction : Action() {

    override val action: String
        get() = "nameChange"

    override fun doAction(context: Context, data: Map<String, Any>) {
        if (data == null) {
            return
        }

        val receiver = data["receiver"].toString()
        val sender = data["sender"].toString()
        val name = data["invitator_name"].toString()

        // 数据存储
        val friend = FriendBusiness.getFriendById(receiver, sender)
        friend!!.name = name
        FriendBusiness.update(friend)

        // 发送广播
        val intent = Intent(PushReceiver.ACTION_NAME_CHANGE)
        intent.putExtra(PushReceiver.KEY_FROM, sender)
        intent.putExtra(PushReceiver.KEY_TO, receiver)
        context.sendBroadcast(intent)
    }

}
