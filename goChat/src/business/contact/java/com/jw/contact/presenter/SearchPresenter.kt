package com.jw.contact.presenter

/**
 * 创建时间:2018/9/18 on 14:38
 * 创建人:jinwangx
 * 描述:
 */
interface SearchPresenter {
    fun validate(friendAccount: String)
    fun onDestroy()
}