package org.heima.chat.service.impl;

import java.util.List;

import org.heima.chat.dao.MessageDao;
import org.heima.chat.pojo.Message;
import org.heima.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl	implements
								MessageService
{

	@Autowired
	MessageDao	dao;

	@Override
	public void addMessage(Message msg) {
		dao.addMessage(msg);
	}

	@Override
	public void updateMessage(Message msg) {
		dao.updateMessage(msg);
	}

	@Override
	public List<Message> findPendingMessages(String account) {
		return dao.findByAccountAndState(account, 0);
	}

}
