package org.heima.chat.control.nio;

import java.sql.Timestamp;

import org.heima.chat.core.Action;
import org.heima.chat.nio.ChatRequest;
import org.heima.chat.nio.ChatResponse;
import org.heima.chat.nio.MessagePusher;
import org.heima.chat.nio.PushCallback;
import org.heima.chat.nio.PushMessage;
import org.heima.chat.nio.body.InvitationBody;
import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.Invitation;
import org.heima.chat.pojo.User;
import org.heima.chat.service.FriendService;
import org.heima.chat.service.InvitationService;
import org.heima.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// @Control(value = "invitation")
@Component(value = "nio:invitation")
public class InvitationControl	implements
								Action
{
	@Autowired
	MessagePusher		messagePusher;

	@Autowired
	UserService			userService;

	@Autowired
	InvitationService	invitationService;

	@Autowired
	FriendService		friendService;

	@Override
	public void doAction(ChatRequest request, ChatResponse response) {

		String sender = (String) request.getSender();

		String receiver = (String) request.get("receiver");
		String content = (String) request.get("content");

		//用于存储纪录
		//存储邀请信息
		final Invitation invitation = new Invitation();
		invitation.setContent(content);
		invitation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		invitation.setSender(sender);
		invitation.setReceiver(receiver);

		//存储friend信息
		User account = userService.findUserByAccount(receiver);
		Friend friend = friendService.queryFriendByOwnerAndFriend(sender, receiver);
		if (friend == null && account != null) {
			friend = new Friend();
			friend.setOwnerAccount(sender);
			friend.setFriendAccount(receiver);
			friend.setAddTime(new Timestamp(System.currentTimeMillis()));
			friend.setFriendName(account.getName());
			friend.setState(0);//未激活
			friend.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			friendService.addFriend(friend);
		}

		response.put("flag", true);
		response.writeResponse();

		try {
			//判断接收者是否在线
			if (messagePusher.isOnline(receiver)) {
				//在线
				System.out.println("receiver : online");

				//存储到数据库中
				invitation.setState(1); //正在发送
				invitationService.addInvitation(invitation);

				User user = userService.findUserByAccount(sender);

				//生成 msg
				PushMessage msg = PushMessage.createPushMessage(PushMessage.Type.INVITATION);
				msg.setReceiver(receiver);
				msg.setBody(new InvitationBody(content, user.getName(), user.getIcon()));
				msg.setSender(sender);

				messagePusher.push(msg, new PushCallback() {
					@Override
					public void onSuccess() {
						invitation.setState(2);//发送成功
						invitationService.updateInvitation(invitation);
					}

					@Override
					public void onProgress(int progress) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onError(int error, String msg) {
						invitation.setState(3);//发送失败
						invitationService.updateInvitation(invitation);
					}
				});
			} else {
				//不在线
				System.out.println("receiver : offline");

				//存储到数据库中
				invitation.setState(0); //等待发送
				invitationService.updateInvitation(invitation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}