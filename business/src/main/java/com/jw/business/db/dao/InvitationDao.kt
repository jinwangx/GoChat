package com.jw.business.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.business.model.bean.Invitation

@Dao
interface InvitationDao {

    @Query("select * from invitation where 'owner'=:owner")
    fun queryCursor(owner: String): Cursor

    @Query("select * from invitation where 'owner'=:owner and 'account'=:account")
    fun queryInvitation(owner: String, account: String): Invitation?

    @Insert
    fun addInvitation(invitation: Invitation)

    @Update
    fun updateInvitation(invitation: Invitation)

/*    @Query("select count('_id') from invitation where 'agree' =0")
    fun hasUnagree(owner: String): Boolean*/


/*    fun hasUnagree(owner: String): Boolean {
        val db = helper.writableDatabase
        val sql = ("select count(" + GCDB.Invitation.COLUMN_ID + ") from "
                + GCDB.Invitation.TABLE_NAME + " where "
                + GCDB.Invitation.COLUMN_AGREE + "=0")
        val cursor = db.rawQuery(sql, null)
        var count = 0
        if (cursor != null) {
            if (cursor.moveToNext()) {
                count = cursor.getInt(0)
            }
            cursor.close()
        }
        return count != 0
    }*/
}