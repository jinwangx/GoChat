package com.jw.gochat.service

import android.content.Intent
import android.util.Log
import com.jw.business.business.BackTaskBusiness
import com.jw.business.db.AppDatabase
import com.jw.business.model.bean.NetTask
import com.jw.business.db.GCDB
import com.jw.business.db.dao.BackTaskDao
import com.jw.chat.GoChat
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
    private val me = ChatApplication.getAccountInfo()
    private var headers: HashMap<String, String>? = null
    override fun onHandleIntent(intent: Intent?) {

        GoChatManager.getInstance(ChatApplication.getOkHttpClient()).initAccount(me.account!!,
                me.token!!)
        headers = HashMap()
        Log.v("attt", me.account + "-" + me.token)
        headers!!["account"] = me.account!!
        headers!!["token"] = me.token!!
        val dao = AppDatabase.getInstance(GoChat.getContext()).backTaskDao()
        val map = HashMap<Long, String>()
        val owner = me.account
        val cursor = BackTaskBusiness.getTaskByStateOfOwner(owner!!, 0)
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
                    doNormalTask(id, task)
                } else if (type == NetTask.TYPE_UPLOAD) {
                    doUploadTask(task)
                } else if (type == NetTask.TYPE_DOWNLOAD) {
                    doDownloadTask(task)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun doNormalTask(id: Long?, task: NetTask) {
        val result = HttpUtils.getInstance().post(GoChatURL.BASE_HTTP + task.path!!, headers,
                task.params)
        if (result) {
            Log.v("backGroundService", task.type.toString() + "任务后台执行成功")
            BackTaskBusiness.updateTaskStateById(id!!, 2)
        }
    }

    private fun doUploadTask(task: NetTask) {
        val files = task.files
        if (files != null) {
            HttpUtils.getInstance().upload(GoChatURL.BASE_HTTP + task.path!!, headers, task.params,
                    task.files)
        }
    }

    private fun doDownloadTask(task: NetTask) {
        val files = task.files
        if (files != null) {
            HttpUtils.getInstance().download(GoChatURL.BASE_HTTP + task.path!!, headers, task.params,
                    task.files)
        }
    }
}