package com.jw.gochat.action

import android.content.Context
import com.jw.business.business.FriendBusiness
import com.jw.business.db.model.Friend
import com.jw.library.utils.CommonUtils
import com.jw.chat.GoChatURL
import com.jw.gochat.event.ReInvitationEvent
import org.greenrobot.eventbus.EventBus

/**
 * 创建时间：2017/10/29 2017/10/29
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送用户已接受邀请时，执行对应数据库更新，并通知界面更新
 * key	 	 类型	  说明
 * type		String	请求：request
 * sequence	String	请求的序列号
 * action		String	请求的行为:reinvitation
 * sender		String	发送者账号
 * receiver	String	接收者的账号
 * invitator_name		String	接受邀请者的名字
 * invitator_icon		String	接受邀请者的头像
 * content	String	邀请的文本内容
 */

class ReinvitationAction : Action() {

    override val action: String
        get() = "reinvitation"

    override fun doAction(context: Context, data: Map<String, Any>) {
        if (data == null) {
            return
        }
        val receiver = data["receiver"].toString()
        val sender = data["sender"].toString()
        var name: String? = null
        var icon: String? = null
        val nameObj = data["name"]
        if (nameObj != null) {
            name = nameObj as String?
        }
        val iconObj = data["icon"]
        if (iconObj != null) {
            icon = iconObj as String?
        }
        // 数据存储
        var friend = FriendBusiness.getFriendById(receiver, sender)
        if (friend == null) {
            friend = Friend()
            friend.account = sender
            friend.alpha = CommonUtils.getFirstAlpha(name)
            if (icon != null) {
                friend.icon = GoChatURL.BASE_HTTP + icon.replace("\\", "/")
            }
            friend.name = name
            friend.owner = receiver
            friend.sort = 0
            FriendBusiness.insert(friend)
        }
        EventBus.getDefault().post(ReInvitationEvent(friend))
    }
}