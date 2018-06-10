package com.jw.chat.core

import android.util.Log
import org.apache.mina.core.service.IoHandler
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.textline.TextLineCodecFactory
import org.apache.mina.transport.socket.SocketSessionConfig
import org.apache.mina.transport.socket.nio.NioSocketConnector
import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.util.concurrent.LinkedBlockingQueue

/**
 * 创建时间：
 * 更新时间 2017/11/1 22:15
 * 版本：
 * 作者：Mr.jin
 * 描述：TCP通信底层Connector类
 */

class PacketConnector
/**
 * 初始化Connector
 * @param host 主机地址
 * @param port    端口号
 * @param threadSize 可供线程数量
 */
(private val host: String, private val port: Int, private val poolSize: Int) {
    /**
     * 是否连接
     * @return
     */
    var isConnected: Boolean = false
        private set
    private var connector: NioSocketConnector? = null
    private var ioSession: IoSession? = null
    private var connectListener: ConnectListener? = null
    private var ioListener: IOListener? = null
    private val requestQueue = LinkedBlockingQueue<ChatRequest>(
            128)

    /**
     * 开始连接
     */
    fun connect() {
        if (isConnected) {
            return
        }
        var count = 0
        while (count < 3) {
            try {
                // 对外告知重连
                if (count > 0 && connectListener != null) {
                    connectListener!!.onReConnecting()
                } else if (count == 0 && connectListener != null) {
                    connectListener!!.onConnecting()
                }
                if (connector == null)
                    connector = NioSocketConnector()
                // 设置超时时间
                connector!!.connectTimeoutMillis = (30 * 1000).toLong()
                // 设置过滤链
                val filterChain = connector!!
                        .filterChain
                filterChain.addLast("codec", ProtocolCodecFilter(
                        TextLineCodecFactory(Charset.forName("utf-8"))))
                // 设置监听
                connector!!.handler = PacketHandler()

                val future = connector!!.connect(InetSocketAddress(
                        host, port))
                future.awaitUninterruptibly()
                ioSession = future.session
                isConnected = true

                // 对外告知已经连接上
                if (connectListener != null) {
                    connectListener!!.onConnected()
                }

                //遍历并执行请求池中的任务
                for (i in 0 until poolSize) {
                    Thread(RequestWorker()).start()
                }
                break
            } catch (e: Exception) {
                isConnected = false
                count++
            }

        }
    }

    /**
     * 断开连接
     */
    fun disconnect() {
        try {
            if (connector != null) {
                isConnected = false
                connector!!.dispose()
                connector = null
                if (connectListener != null) {
                    connectListener!!.onDisconnected()
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 发送请求的任务
     */
    private inner class PacketHandler : IoHandler {
        @Throws(Exception::class)
        override fun sessionCreated(session: IoSession) {
            Log.d("GoChat_connector", "sessionCreated : " + session.id)
            val cfg = session.config as SocketSessionConfig
            cfg.isKeepAlive = true
            cfg.readBufferSize = 4 * 1024
            cfg.receiveBufferSize = 4 * 1024
            cfg.sendBufferSize = 4 * 1024
            cfg.isTcpNoDelay = true
            cfg.bothIdleTime = 60 * 1000
        }

        @Throws(Exception::class)
        override fun sessionOpened(session: IoSession) {
            // session.setAttributeIfAbsent("token", "abc");
            // session.setAttribute("token", "abc");
            Log.d("GoChat_connector", "sessionOpen : " + session.id)
        }

        @Throws(Exception::class)
        override fun sessionClosed(session: IoSession) {
            Log.d("GoChat_connector", "sessionClosed : " + session.id)
            isConnected = false
            //tcp该次通信完成，执行断开回调
            if (connectListener != null) {
                connectListener!!.onDisconnected()
            }
        }

        @Throws(Exception::class)
        override fun sessionIdle(session: IoSession, status: IdleStatus) {
            Log.d("GoChat_connector", "sessionIdle : " + session.id)
        }

        @Throws(Exception::class)
        override fun exceptionCaught(session: IoSession, cause: Throwable) {
            Log.d("GoChat_connector", "exceptionCaught : " + session.id + "caise" + cause)
            isConnected = false
            if (connectListener != null) {
                connectListener!!.onDisconnected()
            }
        }

        //有消息成功接收
        @Throws(Exception::class)
        override fun messageReceived(session: IoSession, message: Any) {
            Log.d("GoChat_connector", "messageReceived : " + session.id + "message:" + message.toString())
            if (ioListener != null) {
                ioListener!!.onInputComed(session, message)
            }
        }

        //有消息发送，但这里以服务器响应成功为消息成功发送标准
        @Throws(Exception::class)
        override fun messageSent(session: IoSession, message: Any) {
            Log.d("GoChat_connector", "messageSent : " + session.id + "message" + message.toString())
        }
    }

    fun addRequest(request: ChatRequest) {
        try {
            requestQueue.put(request)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            // 告知外界请求失败
            if (ioListener != null) {
                ioListener!!.onOutputFailed(request, e)
            }
        }

    }

    inner class RequestWorker : Runnable {
        override fun run() {
            while (isConnected) {
                var request: ChatRequest? = null
                try {
                    request = requestQueue.take()// 阻塞方法
                    ioSession!!.write(request!!.transport)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    // 告知外界请求失败
                    if (ioListener != null) {
                        ioListener!!.onOutputFailed(request, e)
                    }
                }

            }
        }

    }

    /**
     * 设置输入输出监听
     * @param listener
     */
    fun setIOListener(listener: IOListener) {
        this.ioListener = listener
    }

    /**
     * 设置连接监听
     * @param listener
     */
    fun setConnectListener(listener: ConnectListener) {
        this.connectListener = listener
    }

    interface IOListener {
        fun onOutputFailed(request: ChatRequest?, e: Exception)
        fun onInputComed(session: IoSession, message: Any)
    }

    interface ConnectListener {
        fun onConnecting()
        fun onConnected()
        fun onReConnecting()
        fun onDisconnected()
        fun onAuthFailed()
    }

}