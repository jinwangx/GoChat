package com.jw.business.business

import com.jw.business.db.AppDatabase
import com.jw.business.model.bean.Friend

/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/13.
 * Description : 描述
 */
object FriendBusiness {

    /**
     * 根据拥有者查找好友列表
     * @param owner String
     * @return Cursor
     */
    fun getFriendAll(owner: String) = AppDatabase.getInstance().friendDao().getFriendAll(owner)

    /**
     * 查找拥有者对应id的好友
     * @param owner String
     * @param account String
     * @return Friend
     */
    fun getFriendById(owner: String, account: String) = AppDatabase.getInstance().friendDao().getFriendByAccount(owner, account)

    /**
     * 新增好友
     * @param friend Friend
     */
    fun insert(friend: Friend) {
        AppDatabase.getInstance().friendDao().insert(friend)
    }

    /**
     * 更新好友
     * @param friend Friend
     */
    fun update(friend: Friend) {
        AppDatabase.getInstance().friendDao().update(friend)
    }

    /**
     * 更新好友的头像存储地址
     * @param account String
     * @param iconPath String
     */
    fun updateFriend(owner:String,account: String, iconPath: String) {
        AppDatabase.getInstance().friendDao().updateFriend(owner,account, iconPath)
    }

}