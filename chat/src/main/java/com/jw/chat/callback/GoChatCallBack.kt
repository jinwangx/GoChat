package com.jw.chat.callback

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:49
 * 版本：
 * 作者：Mr.jin
 * 描述：本项目通用回调
 */

interface GoChatCallBack {
    fun onSuccess()
    fun onProgress(progress: Int)
    fun onError(error: Int, msg: String)
}