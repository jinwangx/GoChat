package org.heima.chat.service;

import java.util.List;

import org.heima.chat.pojo.Message;

public interface MessageService {

	void addMessage(Message msg);

	void updateMessage(Message msg);

	List<Message> findPendingMessages(String account);
}
