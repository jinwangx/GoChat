package com.jw.business.business

import com.jw.business.db.AppDatabase
import com.jw.business.model.bean.BackTask

/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/13.
 * Description : 描述
 */
object BackTaskBusiness {
    fun getTaskByOwner(owner: String) = AppDatabase.getInstance().backTaskDao().getTaskByOwner(owner)

    fun getTaskByStateOfOwner(owner: String, state: Int) = AppDatabase.getInstance().backTaskDao().getTaskByState(owner, state)

    fun insert(task: BackTask) {
        AppDatabase.getInstance().backTaskDao().insert(task)
    }

    fun update(task: BackTask) {
        AppDatabase.getInstance().backTaskDao().update(task)
    }

    fun updateTaskStateById(id: Long, state: Int) {
        AppDatabase.getInstance().backTaskDao().updateTaskStateById(id, state)
    }
}