package com.jw.gochat

import android.app.Application
import com.facebook.stetho.Stetho
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.jw.business.business.AccountInfoBusiness
import com.jw.business.db.model.AccountInfo
import com.jw.gochat.http.ScHttpClient
import com.jw.gochat.http.ScHttpConfig
import com.jw.gochatbase.gochat.GoChat
import com.jw.gochatbase.gochat.GoChatURL
import com.jw.library.utils.HttpUtils
import com.jw.uilibrary.base.application.BaseApplication

class GoChatApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        application = this
        //初始化应用上下文
        GoChat.getInstance().init(this)
        //初始化http请求引擎
        ScHttpClient.init(ScHttpConfig.create().setBaseUrl(GoChatURL.BASE_HTTP))
        HttpUtils.init(ScHttpClient.getOkHttpClient())
        //初始化语音识别sdk
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59f0548e")
        //stetho调试集成
        Stetho.initializeWithDefaults(this)
    }

    companion object {
        var application: Application? = null
            private set

        fun getAccountInfo(): AccountInfo? {
            return AccountInfoBusiness.getCurrentAccountInfo()
        }
    }
}
