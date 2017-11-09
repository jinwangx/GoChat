package org.heima.chat.dao;

import java.util.List;

import org.heima.chat.pojo.Message;

public interface MessageDao {

	void addMessage(Message msg);

	void updateMessage(Message msg);

	List<Message> findByAccountAndState(String account, int state);

}
