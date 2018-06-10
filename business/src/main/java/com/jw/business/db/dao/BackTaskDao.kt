package com.jw.business.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.jw.business.bean.BackTask
import com.jw.business.db.GCDB
import com.jw.business.db.GCDBOpenHelper

class BackTaskDao(context: Context) {
    private val helper: GCDBOpenHelper = GCDBOpenHelper.getInstance(context)

    fun addTask(task: BackTask) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.BackTask.COLUMN_OWNER, task.owner)
        values.put(GCDB.BackTask.COLUMN_PATH, task.path)
        values.put(GCDB.BackTask.COLUMN_STATE, task.state)
        task.id = db.insert(GCDB.BackTask.TABLE_NAME, null, values)
    }

    fun updateTask(task: BackTask) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.BackTask.COLUMN_OWNER, task.owner)
        values.put(GCDB.BackTask.COLUMN_PATH, task.path)
        values.put(GCDB.BackTask.COLUMN_STATE, task.state)
        val whereClause = GCDB.BackTask.COLUMN_ID + "=?"
        val whereArgs = arrayOf(task.id.toString() + "")
        db.update(GCDB.BackTask.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun updateState(id: Long, state: Int) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.BackTask.COLUMN_STATE, state)
        val whereClause = GCDB.BackTask.COLUMN_ID + "=?"
        val whereArgs = arrayOf(id.toString() + "")
        db.update(GCDB.BackTask.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun query(owner: String): Cursor {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.BackTask.TABLE_NAME + " where "
                + GCDB.BackTask.COLUMN_OWNER + "=?")
        return db.rawQuery(sql, arrayOf(owner))
    }

    fun query(owner: String, state: Int): Cursor {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.BackTask.TABLE_NAME + " where "
                + GCDB.BackTask.COLUMN_OWNER + "=? and "
                + GCDB.BackTask.COLUMN_STATE + "=?")
        return db.rawQuery(sql, arrayOf(owner, "0"))
    }
}