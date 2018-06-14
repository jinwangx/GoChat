package com.jw.gochat;

import android.app.Application;
import android.util.Log;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jw.business.business.AccountInfoBusiness;
import com.jw.business.db.model.AccountInfo;
import com.jw.chat.GoChat;
import com.jw.gochatbase.BaseApplication;
import com.jw.library.utils.HttpUtils;
import okhttp3.OkHttpClient;

public class ChatApplication extends BaseApplication {
    private static AccountInfo accountInfo;
    private static OkHttpClient client;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(StethoStub.getInterceptor())
                .build();
        application = this;
        Log.d("ChatApplication", "init");
        // 初始化应用全局对象，即(ChatApplication)
        GoChat.getInstance().init(this);
        HttpUtils.init(client);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59f0548e");
        Stetho.initializeWithDefaults(this);
    }

    public static Application getApplication() {
        return application;
    }

    public static OkHttpClient getOkHttpClient() {
        return client;
    }

    public static AccountInfo getAccountInfo(){
        if (accountInfo == null) {
            accountInfo = AccountInfoBusiness.INSTANCE.getCurrentAccountInfo();
        }
        return accountInfo;
    }
}
