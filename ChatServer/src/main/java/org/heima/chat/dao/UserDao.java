package org.heima.chat.dao;

import org.heima.chat.pojo.User;

public interface UserDao {

	User findUserByAccount(String account);

	User findById(String id);
	
	void addUser(User user);
	
	void updateUser(User user);
}
