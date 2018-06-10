package com.jw.acccount

import android.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityMqrBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity

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

class MQrActivity : BaseActivity(), NormalTopBar.BackListener {
    private val me = ChatApplication.getAccount()
    private var mBinding: ActivityMqrBinding? = null

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mqr)
    }

    override fun initView() {
        super.initView()
        Glide.with(this).load(me.icon).into(mBinding!!.ivQrIcon)
        mBinding!!.ntQr.setBackListener(this)
    }

    override fun back() {
        finish()
    }
}