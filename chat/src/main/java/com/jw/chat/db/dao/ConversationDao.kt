package com.jw.chat.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.chat.db.bean.Conversation

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 数据库操作(会话消息)
 */
@Dao
interface ConversationDao {

    @Insert
    fun insert(conversation: Conversation): Long

    @Update
    fun update(conversation: Conversation): Int

    @Query("select * from conversation where owner=:owner")
    fun query(owner: String): Cursor?

    @Query("select * from conversation where owner=:owner and account=:account")
    fun query(owner: String, account: String): Conversation?

    @Query("update conversation set unread_count=:unread_count where owner =:owner")
    fun update(owner: String, unread_count: Int): Int

    @Query("update conversation set unread_count=:unread_count where owner =:owner and account=:account")
    fun update(owner: String, account: String, unread_count: Int): Int

    @Query("select sum(unread_count) from conversation where owner=:owner")
    fun getUnreadByOwner(owner: String): Int

    @Query("select unread_count from conversation where owner=:owner and account=:account")
    fun getUnreadByAccount(owner: String, account: String): Int

}