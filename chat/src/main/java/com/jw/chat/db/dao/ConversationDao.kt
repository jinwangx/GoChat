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
    fun insert(conversation:Conversation)

    @Update
    fun update(conversation:Conversation)

    @Query("update conversation set 'unread'=:count where 'owner' =:owner and 'account'=:account")
    fun update(owner: String, account: String,count:Int): Cursor

    @Query("select sum(unread) from conversation where 'owner'=:owner")
    fun getUnread(owner: String):Cursor
}