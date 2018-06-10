package org.heima.chat.dao;

import java.util.List;

import org.heima.chat.pojo.Friend;

public interface FriendDao {

	void addFriend(Friend friend);

	void updateFriend(Friend invitorFriend);

	List<Friend> findFriendByFields(String[] strings, Object[] objects);
}
