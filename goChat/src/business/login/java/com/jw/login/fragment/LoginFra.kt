package com.jw.login.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.adapters.TextViewBindingAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jw.business.business.AccountInfoBusiness
import com.jw.business.db.model.AccountInfo
import com.jw.chat.IMClient
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.activity.HomeActivity
import com.jw.gochat.databinding.DialogIpBinding
import com.jw.gochat.databinding.FragmentLoginBinding
import com.jw.gochat.http.ScHttpClient
import com.jw.gochat.http.service.GoChatService
import com.jw.gochat.utils.CacheUtils
import com.jw.gochat.view.DialogSheet
import com.jw.gochatbase.gochat.GoChatURL
import com.jw.library.utils.ThemeUtils
import com.jw.login.LoginActivity
import com.jw.gochat.GoChatBindingFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：登陆页面
 */

class LoginFra : GoChatBindingFragment<FragmentLoginBinding>() {
    internal var dialog: Dialog? = null

    override fun getLayoutId() = R.layout.fragment_login

    override fun doConfig(arguments: Bundle?) {
        val binding = binding
        Glide.with(this).load(R.drawable.default_icon_user).into(binding!!.ivLoginIcon)
        binding.apply {
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.btn_login -> {
                        val account = binding.etLoginAct.text.toString()
                        val password = binding.etLoginPsw.text.toString()
                        ScHttpClient.getService(GoChatService::class.java)
                                .login(account, password)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    val accountInfo = Gson().fromJson<AccountInfo>(it.getString("data"), AccountInfo::class.java)
                                    accountInfo.current = 1
                                    accountInfo.icon = GoChatURL.BASE_HTTP + accountInfo.icon!!
                                            .replace("\\", "/")
                                    // 初始化用户连接安全信息
                                    IMClient.getInstance().initAccount(
                                            accountInfo.account!!, accountInfo.token!!)
                                    if (AccountInfoBusiness.getAccountInfoByAccount(accountInfo.account!!) != null)
                                        AccountInfoBusiness.update(accountInfo)
                                    else
                                        AccountInfoBusiness.insert(accountInfo)
                                    startActivity(Intent(activity, HomeActivity::class.java))
                                }) {
                                    ThemeUtils.show(activity, it.message)
                                }
                    }
                    R.id.tv_register -> {
                        (activity as LoginActivity).goRegisterFra()
                    }
                    R.id.tv_login_psw_miss -> {
                        val dialogSheet = DialogSheet(context!!)
                        dialogSheet.setPassListener(dialogListener)
                        dialogSheet.show()//显示对话框
                    }
                }
            }
            pasNotNullListener = TextViewBindingAdapter.OnTextChanged { s, _, _, _ ->
                if (s != null) {
                    binding.btnLogin.isEnabled = true
                    val account = AccountInfoBusiness.getAccountInfoByAccount(s.toString())
                    if (account != null) {
                        Glide.with(activity!!).load(account.icon).into(binding.ivLoginIcon)
                    }
                } else
                    binding.btnLogin.isEnabled = false
            }
        }
    }

    override fun doLaunch() {
    }

    override fun doRefresh() {
    }

    /**
     * 忘记密码dialog点击监听
     */
    private val dialogListener = object : DialogSheet.DialogSheetListener {
        override fun foundPass() {
            Toast.makeText(activity, "找回密码", Toast.LENGTH_SHORT).show()
        }

        override fun loginSms() {
            Toast.makeText(activity, "短信验证登录", Toast.LENGTH_SHORT).show()
        }

        override fun changeIp() {
            val builder = AlertDialog.Builder(activity)
            val ipDialog = builder.create()
            val view = View.inflate(activity, R.layout.dialog_ip, null)
            val dialogIpBinding = DataBindingUtil.bind<DialogIpBinding>(view)
            dialogIpBinding?.apply {
                clickListener = View.OnClickListener {
                    when (it.id) {
                        R.id.btn_ip_ok -> {
                            val ipHost = etIpNew.text.toString()
                            if (!TextUtils.isEmpty(ipHost)) {
                                ipDialog.dismiss()
                                dialog!!.dismiss()
                                CacheUtils.setCache("BASE_QQ_HOST", ipHost, activity as Context)
                                ThemeUtils.show(activity, "应用将在2s后关闭,请重新启动以完成ip初始化")
                                android.os.Handler().postDelayed({ (activity!!.application as GoChatApplication).exit() }, 2000)
                            } else
                                etIpNew.error = "ip地址不能为空"
                        }
                        R.id.btn_ip_cancel -> {
                            ipDialog.setView(view)
                            ipDialog.show()
                        }
                    }
                }
            }
        }
    }
}