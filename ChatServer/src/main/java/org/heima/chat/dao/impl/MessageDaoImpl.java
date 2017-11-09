package org.heima.chat.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.heima.chat.dao.BaseDaoSupport;
import org.heima.chat.dao.MessageDao;
import org.heima.chat.pojo.Message;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDaoImpl	extends
							BaseDaoSupport<Message>	implements
													MessageDao
{

	@Override
	public void addMessage(Message msg) {
		Serializable id = save(msg);
		msg.setId((String) id);
	}

	@Override
	public void updateMessage(Message msg) {
		update(msg);
	}

	@Override
	public List<Message> findByAccountAndState(String account, int state) {
		return findByFields(Message.class, new String[] { "receiver", "state" }, new Object[] {
				account,
				state });
	}

}
