package com.jw.contact.presenter

import com.jw.business.db.model.Friend

/**
 * 创建时间:2018/9/18 on 14:28
 * 创建人:jinwangx
 * 描述:
 */
interface SearchResultLitener {
    fun onSuccess(friend:Friend)
    fun onError(msg:String)
}