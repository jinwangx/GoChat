package com.jw.business.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.business.model.bean.Contact

@Dao
interface FriendDao {

    @Query("select * from friend where 'owner'=:owner")
    fun queryFriends(owner: String):Cursor

    @Query("select * from friend where 'owner'=:owner and 'account'=:account")
    fun queryFriendByAccount(owner: String, account: String): Contact

    @Insert
    fun addFriend(friend: Contact)

    @Update
    fun updateFriend(friend: Contact)

    @Query("update friend set 'icon' =:iconPath where 'account'=:account")
    fun updateFriend(account: String, iconPath: String)

}