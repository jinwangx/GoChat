package com.jw.gochat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jw.gochat.service.IMService
import com.jw.library.utils.ThemeUtils

/**
 * 创建时间：
 * 更新时间 2017/11/1 21:22
 * 版本：
 * 作者：Mr.jin
 * 描述：接受到系统开机的广播后，开启聊天引擎核心服务
 */

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //开启聊天引擎核心服务
        if (!ThemeUtils.isServiceRunning(context, IMService::class.java)) {
            context.startService(Intent(context, IMService::class.java))
        }
    }
}