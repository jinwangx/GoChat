package com.jw.acccount

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.jw.business.db.dao.AccountDao
import com.jw.chat.GoChatURL
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivitySettingBinding
import com.jw.gochat.service.ChatCoreService
import com.jw.gochat.utils.CacheUtils
import com.jw.gochat.view.DialogLogout
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：设置页面，退出账号页面
 */

class SettingActivity : BaseActivity(), View.OnClickListener, NormalTopBar.BackListener {

    private var mBinding: ActivitySettingBinding? = null

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
    }

    override fun initView() {
        super.initView()
        mBinding!!.tvSetIp.text = GoChatURL.BASE_QQ_HOST
        mBinding!!.rlSetIp.setOnClickListener(this)
        mBinding!!.ntSetting.setBackListener(this)
    }

    fun exit(view: View) {
        val dialogLogout = DialogLogout(this)
        dialogLogout.show()
        dialogLogout.setClickLogoutListener {
            val account = ChatApplication.getAccount()
            account.isCurrent = false
            val dao = AccountDao(this@SettingActivity)
            dao.updateAccount(account)
            (application as ChatApplication).exit()
            dialogLogout.dismiss()
            stopService(Intent(this@SettingActivity, ChatCoreService::class.java))
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_set_ip -> {
                val builder = AlertDialog.Builder(this)
                val dialog = builder.create()
                val view = View.inflate(this, R.layout.dialog_ip, null)
                val etIp = view.findViewById<View>(R.id.et_ip_new) as EditText
                val btnIpOk = view.findViewById<View>(R.id.btn_ip_ok) as Button
                val btnIpCancel = view.findViewById<View>(R.id.btn_ip_cancel) as Button
                btnIpOk.setOnClickListener {
                    val ipHost = etIp.text.toString()
                    if (!TextUtils.isEmpty(ipHost)) {
                        dialog.dismiss()
                        CacheUtils.setCache("BASE_QQ_HOST", etIp.text.toString(), this@SettingActivity)
                        mBinding!!.tvSetIp.text = GoChatURL.BASE_QQ_HOST
                        ThemeUtils.show(this@SettingActivity, "应用将在2s后关闭,请重新启动以完成ip初始化")
                        android.os.Handler().postDelayed({ (application as ChatApplication).exit() }, 2000)
                    } else
                        etIp.error = "ip地址不能为空"
                }
                btnIpCancel.setOnClickListener { dialog.dismiss() }
                dialog.setView(view)
                dialog.show()
            }
        }
    }

    override fun back() {
        finish()
    }
}