package com.jw.gochat.base;

import android.app.IntentService;

import com.jw.gochat.ChatApplication;

/**
 * 创建时间：
 * 更新时间 2017/11/4 19:39
 * 版本：
 * 作者：Mr.jin
 * 描述：IntentService在主线程中运行，可多次创建执行，执行完后自动销毁
 */

public abstract class BaseIntentService extends IntentService {

	public BaseIntentService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((ChatApplication) getApplication()).addService(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		((ChatApplication) getApplication()).removeService(this);
	}
}
