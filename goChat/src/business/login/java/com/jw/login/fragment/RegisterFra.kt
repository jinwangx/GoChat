package com.jw.login.fragment

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.jw.business.business.AccountInfoBusiness
import com.jw.business.db.model.AccountInfo
import com.jw.chat.IMClient
import com.jw.gochat.R
import com.jw.gochat.databinding.FragmentRegisterBinding
import com.jw.gochat.http.ScHttpClient
import com.jw.gochat.http.service.GoChatService
import com.jw.gochat.view.DialogLoading
import com.jw.gochat.view.NormalTopBar
import com.jw.library.utils.ThemeUtils
import com.sencent.mm.GoChatBindingFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：用户注册页面
 */

class RegisterFra : GoChatBindingFragment<FragmentRegisterBinding>(), NormalTopBar.BackListener {
    private var dialog: DialogLoading? = null

    override fun getLayoutId() = R.layout.fragment_register

    override fun doConfig(arguments: Bundle?) {
        val binding = binding
        binding!!.ntRegister.setBackListener(this)
        binding.apply {
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.btn_register -> {
                        val account = binding.etRegisterAct.text.toString()
                        val password = binding.etRegisterPsw.text.toString()
                        dialog = DialogLoading(activity)
                        dialog!!.show()
                        ScHttpClient.getService(GoChatService::class.java).register(account, password)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    val accountInfo = Gson().fromJson<AccountInfo>(it.getString("data"), AccountInfo::class.java)
                                    // 初始化用户连接安全信息
                                    IMClient.getInstance().initAccount(accountInfo.account, accountInfo.token)
                                    // 存储用户
                                    accountInfo.current = 1
                                    AccountInfoBusiness.insert(accountInfo)
                                    dialog!!.dismiss()
                                    sendDataToActivity("register", null)
                                }) {
                                    dialog!!.dismiss()
                                    ThemeUtils.show(activity, it.message)
                                }
                    }
                }
            }
        }
    }

    override fun back() {
        fragmentManager!!.popBackStack()
    }
}