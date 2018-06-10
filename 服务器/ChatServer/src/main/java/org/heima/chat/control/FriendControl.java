package org.heima.chat.control;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.heima.chat.nio.MessagePusher;
import org.heima.chat.nio.PushCallback;
import org.heima.chat.nio.PushMessage;
import org.heima.chat.nio.PushMessage.Type;
import org.heima.chat.nio.body.ReInvitationBody;
import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.User;
import org.heima.chat.service.FriendService;
import org.heima.chat.service.UserService;
import org.heima.chat.vo.ClientFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class FriendControl {
	private final static int	LOGIN_ACCOUNT_MISS	= 101;

	@Autowired
	FriendService				friendService;

	@Autowired
	UserService					userService;

	@Autowired
	MessagePusher				pusher;

	@RequestMapping(value = "/friend/accept")
	@ResponseBody
	public String accept(String invitor, String acceptor) {
		//存储邀请者
		User acceptorUser = userService.findUserByAccount(acceptor);
		Friend invitorFriend = friendService.queryFriendByOwnerAndFriend(invitor, acceptor);
		if (invitorFriend == null) {
			invitorFriend = new Friend();
			invitorFriend.setOwnerAccount(invitor);
			invitorFriend.setFriendAccount(acceptor);
			invitorFriend.setAddTime(new Timestamp(System.currentTimeMillis()));
			invitorFriend.setFriendName(acceptorUser.getName());
			invitorFriend.setState(0);//激活
			invitorFriend.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			friendService.addFriend(invitorFriend);
		} else {
			//			invitorFriend.setState(0);//激活
			//			invitorFriend.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			//			friendService.updateFriend(invitorFriend);
		}

		//存储接受者
		User invitorUser = userService.findUserByAccount(invitor);
		Friend acceptorFriend = friendService.queryFriendByOwnerAndFriend(acceptor, invitor);
		if (acceptorFriend == null) {
			acceptorFriend = new Friend();
			acceptorFriend.setOwnerAccount(acceptor);
			acceptorFriend.setFriendAccount(invitor);
			acceptorFriend.setAddTime(new Timestamp(System.currentTimeMillis()));
			acceptorFriend.setFriendName(invitorUser.getName());
			acceptorFriend.setState(1);//激活
			acceptorFriend.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			friendService.addFriend(acceptorFriend);
		} else {
			acceptorFriend.setState(1);//激活
			acceptorFriend.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			friendService.updateFriend(acceptorFriend);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);

		if (pusher.isOnline(invitor)) {
			//在线
			final Friend f = invitorFriend;

			PushMessage message = PushMessage.createPushMessage(Type.REINVITATION);
			message.setReceiver(invitor);
			message.setSender(acceptor);
			message.setBody(new ReInvitationBody("同意", true, acceptorUser.getIcon(),
													acceptorUser.getName()));

			pusher.push(message, new PushCallback() {

				@Override
				public void onSuccess() {
					f.setState(1);
					f.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					friendService.updateFriend(f);
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
		} else {
			//不在线

		}
		return new Gson().toJson(map);
	}

	@RequestMapping(value = "/friend/list")
	@ResponseBody
	public String list(HttpServletRequest request) {
		String account = request.getHeader("account");

		User owner = userService.findUserByAccount(account);

		System.out.println(owner);
		Map<String, Object> map = new HashMap<String, Object>();
		if (owner == null) {
			map.put("flag", false);
			map.put("errorCode", LOGIN_ACCOUNT_MISS);
			map.put("errorString", "用户不存在");
		} else {
			map.put("flag", true);
			List<Friend> list = friendService.queryAllFriends(owner.getAccount());
			if (list != null) {
				System.out.println(list.size());
				List<ClientFriend> friends = new ArrayList<ClientFriend>();
				for (Friend friend : list) {
					User user = userService.findUserByAccount(friend.getFriendAccount());
					friends.add(ClientFriend.toFriend(friend, user, owner));
				}
				map.put("data", friends);
			}
		}
		return new Gson().toJson(map);
	}

}
