package org.heima.chat.service.impl;

import java.util.List;

import org.heima.chat.dao.BackPushTaskDao;
import org.heima.chat.pojo.BackPushTask;
import org.heima.chat.service.BackPushTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackPushTaskServiceImpl implements
									BackPushTaskService
{
	@Autowired
	BackPushTaskDao	taskDao;

	@Override
	public void addTask(BackPushTask task) {
		taskDao.addTask(task);
	}

	@Override
	public void updateTask(BackPushTask task) {
		taskDao.updateTask(task);
	}

	@Override
	public List<BackPushTask> queryPendingTaskByAccount(String account) {
		return taskDao.queryPendingTaskByAccount(account);
	}

	@Override
	public List<BackPushTask> queryAllPendingTask() {
		return taskDao.queryAllPendingTask();
	}

}
