package com.jw.login.fragment

import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Message
import android.view.View
import com.jw.business.business.AccountInfoBusiness
import com.jw.business.model.bean.AccountInfo
import com.jw.chat.GoChatError
import com.jw.chat.GoChatManager
import com.jw.chat.callback.GoChatObjectCallBack
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.FragmentRegisterBinding
import com.jw.gochat.view.DialogLoading
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseFragment
import com.jw.library.utils.ThemeUtils
import com.jw.login.LoginActivity

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：用户注册页面
 */

class RegisterFra : BaseFragment(), View.OnClickListener, NormalTopBar.BackListener {

    private var dialog: DialogLoading? = null
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            SUCCESS -> ThemeUtils.show(activity, "登陆成功")
            ERROR_NET -> ThemeUtils.show(activity, "客户端网络异常")
            ERROR_WEB -> ThemeUtils.show(activity, "服务器异常")
            ERROR_EXISTED -> ThemeUtils.show(activity, "用户已存在")
        }
        false
    })
    private var mBinding: FragmentRegisterBinding? = null

    private val registerCallBack = object : GoChatObjectCallBack<AccountInfo>() {
        override fun onSuccess(accountInfo: AccountInfo) {
            val message = Message.obtain()
            // 初始化用户连接安全信息
            GoChatManager.getInstance(ChatApplication.getOkHttpClient()).initAccount(
                    accountInfo.account, accountInfo.token)
            message.what = SUCCESS
            mHandler.sendMessage(message)
            // 存储用户
            accountInfo.current = 1
            AccountInfoBusiness!!.insert(accountInfo)
            dialog!!.dismiss()
            (activity as LoginActivity).goFillInfoFra()
        }

        override fun onError(error: Int, msg: String) {
            val message = Message.obtain()
            dialog!!.dismiss()
            when (error) {
                GoChatError.ERROR_CLIENT_NET -> message.what = ERROR_NET
                GoChatError.ERROR_SERVER -> message.what = ERROR_WEB
                GoChatError.Register.ACCOUNT_EXIST -> message.what = ERROR_EXISTED
                else -> {
                }
            }
            mHandler.sendMessage(message)
        }
    }

    public override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_register, null, false)
        return mBinding!!.root
    }

    override fun initView() {
        mBinding!!.btnRegister.setOnClickListener(this)
        mBinding!!.ntRegister.setBackListener(this)
    }

    override fun onClick(v: View) {
        val account = mBinding!!.etRegisterAct.text.toString()
        val password = mBinding!!.etRegisterPsw.text.toString()
        dialog = DialogLoading(activity)
        dialog!!.show()
        GoChatManager.getInstance(ChatApplication.getOkHttpClient()).register(account, password, registerCallBack)
    }

    override fun back() {
        fragmentManager!!.popBackStack()
    }

    companion object {
        private const val SUCCESS = 0
        private const val ERROR_NET = 1
        private const val ERROR_WEB = 2
        private const val ERROR_EXISTED = 3
    }
}