package com.jw.business.model

import java.io.Serializable
import java.util.HashMap

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：网络任务，其通过序列化存储在后台任务javaBean类中，后台任务执行时，去除网络任务类
 * 中数据，进行请求(正常请求，上传，下载)
 */

class NetTask : Serializable {
    var id: Long = 0
    var path: String? = null
    var params: HashMap<String, String>? = null
    var files: HashMap<String, String>? = null
    var method = "POST"
    var protocol: String? = null
    var type: Int = 0

    companion object {
        private const val serialVersionUID = 6231882517017053073L
        const val TYPE_NORMAL = 0
        const val TYPE_UPLOAD = 1
        const val TYPE_DOWNLOAD = 2
    }
}