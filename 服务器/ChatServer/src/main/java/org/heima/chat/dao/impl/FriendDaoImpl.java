package org.heima.chat.dao.impl;

import java.util.List;

import org.heima.chat.dao.BaseDaoSupport;
import org.heima.chat.dao.FriendDao;
import org.heima.chat.pojo.Friend;
import org.springframework.stereotype.Repository;

@Repository
public class FriendDaoImpl	extends
							BaseDaoSupport<Friend>	implements
													FriendDao
{

	@Override
	public void addFriend(Friend friend) {
		friend.setId((String) save(friend));
	}

	@Override
	public void updateFriend(Friend invitorFriend) {
		super.update(invitorFriend);
	}

	@Override
	public List<Friend> findFriendByFields(String[] strings, Object[] objects) {
		return findByFields(Friend.class, strings, objects);
	}

}
