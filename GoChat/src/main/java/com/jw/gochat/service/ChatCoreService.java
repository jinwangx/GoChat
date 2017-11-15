package com.jw.gochat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import com.jw.gochat.R;
import com.jw.gochat.action.Action;
import com.jw.gochat.base.BaseService;
import com.jw.gochat.bean.Account;
import com.jw.gochat.db.AccountDao;
import com.jw.gochat.utils.NetUtils;
import com.jw.gochat.utils.ThemeUtils;

import java.util.HashMap;
import java.util.Map;
import Lib.GoChat;
import Lib.GoChatManager;
import Lib.core.PacketConnector;

/**
 * 创建时间：
 * 更新时间 2017/11/1 21:41
 * 版本：
 * 作者：Mr.jin
 * 描述：
 *       1.该服务第一次启动时，实例化各个Action,并注册网络状态广播
 * 		 2.若网络已连接，才会去尝试开启核心服务。
 * 		 3.如果有本地账户，GoChatManager类对服务器的连接进行各种监听，并且客户端通过tcp通道开始连接服务器进行认证
 * 		 4.同时开启后台服务，向服务器提交之前未完成的网络任务
 */

public class ChatCoreService extends BaseService implements PacketConnector.ConnectListener,
		GoChatManager.OnPushListener {
	private GoChatManager chatManager;

	private int reconnectCount = 0;// 重连次数

	private Map<String, Action> actionMaps = new HashMap<String, Action>();

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				if (NetUtils.isNetConnected(ChatCoreService.this)) {
					//ThemeUtils.show(GoChat.getContext(),"网络已连接");
					connectServer();
				}else {
					ThemeUtils.show(GoChat.getContext(),"没有网络");
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("GoChat_Core", "聊天引擎创建了");
		// 注册网络监听
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		scanClass();
	}

	private void connectServer() {
		Log.d("GoChat_Core", "正在连接服务器");
		Account account = new AccountDao(this).getCurrentAccount();
		if (account != null) {
			chatManager = GoChatManager.getInstance();
			chatManager.addConnectionListener(this);
			chatManager.setPushListener(this);
			chatManager.auth(account.getAccount(), account.getToken());

			// 后台服务开启
			startService(new Intent(this, BackgroundService.class));
		}
	}

	@Override
	public void onConnecting() {
		// TODO Auto-generated method stub
		Log.d("GoChat_Core", "正在连接");
	}

	@Override
	public void onConnected() {
		// TODO
		reconnectCount = 0;
		Log.d("GoChat_Core", "连接成功");
	}

	@Override
	public void onReConnecting() {
		Log.d("GoChat_Core", "正在重连");
	}

	@Override
	public void onDisconnected() {
		Log.d("GoChat_Core", "连接断开");

		if (NetUtils.isNetConnected(ChatCoreService.this)) {
			// 有网络的
			Log.d("GoChat_Core", "网络已经开启，正在开始第"	+(++reconnectCount)+"连接");
			if (reconnectCount < 10) {
				connectServer();
			}
		}
	}

	@Override
	public void onAuthFailed() {
		Log.d("GoChat_Core", "认证失败");
		// TODO Auto-generated method stub

	}

	private void scanClass() {
		Log.d("GoChat_Core", "正在实例化各个Action");
		String[] array = getResources().getStringArray(R.array.actions);

		if (array == null) {
			return;
		}

		String packageName = getPackageName();
		ClassLoader classLoader = getClassLoader();

		for (int i = 0; i < array.length; i++) {
			try {

				Class<?> clazz = classLoader.loadClass(packageName + "."
						+ array[i]);

				Class<?> superclass = clazz.getSuperclass();

				if (superclass != null
						&& Action.class.getName().equals(superclass.getName())) {

					Action action = (Action) clazz.newInstance();
					actionMaps.put(action.getAction(), action);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 成功接收到推送的消息，开始执行数据库操作，数据库操作执行完后，发送广播给ui,页面更新
	 * @param action
	 * @param data
	 * @return
	 */
	@Override
	public boolean onPush(String action, Map<String, Object> data) {
		Action actioner = actionMaps.get(action);
		if (actioner != null) {
			actioner.doAction(this, data);
		}

		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("GoChat_Core", "onDestroy");
		unregisterReceiver(mReceiver);
		// 断开连接
		chatManager.closeSocket();
		chatManager.removeConnectionListener(this);
	}
}
