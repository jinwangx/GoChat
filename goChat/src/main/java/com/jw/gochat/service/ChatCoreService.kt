package com.jw.gochat.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.IBinder
import android.util.Log
import com.jw.business.db.AppDatabase
import com.jw.chat.GoChat
import com.jw.chat.GoChatManager
import com.jw.chat.core.PacketConnector
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.action.Action
import com.jw.gochatbase.BaseService
import com.jw.library.utils.NetUtils
import com.jw.library.utils.ThemeUtils
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/1 21:41
 * 版本：
 * 作者：Mr.jin
 * 描述：
 * 1.该服务第一次启动时，实例化各个Action,并注册网络状态广播
 * 2.若网络已连接，才会去尝试开启核心服务。
 * 3.如果有本地账户，GoChatManager类对服务器的连接进行各种监听，并且客户端通过tcp通道开始连接服务器进行认证
 * 4.同时开启后台服务，向服务器提交之前未完成的网络任务
 */

class ChatCoreService : BaseService(), PacketConnector.ConnectListener, GoChatManager.OnPushListener {
    private var chatManager: GoChatManager? = null

    private var reconnectCount = 0// 重连次数

    private val actionMaps = HashMap<String, Action>()

    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                if (NetUtils.isNetConnected(this@ChatCoreService)) {
                    //ThemeUtils.show(GoChat.getContext(),"网络已连接");
                    connectServer()
                } else {
                    ThemeUtils.show(GoChat.getContext(), "没有网络")
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("GoChat_Core", "聊天引擎创建了")
        // 注册网络监听
        val mFilter = IntentFilter()
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mReceiver, mFilter)
        scanClass()
    }

    private fun connectServer() {
        Log.d("GoChat_Core", "正在连接服务器")
        val account = AppDatabase.getInstance(GoChat.getContext()).accountDao().getCurrentAccountInfo()
        if (account != null) {
            chatManager = GoChatManager.getInstance(ChatApplication.getOkHttpClient())
            chatManager!!.addConnectionListener(this)
            chatManager!!.setPushListener(this)
            chatManager!!.auth(account.account!!, account.token!!)

            // 后台服务开启
            startService(Intent(this, BackgroundService::class.java))
        }
    }

    override fun onConnecting() {
        Log.d("GoChat_Core", "正在连接")
    }

    override fun onConnected() {
        reconnectCount = 0
        Log.d("GoChat_Core", "连接成功")
    }

    override fun onReConnecting() {
        Log.d("GoChat_Core", "正在重连")
    }

    override fun onDisconnected() {
        Log.d("GoChat_Core", "连接断开")

        if (NetUtils.isNetConnected(this@ChatCoreService)) {
            // 有网络的
            Log.d("GoChat_Core", "网络已经开启，正在开始第" + ++reconnectCount + "连接")
            if (reconnectCount < 10) {
                connectServer()
            }
        }
    }

    override fun onAuthFailed() {
        Log.d("GoChat_Core", "认证失败")
    }

    private fun scanClass() {
        Log.d("GoChat_Core", "正在实例化各个Action")
        val array = resources.getStringArray(R.array.actions) ?: return
        val packageName = packageName
        val classLoader = classLoader
        for (i in array.indices) {
            try {
                val clazz = classLoader.loadClass(packageName + "."
                        + array[i])
                val superclass = clazz.superclass
                if (superclass != null && Action::class.java.name == superclass.name) {
                    val action = clazz.newInstance() as Action
                    actionMaps[action.action] = action
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /**
     * 成功接收到推送的消息，开始执行数据库操作，数据库操作执行完后，发送广播给ui,页面更新
     * @param action
     * @param data
     * @return
     */
    override fun onPush(action: String, data: Map<String, Any>): Boolean {
        val actioner = actionMaps[action]
        actioner?.doAction(this, data)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("GoChat_Core", "onDestroy")
        unregisterReceiver(mReceiver)
        // 断开连接
        chatManager!!.closeSocket()
        chatManager!!.removeConnectionListener(this)
    }
}