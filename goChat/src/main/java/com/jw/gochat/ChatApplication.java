package com.jw.gochat;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jw.business.bean.Account;
import com.jw.business.db.dao.AccountDao;
import com.jw.library.utils.HttpUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.jw.chat.GoChat;
import okhttp3.OkHttpClient;

public class ChatApplication extends Application {
	private List<Activity> activitys = new LinkedList<>();
	private List<Service> services = new LinkedList<>();
	private static Account account;
	private static OkHttpClient client;
	private static Application application;
	private static android.os.Handler handler;
	private static int mainTid;

	@Override
	public void onCreate() {
		super.onCreate();
		client=new OkHttpClient();
		application=this;
		mainTid = android.os.Process.myTid();
		handler=new android.os.Handler();
		Log.d("ChatApplication", "init");
		// 初始化应用全局对象，即(ChatApplication)
		GoChat.getInstance().init(this);
		HttpUtils.init(client);
		// 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
		// 请勿在“=”与appid之间添加任何空字符或者转义符
		SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59f0548e");
	}

	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	public void removeActivity(Activity activity) {
		activitys.remove(activity);
	}

	public void addService(Service service) {
		services.add(service);
	}

	public void removeService(Service service) {
		services.remove(service);
	}

	public void closeApplication() {
		closeActivitys();
		closeServices();
		Process.killProcess(mainTid);
	}

	private void closeActivitys() {
		ListIterator<Activity> iterator = activitys.listIterator();
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity != null) {
				activity.finish();
			}
		}
	}

	private void closeServices() {
		ListIterator<Service> iterator = services.listIterator();
		while (iterator.hasNext()) {
			Service service = iterator.next();
			if (service != null) {
				stopService(new Intent(this, service.getClass()));
			}
		}
	}


	public static Account getAccount(){
		if (account == null) {
			AccountDao dao = new AccountDao(getApplication());
			account = dao.getCurrentAccount();
		}
		return account;
	}
	public static Application getApplication(){
		return application;
	}
	public static android.os.Handler getHandler() {
		return handler;
	}
	public static int getMainTid() {
		return mainTid;
	}
	public static OkHttpClient getOkHttpClient(){
		return client;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
