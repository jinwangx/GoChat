package com.jw.chat.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.content.ContentValues
import android.database.Cursor
import android.provider.SyncStateContract.Helpers.update
import com.jw.chat.db.ChatDataBase
import com.jw.chat.db.GCDB
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message

@Dao
interface MessageDao {

    fun addMessage(message: Message) {
        insert(message)
        val cursor=queryMessage(message.owner!!, message.account!!)
        if (cursor.moveToNext()) {
            // 关闭cursor
            cursor.close()
            var unread = 0
            val cursor = queryUnreadCount(message.owner!!, message.account!!)
            if (cursor.moveToNext()) {
                unread = cursor.getInt(0)
            }
            val conversation=Conversation()
            conversation.account=message.account
            conversation.content=if(message.type==0) message.content else "图片"
            conversation.owner=message.owner
            conversation.unread=unread
            conversation.updateTime=System.currentTimeMillis()
            ChatDataBase.getInstance().conversationDao().update(conversation)
        } else {
            val conversation = Conversation()
            conversation.account = message.account
            conversation.content = if(message.type==0) message.content else "图片"
            conversation.owner = message.owner
            conversation.unread = if (message.isRead) 0 else 1
            conversation.updateTime = System.currentTimeMillis()
            ChatDataBase.getInstance().conversationDao().insert(conversation)
        }
    }

    @Insert
    fun insert(message: Message):Int

    @Update
    fun updateMessage(message: Message)

    @Query("update message set 'isRead'=:read where 'owner' =:owner and 'account'=:account")
    fun update(owner: String, account: String,read:Boolean): Cursor

    @Query("select * from message where 'owner' =:owner and 'account'=:account order by 'create_time' asc")
    fun queryMessage(owner: String, account: String): Cursor

    @Query("select count('_id') from message where 'read'=0 and 'account'=:account and 'owner'=:owner")
    fun queryUnreadCount(owner: String, account: String): Cursor

    @Query("select * from message where 'owner' =:owner  order by 'create_time' desc")
    fun queryConversation(owner: String): Cursor

    fun clearUnread(owner: String, account: String) {
        update(owner,account,true)
        ChatDataBase.getInstance().conversationDao().update(owner,account,0)
    }

    fun getAllUnread(owner: String): Int {
        val cursor = ChatDataBase.getInstance().conversationDao().getUnread(owner)
        var sum = 0
        if (cursor != null) {
            if (cursor.moveToNext()) {
                sum = cursor.getInt(0)
            }
            cursor.close()
        }
        return sum
    }
}