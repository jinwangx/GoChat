package com.jw.gochat

import okhttp3.Interceptor

/**
 * Created by liyuan on 16/11/17.
 */
object HttpUtil {

    /**
     * A empty okhttp interceptor
     */
    val EMPTY_INTERCEPTOR = Interceptor { chain: Interceptor.Chain -> chain.proceed(chain.request()) }

}