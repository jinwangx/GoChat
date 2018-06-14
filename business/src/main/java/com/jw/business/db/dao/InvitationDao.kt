package com.jw.business.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.business.db.model.Invitation

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 数据库操作(邀请信息)
 */
@Dao
interface InvitationDao {

    @Query("select * from invitation where owner=:owner")
    fun getInvitationByOwner(owner: String): Cursor?

    @Query("select * from invitation where owner=:owner and invitator_account=:account limit 0,1")
    fun getInvitationByAccount(owner: String, account: String): Invitation

    @Query("select count(_id) from invitation where owner=:owner and agree=0")
    fun getUnAgreeCount(owner: String): Int

    @Insert
    fun addInvitation(invitation: Invitation):Long

    @Update
    fun updateInvitation(invitation: Invitation):Int
}