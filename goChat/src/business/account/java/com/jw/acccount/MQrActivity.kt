package com.jw.acccount

import android.content.Intent
import com.bumptech.glide.Glide
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityMqrBinding
import com.jw.gochat.view.NormalTopBar
import com.sencent.mm.GoChatBindingActivity

/**
 * Created by Administrator on 2017/4/15.
 */

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：扫二维码，添加自己为好友页面
 */

class MQrActivity : GoChatBindingActivity<ActivityMqrBinding>(), NormalTopBar.BackListener {
    private val me = GoChatApplication.getAccountInfo()!!

    override fun getLayoutId() = R.layout.activity_mqr

    override fun doConfig(arguments: Intent) {
        Glide.with(this).load(me.icon).into(binding.ivQrIcon)
        binding.ntQr.setBackListener(this)
    }

    override fun back() {
        finish()
    }
}