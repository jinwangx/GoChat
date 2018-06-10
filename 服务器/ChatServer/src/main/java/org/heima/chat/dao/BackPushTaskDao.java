package org.heima.chat.dao;

import java.util.List;

import org.heima.chat.pojo.BackPushTask;

public interface BackPushTaskDao
{
	void addTask(BackPushTask task);

	void updateTask(BackPushTask task);

	List<BackPushTask> queryPendingTaskByAccount(String account);

	List<BackPushTask> queryAllPendingTask();
}
