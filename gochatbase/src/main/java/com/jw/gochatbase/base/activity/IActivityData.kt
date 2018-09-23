package com.sencent.library.base.activity

/**
 * 由启圣于2015/6/5创建
 */
interface IActivityData {
    /**
     * 接受并处理来自Fragment的异步消息
     *
     * @param tag 发送消息的Fragment的标签
     * @param name 消息名称
     * @param data 消息内容
     */
    fun onReceiveDataFromFragment(tag: String, name: String, data: Any?)

    /**
     * 对Fragment异步发送数据
     *
     * @param tag 目标Fragment在的标签
     * @param name 消息名称
     * @param data 消息内容
     */
    fun sendDataToFragment(tag: String, name: String, data: Any?)

    /**
     * 对Fragment同步发送查询请求
     *
     * @param tag 目标Fragment在的标签
     * @param name 消息名称
     * @return 消息返回值
     */
    fun queryFragmentData(tag: String, name: String): Any?

    fun onQueryDataFromFragment(tag: String, name: String): Any?
}