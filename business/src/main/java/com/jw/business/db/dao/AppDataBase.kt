package com.jw.business.db.dao

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.jw.business.model.bean.Account
import com.jw.business.model.bean.BackTask
import com.jw.business.model.bean.Contact
import com.jw.business.model.bean.Invitation
import com.jw.gochatbase.BaseApplication


/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/12.
 * Description : 描述
 */
@Database(entities = [Account::class, (BackTask::class), (Contact::class), (Invitation::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
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