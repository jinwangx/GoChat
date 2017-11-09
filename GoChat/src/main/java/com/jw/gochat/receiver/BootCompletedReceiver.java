package com.jw.gochat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jw.gochat.service.ChatCoreService;
import com.jw.gochat.utils.CommonUtils;

/**
 * 创建时间：
 * 更新时间 2017/11/1 21:22
 * 版本：
 * 作者：Mr.jin
 * 描述：接受到系统开机的广播后，开启聊天引擎核心服务
 */

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//开启聊天引擎核心服务
		if (!CommonUtils.isServiceRunning(context, ChatCoreService.class)) {
			context.startService(new Intent(context, ChatCoreService.class));
		}
	}
}