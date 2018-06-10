package com.jw.chat.callback

import java.io.File

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:46
 * 版本：
 * 作者：Mr.jin
 * 描述：文件下载回调接口，本项目虽用到头像下载，但图片的下载由Glide控制
 */

abstract class GoChatFileCallBack {
    abstract fun onSuccess(file: File)
    abstract fun onProgress(progress: Int)
    abstract fun onError(error: Int, msg: String)
}