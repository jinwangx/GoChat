package com.jw.chat

interface GoChatFuture {
    val isCancelled: Boolean
    val isFinished: Boolean
    fun cancel(mayInterruptIfRunning: Boolean): Boolean
}