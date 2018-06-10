package org.heima.chat.service.impl;

import java.util.List;

import org.heima.chat.dao.FriendDao;
import org.heima.chat.pojo.Friend;
import org.heima.chat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl	implements
								FriendService
{

	@Autowired
	FriendDao	friendDao;

	@Override
	public void addFriend(Friend friend) {
		friendDao.addFriend(friend);
	}

	@Override
	public void updateFriend(Friend invitorFriend) {
		friendDao.updateFriend(invitorFriend);
	}

	@Override
	public List<Friend> queryPendingFriendByOwner(String owner) {

		return friendDao.findFriendByFields(new String[] { "ownerAccount", "state" }, new Object[] {
				owner, 0 });
	}

	@Override
	public List<Friend> queryAllFriends(String owner) {
		return friendDao.findFriendByFields(new String[] { "ownerAccount", "state" }, new Object[] {
				owner, 1 });
	}

	@Override
	public Friend queryFriendByOwnerAndFriend(String owner, String friendAccount) {
		List<Friend> friends =
								friendDao.findFriendByFields(new String[] { "ownerAccount",
										"friendAccount" }, new Object[] {
										owner, friendAccount });
		if (friends == null || friends.size() == 0) { return null; }
		return friends.get(0);
	}
}
