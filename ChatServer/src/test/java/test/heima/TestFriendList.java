package test.heima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.User;
import org.heima.chat.service.FriendService;
import org.heima.chat.service.UserService;
import org.heima.chat.vo.ClientFriend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
@Component
public class TestFriendList {

	@Autowired
	UserService		userService;

	@Autowired
	FriendService	friendService;

	@Test
	public void test() {

		User owner = userService.findUserByAccount("iphone");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);
		List<Friend> list = friendService.queryAllFriends(owner.getAccount());
		if (list != null) {
			List<ClientFriend> friends = new ArrayList<ClientFriend>();
			for (Friend friend : list) {
				User user = userService.findUserByAccount(friend.getFriendAccount());
				friends.add(ClientFriend.toFriend(friend, user, owner));
			}
			map.put("data", friends);
		}
		String json = new Gson().toJson(map);
		System.out.println(json);

	}
}
