package com.jw.gochat.action;

import android.content.Context;

import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.utils.CommonUtil;

import java.io.File;
import java.util.Map;

/**
 * 创建时间：2017/10/29 2017/10/29
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送头像变更时，执行对应数据库更新，并通知界面更新
 */

public class IconChangeAction extends Action {

	@Override
	public String getAction() {
		return "nameChange";
	}

	@Override
	public void doAction(final Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		final String receiver = data.get("receiver").toString();
		final String sender = data.get("sender").toString();
		String iconPath = data.get("iconPath").toString();

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			return;
		}

		friend.setIcon(CommonUtil.getIconDir(context) + iconPath);
		friendDao.updateFriend(friend);

		// 下载朋友的icon
		File file = new File(friend.getIcon());
/*
		QQChatManager.getInstance().downloadFile(iconPath, file,
				new GoChatFileCallBack() {

					@Override
					public void onSuccess(File file) {
						// 发送广播
						Intent intent = new Intent(
								PushReceiver.ACTION_ICON_CHANGE);
						intent.putExtra(PushReceiver.KEY_FROM, sender);
						intent.putExtra(PushReceiver.KEY_TO, receiver);
						context.sendBroadcast(intent);
					}

					@Override
					public void onProgress(int writen, int total) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(int error, String msg) {
						// TODO Auto-generated method stub

					}
				});
*/

	}
}
