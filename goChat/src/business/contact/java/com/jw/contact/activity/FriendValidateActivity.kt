package com.jw.contact

import android.content.Intent
import com.jw.chat.IMClient
import com.jw.chat.callback.GoChatCallBack
import com.jw.chat.model.ChatMessage
import com.jw.chat.model.InvitationBody
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendValidateBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.library.utils.ThemeUtils
import com.sencent.mm.GoChatBindingActivity

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：朋友验证页面
 */

class FriendValidateActivity : GoChatBindingActivity<ActivityFriendValidateBinding>(), NormalTopBar.BackListener {
    private var receiver: String? = null

    override fun getLayoutId() = R.layout.activity_friend_validate

    override fun doConfig(arguments: Intent) {
        receiver = intent.getStringExtra("receiver")
        binding.ntValidate.setBackListener(this)
        binding.ntValidate.setSendListener(sendValidateListener)
    }

    private val sendValidateListener = NormalTopBar.SendValidateListener {
        val content = binding.etValidateMsg.text.toString()
        val account = GoChatApplication.getAccountInfo()!!
        val message = ChatMessage
                .createMessage(ChatMessage.Type.INVITATION)
        message.body = InvitationBody(content)
        message.setAccount(account.account!!)
        message.setToken(account.token!!)
        message.receiver = receiver
        IMClient.getInstance().sendMessage(message, object : GoChatCallBack {

            override fun onSuccess() {
                ThemeUtils.show(this@FriendValidateActivity, "邀请发送成功")
                finish()
            }

            override fun onProgress(progress: Int) {}

            override fun onError(error: Int, msg: String) {
                ThemeUtils.show(this@FriendValidateActivity, "邀请发送失败")
            }
        })
    }

    override fun back() {
        finish()
    }
}