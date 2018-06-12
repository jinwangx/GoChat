package com.jw.business.model.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：后台任务javaBean类，存在于本地数据库，用于有网络时自动执行存储的任务
 * 如上传头像，更改用户名
 */

@Entity(tableName = "back_task")
class BackTask {
    @PrimaryKey
    @NotNull
    var id: Long = 0
    @ColumnInfo(name = "owner")
    var owner: String? = null
    @ColumnInfo(name = "path")
    var path: String? = null
    @ColumnInfo(name = "state")
    var state: Int = 0
}