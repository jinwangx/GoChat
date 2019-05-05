package com.jw.chat.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message
import com.jw.chat.db.dao.ConversationDao
import com.jw.chat.db.dao.MessageDao
import com.jw.uilibrary.base.application.BaseApplication


/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/12
 * 描述 : 数据库创建(聊天信息、会话)
 */
@Database(entities = [Message::class, Conversation::class], version = 1, exportSchema = false)
abstract class ChatDataBase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao

    companion object {

        private var INSTANCE: ChatDataBase? = null

        @Synchronized
        fun getInstance(context: Context): ChatDataBase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.applicationContext,
                    ChatDataBase::class.java, "chat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }

        @Synchronized
        fun getInstance(): ChatDataBase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(BaseApplication.getContext()!!.applicationContext,
                    ChatDataBase::class.java, "chat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }
    }
}