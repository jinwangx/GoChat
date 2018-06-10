package com.jw.chat.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GCDBOpenHelper private constructor(context: Context) : SQLiteOpenHelper(context, GCDB.NAME, null, GCDB.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GCDB.Account.SQL_CREATE_TABLE)
        db.execSQL(GCDB.Friend.SQL_CREATE_TABLE)
        db.execSQL(GCDB.Invitation.SQL_CREATE_TABLE)
        db.execSQL(GCDB.Message.SQL_CREATE_TABLE)
        db.execSQL(GCDB.Conversation.SQL_CREATE_TABLE)
        db.execSQL(GCDB.BackTask.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private var instance: GCDBOpenHelper? = null
        fun getInstance(context: Context): GCDBOpenHelper {
            if (instance == null) {
                /*			当一个线程访问HMDBOpenHelper的一个synchronized(this)同步代码块时，它就获得了这个HMDBOpenHelper的对象锁。
			结果，其它线程对该HMDBOpenHelper对象所有同步代码部分的访问都被暂时阻塞*/
                synchronized(GCDBOpenHelper::class.java) {
                    if (instance == null) {
                        instance = GCDBOpenHelper(context)
                    }
                }
            }
            return this!!.instance!!
        }
    }
}