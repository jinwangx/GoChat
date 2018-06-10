package com.jw.business.bean

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：后台任务javaBean类，存在于本地数据库，用于有网络时自动执行存储的任务
 * 如上传头像，更改用户名
 */

class BackTask {
    var id: Long = 0
    var owner: String? = null
    var path: String? = null
    var state: Int = 0
}