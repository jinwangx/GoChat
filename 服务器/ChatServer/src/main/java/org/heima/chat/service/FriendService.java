package org.heima.chat.service;

import java.util.List;

import org.heima.chat.pojo.Friend;

public interface FriendService {

	void addFriend(Friend friend);

	void updateFriend(Friend invitorFriend);

	List<Friend> queryPendingFriendByOwner(String owner);

	List<Friend> queryAllFriends(String owner);

	Friend queryFriendByOwnerAndFriend(String owner, String friendAccount);
}
