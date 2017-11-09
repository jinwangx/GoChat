package org.heima.chat.dao.impl;

import java.io.Serializable;

import org.heima.chat.dao.BaseDaoSupport;
import org.heima.chat.dao.UserDao;
import org.heima.chat.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends
						BaseDaoSupport<User> implements
											UserDao
{

	@Override
	public User findUserByAccount(String account) {
		return findOneByField(User.class, "account", account);
	}

	@Override
	public void addUser(User user) {
		Serializable id = save(user);
		user.setId((String) id);
	}

	@Override
	public User findById(String id) {
		return super.findById(id);
	}

	@Override
	public void updateUser(User user) {
		update(user);
	}

}
