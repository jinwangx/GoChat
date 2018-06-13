package com.jw.contact

import android.databinding.DataBindingUtil
import com.jw.chat.GoChatManager
import com.jw.chat.callback.GoChatCallBack
import com.jw.chat.msg.ChatMessage
import com.jw.chat.msg.InvitationBody
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendValidateBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：朋友验证页面
 */

class FriendValidateActivity : BaseActivity(), NormalTopBar.BackListener {

    private var receiver: String? = null
    private var mBinding: ActivityFriendValidateBinding? = null

    private val sendValidateListener = NormalTopBar.SendValidateListener {
        val content = mBinding!!.etValidateMsg.text.toString()
        if (content != null) {
            val account = ChatApplication.getAccountInfo()
            val message = ChatMessage
                    .createMessage(ChatMessage.Type.INVITATION)
            message.body = InvitationBody(content)
            message.setAccount(account.account!!)
            message.setToken(account.token!!)
            message.receiver = receiver
            GoChatManager.getInstance(ChatApplication.getOkHttpClient()).sendMessage(message, object : GoChatCallBack {

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
    }

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_validate)
    }

    override fun initView() {
        super.initView()
        receiver = intent.getStringExtra("receiver")
        mBinding!!.ntValidate.setBackListener(this)
        mBinding!!.ntValidate.setSendListener(sendValidateListener)
    }

    override fun back() {
        finish()
    }
}