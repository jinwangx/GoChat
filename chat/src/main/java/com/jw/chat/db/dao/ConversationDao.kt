package com.jw.chat.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.content.ContentValues
import android.database.Cursor
import com.jw.chat.db.GCDB
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message

@Dao
interface ConversationDao {

    @Insert
    fun insert(conversation:Conversation):Long

    @Update
    fun update(conversation:Conversation):Int

    @Query("select * from conversation where 'owner'=:owner")
    fun query(owner:String):Cursor?

    @Query("update conversation set 'unread_count'=:unread_count where 'owner' =:owner")
    fun update(owner: String,unread_count:Int): Int

    @Query("update conversation set 'unread_count'=:unread_count where 'owner' =:owner and 'account'=:account")
    fun update(owner: String, account: String,unread_count:Int): Int

    @Query("select sum(unread_count) from conversation where 'owner'=:owner")
    fun getUnreadByOwner(owner: String):Int

    @Query("select unread_count from conversation where 'owner'=:owner and 'account'=:account")
    fun getUnreadByAccount(owner: String, account: String):Int
}