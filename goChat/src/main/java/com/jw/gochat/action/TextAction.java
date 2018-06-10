package com.jw.gochat.action;

import android.content.Context;
import android.content.Intent;

import com.jw.chat.db.bean.Message;
import com.jw.chat.db.dao.MessageDao;
import com.jw.gochat.receiver.PushReceiver;

import java.util.Map;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到聊天消息或发送聊天信息时执行对应数据库更新，并通知界面更新
 */

public class TextAction extends Action {

	@Override
	public String getAction() {
		return "text";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();

		String content = data.get("content").toString();

		// 数据存储
		MessageDao dao = new MessageDao(context);
		Message message = new Message();
		message.setAccount(sender);
		message.setContent(content);
		message.setCreateTime(System.currentTimeMillis());
		message.setDirection(1);  //1代表接受，0代表发送
		message.setOwner(receiver);
		message.setRead(false);
		message.setType(0);  //0代表文字，1代表图片
		dao.addMessage(message);

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_TEXT);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		intent.putExtra(PushReceiver.KEY_TEXT_CONTENT, content);
		context.sendBroadcast(intent);
	}

}
