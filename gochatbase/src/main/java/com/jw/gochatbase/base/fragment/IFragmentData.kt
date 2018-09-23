package com.sencent.library.base.fragment

/**
 * 由 jinwangx 创建于 2017/12/8.
 */
interface IFragmentData {
    /**
     * 接受并处理来自Activity的消息
     *
     * @param name 消息名称
     * @param data 消息内容
     */
    fun onReceiveDataFromActivity(name: String, data: Any?)

    /**
     * 对Activity发送数据
     *
     * @param name 消息名称
     * @param data 消息内容
     */
    fun sendDataToActivity(name: String, data: Any?)

    fun onQueryDataFromActivity(name: String): Any?

    fun queryActivityData(name: String): Any?
}