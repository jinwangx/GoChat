package com.jw.gochat.action;

import android.content.Context;

import java.util.Map;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：监听到客户端tcp有消息成功接收或发送时，首先来到这里，执行数据库更新.
 *       数据库更新完毕后，再发送广播，通知界面更新
 */

public abstract class Action {

	public abstract String getAction();

	public abstract void doAction(Context context, Map<String, Object> data);
}