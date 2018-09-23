package com.jw.contact.model

import com.google.gson.Gson
import com.jw.business.business.FriendBusiness
import com.jw.business.db.model.Friend
import com.jw.contact.presenter.SearchResultLitener
import com.jw.gochat.GoChatApplication
import com.jw.gochat.http.service.GoChatService
import com.jw.gochat.http.ScHttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 创建时间:2018/9/18 on 14:29
 * 创建人:jinwangx
 * 描述:
 */
class SearchModelImpl : SearchModel {
    override fun searchFriend(friendAccount: String, listener: SearchResultLitener) {
        val me = GoChatApplication.getAccountInfo()!!
        if (me.account == friendAccount) {
            listener.onError("不要找自己啦")
            return
        }
        // 已有的朋友
        val friend = FriendBusiness.getFriendById(me.account!!, friendAccount)
        if (friend != null) {
            listener.onError("已经存在该朋友")
        } else {
            ScHttpClient.getService(GoChatService::class.java)
                    .searchAccount(me.account!!, me.token!!, friendAccount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val friend1 = Gson().fromJson<Friend>(it.getString("data"), Friend::class.java)
                        listener.onSuccess(friend1)
                    }) {
                        listener.onError(it.message!!)
                    }
        }
    }
}