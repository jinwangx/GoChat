package org.heima.chat.service;

import java.util.List;

import org.heima.chat.pojo.BackPushTask;

public interface BackPushTaskService {

	void addTask(BackPushTask task);

	void updateTask(BackPushTask task);

	List<BackPushTask> queryPendingTaskByAccount(String account);

	List<BackPushTask> queryAllPendingTask();
}
