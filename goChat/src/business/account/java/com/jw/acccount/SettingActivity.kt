package com.jw.acccount

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.view.View
import com.jw.business.db.AppDatabase
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivitySettingBinding
import com.jw.gochat.databinding.DialogIpBinding
import com.jw.gochat.service.IMService
import com.jw.gochat.utils.CacheUtils
import com.jw.gochat.view.DialogLogout
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.gochat.GoChatURL
import com.jw.library.utils.ThemeUtils
import com.sencent.mm.GoChatBindingActivity

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：设置页面，退出账号页面
 */

class SettingActivity : GoChatBindingActivity<ActivitySettingBinding>(), NormalTopBar.BackListener {

    override fun getLayoutId() = R.layout.activity_setting

    override fun doConfig(arguments: Intent) {
        val binding=binding
        binding.tvSetIp.text = GoChatURL.BASE_QQ_HOST
        binding.apply {
            ntSetting.setBackListener(this@SettingActivity)
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.rl_set_ip -> {
                        val builder = AlertDialog.Builder(this@SettingActivity)
                        val dialog = builder.create()
                        val view = View.inflate(this@SettingActivity, R.layout.dialog_ip, null)
                        val dialogIpBinding = DataBindingUtil.bind<DialogIpBinding>(view)
                        dialogIpBinding?.apply {
                            clickListener = View.OnClickListener {
                                when (it.id) {
                                    R.id.btn_ip_ok -> {
                                        val ipHost = etIpNew.text.toString()
                                        if (!TextUtils.isEmpty(ipHost)) {
                                            dialog.dismiss()
                                            CacheUtils.setCache("BASE_QQ_HOST", etIpNew.text.toString(), this@SettingActivity)
                                            binding.tvSetIp.text = GoChatURL.BASE_QQ_HOST
                                            ThemeUtils.show(this@SettingActivity, "应用将在2s后关闭,请重新启动以完成ip初始化")
                                            android.os.Handler().postDelayed({ (application as GoChatApplication).exit() }, 2000)
                                        } else
                                            etIpNew.error = "ip地址不能为空"
                                    }
                                    R.id.btn_ip_cancel -> {
                                        dialog.dismiss()
                                    }
                                }

                            }
                        }
                        dialog.setView(view)
                        dialog.show()
                    }
                    R.id.btn_exit -> {
                        val dialogLogout = DialogLogout(this@SettingActivity)
                        dialogLogout.show()
                        dialogLogout.setClickLogoutListener {
                            val account = GoChatApplication.getAccountInfo()!!
                            account.current = 0
                            val dao = AppDatabase.getInstance().accountDao()
                            dao.update(account)
                            (application as GoChatApplication).exit()
                            dialogLogout.dismiss()
                            stopService(Intent(this@SettingActivity, IMService::class.java))
                        }
                    }
                }
            }
        }
    }

    override fun back() {
        finish()
    }
}