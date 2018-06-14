package com.jw.chat.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.chat.db.bean.Message

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 数据库操作(聊天信息)
 */
@Dao
interface MessageDao {

    @Insert
    fun insert(message: Message): Long

    @Update
    fun update(message: Message): Int

    @Query("update message set read=:isRead where owner =:owner and account=:account")
    fun update(owner: String, account: String, isRead: Boolean): Int

    @Query("update message set read=:isRead where owner =:owner")
    fun update(owner: String, isRead: Boolean): Int

    @Query("select * from message where owner =:owner and account=:account order by create_time asc")
    fun query(owner: String, account: String): Cursor

    @Query("select count(_id) from message where read=0 and owner=:owner")
    fun getUnreadCountByOwner(owner: String): Int

    @Query("select count(_id) from message where read=0 and account=:account and owner=:owner")
    fun getUnreadCountByAccount(owner: String, account: String): Int

    @Query("select * from message where owner =:owner  order by create_time desc")
    fun queryAllByOwner(owner: String): Cursor

}