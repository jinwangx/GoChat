package com.jw.business.business

import com.jw.business.db.AppDatabase
import com.jw.business.db.model.BackTask

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 业务类(后台任务)
 */
object BackTaskBusiness {
    /**
     * 得到当前账号所有后台任务Cursor
     * @param owner String
     * @return List<BackTask>?
     */
    fun getTaskByOwner(owner: String) = AppDatabase.getInstance().backTaskDao().getTaskByOwner(owner)

    /**
     * 得到当前账号某一状态所有后台任务(执行或者待执行)
     * @param owner String
     * @param state Int
     * @return Cursor?
     */
    fun getTaskByStateOfOwner(owner: String, state: Int) = AppDatabase.getInstance().backTaskDao().getTaskByState(owner, state)

    /**
     * 新增后台任务
     * @param task BackTask
     */
    fun insert(task: BackTask) {
        AppDatabase.getInstance().backTaskDao().insert(task)
    }

    /**
     * 更新后台任务
     * @param task BackTask
     */
    fun update(task: BackTask) {
        AppDatabase.getInstance().backTaskDao().update(task)
    }

    /**
     * 根据id更新后台任务执行状态
     * @param id Long
     * @param state Int
     */
    fun updateTaskStateById(id: Long, state: Int) {
        AppDatabase.getInstance().backTaskDao().updateTaskStateById(id, state)
    }
}