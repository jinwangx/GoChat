package com.jw.gochat.utils;

import com.jw.gochat.bean.NetTask;

import java.util.HashMap;

public class BackTaskFactory {

	public static NetTask newFriendAcceptTask(String invitor, String acceptor) {

		NetTask task = new NetTask();
		task.setMethod("POST");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("invitor", invitor);
		params.put("acceptor", acceptor);

		task.setParams(params);
		task.setPath("/friend/accept");
		task.setProtocol("HTTP");
		return task;
	}

	public static NetTask userIconChangeTask(String name,String iconPath) {
		NetTask task = new NetTask();
		task.setMethod("POST");
		task.setType(NetTask.TYPE_UPLOAD);
		HashMap<String, String> files = new HashMap<String, String>();
		files.put(name+".png", iconPath);
		HashMap<String, String> params = new HashMap<String, String>();
		task.setParams(params);
		task.setFiles(files);
		task.setPath("/user/icon");
		task.setProtocol("HTTP");
		return task;
	}

	public static NetTask userNameChangeTask(String name) {
		NetTask task = new NetTask();
		task.setMethod("POST");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		task.setParams(params);
		task.setPath("/user/name");
		task.setProtocol("HTTP");
		return task;
	}
}
