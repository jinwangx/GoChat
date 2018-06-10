package com.jw.chat;

import android.content.Context;

/**
 * 创建时间：
 * 更新时间 2017/10/30 0:35
 * 版本：
 * 作者：Mr.jin
 * 描述：得到整个应用上下文对象
 */

public class GoChat {
	private static GoChat instance;
	private static Context context;

	public static GoChat getInstance() {
		if (instance == null) {
			synchronized (GoChat.class) {
				instance = new GoChat();
			}
		}
		return instance;
	}

	public static Context getContext() {
		if (context == null) {
			throw new RuntimeException(
				"请在Application的onCreate方法中调用GoChat.getInstance().init(context)初始化聊天引擎.");
		}
		return context;
	}

	public void init(Context context) {
		this.context = context;
	}

}