package com.jw.chat.future

import com.jw.chat.GoChatFuture
import okhttp3.Call

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:28
 * 版本：
 * 作者：Mr.jin
 * 描述：Http请求返回的future，可以操作网络请求，如取消请求等
 */

class HttpFuture(private val call: Call?) : GoChatFuture {
    override val isCancelled: Boolean
        get() = call == null || call.isCanceled
    override val isFinished: Boolean
        get() = call == null || call.isExecuted
    //return call == null || call.cancel(mayInterruptIfRunning);
    override fun cancel(mayInterruptIfRunning: Boolean)=mayInterruptIfRunning
}