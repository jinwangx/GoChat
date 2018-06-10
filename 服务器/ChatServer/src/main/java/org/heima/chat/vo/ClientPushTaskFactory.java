package org.heima.chat.vo;

import java.util.HashMap;

public class ClientPushTaskFactory {

	public static ClientPushTask iconChangeTask(String sender, String receiver, String iconPath) {
		ClientPushTask task = new ClientPushTask();
		task.setAction("iconChange");
		task.setSender(sender);
		task.setReceiver(receiver);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("iconPath", iconPath);
		task.setParams(params);

		return task;
	}

	public static ClientPushTask nameChangeTask(String sender, String receiver, String name) {
		ClientPushTask task = new ClientPushTask();
		task.setAction("nameChange");
		task.setSender(sender);
		task.setReceiver(receiver);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		task.setParams(params);

		return task;
	}
}
