package com.sencent.library.base.fragment

/**
 * 由 jinwangx 创建于 2017/12/8.
 */
interface IOtherFragmentData {
    fun onReceiveDataFromOtherFragment(senderTag: String, name: String, data: Any?)

    fun sendDataToOtherFragment(receiverTag: String, name: String, data: Any?)
}