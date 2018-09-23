package com.jw.contact.activity

import com.jw.business.db.model.Friend

/**
 * 创建时间:2018/9/18 on 14:33
 * 创建人:jinwangx
 * 描述:
 */
interface SearchView {
    fun showError(msg: String)
    fun showSuccess(friend: Friend)
    fun showLoading()
}