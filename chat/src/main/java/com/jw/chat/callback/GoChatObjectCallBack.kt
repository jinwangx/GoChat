package com.jw.chat.callback

import java.lang.reflect.ParameterizedType

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:40
 * 版本：
 * 作者：Mr.jin
 * 描述：网络请求回调，支持泛型，如果成功，可以将访问到的数据转化为javaBean对象，
 * 失败则返回错误代码和信息
 */

abstract class GoChatObjectCallBack<T> {
    val clazz: Class<T>
    init {
        val type = this.javaClass
                .genericSuperclass as ParameterizedType
        this.clazz = type.actualTypeArguments[0] as Class<T>
    }
    abstract fun onSuccess(t: T)
    abstract fun onError(error: Int, msg: String)
}