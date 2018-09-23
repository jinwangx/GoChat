package com.jw.contact.model

import com.jw.contact.presenter.SearchResultLitener

/**
 * 创建时间:2018/9/18 on 14:26
 * 创建人:jinwangx
 * 描述:
 */
interface SearchModel {
    fun searchFriend(friendAccount: String, listener: SearchResultLitener)
}