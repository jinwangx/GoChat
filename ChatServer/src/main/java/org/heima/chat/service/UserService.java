package org.heima.chat.service;

import org.heima.chat.pojo.User;

public interface UserService {

	boolean isExist(String account);

	User addUser(String account, String password);

	User findUserByAccount(String account);

	void updateToken(User user);
}
