package org.heima.chat.nio;

import org.heima.chat.core.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePusher {

//	@Autowired
//	SocketDispatcher	socketDispatcher;

	@Autowired
	ContextUtil			contextUtil;

	public void push(PushMessage msg, PushCallback callback) {
		SocketDispatcher socketDispatcher =
											ContextUtil.getApplicationContext()
														.getBean(SocketDispatcher.class);
		socketDispatcher.addPush(new PushRequest(callback, msg));
	}

	public boolean isOnline(String account) {
		SocketDispatcher socketDispatcher =
											ContextUtil.getApplicationContext()
														.getBean(SocketDispatcher.class);
		return socketDispatcher.isOnline(account);
	}
}