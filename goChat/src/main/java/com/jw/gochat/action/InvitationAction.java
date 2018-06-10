package com.jw.gochat.action;

import android.content.Context;
import android.content.Intent;

import com.jw.business.bean.Invitation;
import com.jw.business.db.dao.InvitationDao;
import com.jw.chat.GoChatURL;
import com.jw.gochat.receiver.PushReceiver;

import java.util.Map;

/**
  * 创建时间：
  * 更新时间 2017/10/29 2017/10/29
  * 版本：
  * 作者：Mr.jin
  * 描述：客户端发送邀请时（或者收到服务器推送用户邀请）执行对应数据库操作并通知界面更新
  *
  *    推送用户邀请：
  * 		 key			类型	说明
		 type			String	请求：request
		 sequence		String	请求的序列号
		 action			String	请求的行为:invitation
		 sender			String	发送者账号
		 receiver		String	接收者的账号
		 content		String	邀请的文本内容

		 invitor_name	String	邀请者的名字
		 invitor_icon	String	邀请者的头像

		 *
		 *    发送邀请
		 *       key			类型	说明
		 type			String	请求：request
		 sequence		String	请求的序列号
		 action			String	请求的行为:invitation
		 sender			String	发送者账号
		 receiver		String	接收者的账号
		 content		String	邀请的文本内容
		 token			String	发送者token标志
  */

public class InvitationAction extends Action {

	@Override
	public String getAction() {
		return "invitation";
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
		String token=null;

		Object nameObj = data.get("invitor_name");
		if (nameObj != null) {
			name = (String) nameObj;
		}

		Object iconObj = data.get("invitor_icon");
		if (iconObj != null) {
			icon = (String) iconObj;
		}

		Object tokenObj = data.get("token");
		if (tokenObj != null) {
			token = (String) tokenObj;
		}


		//如果token不为空，则是发送邀请,为空则是推送的邀请
		if(token!=null){

		}else {

		}
		// 存取数据
		InvitationDao invitationDao = new InvitationDao(context);
		Invitation invitation = invitationDao.queryInvitation(receiver, sender);
		if (invitation == null) {
			invitation = new Invitation();
			invitation.setAccount(sender);
			invitation.setOwner(receiver);
			invitation.setAgree(false);
			if (icon != null) {
				invitation.setIcon(GoChatURL.BASE_HTTP+icon.replace("\\","/"));
			}
			invitation.setName(name);
			invitationDao.addInvitation(invitation);
		} else {
			invitation.setAgree(false);
			if (icon != null) {
				invitation.setIcon(GoChatURL.BASE_HTTP+icon.replace("\\","/"));
			}
			invitationDao.updateInvitation(invitation);
		}

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_INVATION);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}
}
