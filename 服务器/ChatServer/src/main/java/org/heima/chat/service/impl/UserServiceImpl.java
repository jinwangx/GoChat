package org.heima.chat.service.impl;

import org.heima.chat.dao.UserDao;
import org.heima.chat.pojo.User;
import org.heima.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements
							UserService
{
	@Autowired
	UserDao	userDao;

	@Override
	public boolean isExist(String account) {
		User user = userDao.findUserByAccount(account);
		return user != null;
	}

	@Override
	public User addUser(String account, String password) {
		User user = new User();
		user.setAccount(account);
		user.setPassword(password);
		userDao.addUser(user);
		
		return user;
	}

	@Override
	public User findUserByAccount(String account) {
		return userDao.findUserByAccount(account);
	}

	@Override
	public void updateToken(User user) {
		userDao.updateUser(user);
	}

}
