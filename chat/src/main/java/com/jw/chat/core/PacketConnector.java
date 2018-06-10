package com.jw.chat.core;

import android.util.Log;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建时间：
 * 更新时间 2017/11/1 22:15
 * 版本：
 * 作者：Mr.jin
 * 描述：TCP通信底层Connector类
 */

public class PacketConnector {
	private String host;
	private int port;
	private boolean isConnected;
	private NioSocketConnector connector;
	private IoSession ioSession;
	private int poolSize;
	private ConnectListener connectListener;
	private IOListener ioListener;
	private LinkedBlockingQueue<ChatRequest> requestQueue = new LinkedBlockingQueue<ChatRequest>(
			128);

	/**
	 * 初始化Connector
	 * @param host 主机地址
	 * @param port	端口号
	 * @param threadSize 可供线程数量
	 */
	public PacketConnector(String host, int port, int threadSize) {
		this.host = host;
		this.port = port;
		this.poolSize = threadSize;
	}

	/**
	 * 开始连接
	 */
	public void connect() {
		if (isConnected) {
			return;
		}
		int count = 0;
		while (count < 3) {
			try {
				// 对外告知重连
				if (count > 0 && connectListener != null) {
					connectListener.onReConnecting();
				}else if(count==0&&connectListener!=null){
					connectListener.onConnecting();
				}
				if(connector==null)
					connector = new NioSocketConnector();
				// 设置超时时间
				connector.setConnectTimeoutMillis(30 * 1000);
				// 设置过滤链
				DefaultIoFilterChainBuilder filterChain = connector
						.getFilterChain();
				filterChain.addLast("codec", new ProtocolCodecFilter(
						new TextLineCodecFactory(Charset.forName("utf-8"))));
				// 设置监听
				connector.setHandler(new PacketHandler());

				ConnectFuture future = connector.connect(new InetSocketAddress(
						host, port));
				future.awaitUninterruptibly();
				ioSession = future.getSession();
				isConnected = true;

				// 对外告知已经连接上
				if (connectListener != null) {
					connectListener.onConnected();
				}

				//遍历并执行请求池中的任务
				for (int i = 0; i < poolSize; i++) {
					new Thread(new RequestWorker()).start();
				}

				break;
			} catch (Exception e) {
				isConnected = false;
				count++;
			}
		}
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		try {
			if (connector != null) {
				isConnected = false;
				connector.dispose();
				connector = null;
				if(connectListener!=null){
					connectListener.onDisconnected();
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 发送请求的任务
	 */
	private final class PacketHandler implements IoHandler {
		@Override
		public void sessionCreated(IoSession session) throws Exception {
			Log.d("GoChat_connector", "sessionCreated : " + session.getId());
			SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
			cfg.setKeepAlive(true);
			cfg.setReadBufferSize(4 * 1024);
			cfg.setReceiveBufferSize(4 * 1024);
			cfg.setSendBufferSize(4 * 1024);
			cfg.setTcpNoDelay(true);
			cfg.setBothIdleTime(60 * 1000);
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			// session.setAttributeIfAbsent("token", "abc");
			// session.setAttribute("token", "abc");
			Log.d("GoChat_connector", "sessionOpen : " + session.getId());
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			Log.d("GoChat_connector", "sessionClosed : " + session.getId());
			isConnected = false;
			//tcp该次通信完成，执行断开回调
			if (connectListener != null) {
				connectListener.onDisconnected();
			}
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			Log.d("GoChat_connector", "sessionIdle : " + session.getId());
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			Log.d("GoChat_connector", "exceptionCaught : " + session.getId()+"caise"+cause);
			isConnected = false;
			if (connectListener != null) {
				connectListener.onDisconnected();
			}
		}

		//有消息成功接收
		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			Log.d("GoChat_connector", "messageReceived : " + session.getId()+"message:"+message.toString());
			if (ioListener != null) {
				ioListener.onInputComed(session, message);
			}
		}

		//有消息发送，但这里以服务器响应成功为消息成功发送标准
		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			Log.d("GoChat_connector", "messageSent : " + session.getId()+"message"+message.toString());
		}
	}

	public void addRequest(ChatRequest request) {
		try {
			requestQueue.put(request);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// 告知外界请求失败
			if (ioListener != null) {
				ioListener.onOutputFailed(request, e);
			}
		}
	}
	public final class RequestWorker implements Runnable {
		@Override
		public void run() {
			while (isConnected) {
				ChatRequest request = null;
				try {
					request = requestQueue.take();// 阻塞方法
					ioSession.write(request.getTransport());
				} catch (InterruptedException e) {
					e.printStackTrace();
					// 告知外界请求失败
					if (ioListener != null) {
						ioListener.onOutputFailed(request, e);
					}
				}
			}
		}

	}

	/**
	 * 是否连接
	 * @return
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * 设置输入输出监听
	 * @param listener
	 */
	public void setIOListener(IOListener listener) {
		this.ioListener = listener;
	}

	/**
	 * 设置连接监听
	 * @param listener
	 */
	public void setConnectListener(ConnectListener listener) {
		this.connectListener = listener;
	}

	public interface IOListener {
		void onOutputFailed(ChatRequest request, Exception e);
		void onInputComed(IoSession session, Object message);
	}

	public interface ConnectListener {
		void onConnecting();
		void onConnected();
		void onReConnecting();
		void onDisconnected();
		void onAuthFailed();
	}

}
