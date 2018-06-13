package com.jw.login.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jw.business.business.AccountInfoBusiness
import com.jw.business.model.bean.AccountInfo
import com.jw.chat.GoChatManager
import com.jw.chat.GoChatURL
import com.jw.chat.callback.GoChatObjectCallBack
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.activity.HomeActivity
import com.jw.gochat.databinding.FragmentLoginBinding
import com.jw.gochat.utils.CacheUtils
import com.jw.gochat.view.DialogSheet
import com.jw.gochatbase.BaseFragment
import com.jw.library.utils.ThemeUtils
import com.jw.login.LoginActivity

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：登陆页面
 */

class LoginFra : BaseFragment(), View.OnClickListener, TextWatcher {

    internal var dialog: Dialog? = null
    private var mBinding: FragmentLoginBinding? = null

    /**
     * 登陆请求监听，如果成功，保存账户，进入主界面HomeActivity
     */
    private val loginCallBack = object : GoChatObjectCallBack<AccountInfo>() {
        override fun onSuccess(accountInfo: AccountInfo) {
            accountInfo.current = true
            accountInfo.icon = GoChatURL.BASE_HTTP + accountInfo.icon!!
                    .replace("\\", "/")
            // 初始化用户连接安全信息
            GoChatManager.getInstance(ChatApplication.getOkHttpClient()).initAccount(
                    accountInfo.account!!, accountInfo.token!!)
            if (AccountInfoBusiness.getAccountInfoByAccount(accountInfo.account!!) != null)
                AccountInfoBusiness.update(accountInfo)
            else
                AccountInfoBusiness.insert(accountInfo)
            startActivity(Intent(activity, HomeActivity::class.java))
        }

        override fun onError(error: Int, msg: String) {
            ThemeUtils.show(activity, "错误码:$error,错误信息:$msg")
        }
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
            val etIp = view.findViewById<View>(R.id.et_ip_new) as EditText
            val btnIpOk = view.findViewById<View>(R.id.btn_ip_ok) as Button
            val btnIpCancel = view.findViewById<View>(R.id.btn_ip_cancel) as Button
            btnIpOk.setOnClickListener {
                val ipHost = etIp.text.toString()
                if (!TextUtils.isEmpty(ipHost)) {
                    ipDialog.dismiss()
                    dialog!!.dismiss()
                    CacheUtils.setCache("BASE_QQ_HOST", ipHost, activity as Context)
                    ThemeUtils.show(activity, "应用将在2s后关闭,请重新启动以完成ip初始化")
                    android.os.Handler().postDelayed({ (activity!!.application as ChatApplication).exit() }, 2000)
                } else
                    etIp.error = "ip地址不能为空"
            }
            btnIpCancel.setOnClickListener { ipDialog.dismiss() }
            ipDialog.setView(view)
            ipDialog.show()
        }
    }

    public override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_login, null, false)
        return mBinding!!.root
    }

    override fun initView() {
        Glide.with(this).load(R.drawable.default_icon_user).into(mBinding!!.ivLoginIcon)
        mBinding!!.btnLogin.setOnClickListener(this)
        mBinding!!.tvRegister.setOnClickListener(this)
        mBinding!!.tvLoginPswMiss.setOnClickListener(this)
        mBinding!!.etLoginPsw.addTextChangedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                val account = mBinding!!.etLoginAct.text.toString()
                val password = mBinding!!.etLoginPsw.text.toString()
                GoChatManager.getInstance(ChatApplication.getOkHttpClient()).login(account, password, loginCallBack)
            }
            R.id.tv_register -> (activity as LoginActivity).goRegisterFra()
            R.id.tv_login_psw_miss -> {
                val dialogSheet = DialogSheet(context!!)
                dialogSheet.setPassListener(dialogListener)
                dialogSheet.show()//显示对话框
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null) {
            mBinding!!.btnLogin.isEnabled = true
            val account = AccountInfoBusiness.getAccountInfoByAccount(s.toString())
            if (account != null) {
                Glide.with(activity!!).load(account.icon).into(mBinding!!.ivLoginIcon)
            }
        } else
            mBinding!!.btnLogin.isEnabled = false
    }

    override fun afterTextChanged(s: Editable) {}
}