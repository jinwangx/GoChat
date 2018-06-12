package com.jw.business.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.jw.business.model.bean.BackTask

@Dao
interface BackTaskDao {

    @Query("select * from back_task where 'owner'=:owner")
    fun query(owner: String): BackTask

    @Query("select * from back_task where 'owner'=:owner and 'state='=:state")
    fun query(owner: String, state: Int):Cursor

    @Insert
    fun addTask(task: BackTask)

    @Update
    fun updateTask(task: BackTask)

    @Query("update back_task set 'state'=:state where '_id'=:id")
    fun update(id: Long, state: Int)

}