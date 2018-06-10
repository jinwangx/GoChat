package com.jw.gochat.service

import android.content.Intent
import android.util.Log
import com.jw.business.bean.NetTask
import com.jw.business.db.GCDB
import com.jw.business.db.dao.BackTaskDao
import com.jw.chat.GoChatManager
import com.jw.chat.GoChatURL
import com.jw.gochat.ChatApplication
import com.jw.gochatbase.BaseIntentService
import com.jw.library.utils.FileUtils
import com.jw.library.utils.HttpUtils
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/10/30 0:23
 * 版本：
 * 作者：Mr.jin
 * 描述：后台服务，可执行多次，每次任务执行完后自动销毁，用于执行后台任务
 */

class BackgroundService : BaseIntentService("background") {
    private val me = ChatApplication.getAccount()
    private var headers: HashMap<String, String>? = null
    override fun onHandleIntent(intent: Intent?) {

        GoChatManager.getInstance(ChatApplication.getOkHttpClient()).initAccount(me.account,
                me.token)
        headers = HashMap()
        Log.v("attt", me.account + "-" + me.token)
        headers!!["account"] = me.account!!
        headers!!["token"] = me.token!!
        val dao = BackTaskDao(this)
        val map = HashMap<Long, String>()
        val owner = me.account
        val cursor = dao.query(owner!!, 0)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor
                        .getColumnIndex(GCDB.BackTask.COLUMN_ID))
                val filePath = cursor.getString(cursor
                        .getColumnIndex(GCDB.BackTask.COLUMN_PATH))
                map[id] = filePath
            }
            cursor.close()
        }

        for ((id, filePath) in map) {
            try {
                val task = FileUtils.read(filePath) as NetTask
                val type = task.type
                if (type == NetTask.TYPE_NORMAL) {
                    doNormalTask(dao, id, task)
                } else if (type == NetTask.TYPE_UPLOAD) {
                    doUploadTask(dao, id, task)
                } else if (type == NetTask.TYPE_DOWNLOAD) {
                    doDownloadTask(dao, id, task)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun doNormalTask(dao: BackTaskDao, id: Long?, task: NetTask) {
        val result = HttpUtils.getInstance().post(GoChatURL.BASE_HTTP + task.path!!, headers,
                task.params)
        if (result) {
            Log.v("backGroundService", task.type.toString() + "任务后台执行成功")
            dao.updateState(id!!, 2)
        }
    }

    private fun doUploadTask(dao: BackTaskDao, id: Long?, task: NetTask) {
        val files = task.files
        if (files != null) {
            HttpUtils.getInstance().upload(GoChatURL.BASE_HTTP + task.path!!, headers, task.params,
                    task.files)
        }
    }

    private fun doDownloadTask(dao: BackTaskDao, id: Long?, task: NetTask) {
        val files = task.files
        if (files != null) {
            HttpUtils.getInstance().download(GoChatURL.BASE_HTTP + task.path!!, headers, task.params,
                    task.files)
        }
    }
}