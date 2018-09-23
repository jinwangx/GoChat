package com.sencent.library.base.activity

/**
 * 由huoqisheng于2016/9/5创建
 */
interface IActivityOtherFragmentData {
    fun onReceiveOtherFragmentData(senderTag: String, receiverTag: String,
                                   name: String, data: Any?)
}