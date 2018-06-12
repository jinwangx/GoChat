package com.jw.chat.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.jw.business.db.dao.*
import com.jw.business.model.bean.Account
import com.jw.business.model.bean.BackTask
import com.jw.business.model.bean.Contact
import com.jw.business.model.bean.Invitation
import com.jw.chat.db.dao.ConversationDao
import com.jw.chat.db.dao.MessageDao
import com.jw.gochatbase.BaseApplication


/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/12.
 * Description : 描述
 */
@Database(entities = [Message::class,ConversationDao], version = 1, exportSchema = false)
abstract class ChatDataBase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao

    companion object {

        private var INSTANCE: ChatDataBase? = null

        @Synchronized
        fun getInstance(context: Context): ChatDataBase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.applicationContext,
                    ChatDataBase::class.java, "gochat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }

        @Synchronized
        fun getInstance(): ChatDataBase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(BaseApplication.getContext()!!.applicationContext,
                    ChatDataBase::class.java, "gochat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }
    }
}