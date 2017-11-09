package com.jw.gochat.action;

import android.content.Context;
import android.content.Intent;

import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.utils.CommonUtils;

import java.util.Map;

import Lib.GoChatURL;

/**
 * 创建时间：2017/10/29 2017/10/29
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送用户已接受邀请时，执行对应数据库更新，并通知界面更新
 *        key	 	 类型	  说明
		 type		String	请求：request
		 sequence	String	请求的序列号
		 action		String	请求的行为:reinvitation
		 sender		String	发送者账号
		 receiver	String	接收者的账号
		 name		String	接受邀请者的名字
		 icon		String	接受邀请者的头像
		 content	String	邀请的文本内容
 */

public class ReinvitationAction extends Action {

	@Override
	public String getAction() {
		return "reinvitation";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();

		String name = null;
		String icon = null;

		Object nameObj = data.get("name");
		if (nameObj != null) {
			name = (String) nameObj;
		}

		Object iconObj = data.get("icon");
		if (iconObj != null) {
			icon = (String) iconObj;
		}

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			friend = new Friend();
			friend.setAccount(sender);
			friend.setAlpha(CommonUtils.getFirstAlpha(name));
			if (icon != null) {
				friend.setIcon(GoChatURL.BASE_HTTP+icon.replace("\\","/"));
			}
			friend.setName(name);
			friend.setOwner(receiver);
			friend.setSort(0);

			friendDao.addFriend(friend);
		}

		Intent intent = new Intent(PushReceiver.ACTION_REINVATION);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}

}
