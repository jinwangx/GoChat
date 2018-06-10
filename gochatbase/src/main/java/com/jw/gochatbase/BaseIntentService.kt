package com.jw.gochatbase

import android.app.IntentService

/**
 * 创建时间：
 * 更新时间 2017/11/4 19:39
 * 版本：
 * 作者：Mr.jin
 * 描述：IntentService在主线程中运行，可多次创建执行，执行完后自动销毁
 */

abstract class BaseIntentService(name: String) : IntentService(name) {

    override fun onCreate() {
        super.onCreate()
        //((ChatApplication) getApplication()).addService(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        //((ChatApplication) getApplication()).removeService(this);
    }
}