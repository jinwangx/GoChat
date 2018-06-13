package com.jw.gochat.action

import android.content.Context
import com.jw.business.business.FriendBusiness

import com.jw.gochat.utils.CommonUtil

import java.io.File

/**
 * 创建时间：2017/10/29 2017/10/29
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：收到服务器推送头像变更时，执行对应数据库更新，并通知界面更新
 */

class IconChangeAction : Action() {

    override val action: String
        get() = "nameChange"

    override fun doAction(context: Context, data: Map<String, Any>) {
        if (data == null) {
            return
        }

        val receiver = data["receiver"].toString()
        val sender = data["sender"].toString()
        val iconPath = data["iconPath"].toString()

        // 数据存储
        val friend = FriendBusiness.getFriendById(receiver, sender)
        friend!!.icon = CommonUtil.getIconDir(context) + iconPath
        FriendBusiness.update(friend)
        // 下载朋友的icon
        val file = File(friend.icon!!)
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
