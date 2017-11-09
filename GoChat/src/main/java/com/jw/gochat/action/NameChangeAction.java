package com.jw.gochat.action;

import android.content.Context;
import android.content.Intent;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.receiver.PushReceiver;
import java.util.Map;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送名称变更时，执行对应数据库更新，并通知界面更新
 */

public class NameChangeAction extends Action {

	@Override
	public String getAction() {
		return "nameChange";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();
		String name = data.get("name").toString();

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			return;
		}
		friend.setName(name);
		friendDao.updateFriend(friend);

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_NAME_CHANGE);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}

}
