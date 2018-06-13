package com.jw.gochat.action

import android.content.Context
import android.content.Intent
import com.jw.business.business.InvitationBusiness
import com.jw.business.model.bean.Invitation
import com.jw.chat.GoChatURL
import com.jw.gochat.receiver.PushReceiver

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：客户端发送邀请时（或者收到服务器推送用户邀请）执行对应数据库操作并通知界面更新
 *
 * 推送用户邀请：
 * key			类型	说明
 * type			String	请求：request
 * sequence		String	请求的序列号
 * action			String	请求的行为:invitation
 * sender			String	发送者账号
 * receiver		String	接收者的账号
 * content		String	邀请的文本内容
 *
 * invitor_name	String	邀请者的名字
 * invitor_icon	String	邀请者的头像
 *
 *
 * 发送邀请
 * key			类型	说明
 * type			String	请求：request
 * sequence		String	请求的序列号
 * action			String	请求的行为:invitation
 * sender			String	发送者账号
 * receiver		String	接收者的账号
 * content		String	邀请的文本内容
 * token			String	发送者token标志
 */

class InvitationAction : Action() {

    override val action: String
        get() = "invitation"

    override fun doAction(context: Context, data: Map<String, Any>) {
        if (data == null) {
            return
        }
        val receiver = data["receiver"].toString()
        val sender = data["sender"].toString()
        var name: String? = null
        var icon: String? = null
        var token: String? = null
        val nameObj = data["invitor_name"]
        if (nameObj != null) {
            name = nameObj as String?
        }
        val iconObj = data["invitor_icon"]
        if (iconObj != null) {
            icon = iconObj as String?
        }
        val tokenObj = data["token"]
        if (tokenObj != null) {
            token = tokenObj as String?
        }
        //如果token不为空，则是发送邀请,为空则是推送的邀请
        if (token != null) {
        } else {
        }
        // 存取数据
        var invitation = InvitationBusiness.getInvitationByAccount(receiver, sender)
        if (invitation == null) {
            invitation = Invitation()
            invitation.invitator_account = sender
            invitation.owner = receiver
            invitation.agree = false
            if (icon != null) {
                invitation.invitator_icon = GoChatURL.BASE_HTTP + icon.replace("\\", "/")
            }
            invitation.invitator_name = name
            InvitationBusiness.addInvitation(invitation)
        } else {
            invitation.agree = false
            if (icon != null) {
                invitation.invitator_icon = GoChatURL.BASE_HTTP + icon.replace("\\", "/")
            }
            InvitationBusiness.updateInvitation(invitation)
        }
        // 发送广播
        val intent = Intent(PushReceiver.ACTION_INVATION)
        intent.putExtra(PushReceiver.KEY_FROM, sender)
        intent.putExtra(PushReceiver.KEY_TO, receiver)
        context.sendBroadcast(intent)
    }
}