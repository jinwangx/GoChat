package com.jw.business.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jw.business.db.dao.AccountInfoDao
import com.jw.business.db.dao.BackTaskDao
import com.jw.business.db.dao.FriendDao
import com.jw.business.db.dao.InvitationDao
import com.jw.business.db.model.AccountInfo
import com.jw.business.db.model.BackTask
import com.jw.business.db.model.Friend
import com.jw.business.db.model.Invitation
import com.jw.gochatbase.BaseApplication


/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/12
 * 描述 : 数据库创建(业务)
 */
@Database(entities = [AccountInfo::class, (BackTask::class), (Friend::class), (Invitation::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountInfoDao
    abstract fun backTaskDao(): BackTaskDao
    abstract fun friendDao(): FriendDao
    abstract fun invitationDao(): InvitationDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "gochat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }

        @Synchronized
        fun getInstance(): AppDatabase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(BaseApplication.getContext()!!.applicationContext,
                    AppDatabase::class.java, "gochat.db")
                    .allowMainThreadQueries()
                    .build()
            return INSTANCE!!
        }
    }
}