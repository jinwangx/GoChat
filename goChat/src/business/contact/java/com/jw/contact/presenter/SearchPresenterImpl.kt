package com.jw.contact.presenter

import com.jw.business.db.model.Friend
import com.jw.contact.model.SearchModelImpl
import com.jw.contact.activity.SearchView

/**
 * 创建时间:2018/9/18 on 14:39
 * 创建人:jinwangx
 * 描述:
 */
class SearchPresenterImpl(var searchView: SearchView?) : SearchPresenter, SearchResultLitener {
    private var searchModel = SearchModelImpl()

    override fun onSuccess(friend: Friend) {
        searchView?.showSuccess(friend)
    }

    override fun onError(msg: String) {
        searchView?.showError(msg)
    }

    override fun validate(friendAccount: String) {
        searchView?.showLoading()
        searchModel.searchFriend(friendAccount, this)
    }

    override fun onDestroy() {
        searchView = null
    }
}