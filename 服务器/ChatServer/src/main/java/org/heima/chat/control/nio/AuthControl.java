package org.heima.chat.control.nio;

import java.util.List;

import org.heima.chat.core.Action;
import org.heima.chat.nio.BackTaskPusher;
import org.heima.chat.nio.ChatRequest;
import org.heima.chat.nio.ChatResponse;
import org.heima.chat.nio.MessagePusher;
import org.heima.chat.nio.PushCallback;
import org.heima.chat.nio.PushMessage;
import org.heima.chat.nio.PushMessage.Type;
import org.heima.chat.nio.body.InvitationBody;
import org.heima.chat.nio.body.ReInvitationBody;
import org.heima.chat.nio.body.TextBody;
import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.Invitation;
import org.heima.chat.pojo.Message;
import org.heima.chat.pojo.User;
import org.heima.chat.service.FriendService;
import org.heima.chat.service.InvitationService;
import org.heima.chat.service.MessageService;
import org.heima.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "nio:auth")
public class AuthControl implements
						Action
{
	private static final int	ERROR_USER_NOT_EXIST		= 400;
	private static final int	ERROR_USER_LOGINED_OTHER	= 401;

	@Autowired
	UserService					userService;

	@Autowired
	MessageService				messageService;

	@Autowired
	InvitationService			invitationService;

	@Autowired
	FriendService				friendService;

	@Autowired
	MessagePusher				messagePusher;

	@Autowired
	BackTaskPusher				backPusher;

	@Override
	public void doAction(ChatRequest request, ChatResponse response) {
		String account = (String) request.getSender();
		try {
			String token = (String) request.get("token");

			User user = userService.findUserByAccount(account);

			System.out.println("user : " + user);

			if (user == null) {
				response.put("flag", false);
				response.put("errorCode", ERROR_USER_NOT_EXIST);
				response.put("errorString", "用户不存在");
				response.writeResponse();

				response.disconnect();
				return;
			}

			String userToken = user.getToken();
			if (!token.equals(userToken)) {
				response.put("flag", false);
				response.put("errorCode", ERROR_USER_LOGINED_OTHER);
				response.put("errorString", "用户在其他设备上登录了");
				response.writeResponse();

				response.disconnect();
			} else {
				response.put("flag", true);
				response.writeResponse();
			}

			//检查是否有要推送的消息
			List<Message> pendingMessages = messageService.findPendingMessages(account);
			if (pendingMessages != null) {
				for (final Message msg : pendingMessages) {
					PushMessage message = null;
					if (msg.getType() == PushMessage.Type.TEXT.ordinal()) {
						message = PushMessage.createPushMessage(PushMessage.Type.TEXT);
						message.setBody(new TextBody(msg.getContent()));
						message.setReceiver(account);
						message.setSender(msg.getSender());
					} else {
						//TODO:
					}

					msg.setState(1);
					messageService.updateMessage(msg);

					messagePusher.push(message, new PushCallback() {

						@Override
						public void onSuccess() {
							msg.setState(2);
							messageService.updateMessage(msg);
						}

						@Override
						public void onProgress(int progress) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(int errorCode, String errorString) {
							msg.setState(3);
							messageService.updateMessage(msg);
						}
					});
				}
			}

			//朋友邀请推送
			List<Invitation> invitations = invitationService.findPendingInvitation(account);

			if (invitations != null) {
				for (final Invitation invitation : invitations) {
					User sender = userService.findUserByAccount(invitation.getSender());

					//生成 msg
					PushMessage msg = PushMessage.createPushMessage(PushMessage.Type.INVITATION);
					msg.setReceiver(invitation.getReceiver());
					msg.setBody(new InvitationBody(invitation.getContent(), sender.getName(),
													sender.getIcon()));
					msg.setSender(invitation.getSender());

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
				}
			}

			//朋友 接受推送
			List<Friend> friends = friendService.queryPendingFriendByOwner(account);
			if (friends != null) {
				for (final Friend friend : friends) {

					User u = userService.findUserByAccount(friend.getFriendAccount());

					PushMessage message = PushMessage.createPushMessage(Type.REINVITATION);
					message.setReceiver(account);
					message.setSender(friend.getFriendAccount());
					message.setBody(new ReInvitationBody("同意", true, u.getIcon(), u.getName()));

					messagePusher.push(message, new PushCallback() {

						@Override
						public void onSuccess() {
							friend.setState(1);
							friendService.updateFriend(friend);
						}

						@Override
						public void onProgress(int progress) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(int error, String msg) {
							// TODO Auto-generated method stub

						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//后台推送服务
		backPusher.doPush(account);
	}
}
