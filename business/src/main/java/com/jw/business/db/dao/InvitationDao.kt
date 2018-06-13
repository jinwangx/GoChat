package com.jw.business.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.business.db.GCDB
import com.jw.business.model.bean.Invitation

@Dao
interface InvitationDao {

    @Query("select * from invitation where 'owner'=:owner")
    fun getInvitationByOwner(owner: String): Cursor?

    @Query("select * from invitation where 'owner'=:owner and invitator_account=:account")
    fun getInvitationByAccount(owner: String, account: String): Invitation?

    @Query("select count('_id') from invitation where 'owner'=:owner and 'agree'=0")
    fun getUnAgreeCount(owner: String): Int

    @Insert
    fun addInvitation(invitation: Invitation):Long

    @Update
    fun updateInvitation(invitation: Invitation):Int
}