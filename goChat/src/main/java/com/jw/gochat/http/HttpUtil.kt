package com.jw.gochat.http

import okhttp3.Interceptor

/**
 * Created by jinwangx on 16/11/17.
 */
object HttpUtil {

    /**
     * A empty okhttp interceptor
     */
    val EMPTY_INTERCEPTOR = Interceptor { chain: Interceptor.Chain -> chain.proceed(chain.request()) }

}